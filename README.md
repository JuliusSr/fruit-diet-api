# Fruit Diet API

## Requirements
1) List all fruits given a max amount of calories
2) Given 2 fruit names as input return an object containing the difference between all the nutrients
3) Given a fruit’s family as input return the average nutritional values for the family
4) given a range of calories compute a list of fruits that will satisfy the input range
   - A fruit can be repeated more times in the list, but it should be preferred to use distinct fruits
   - The fruit list must be sorted in ascending order based on the calories value

## General considerations
All requirements have retrieval as target, and all input is simple enough. Because of that all the following API use the HTTP method "GET".  
For simplicity’s sake the same NutritionalData object is being returned in all API (in case of the filter API, it's nested inside the fruit objects), but it represents three logically different concepts:
- The actual nutritional data
- The nutritional data delta
- The nutritional data average

In the future it could be opportune to return three different DTO objects (with appropriate naming for the delta and average).  

## Project structure
### /controller
The controller is responsible for handling the API requests and responses.
### /data
Data objects
### /exceptions
Custom exceptions
### /fruitsinfo
Here resides the fruits' info retrieval service(s).  
A FruitInfoService is responsible for retrieving fruits' info and returning it in a standard form (using the objects defined in /data).  
Currently, there is only the FruityViceFruitsInfoService, but should the need arise a different source (other api, database, ...) could be used and a new implementation of FruitInfoService could be used in its place.  
### /service
The FruitDietService is responsible for the business logic, operating on and transforming the data retrieved with a FruitsInfoService.
### /utils
ArithmeticUtils contains some methods useful for arithmetic operations.
### resources
The fruit info API url is defined in an application.properties file so that it's not hardcoded. This allows us to have possibly different endpoints for different environments.  

## API

### Filter by nutritional value
```
[GET] /diet/fruit/filter/{nutritionalValue}?min={min}&max={max}&order={order}
```
This API is a generalization ad a combination of requirements 1 and 4.  
Seeing that requirement 4 builds on top of requirement 1 by requiring also a minimum amount of calories and an ordering, those can be put together to form a single API.  
And because we could be interested in doing a similar filter and ordering on other nutritional values, this API was built to work with all nutritional values (including calories as initially required).  

The API uses the "Request Fruits information by nutritional value" FruityVice API to get all fruits in a nutritional value range.  
If an order is specified it returns the sorted range, otherwise it preserves the FruityVice order.
#### example requirement 1
###### request
```
[GET] http://localhost:8080/diet/fruits/filter/calories?max=25
```
###### response
```json
[
  {
    "name": "GreenApple",
    "family": "Rosaceae",
    "genus": "Malus",
    "order": "Rosales",
    "nutritionalData": {
      "carbohydrates": 3.1,
      "protein": 0.4,
      "fat": 0.1,
      "calories": 21,
      "sugar": 6.4
    }
  },
  {
    "name": "Apricot",
    "family": "Rosaceae",
    "genus": "Prunus",
    "order": "Rosales",
    "nutritionalData": {
      "carbohydrates": 3.9,
      "protein": 0.5,
      "fat": 0.1,
      "calories": 15,
      "sugar": 3.2
    }
  }
]
```
#### example requirement 4
###### request
```
[GET] http://localhost:8080/diet/fruits/filter/calories?min=90&max=150&order=asc
```
###### response
```json
[
  {
    "name": "Banana",
    "family": "Musaceae",
    "genus": "Musa",
    "order": "Zingiberales",
    "nutritionalData": {
      "carbohydrates": 22.0,
      "protein": 1.0,
      "fat": 0.2,
      "calories": 96,
      "sugar": 17.2
    }
  },
  {
    "name": "Passionfruit",
    "family": "Passifloraceae",
    "genus": "Passiflora",
    "order": "Malpighiales",
    "nutritionalData": {
      "carbohydrates": 22.4,
      "protein": 2.2,
      "fat": 0.7,
      "calories": 97,
      "sugar": 11.2
    }
  },
  {
    "name": "Durian",
    "family": "Malvaceae",
    "genus": "Durio",
    "order": "Malvales",
    "nutritionalData": {
      "carbohydrates": 27.1,
      "protein": 1.5,
      "fat": 5.3,
      "calories": 147,
      "sugar": 6.75
    }
  }
]
```
#### example: filter on other nutritional value
###### request
```
[GET] http://localhost:8080/diet/fruits/filter/protein?min=1.5&order=desc
```
###### response
```json
[
  {
    "name": "Guava",
    "family": "Myrtaceae",
    "genus": "Psidium",
    "order": "Myrtales",
    "nutritionalData": {
      "carbohydrates": 14.0,
      "protein": 2.6,
      "fat": 1.0,
      "calories": 68,
      "sugar": 9.0
    }
  },
  {
    "name": "Passionfruit",
    "family": "Passifloraceae",
    "genus": "Passiflora",
    "order": "Malpighiales",
    "nutritionalData": {
      "carbohydrates": 22.4,
      "protein": 2.2,
      "fat": 0.7,
      "calories": 97,
      "sugar": 11.2
    }
  }
]
```

### Compare Fruits
```
[GET] /diet/fruit/comparison?baselineFruit={baselineFruit}&comparedFruit={comparedFruit}
```
This API follows requirement 2.
The API uses the "Request Fruit information" FruityVice API to retrieve a specific fruit data.  
In order to get the difference of nutritional data, one of the two fruits is taken as baseline. The result of the comparison will be the delta between nutritional data of the second fruit compared to the baseline fruit.  
#### example
###### request
```
[GET] http://localhost:8080/diet/fruits/comparison?baselineFruit=apple&comparedFruit=pear
```
###### response
```json
{
  "carbohydrates": 3.6,
  "protein": 0.1,
  "fat": -0.3,
  "calories": 5,
  "sugar": -0.3
}
```

### Average nutritional values of fruit family 
```
[GET] /average/family/{family}
```
This API follows requirement 3.
The API uses the "Request fruits with given family" FruityVice API to retrieve all fruits in a given family.  
After the fruits are retrieved all nutritional data are respectively added together and then divided by the total number of fruits in the family to obtain the average.
#### example
###### request
```
[GET] http://localhost:8080/average/family/Rosaceae
```
###### response
```json
{
    "carbohydrates": 8.88,
    "protein": 0.66,
    "fat": 0.32,
    "calories": 39,
    "sugar": 6.75
}
```

## Improvements

### Error handling
Currently, error handing needs improvements:
1) In FruityViceFruitsInfoService we catch Exception.class, but we should handle different exceptions separately (api returns bad request vs fruit not found vs some other error).  
2) As a result of point 1 our API return the same error object for different error cases.
3) We should define an error object structure and only return custom error objects (we are currently relying on SpringBoot default error messages for bad requests, etc...)

### Concurrent API calls
The comparison API currently retrieves the two fruits sequentially.  
Because the second API call does not depend on the first one, we could perform them both at the same time in order to improve performance.  
A first version is available on branch feature/getFruitByNameAsync and although it runs correctly, it has issues during test context initialization that need to be fixed.  

# Recipe API

The resource of this API is ``Recipe``. The ``Recipe`` entity has a ``name`` and a list of ``ingredients`` as its attributes.
An ingredient has a ``name``,  ``weight`` an a ``type``. The ``type`` can be either ``WET`` or ``DRY``.

Endpoints:

````
GET http://localhost:8080/recipes
GET http://localhost:8080/recipes/{id}
POST http://localhost:8080/recipes
PUT http://localhost:8080/recipes
DELETE http://localhost:8080/recipes/{id}
````

Example request for creating/updating a recipe:

````json

{
  "recipeName": "Pizza Dough",
  "ingredients": [
    {
      "name": "Water",
      "type": "WET",
      "weight": 8000
    },
    {
      "name": "Flower",
      "type": "DRY",
      "weight": 1000
    },
    {
      "name": "Yeast",
      "type": "DRY",
      "weight": 2
    },
    {
      "name": "Salt",
      "type": "DRY",
      "weight": 20
    }
  ]
}

````

And the response you will get back:

````json
{
  "id": 1,
  "recipeName": "Pizza",
  "ingredients": [
    {
      "id": 4,
      "name": "Water",
      "type": "WET",
      "weight": 8000
    },
    {
      "id": 2,
      "name": "Flower",
      "type": "DRY",
      "weight": 1000
    },
    {
      "id": 1,
      "name": "Yeast",
      "type": "DRY",
      "weight": 2
    },
    {
      "id": 3,
      "name": "Salt",
      "type": "DRY",
      "weight": 20
    }
  ]
}
````



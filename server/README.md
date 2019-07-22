# BudgetMe

### Overview
For all the response/request payloads, if applicable, will be defined in a `application/json`. There will be two types of requests in general -- a success or failed response

Example of a good response:

```json

{
    "status": 200,
    "message": "Success",
    "data": {
        "dataField1": "data information we defined in the database"
    }
}

``` 
Example of a bad response

```json
{
    "status": 400,
    "message": "Failed",
    "data": null
}
```
   
   
 ### API Interface  
 The BudgetMe APIs will be categorized into following 3 subsections based on its functionalities
   * 1 UserActivity (e.g. authentication, goal setting etc.)
   * 2 TransactionActivity - Individual
   * 3 TransactionActivity - Group

**UserActivity**
* Register an account, send `POST /user` with:
```json
{
    "username": "Mingyuan Ryan (Wendy)",
    "email": "abc@gmail.com",
    "password": "12345678 (will be a hashed value when sent it)"
}
```

* Login an account, send `GET /user/{userEmail}/{password}`

If username and password are valid, then it will return 
a `response` with:
```json
{
    "username": "Mingyuan Ryan (Wendy)",
    "defaultGroupId": "master_group_token",
    "groupList": [
        {
            "groupId": "abc_valid_group_id",
            "groupName": "GROUP_TEST1",
            "userList": ["user1", "user2"]
        }
    ],
    "userAuthToken":"5d30ff4e6397c4000427fabe"
    
}
```
If username or password are invalid, then it will return a `response` with:
```json
{
    "status": 401,
    "message": "Invalid username/password",
    "data": null
}
```

 * Set a target goal, send `POST /goal/{user_token}/{group_id}` with:
 ```json
{
      "startDate": "20/04/2019",
      "endDate": "20/06/2019",
      "targetAmount": 2000.00,
      "description": "Description to explain the goal",
      "categoryList": [
          {
              "categoryId": "valid_category_id",
              "categoryName": "TEST1"
          }, 
          {
              "cateogoryId": "valid_category_id",
              "categoryName": "TEST2"  
          }]
}
```
`response`
```json
{
    "status": 200,
    "message": "Goal creation success",
    "data": {
        "goalId": "abc_valid_goal_id"
    }
}
```

* Get goal list of a user, send `GET /goal/{user_token}/{group_id}`

`response`
```json
{
    "status": 200,
    "message": "Goal deletion success",
    "data": [
        goalObject1,
        goalObbject2
    ]
}
```

* Delete a target goal, send `DELETE /goal/{user_token}/{group_id}/{goal_Id}`

`response`
```json
{
    "status": 200,
    "message": "Goal deletion success",
    "data": null
}
```


* Create a category, send `POST /category/{user_token}/{group_id}` with 
```json
{
    "categoryName": "SchoolActivityExpenditure"
}
```
`response`
```json
{
    "status": 200,
    "message": "Category creation success",
    "data": null
}
```

* Get category list of a group, send `GET /category/{user_token}/{group_id}` 

`response` (if the category token & user toekn valid) 
```json
{
    "status": 200,
    "message": "Category deletion success",
    "data": [
        {
            "categoryId": "valid_category_id",
            "categoryName": "SchoolActivityExpenditure"
        }
    ]
}
```

* Delete a category, send `DELETE /category/{user_token}/{group_id}/{categoryId}` 

`response` (if the category token & user toekn valid) 
```json
{
    "status": 200,
    "message": "Category deletion success",
    "data": null
}
```

* Create a group, send `POST /group/{userToken}` with 
```json
{
    "groupName": "group_test",
    "userList": ["user1", "user2", "user3"]
}
```
`response`
```json
{
    "status": 200,
    "message": "Group creation success",
    "data": {
        "groupId": "abc_valid_group_id"
    }
}
```

* Add to a group, send `POST /group/{userToken}` with
```json
{
    "groupName": "abc_valid_group_name"
}
````

`response`
```json
{
    "status": 200,
    "message": "Group Added success",
    "data": null
}
```

* Get a group list of user, send `GET /group/{user_token}`
```json
{
    "status": 200,
    "message": "Group Retrieval success",
    "data": {
        GroupObject1,
        GroupObject2 ...
    }
}
``` 

* Delete a category, send `DELETE /group/{user_token}/{group_id}` 

`response`
```json
{
    "status": 200,
    "message": "Group deletion success",
    "data": null
}
```
 
 **TransactionActivity - Individual**
 * Create/modify a new transaction activity, send `POST /trans/{user_token}/{group_id}` with:
 ```json
{
    "category": {
        "categoryId": "a_valid_category_id",
        "categoryName": "grocery"
    },
    "cost": 120.00,
    "date": "20/04/2019",
    "description": "N/A"
}
```
`response`
```json
{
    "status": 201,
    "message": "Transaction creation/modification success",
    "data": {
        "transId": "abc_activity_id"
    }
}
```

* Read all transaction activities, send `GET /trans/{user_token}/{group_id}`
`response`
```json
{
    "status": 200,
    "message": "Transactions read success",
    "data": {
        "transactions": [
            {
                "transId": "abc_activity_token",
                "category": { "categoryId":  "", "categoryName":  "" },
                "cost": 120.00,
                "date": "2019-04-20",
                "description": "N/A"
            },
            {
                "transId": "def_activity_token",
                "category": { "categoryId":  "", "categoryName": "" },
                "cost": 200.00,
                "date": "2019-05_20",
                "description": "Used for personal business"
            }
        ]
    }
    
}
```

* Delete a transaction, send `DELETE /trans/{user_token}/{group_id}/{transaction_id}` with
```json
{
    "status": 200,
    "message": "Transaction deletion success ",
    "data": null
}
```
 
 **TransactionActivity - Group**
 
 * Create/modify a new transaction activity is the same as create an individual transaction (see above section)
 `e.g. POST /trans/{user_token}/{group_id}`

* Read transaction activities is the same as individual transaction read (see above section)
 `e.g. GET /trans/{user_token}/{group_id}`

* Delete a transaction is the same as individual transaction deletion (see above section)
 `e.g. DELETE /trans/{user_token}/{group_id}/`

For modifying any transaction, in order handle synchronization (if other users modify same set of data),
 `e.g. POST /pretrans/{user_token}/{group_Id}/{transId}` with
 ```json
{
    "category": "Grocery",
    "cost": 120.00,
    "date": "2019-04-20",
    "description": "N/A"
}
```
If transaction has not been modified, execute as normal (see above section). 
However, if the transaction has been modified, then respond with new modified data to notify 
 `e.g. POST /pretrans/{user_token}/{group_Id}/{transId}`
```json
{
    "status": 200,
    "message": "Transaction has been modified",
    "data": {
        "transId": "abc_activity_token",
        "category": "Grocery",
        "cost": 120.00,
        "date": "2019-04-20",
        "description": "N/A"
    }
}
```


### Edge Case

There are several scenario where we will return a bad response, which include:
   * Requests without correct authorized account info or valid user token (unauthorized request, 401)
   * Requests for any resource where it does not exist anymore (not found, 404)
   * Requests with invalid input (invalid request, 400)
   
   
For all activities, error code will be responded when occurred in the following scenarios:

* If the user token is unauthorized, then respond with
```json
{
    "status": 401,
    "message": "Invalid user token",
    "data": null
}
```

* If the category token does not exist (invalid), then respond with 
```json
{
    "status": 404,
    "message":  "Category didn't found",
    "data": null
}
```

* If the group token does not exist (invalid), then respond with 
```json
{
    "status": 404,
    "message":  "Group didn't found",
    "data": null
}
```

* If the transaction token does not exist (invalid), then respond with 
```json
{
    "status": 404,
    "message":  "Activity didn't found",
    "data": null
}
```

* If the input value is invalid (e.g. out of range, incorrect format), then respond with
```json
{
    "status": 400,
    "message":  "Bad Request",
    "data": null
}
```
 
   
 
 


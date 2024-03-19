# About the project


It's an API made with MySQL that register the user's poems. I recommend testing each method in Postman using the form-data.
To register a user you only to send the username, email, password and the password's confirmation. Then you can login with the username and the password.


In order to this API work, it's necessary the creation of a '.env' file with this structure:


```
JWT_SECRET=create_any_password
DB_USER=your_mysql_username
DB_PASSWORD=your_mysql_password
DB_NAME=database_name
DB_HOST=localhost:3306_or_any_other_host
```


## Available methods


### User


[POST] Registering a user with Postman (http://localhost:8080/register):

```
username:
email:
password:
confirmPassword:
```


[POST] Login (http://localhost:8080/login):

```
username:
password:
```


After that, it's possible to create the user's profile data by adding information like the birthday, first name, last name, gender and give some personal description. There's a seperate function for changing the password and another to change the email.


[PuT] Changing the password (http://localhost:8080/profile/change_password) [Requires JWT]:

```
currentPassword:
newPassword:
confirmPassword:
```


[PuT] Changing the email (http://localhost:8080/profile/change_email) [Requires JWT]:

```
email:
```


[PUT] Adding the profile data (http://localhost:8080/profile/update) [Requires JWT]:


```
firstName:
lastName:
gender: M or F
birthday: yyyy-mm-dd
description:
```


These are the request methods that don't require filling a form:


```
[GET] http://localhost:8080/{username}
[GET] http://localhost:8080/user-list
[GET] http://localhost:8080/profile [Requires JWT]
[DELETE] http://localhost:8080/profile/delete [Requires JWT]
```


### Poem


[POST] Create new poem (http://localhost:8080/profile/new_poem) [Requires JWT]:


```
title:
content:
```

[PUT] Edit poem (http://localhost:8080/profile//update_poem/{poem_id}) [Requires JWT]:


```
title:
content:
```


These are the request methods that don't require filling a form:


```
[GET] http://localhost:8080/{username}/poems
[GET] http://localhost:8080/poem/{poem_id}
[GET] http://localhost:8080/profile/poems [Requires JWT]
[DELETE] http://localhost:8080/poem/{poem_id} [Requires JWT]
```


### Comment


[POST] Create new comment (http://localhost:8080/poem/{poem_id}/new_comment) [Requires JWT]:


```
content:
```

[PUT] Edit comment (http://localhost:8080/comment/{comment_id}) [Requires JWT]:


```
content:
```


```
[GET] http://localhost:8080/poem/{poem_id}/comments
[GET] http://localhost:8080/comment/{comment_id}
[GET] http://localhost:8080/profile/comments [Requires JWT]
[DELETE] http://localhost:8080/comment/{comment_id} [Requires JWT]
```
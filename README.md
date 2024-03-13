An API made with MySQL that register the user's poems. I recommend testing each method in Postman using the form-data.
To register a user you only to send the username, email, password and the password's confirmation. Then you can login with the username and the password.


Registering a user with Postman:

```
username:
email:
password:
confirmPassword:
```


Login:

```
username:
password:
```

After that, it's possible to create the user's profile data by adding information like the birthday, first name, last name, gender and give some personal description. There's a seperate function for changing the password and another to change the email.


Changing the password:

```
currentPassword:
newPassword:
confirmPassword:
```


Changing the email:

```
email:
```


Adding the profile data:


```
firstName:
lastName:
gender: M or F
birthday: yyyy-mm-dd
description:
```


In order to this API work, it's necessary the creation of a '.env' file with this structure:

```
JWT_SECRET=create_any_password
DB_USER=your_mysql_username
DB_PASSWORD=your_mysql_password
DB_NAME=database_name
DB_HOST=localhost:3306_or_any_other_host
```
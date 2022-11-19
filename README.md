# b07 - User_branch
The branch of user class for b07 project.

## Class
### User
#### Fields
User class contains four fields in total:
1. `String username`
 
    A string field that acts as an unique identifier for each user.

2. `String password`
 
    A string field of *unhashed* password.
 
3. `String [] courses`
 
    A string array field that contains all course code that this user is currently 
    enrolled (or is teaching).
 
4. `boolean privileged`
 
    A boolean field that indicates whether user is a student of admin, more 
    precisely:
    - privileged is true if and only if the user  is an admin,
    - privileged is false if and only if the user is a student.

#### Methods
1. `private User(String username, String password, String [] courses, boolean privileged);`
 
    The constructor of the class.
 
2. `public boolean isUserExists(String username);`
 
    Check if the username exists in database.

3. `public static User login(String username, String password);`

    Log the user in with given username and password. Method returns user
    object with corresponding username, password, courses, and privileged fields
    if and only if username exists and with correct password.

4. `public static User signup(String username, String password, boolean privileged);`

    Add new user to the database if and only if the username does not exists.
    When User object is pushed into Firedatabase, we following the following 
    procedures:
    1. We create a new node in the Users node in Firedatabase with name as the 
       username of 'User' object
    2. The node contains three branches, password, courses, privileged accordingly.
    3. the courses branch lists all the course code of the user takes, and 
       and separates them with comma.
    
    For example, when we are to push a User object with:
    - username: Alice
    - password: 123456
    - courses: ["CSCB07", "CSCB36", "STAB52"]
    - privileged: false
    
    our database would change to following:
    
    ```sh
    .
    ├── courses
    └── users
        ├── ... (some other users)
        └── Alice
            ├── password: "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"
            ├── courses: "CSCB07,CSCB36,STAB52"
            └── privileged: "false"
    ```
    

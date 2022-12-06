# The Main Tasks Done by Each Member of the Group
> needed for submission, see handout.pdf<br>

## Yulong Liu
- creating and managing issues on Jira and assign them to each member after explaining the details of each task
- review everyone's pull request and merge them after refactoring
- implement recycler view to be reused for courses taken list, course wishlist, show courses to be added to taken list, and show courses to be added to wishlist
- connect Leon's create course (as admin) frontend with Jiaming's business logic to create/modify/delete courses in database
- write unit tests for login presenter with 100% line coverage
- design navigation graph to connect the fragments other team members have created
- provide starter code for Jiaming and Kim for course's and user's business logic

## Leon Lee

- Worked on the "create-course" fragment to allow admins to add new courses to the data base
  - enter new course code
  - enter new course name
  - choose sessions
  - choose prereqs
  - reading and writing to firebase

- Refactoring code to fit the MVP model
  - rewriting code and moving code to more appropriate classes
   - create classes if there weren't previously existing ones that are fit

- Design App Icon

- Designing the UI of the app
  - added and designed the majority of the view design seen by the users using XML files and Constraint Layout
  - set constraints for all the elements

## Jiaming Lu
- Student: Delete Taken Courses
  - for each course listed, add a delete button that deletes it from taken

- Student Courses
  - User will have a field called Set<Courses> courses and it’s automatically updated when db changes
     the behavior changes depending on Student or Admin
  - Student adds and removes courses from users[name].courses in firebase
  - Admin adds and removes courses from courses in firebase
  - When a user is loggin, the instance should listen to courses in firebase for changes
- Admin Courses
  - Business logic for admin to add/remove/view courses

## Hansen Lin

- Design and implement methods to generate timeline
  - get taken list and want list
  - generate the timeline
  - show it in ui to users

- Import data into firebase for early testing
  - get all the course codes, name, session, prereqs

- Taken list
  - implement the view for students to add courses into taken list
  - design and implement the method that put selected courses into firebase
  - show the taken list to students
  - sorted the list views

- Admin create courses
  - fixed it such that it can import name of courses into firebase


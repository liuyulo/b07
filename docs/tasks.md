# The Main Tasks Done by Each Member of the Group
> needed for submission, see handout.pdf<br>
> just copy the PR comments or Jira issues)

## Yulong Liu

- lol

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


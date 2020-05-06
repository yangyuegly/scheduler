# cs0320 Term Project 2020

**Team Members:** Abby Goldberg, Shenandoah Duraideivamani, Rachel Fuller, Yue (Jane) Yang

**Team Strengths and Weaknesses:**

- Jane Yang
  - Strength: Back-end, database, systems
  - Weakness: front-end design

- Rachel
  - strengths: time management, sticking to a plan, algorithms, testing sufficiently, working on the weekends, waking up early to go to unpopular TA hours times, asking for help
  - weaknesses: getting stressed without a specific plan, getting overwhelmed by details, I’m confused by object oriented programming, getting too frustrated and leaving instead of continuing to work on a problem, html and css

- Shenandoah
  - Strengths: determination; time management; know when to call a break; starting projects early; I’m good at functional programming and testing.
  - Weakness: I’m a bit slow in grasping concepts. I need to take my time to understand the different requirements and concepts taught in class.I sometimes can’t focus if I’m thinking about something and I let it interrupt my schedule but I’m improving now and it’s been getting much better. In terms of code, I’m not good at front-end design.

- Abby
  - Strengths: attention to details, organization, willing to take the time to thoroughly understand topics, time management
  - Weaknesses: easily stressed, art-related activities, speed of decision making, focus on details too much before determining the general outline

**Project Ideas:**

### Idea 1

A webapp to help students off meal plan with cooking and finding food:

- Problem: People often have leftover food that they don't know what to do with, so they just throw it out.  Also, people often purchase cooking supplies that they could easily borrow from other students.  Both students off meal plan and the environment can benefit from shared resources.
- Our app would have the following features to address the problem:
  - a platform for students to share utensils/ingredients/condiments
  - community feature for students in different dorms
  - a more efficient meal credit sharing algorithm
  - splitting delivery fee between users
  - sharing of leftover food
- Most challenging:
  - organizing different features and providing a user interface that's not clustered.
  - determining how convenient it is for a user to meet another user in order to borrow something
  
Rejected - This is a typical "task" app and it does not provide interesting design decisions

### Idea 2

A webapp that does exam scheduling for different courses in different universities:

- Problem: It is hard to schedule conflicts with a large number of students taking multiple courses. Often, students have to take multiple exams in a day.  Sometimes, certain class are taken at the same time, yet their exams can still be on the same day.  Our product will minimize exam conflicts where students sometimes have to take different exams on the same day. 
- Our app would have the following features to address the problem:
  - Spacing out exams in the same concentration (considering if two courses are typically taken in unison)
  - Creating a database with different students in different courses since we do not have access the data from Brown
  - Allow the user to specify considerations other than just minimizing conflicts -- for example, trying to have the most number of freshman get their exams over early in the Fall so they can go home early
  - Tell the user which classes have the most conflicts 
- Most challenging:
  - the algorithm to minimize conflicts because the dataset will be large since it will have all the students and courses at Brown
  - allow flexibility for the user to specify other concerns 
  
Approved - Lots of time should be spent on accumulating data and the algorithm itself, likely dynamic programming

### Idea 3

An automatic course plan planner:

- Problem: it can be really confusing during Shopping Period to manage all the classes students consider. The current Courses@Brown does not offer a very intuitive way to organize different courses; we can only throw them into various carts. 
- Our app would have the following features to address the problem:
  - labelling a course as a requirement or an elective
  - categorizing courses (i.e. labelling an Environment Science course and a Geology course as both in the “Science” category)
  - determining whether two course locations are too far away to reach in a ten-minute passing period
  - when viewing the course schedule, select or deselect courses to view in the schedule instead of entirely removing or adding courses from the plan as in CAB
  - pulling the time from the critical review to see the workload of a proposed schedule
- Most Challenging:
  - on the front end: displaying the courses on the schedule in a dynamic way (allow users to click on courses they want to display or hide, color-coding different courses/possible schedules, displaying multiple courses in the same time)
  - using a map algorithm to determine if the distance between course locations is too far to go 
  - pulling the data on the workload of a course from the Critical Review, which is a website online
  
Approved - how courses are clustered show be the main focus. There should be differentiation from Maps if distance between classes is taken into account 

Note: You do not need to resubmit your final project ideas.

**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings

_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 13)_

**4-Way Checkpoint:** _(Schedule for on or before April 23)_

**Adversary Checkpoint:** _(Schedule for on or before April 29 once you are assigned an adversary TA)_

## What the project is

A scheduler for a convention. A convention is the main event which contains sub-events. For example, a hackathon would be a convention and the workshops would be sub events. Our project also supports scheduling final exams for courses from multiple departments of the college of your choice. You could also add collaborators to your conventions.

## How to Build and Run

<link to URL>
To schedule a final exam: After logging in, click the schedule exam button either on the navigation bar or the "Schedule Exams for the college of your choice" button. This will take you to the page where you can enter details about your convention. Choose your college from the drop down list, start date of the convention either manually or through the calendar drop down, total number of days of the convention and the start and end time of the convention by typing in the respective time slots. After you're done, click the schedule now button. This will take you to the calendar page.


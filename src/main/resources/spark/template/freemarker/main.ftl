<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/css/term_project.css">
  </head>
  <body>

    <div class="sched-header">
      <ul>
        <li><a href=/home>Home</a>     <!-- <h1 class="project-title">Scheduler</h1></li> -->
        <li><a href=/account>My Account</a></li>
        <li><a href=/create_convention>Create Convention</a></li>
        <li><a href=/create_exam_conv>Schedule Exams</a></li>
        <li><a href=/home id="loginHeaderLink">Log In</a></li>

        <!-- <div class="user-dropdown" id="userDropdownDiv"> -->
          <li class="user-dropdown">
            <button id="userHeaderLink" class="user-dropdown-button">
              <svg class="bi bi-people-circle" width="1.8em" height="1.8em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path d="M13.468 12.37C12.758 11.226 11.195 10 8 10s-4.757 1.225-5.468 2.37A6.987 6.987 0 008 15a6.987 6.987 0 005.468-2.63z"/>
                <path fill-rule="evenodd" d="M8 9a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd"/>
                <path fill-rule="evenodd" d="M8 1a7 7 0 100 14A7 7 0 008 1zM0 8a8 8 0 1116 0A8 8 0 010 8z" clip-rule="evenodd"/>
              </svg>
              <svg class="bi bi-caret-down-fill" width="1.8em" height="1.8em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path d="M7.247 11.14L2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 01.753 1.659l-4.796 5.48a1 1 0 01-1.506 0z"/>
              </svg>
            </button>
            <div id="userDropdownContent" class="user-dropdown-content">
              <a href=/account>My Account</a>
              <a href=/logout>Logout</a>
            </div>
          </li>

        <li><a href=/logout>Logout</a></li>

        <!-- </div> -->

<!--
        <div align="right" class="log-in-info">
          <div id="notLoggedInDiv">
            <a href="/home">Log in</a>
          </div>
          <div id="loggedInDiv">
            <label>Logged in as <a href=/home id="loginStatusLink"></a></label>
            <br>
            <a href=/logout id="logoutLink">Log out</a>
          </div>
        </div> -->

    </div>

    <script src="/js/jquery-2.1.1.js"></script>
    <script src="/js/header_login_status.js"></script>

     ${content}



     <!-- bootstrap -->

     <!-- Latest compiled and minified CSS -->
    <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> -->

     <!-- jQuery library -->
     <!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script> -->

     <!-- Latest compiled JavaScript -->
     <!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script> -->
  </body>
</html>

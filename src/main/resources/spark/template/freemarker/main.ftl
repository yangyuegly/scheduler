<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/css/term_project.css">
    <link href='https://fonts.googleapis.com/css?family=Cabin' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Playfair+Display' rel='stylesheet' type='text/css'>

  </head>
  <body>

    <div class="sched-header">
      <ul>
        <li><a href=# class="big">I</a></li> <!-- delete!!!!! -->
        <li><a href=/home>Home</a>     <!-- <h1 class="project-title">Scheduler</h1></li> -->
        <li><a href=/account>My Account</a></li>
        <li><a href=/create_convention>Create Convention</a></li>
        <li><a href=/create_exam_conv>Schedule Exams</a></li>
        <li><a href=/home id="loginHeaderLink">Log In</a></li>

        <!-- <div class="user-dropdown" id="userDropdownDiv"> -->
          <li class="user-dropdown">
            <button id="userHeaderLink" class="user-dropdown-button">
<<<<<<< HEAD
            <svg class="bi bi-person" width="1.2em" height="1.2em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd" d="M13 14s1 0 1-1-1-4-6-4-6 3-6 4 1 1 1 1h10zm-9.995-.944v-.002.002zM3.022 13h9.956a.274.274 0 00.014-.002l.008-.002c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664a1.05 1.05 0 00.022.004zm9.974.056v-.002.002zM8 7a2 2 0 100-4 2 2 0 000 4zm3-2a3 3 0 11-6 0 3 3 0 016 0z" clip-rule="evenodd"/>
            </svg>
              <svg class="bi bi-caret-down-fill" width=".7em" height=".7em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path d="M7.247 11.14L2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 01.753 1.659l-4.796 5.48a1 1 0 01-1.506 0z"/>
              </svg>
            </button>
            <div id="userDropdownContent" class="user-dropdown-content">
              <a href=/account>My Account</a>
              <a href=/logout>Logout</a>
            </div>
          </li>

        <li><a href=/logout>Logout</a></li>
      </ul>

    </div>

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

    <!-- </div> -->

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

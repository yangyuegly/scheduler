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
        <li><a href=/home>Scheduler</label>     <!-- <h1 class="project-title">Scheduler</h1></li> -->
        <li><a href=/account>Account</a></li>
        <li><a href=/create_convention>Create Convention</a></li>

        <div align="right" class="log-in-info">
          <div id="notLoggedInDiv">
            <a href="/home">Log in</a>
          </div>
          <div id="loggedInDiv">
            <label>Logged in as <a href=/home id="loginStatusLink"></a></label>
            <br>
            <a href=/logout id="logoutLink">Log out</a>
          </div>
        </div>

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

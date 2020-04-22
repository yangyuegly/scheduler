<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/css/term_project.css">
  </head>
  <body>

    <div class="sched-header">
      <h1 class="project-title">Scheduler</h1>
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

     ${content}
     <script src="/js/jquery-2.1.1.js"></script>
     <script src="/js/header_login_status.js"></script>
     <script src="/js/build_convention.js"></script>
     <script src="/js/attendee_event_signup.js"></script>
  </body>
</html>

<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <p>
  <h3>Log in:</h3>
  <form method="POST" action="login">
    <label for="email">Email: </label><br>
     <input name="email" id="email" size="30" required></textarea><br><br>
     <label for="password">Password: </label><br>
     <input name="password" id="password" type="password" size="30" required></textarea>
     <br>${message}
     <br><br>
     <input type="submit" value="Log in">
  </form>
  <a href=/create_account>Make a new account</a>
</div>

</#assign>
<#include "main.ftl">

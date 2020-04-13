<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <p>
  <h3>Log in:</h3>
  <form method="POST" action="/login">
    <label for="email">Email: </label>
     <input name="email" id="email" size="30" required><br><br>
     <label for="password">Password: </label>
     <input name="password" id="password" type="password" size="30" required>
     <br>${message}
     <br><br>
     <input type="submit" value="Log in">
  </form>
  <br>
  <br>
  <a href=/create_account>Sign up</a>
</div>

</#assign>
<#include "main.ftl">

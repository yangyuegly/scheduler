<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <p>
  <h3>Create a new account:</h3>
  <form method="POST" action="/create_account">
    <label for="email">Email: </label>
     <input name="email" id="email" size="30" required><br><br>
     <label for="password">Password: </label>
     <input name="password" id="password" type="password" size="30" required>
     <input type="submit" value="Create account">
  </form>
</div>

</#assign>
<#include "main.ftl">

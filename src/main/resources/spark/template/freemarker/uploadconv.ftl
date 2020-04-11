<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <p>
  <h3>Log in:</h3>
  <form method="POST" action="/upload">
    <label for="name">Convention Name: </label><br>
     <input name="name" id="name" size="30" required></input><br><br>
     <label for="file"> Upload a file: </label><br>
     <input type="file" id="file" name="file" accept=".csv, .sql">
     <br>${message}
     <br><br>
     <input type="submit" value="Add this convention">
  </form>
  <a href=/create_account>Make a new account</a>
</div>

</#assign>
<#include "main.ftl">

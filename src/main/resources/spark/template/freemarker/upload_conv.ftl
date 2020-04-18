<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <h3 for="file">Upload a file:</h3>
  <form method="POST" action="/upload_convention/{id}">
     <input type="file" id="file" name="file" accept=".csv">
     <br><br>
     <p class="errorMessage">${message}</p>
     <br>
     <input type="submit" value="Schedule">
  </form>
</div>

</#assign>
<#include "main.ftl">

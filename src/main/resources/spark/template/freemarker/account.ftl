<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
  <div align="right" class="log-in-info">
    ${currUserMessage}
  </div>
</div>

<div class="main-content">
  <h3>Welcome</h3>
  ${conventionLinks}
  <br>
  <br>
  <p>Create a new convention or schedule exams:</p>
  <a href=/create_convention>Create a new convention</a>
  <br><br>
  <a href=/create_exam_conv>Schedule exams for the college of your choice</a>
</div>

</#assign>
<#include "main.ftl">

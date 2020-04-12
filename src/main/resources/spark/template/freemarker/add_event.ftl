<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <p>
  <h2>${convName}</h2>
  <br>
  <h3>New event:</h3>
  <form method="POST" action="/convention">
    <label for="name">Event name: </label><br>
    <input name="name" id="name" size="30" required><br><br>
    <label for="description">Event description: </label><br>
    <textarea rows="10" cols="30" name="description" id="description"></textarea><br><br>
    <br>
    <label for="attendees">If you already know who is attending this event,
      please enter their names with each name on its own line.  If you do not
      yet know your attendees, you can leave this blank</label>
    <textarea rows="300" cols="30" name="attendees" id="description"></textarea><br><br>
    <input type="submit" value="Add this event">
  </form>
</div>

</#assign>
<#include "main.ftl">

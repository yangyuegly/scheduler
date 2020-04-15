<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <p>
  <h2>${convName}</h2>
  <br>
  <h3>Existing Events:</h2>
  <p id="eventNames"></p>
  <br>
  <br>
  <h3>Add another event:</h3>

    <label for="name">Event name: </label><br>
    <input name="name" id="name" size="30" required><br><br>
    <label for="description">Event description: </label><br>
    <textarea rows="3" cols="80" name="description" id="description"></textarea><br><br>
    <br>
    <label for="attendees">If you already know who is attending this event,
      please enter their names with each name on its own line.  If you do not
      yet know your attendees, you can leave this blank</label>
    <textarea rows="10" cols="80" name="attendees" id="description"></textarea><br><br>
    <button id="addEvent">Add Event</button>

</div>

</#assign>
<#include "main.ftl">

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
  <div id="addEventsDiv">
    <h3 id="addEventLabel">Add another event:</h3>

    <label for="name" id="nameLabel">Event name: </label><br>
    <input name="name" id="name" size="30" required><br><br>
    <label for="description" id="descriptionLabel">Event description: </label><br>
    <textarea rows="3" cols="80" name="description" id="description"></textarea><br><br>
    <br>
    <label for="attendees" id="attendeesLabel">If you already know who is attending this event,
      please enter their names with each name on its own line.  If you do not
      yet know your attendees, you can leave this blank.</label>
    <textarea rows="10" cols="80" name="attendees" id="attendees"></textarea><br><br>
    <button id="addEvent">Add event</button>

    <button id="doneAddingEvents">Done adding events</button>
  </div>

  <div id="completedDiv">
    <p id="saveExpl">To let people register as attendees on our website, send them this link:
      _____________.  If you do this, their preferences will be included in the
      schedule.</p>
    <button id="save">Save and schedule later</button><br>

    <p id="schedExpl">If you have all of your attendees, schedule now!</p>
    <button id="schedule">Schedule now</button>
  </div>
  
</div>

</#assign>
<#include "main.ftl">

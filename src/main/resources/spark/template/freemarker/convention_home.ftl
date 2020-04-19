<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <h2>${convName}</h2>
  <br>
  <h3>Existing Events:</h3>
  <p id="eventNames">No events yet.</p>
  <br>
  <div id="addEventsDiv">
    <h3 id="addEventLabel">Add another event:</h3>

    <label for="name" id="nameLabel">Event name: </label><br>
    <input name="name" id="name" size="30" required><br><br>
    <label for="description" id="descriptionLabel">Event description: </label><br>
    <textarea rows="3" cols="80" name="description" id="description"></textarea><br><br>
    <br>
    <button id="addEvent">Add event</button>

    <button id="doneAddingEvents">Done adding events</button>
  </div>

  <div id="completedDiv">
    <p id="saveExpl">To let people register as attendees on our website, send them this link:
      _____________.  If you do this, their preferences will be included in the
      schedule.</p>
    <button id="save">Save and schedule later</button><br>

  </div>

</div>

</#assign>
<#include "main.ftl">

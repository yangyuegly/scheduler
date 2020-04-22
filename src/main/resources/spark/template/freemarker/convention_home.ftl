<#assign content>

<div class="main-content">
  <h2>${convName}</h2>
  <br>
  <h3>Existing Events:</h3>
  <p id="eventNames">${existingEvents}</p>
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

  <div id="addCollaborator">
  <h3 id="addCollaboratorLabel">Add a collaborator for this convention:</h3>
   <label for="colEmail" id="colEmailLabel">User email: </label><br>
    <input name="colEmail" id="colEmail" size="30" required><br><br>
    <br>
    <button id="addEvent">Add Collaborator</button>

    <button id="doneAddingEvents">Done adding collaborators</button>
  </div>

  <div id="completedDiv">
    <p id="saveExpl">To let people register as attendees on our website, send them this link:
      <a href=/convention_signup/${id} class="errorMessage" id="attendeeSignupLink">localhost:45677/convention_signup/${id}</a>.
      If you do this, their preferences will be included in the
      schedule.</p>
    <button id="save">Save and schedule later</button>
    <label> or <label>
    <button id="schedule">Schedule now</button>
  </div>

</div>

<script src="/js/build_convention.js"></script>

</#assign>
<#include "main.ftl">

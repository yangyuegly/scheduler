<#assign content>

<div class="main-content">
  <h2>${convName}</h2>
  <br>
  <h3>Existing Events:</h3>
  <div id="eventNames">${existingEvents}</div>
  <p id="eventsError" class="errorMessage"></p>

  <br>
  <div id="addEventsDiv">
    <h3 id="addEventLabel">Add another event:</h3>

    <label for="name" id="nameLabel">Event name: </label><br>
    <input name="name" id="name" size="30" required><br><br>
    <label for="description" id="descriptionLabel">Event description: </label><br>
    <textarea rows="3" cols="80" name="description" id="description"></textarea>
    <br><br>
    <button id="addEvent">Add event</button>

    <button id="doneAddingEvents">Done adding events</button>
    <p id="addEventError" class="errorMessage"></p>
  </div>

  <br>

  <div id="completedDiv">
    <h3>Attendees</h3>
    <p id="saveExpl">To let people register as attendees on our website, send them this link:
      <a href=/convention_signup/${id} id="attendeeSignupLink">localhost:45677/convention_signup/${id}</a>.
      If you do this, their preferences will be included in the
      schedule.  </p>
    <button id="save">Save and schedule later</button>
    <label> or <label>
    <button id="schedule">
      <!-- calendar icon -->
      <svg class="bi bi-calendar" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" d="M14 0H2a2 2 0 00-2 2v12a2 2 0 002 2h12a2 2 0 002-2V2a2 2 0 00-2-2zM1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857V3.857z" clip-rule="evenodd"/>
        <path fill-rule="evenodd" d="M6.5 7a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm-9 3a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm-9 3a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd"/>
      </svg>
      Schedule now
    </button>
  </div>

  <br>

  <div id="addCollaboratorDiv">
    <h3 id="addCollaboratorLabel">Add a collaborator for this convention:</h3>
    <label for="colEmail" id="colEmailLabel">User email: </label><br>
    <input name="colEmail" id="colEmail" size="30" required><br><br>
    <button id="addCollaborator">Add Collaborator</button>
    <p id="addCollaboratorError" class="errorMessage"></p>
  </div>

</div>

<script src="/js/websocket.js"></script>
<script src="/js/build_convention.js"></script>

</#assign>
<#include "main.ftl">

<#assign content>

<div class="main-content">
  <h3>Sign up for ${conventionName}:</h3>
  <form method="POST" action="/add_attendee/${id}">
    <label for="eventSelections">Select up to </label>
    <label for="eventSelections" id="maxEventsToSelect">${maxEventsToSelect}</label>
    <label for="eventSelections"> events to attend:</label><br>
    ${eventCheckboxes}
    <br>
    <label id="selectEventsErrorMessage" class="errorMessage"></label>
    <br>
    <br>
    <input type="submit" name="submitType" value="Sign up!">
    <br>
  </form>
</div>

<script src="/js/attendee_event_signup.js"></script>


</#assign>
<#include "main.ftl">

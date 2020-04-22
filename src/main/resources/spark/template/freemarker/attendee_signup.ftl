<#assign content>

<div class="main-content">
  <h3>Sign up for ${conventionName}:</h3>
  <form method="POST" action="/add_attendee/${id}">
    <label for="attendeeName">Enter your name: </label><br>
    <input name="attendeeName" id="attendeeName" size="30" required><br><br>
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


</#assign>
<#include "main.ftl">

<#assign content>

<div class="main-content">
  <h3>Sign up for ${conventionName}:</h3>
  <form method="POST" action="/add_attendee/${id}">
    <label for="eventSelections">Select up to </label>
    <label for="eventSelections" id="maxEventsToSelect">${maxEventsToSelect}</label>
    <label for="eventSelections"> events to attend:</label><br>
    ${eventCheckboxes}
    <br>
    <label for="attendeeEmail">To receive a schedule when this convention is scheduled, please enter your email.</label>
    <br>
    <input name="attendeeEmail" id="attendeeEmail" size="30" placeholder="Email address">
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

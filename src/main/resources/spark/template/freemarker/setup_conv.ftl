<#assign content>

<div class="main-content">
  <h2>New convention:</h2>


  <form id="myForm" onsubmit="return validate()" form method="POST" action="/create_convention/${id}">
    <label for="convName">Convention name: </label><br>
    <input name="convName" id="convName" size="30" required><br><br>
    <label for="startDate">Start date:</label>
    <input type="date" id="startDate" name="startDate" min="${currDay}" max="2100-01-01" required>
    <br><br>
    <label for="numDays">Number of days:</label>
    <input type="number" id="numDays" name="numDays" min="0" step="1" required>
    <br><br>
    <label for="eventDuration">How many minutes long is each event in this convention?</label>
    <input list="duration_suggestions" type="number" name="eventDuration" min="1" required>
    <datalist id="duration_suggestions">
      <option value="30" />
      <option value="60" />
      <option value="90" />
      <option value="120" />
      <option value="180" />
    </datalist>
    <br>
    <br>
    <label>What times can we schedule your events?</label><br>
    <label for="startTime"> Start time: </label>
    <input type="time" id="startTime" name="startTime">
    <label for="endTime"> End time:</label>
    <input type="time" id="endTime" name="endTime">
    <br><br><br>
    <button type="submit" name="submitType" value="Add events by hand">
      <svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
        <path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"/>
        <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"/>
      </svg>
      Add events by hand
    </button>
    <label> or </label>
    <button type="submit" name="submitType" value="Upload a file with all events">
      <svg class="bi bi-box-arrow-up" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" d="M4.646 4.354a.5.5 0 00.708 0L8 1.707l2.646 2.647a.5.5 0 00.708-.708l-3-3a.5.5 0 00-.708 0l-3 3a.5.5 0 000 .708z" clip-rule="evenodd"/>
        <path fill-rule="evenodd" d="M8 11.5a.5.5 0 00.5-.5V2a.5.5 0 00-1 0v9a.5.5 0 00.5.5z" clip-rule="evenodd"/>
        <path fill-rule="evenodd" d="M2.5 14A1.5 1.5 0 004 15.5h8a1.5 1.5 0 001.5-1.5V7A1.5 1.5 0 0012 5.5h-1.5a.5.5 0 000 1H12a.5.5 0 01.5.5v7a.5.5 0 01-.5.5H4a.5.5 0 01-.5-.5V7a.5.5 0 01.5-.5h1.5a.5.5 0 000-1H4A1.5 1.5 0 002.5 7v7z" clip-rule="evenodd"/>
      </svg>
      Upload a file with all events
    </button>
    <br>
  </form>
  <p class="errorMessage">${errorMessage}</p>
</div>

<script src="/js/setup_conv.js"></script>
</#assign>
<#include "main.ftl">

<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
  <div align="right" class="log-in-info">
    ${currUserMessage}
  </div>
</div>

<div class="main-content">
  <h3>New convention:</h3>
  <form method="POST" action="/create_convention/${id}">
    <label for="convName">Convention name: </label><br>
    <input name="convName" id="convName" size="30" required><br><br>
    <label for="startDate">Start date:</label>
    <input type="date" id="startDate" name="startDate" min=${currDay} max="2100-01-01" required>
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
    <br><br>
    <input type="submit" name="submitType" value="Add events by hand">
    <label> or </label>
    <input type="submit" name="submitType" value="Upload a file with all events">
    <br>
  </form>
  <p class="errorMessage">${errorMessage}</p>
</div>


</#assign>
<#include "main.ftl">

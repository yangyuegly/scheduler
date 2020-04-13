<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <p>
  <h3>New convention:</h3>
  <form method="POST" action="/convention_home/${id}">
    <label for="convName">Convention name: </label><br>
    <input name="convName" id="convName" size="30" required><br><br>
    <label for="startDate">Start date:</label>
    <input type="date" id="startDate" name="startDate" min=${currDay} max="2100-01-01" required>
    <label for="endDate">End date:</label>
    <input type="date" id="endDate" name="endDate" min=${currDay} max="2100-01-01" required>
    <br>
    <label for="duration">How many minutes long is each event in this convention?</label>
    <input list="duration_suggestions" type="number" name="duration" min="1" required>
    <datalist id="duration_suggestions">
      <option value="30" />
      <option value="60" />
      <option value="90" />
      <option value="120" />
      <option value="180" />
    </datalist>
    <label>What times can we schedule your events?</label><br>
    <label for="startTime"> start: </label>
    <input type="time" id="startTime" name="startTime">
    <label for="endTime"> end:</label>
    <input type="time" id="endTime" name="endTime"> 

    <input type="submit" value="Add event">
    <br>
  </form>
</div>

</#assign>
<#include "main.ftl">

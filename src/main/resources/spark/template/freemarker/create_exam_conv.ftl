<#assign content>

<div class="main-content">
  <p>
  <h3>Final Exams</h3>
  <br>
  <form method="POST" action="/exam_schedule/${id}">
    <label for="schoolName">What is the name of your school?</label>
    <select id="schoolName" name="schoolName">
      ${schoolSuggestions}
    </select>
    <br>
    <br>
    <label for="startDate">Start date:</label>
    <input type="date" id="startDate" name="startDate" min=${currDay} max="2100-01-01" required>
    <br><br>
    <label for="numDays">Number of days:</label>
    <input type="number" id="numDays" name="numDays" min="0" step="1" required>
    <br><br>
    <label for="eventDuration">How many minutes long is each exam?</label>
    <input list="duration_suggestions" type="number" name="eventDuration" min="1" required>
    <datalist id="duration_suggestions">
      <option value="60" />
      <option value="90" />
      <option value="120" />
      <option value="180" />
    </datalist>
    <br>
    <br>
    <label>What times can we schedule exams?</label><br>
    <label for="startTime"> Start time: </label>
    <input type="time" id="startTime" name="startTime">
    <label for="endTime"> End time:</label>
    <input type="time" id="endTime" name="endTime">
    <br><br>
    <input type="submit" value="Schedule">
  </form>
</div>


</#assign>
<#include "main.ftl">

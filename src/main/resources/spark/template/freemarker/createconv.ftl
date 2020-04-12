<#assign content>

<div class="sched-header">
  <h1 class="project-title">Scheduler</h1>
</div>

<div class="main-content">
  <p>
  <h3>New convention:</h3>
  <form method="POST" action="/login">
    <label for="conv_name">Convention name: </label><br>
    <input name="conv_name" id="conv_name" size="30" required><br><br>
    <label for="start">Start date:</label>
    <input type="date" id="start" name="start" min=${currDay} max="2100-01-01" required>
    <label for="end">End date:</label>
    <input type="date" id="end" name="end" min=${currDay} max="2100-01-01" required>
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

!!!!!!!!!!!!!!!!!!!!!!!!



    <input type="submit" value="Add event">
    <br>
    <input type="submit" value="Done adding events">
  </form>
</div>

</#assign>
<#include "main.ftl">

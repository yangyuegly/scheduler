<#assign content>

<div class="main-content">
  <p>
  <h3>Final Exams</h3>
  <br>
  <form method="POST" onsubmit="return validate()" action="/exam_schedule/${id}">
  <div class="row">
    <div class="col-25">
      <label for="schoolName">What is the name of your school?</label>
    </div>
    <div class="col-75">
      <select id="schoolName" name="schoolName">
        ${schoolSuggestions}
      </select>
    </div>
  </div>

  <div class="row">
    <div class="col-25">
      <label for="startDate">Start date:</label>
    </div>
    <div class="col-75">
      <input type="date" id="startDate" name="startDate" min="${currDay}" max="2100-01-01" class="colInput" required>
    </div>
  </div>

  <div class="row">
    <div class="col-25">
      <label for="numDays">Number of days:</label>
    </div>
    <div class="col-75">
      <input type="number" id="numDays" name="numDays" min="0" step="1" class="colInput" required>
    </div>
  </div>

  <div class="row">
    <div class="col-25">
      <label for="eventDuration">Duration of each event (min):</label>
    </div>
    <div class="col-75">
    <input list="duration_suggestions" type="number" name="eventDuration" id="eventDuration" min="1" class="colInput" required>
    <datalist id="duration_suggestions">
      <option value="30" />
      <option value="60" />
      <option value="90" />
      <option value="120" />
      <option value="180" />
    </datalist>
    </div>
  </div>

  <div class="row">
    <div class="col-25">
      <label for="startTime"> Start time on a given day (hour:min AM/PM): </label>
    </div>
    <div class="col-75">
      <input type="time" id="startTime" name="startTime" class="colInput" required>
    </div>
  </div>

  <div class="row">
    <div class="col-25">
      <label for="endTime"> End time on a given day (hour:min AM/PM): </label>
    </div>
    <div class="col-75">
      <input type="time" id="endTime" name="endTime" class="colInput" required>
    </div>
  </div>

  <div class="row">
    <div class="col-25">
    </div>
    <div class="col-75">
      <button type="submit" value="Schedule">
        <!-- calendar icon -->
        <svg class="bi bi-calendar" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
          <path fill-rule="evenodd" d="M14 0H2a2 2 0 00-2 2v12a2 2 0 002 2h12a2 2 0 002-2V2a2 2 0 00-2-2zM1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857V3.857z" clip-rule="evenodd"/>
          <path fill-rule="evenodd" d="M6.5 7a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm-9 3a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm-9 3a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2zm3 0a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd"/>
        </svg>
        Schedule now
      </button>
    </div>
  </div>

  </form>
  <br>
  <p class="errorMessage">${errorMessage}</p>
</div>

<script src="/js/setup_conv.js"></script>

</#assign>
<#include "main.ftl">

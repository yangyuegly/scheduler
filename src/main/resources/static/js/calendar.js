/*
  The backbone of this eventListener comes from the FullCalendar API,
  and we added the get request.  This is used to display the calendar with
  the final schedule.
*/
document.addEventListener('DOMContentLoaded', function() {
  const url = window.location.href;
  var splitURL = url.split("/");
  var id = splitURL[4];

  $.get("/calendar_events/" + id, response => {
    console.log(response);

    responseObject = JSON.parse(response);

    console.log(responseObject);

    myEvents = responseObject.eventsForSchedule;

    console.log(myEvents);

    parsedEvents = JSON.parse(myEvents);
    startDate = responseObject.defaultDate;

    makeCalendar(startDate, parsedEvents);
  });
});

/*
  This function makes a calendar with the given events.  It takes in the start
  date of the convention and an array of events.
*/
function makeCalendar(startDate, parsedEvents) {
  var calendarEl = document.getElementById('calendar');

  var calendar = new FullCalendar.Calendar(calendarEl, {
    plugins: [ 'interaction', 'dayGrid', 'timeGrid' ],
    defaultView: 'timeGridWeek',
    defaultDate: startDate,
    header: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay'
    },
    events: parsedEvents
  });

  calendar.render();
};

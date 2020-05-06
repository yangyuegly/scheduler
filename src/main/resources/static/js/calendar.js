/*
  The backbone of this eventListener comes from the FullCalendar API,
  and we added the get request.  This is used to display the calendar with
  the final schedule.
*/
document.addEventListener("DOMContentLoaded", function() {
  const url = window.location.href;
  var splitURL = url.split("/");
  var id = splitURL[4];

  $.get("/calendar_events/" + id, (response) => {
    responseObject = JSON.parse(response);

    if (responseObject.error == "") {
      myEvents = responseObject.eventsForSchedule;
      startDate = responseObject.defaultDate;
      makeCalendar(startDate, myEvents);

      $("#emailAtt").css("display", "visible");
      document
        .getElementById("emailAtt")
        .addEventListener("click", (event) =>
          emailAttendees(responseObject.eventsForSchedule)
        );
    } else {
      var error = document.getElementById("error");
      error = responseObject.error;
      $("#calendarError").text(error);
      $("#emailAtt").css("display", "none");
    }
  });
});

/*
  This function makes a calendar with the given events.  It takes in the start
  date of the convention and an array of events.
*/
function makeCalendar(startDate, parsedEvents) {
  var calendarEl = document.getElementById("calendar");

  var calendar = new FullCalendar.Calendar(calendarEl, {
    eventClick: function(info) {
      alert("Event: " + info.event.title);

      // change the border color just for fun
      info.el.style.borderColor = "red";
    },
    plugins: ["interaction", "dayGrid", "timeGrid"],
    defaultView: "dayGridMonth",
    defaultDate: startDate,
    header: {
      left: "prev,next today",
      center: "title",
      right: "dayGridMonth,timeGridWeek,timeGridDay",
    },
    events: parsedEvents,
  });

  calendar.render();
}

/*
  Function that gets called when the email attendees button is clicked.
  This uses a POST request to email the attendees. Takes in the events so we
  can tell attendees when the events are.
*/
const emailAttendees = (events) => {
  const eventsJSON = JSON.stringify(events);
  const postParameters = { events: eventsJSON };
  const url = window.location.href;
  var splitURL = url.split("/");
  var convID = splitURL[4];

  // post request to "/add_event/id with added events
  $.post("/email_attendees/" + convID, postParameters, (responseJSON) => {
    responseObject = JSON.parse(responseJSON);
    message = responseObject.message;
    $("#emailMessage").text(message);
  });
};

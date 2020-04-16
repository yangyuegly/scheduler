// this document is used for the convention_home page when events are being added

// array used to store events that have been added
let existingEvents = [];

// jquery fields used to access HTML objects
const $name = $("#name");
const $description = $("#description");
const $attendees = $("#attendees");
const $eventNames = $("#eventNames");

// this string stores the names of the added events in HTML form
let eventNamesString = "";

/*
  When the document is ready, this runs.
*/
$(document).ready(() => {
  $('#addEvent').click(addEvent);
  $('#doneAddingEvents').click(doneAdding);
  $('#save').click(saveConv);
  $('#schedule').click(schedule);

  // hide HTML elements that are used after all events are added
  $('#completedDiv').css("visibility", "hidden");
});

/*
  Function that gets called when the add event button is clicked.
  This handles adding the new event.
*/
const addEvent = () => {
  let newEvent = [$name.val(), $description.val(), $attendees.val()];
  existingEvents.push(newEvent);

  eventNamesString += "<p>" + $name.val() + "</p>";

  // update the existing events on the page
  $eventNames.html(eventNamesString);

  // clear the input boxes
  $name.val("");
  $description.val("");
  $attendees.val("");
}

/*
  Function that gets called when the done adding button is clicked.
  This handles hiding the HTML objects used to add a new event and
  displays the HTML objects associated with being done adding events.
*/
const doneAdding = () => {
  $('#addEventsDiv').remove();
  $('#completedDiv').css("visibility", "visible");
}

// do!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
const saveConv = () => {
  // build javascript object that contains the data for the POST request.
  const myJson = JSON.stringify(existingEvents);
  const postParameters = { existingEvents: myJson };



  // post request to "/save_convention" with added events
  $.post("/save_convention", postParameters, responseJSON => {
    // Parse the JSON response into a JavaScript object.
    //const responseObject = JSON.parse(responseJSON);

    $.get("/account", response => {});
  });
}

// do!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
const schedule = () => {
  // build javascript object that contains the data for the POST request.
  //const postParameters = { flags: selection.join(" ") };

  //const $message = $("#message");

  // post request to "/setflags" endpoint with toggle settings selected
  //$.post("/setflags", postParameters, responseJSON => {
    // Parse the JSON response into a JavaScript object.
  //  const responseObject = JSON.parse(responseJSON);
  //  $message.html(responseObject.props);
  //});
}

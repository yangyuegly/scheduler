// array used to store events that have been added
let existingEvents = [[]];

const $name = $("#name");
const $description = $("#description");
const $attendees = $("#attendees");
const $eventNames = $("#eventNames");

let eventNamesString = "";

/*
  When the document is ready, this runs.
*/
$(document).ready(() => {
  $('#addEvent').click(addEvent);
});

/*
  Function that gets called when the submit button is clicked.
  This handles adding the new event.
*/
const addEvent = () => {
  let newEvent = [$name.val(), $description.val(), $attendees.val()];
  existingEvents.push(newEvent);

  eventNamesString += $name.val() + "</p><br></p>";

  // update the existing events on the page
  $eventNames.text(eventNamesString);

  // clear the input boxes
  $name.val("");
  $description.val("");
  $attendees.val("");
}


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

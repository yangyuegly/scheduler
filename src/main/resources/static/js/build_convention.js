// this document is used for the convention_home page when events are being added

// array used to store events that have been added
let existingEvents = [];

// jquery fields used to access HTML objects
const $name = $("#name");
const $description = $("#description");
const $eventNames = $("#eventNames");

// this string stores the names of the added events in HTML form
let eventNamesString = "";

/*
  When the document is ready, this runs.
*/

$(document).ready(() => {
  setup_live_event_updates();
  $("#addEvent").click(addEvent);
  $("#doneAddingEvents").click(doneAdding);
  $("#save").click(saveConv);
  $("#schedule").click(schedule);
  // hide HTML elements that are used after all events are added
  $("#completedDiv").css("visibility", "hidden");
});

/*
  Function that gets called when the add event button is clicked.
  This handles adding the new event.
*/
const addEvent = () => {
  let newEvent = [$name.val(), $description.val()];
  existingEvents.push(newEvent);

  console.log("this is name" + $name.val());
  console.log(newEvent);

  
  eventNamesString += "<p>" + $name.val() + "</p>";

  add_event("<p>" + $name.val() + "</p>"); //socket code

  // update the existing events on the page
  $eventNames.html(eventNamesString);

  // clear the input boxes
  $name.val("");
  $description.val("");
};

/*
  Function that gets called when the done adding button is clicked.
  This handles hiding the HTML objects used to add a new event and
  displays the HTML objects associated with being done adding events.
*/
const doneAdding = () => {
  $("#addEventsDiv").css("display", "none");
  $("#completedDiv").css("visibility", "visible");
};

/*
  Function that gets called when the save button is clicked.  This uses a post
  request to send the new events to the program, and then causes the page
  to change to the account page.
*/
const saveConv = () => {
  // build javascript object that contains the data for the POST request.
  const myJson = JSON.stringify(existingEvents);
  const url = window.location.href;
  var splitURL = url.split("/");
  var convID = splitURL[4];

  const postParameters = { existingEvents: myJson, conventionID: convID };

  // post request to "/save_convention" with added events
  $.post("/save_convention", postParameters, (responseJSON) => {});

  // go to the account page
  window.location.pathname = "/account";
};

/*
  Function that gets called when the schedule button is clicked.  This uses a
  post request to send the new events to the program so they can be saved.
  Then, it changes the page to the calendar page.
*/
const schedule = () => {
  // build javascript object that contains the data for the POST request.
  const myJson = JSON.stringify(existingEvents);
  const postParameters = { existingEvents: myJson };

  // post request to "/save_convention" with added events
  $.post("/save_convention", postParameters, (responseJSON) => {});

  const url = window.location.href;
  var splitURL = url.split("/");
  var id = splitURL[4];

  // go to the schedule page
  window.location.pathname = "/schedule/" + id;
};

// this document is used for the convention_home page when events are being added

// array used to store events that have been added
let existingEvents = [];

// jquery fields used to access HTML objects
const $name = $("#name");
const $description = $("#description");
const $eventNames = $("#eventNames");
const $emailInput = $("#colEmail");

// this string stores the names of the added events in HTML form
let eventNamesString = $eventNames.val();

/*
  When the document is ready, this runs.
*/

$(document).ready(() => {
  setup_live_event_updates();
  $("#addEvent").click(addEvent);
  $("#doneAddingEvents").click(doneAdding);
  $("#save").click(saveConvAndGoToAccount);
  $("#schedule").click(schedule);
  $("#addCollaborator").click(addCollaborator);
  // hide HTML elements that are used after all events are added
  $("#completedDiv").css("visibility", "hidden");
});

/*
  Function that gets called when the add event button is clicked.
  This handles adding the new event and making a post request so the event
  is stored in the database.
*/
const addEvent = () => {
  let newEvent = [$name.val(), $description.val()];
  existingEvents.push(newEvent);
  eventNamesString += "<p>" + $name.val() + "</p>";

  add_event("<p>" + $name.val() + "</p>"); //socket code

  const eventJson = JSON.stringify(newEvent);
  const url = window.location.href;
  var splitURL = url.split("/");
  var convID = splitURL[4];
  const postParameters = { event: eventJson, conventionID: convID };

  // post request to "/add_event/id with added events
  $.post("/add_event/" + convID, postParameters, (responseJSON) => {
    if (responseJSON != "") {
      // an error occurred
      responseObject = JSON.parse(responseJSON);

      errorMessage = responseObject.errorMessage;
      $("#addEventError").text(errorMessage);
    } else {
      // update the existing events on the page
      $eventNames.html(eventNamesString);
      $("#addEventError").text("");

      // clear the input boxes
      $name.val("");
      $description.val("");
    }
  });
};

/*
  Function that gets called when the add collaborator button is clicked.
  This uses a POST request to add the collaborator.
*/
const addCollaborator = () => {
  const colEmail = $emailInput.val();

  const postParameters = { colEmail: colEmail };

  // post request to "/add_event/id with added events
  $.post("/add_collaborator/" + convID, postParameters, (responseJSON) => {
    if (responseJSON != "") {
      // an error occurred
      responseObject = JSON.parse(responseJSON);

      errorMessage = responseObject.errorMessage;
      $("#addCollaboratorError").text(errorMessage);
    } else {
      // clear the email input box
      $emailInput.val("");
    }
  });
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
  Function that gets called when the save button is clicked.  This brings the
  user to their account home page.
*/
const saveConvAndGoToAccount = () => {
  // go to the account page
  window.location.pathname = "/account";
};

/*
  Function that gets called when the schedule button is clicked.  This schedules
  the convention and changes the page to the calendar page.
*/
const schedule = () => {
  const url = window.location.href;
  var splitURL = url.split("/");
  var id = splitURL[4];

  // go to the schedule page
  window.location.pathname = "/schedule/" + id;
};

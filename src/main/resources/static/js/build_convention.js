// this document is used for the convention_home page when events are being added

// array used to store events that have been added
let existingEvents = [];

// jquery fields used to access HTML objects
const $name = $("#name");
const $description = $("#description");
const $eventNames = $("#eventNames");
const $emailInput = $("#colEmail");

// this string stores the names of the added events in HTML form
let eventNamesString = $eventNames.html();

/*
  When the document is ready, this runs.
*/
$(document).ready(() => {
  // eventNamesString = $eventNames.html();
  console.log(eventNamesString); // delete



  var coll = document.getElementsByClassName("collapsible");
  var i;

  for (i = 0; i < coll.length; i++) {
    coll[i].addEventListener("click", function() {
      this.classList.toggle("active");
      var content = this.nextElementSibling;
      if (content.style.display === "block") {
        content.style.display = "none";
      } else {
        content.style.display = "block";
      }
    });
  }
  setup_live_event_updates();
  $("#addEvent").click(addEvent);
  $("#doneAddingEvents").click(doneAdding);
  $("#save").click(saveConvAndGoToAccount);
  $("#schedule").click(schedule);
  $("#addCollaborator").click(addCollaborator);
  // hide HTML elements that are used after all events are added
  $("#completedDiv").css("display", "none");
});

/*
  This method updates the eventNamesString, so the new event is added to it.
*/
const updateEventNamesString = () => {
  eventNamesString = $eventNames.html();

  if (eventNamesString == "No events yet.") {
    eventNamesString = "";
  }

  eventNamesString += "<p></p><button type=\"button\" class=\"collapsible\">" + $name.val() + "</button>\r\n"
          + "<div class=\"content\">\r\n" + "<p>";

  if ($description.val() == "") {
    eventNamesString += "No description."
  } else {
    eventNamesString += $description.val();
  }

  eventNamesString += "</p>\r\n" + "</div>";
}

/*
  Function that gets called when the add event button is clicked.
  This handles adding the new event and making a post request so the event
  is stored in the database.
*/
const addEvent = () => {
  if ($name.val() != "") {
    let newEvent = [$name.val(), $description.val()];
    existingEvents.push(newEvent);

    const eventJson = JSON.stringify(newEvent);
    const url = window.location.href;
    var splitURL = url.split("/");
    var convID = splitURL[4];
    const postParameters = { event: eventJson, conventionID: convID };

    // post request to "/add_event/id with added events
    $.post("/add_event/" + convID, postParameters, (responseJSON) => {
      responseObject = JSON.parse(responseJSON);
      errorMessage = responseObject.errorMessage;

      if (errorMessage != "") {
        // an error occurred
        $("#addEventError").text(errorMessage);
      } else {
        // update the existing events on the page
        updateEventNamesString();
        add_event(eventNamesString); //socket code

        // $eventNames.html(eventNamesString);
        $eventNames.replaceWith(eventNamesString);

        console.log(eventNamesString); // delete


        $("#addEventError").text("");

        var coll = document.getElementsByClassName("collapsible");
        var i;

        for (i = 0; i < coll.length; i++) {
          coll[i].addEventListener("click", function() {
            this.classList.toggle("active");
            var content = this.nextElementSibling;
            if (content.style.display === "block") {
              content.style.display = "none";
            } else {
              content.style.display = "block";
            }
          });

          // clear the input boxes
          $name.val("");
          $description.val("");
        }
      }
    });
  }
};

/*
  Function that gets called when the add collaborator button is clicked.
  This uses a POST request to add the collaborator.
*/
const addCollaborator = () => {
  const colEmail = $emailInput.val();

  const postParameters = { colEmail: colEmail };
  const url = window.location.href;
  var splitURL = url.split("/");
  var convID = splitURL[4];

  // post request to "/add_event/id with added events
  $.post("/add_collaborator/" + convID, postParameters, (responseJSON) => {
    responseObject = JSON.parse(responseJSON);
    errorMessage = responseObject.errorMessage;

    if (errorMessage != "") {
      // an error occurred
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
  $("#completedDiv").css("display", "block");
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

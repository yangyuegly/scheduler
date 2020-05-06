const MESSAGE_TYPE = {
  CONNECT: 0,
  EVENT: 1,
  UPDATE: 2,
};

let conn = null;
//id: the unique identifier that the server has assigned to the client

let myId = -1;

// Setup the WebSocket connection for live updating of event list.

//Establish the WebSocket connection and set up event handlers
//onmessage
//property is an EventHandler that is called when a message is received from the server

let setup_live_event_updates = () => {
  conn = new WebSocket(
    "wss://" + window.location.host + window.location.pathname + "/websocket"
  );

  conn.onerror = (err) => {
    console.log("Connection error:", err);
  };

  conn.onmessage = (msg) => {
    console.log("in onmessage"); // delete

    const data = JSON.parse(msg.data);
    console.log("data payload is " + data.payload);
    switch (data.type) {
      default:
        console.log("Unknown message type", data.type);
        break;
      case MESSAGE_TYPE.CONNECT:
        console.log("in connect");
        myId = parseInt(data.payload.id, 10);
        break;
      case MESSAGE_TYPE.UPDATE:
        // parse events
        console.log("update message received");
        console.log("this is the payload" + data.payload.text); //is duplica

        $("#eventNames").html(data.payload.text);
        var collap = document.getElementsByClassName("collapsible");
        var j;

        console.log("collap " + collap); // delete
        console.log("collap length: " + collap.length);

        for (j = 0; j < collap.length; j++) {
          collap[j].addEventListener("click", function() {
            console.log("clicked");

            this.classList.toggle("collap-active");
            var content = this.nextElementSibling;
            console.log("content is " + content);
            if (content.style.display === "block") {
              content.style.display = "none";
            } else {
              content.style.display = "block";
            }
          });
        }
        // $("#eventNames").replaceWith("<div id=\"eventName\">" + data.payload.text + "</div>");
        break;
    }
  };
};

const add_event = (eventNamesString) => {
  console.log("in add_event"); // delete
  console.log("eventNamesString in websocket is " + eventNamesString); //not dupli

  const JSONevents = JSON.stringify({
    type: MESSAGE_TYPE.EVENT,
    payload: {
      id: myId,
      text: eventNamesString,
    },
  });
  console.log("json is " + JSONevents);
  conn.send(JSONevents);
};

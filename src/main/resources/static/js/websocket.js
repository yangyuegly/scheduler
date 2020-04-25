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

ws.onclose = () => alert("WebSocket connection closed");
setup_live_event_updates = () => {
  conn = new WebSocket("ws://" + window.location.href);

  conn.onerror = (err) => {
    console.log("Connection error:", err);
  };
  con.onmessage = (msg) => {
    const data = JSON.parse(msg.data);
    switch (data.type) {
      default:
        console.log("Unknown message type", data.type);
        break;
      case MESSAGE_TYPE.CONNECT:
        myId = parseInt(data.payload.id, 10);
        break;
      case MESSAGE_TYPE.UPDATE:
        // parse events
        $("#eventNames").html(data.payload.text);
        break;
    }
  };
};

const add_event = (eventNamesString) => {
  conn.send(
    JSON.stringify({
      type: MESSAGE_TYPE.EVENT,
      payload: {
        id: myId,
        text: eventNamesString,
      },
    })
  );
};

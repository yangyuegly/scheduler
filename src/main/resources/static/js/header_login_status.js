const $loginStatusLink = $("#loginStatusLink");
const $logoutLink = $("#logoutLink");

/*
  When the document is ready, this runs.  It sets up the part of the header
  describing whether or not the user is logged in.
*/
$(document).ready(() => {
  const cookie = document.cookie;

  if (cookie == "") {
    // the user is not logged in
    $('#notLoggedInDiv').css("display", "block");
    $('#loggedInDiv').css("display", "none");
  } else {
    const splitCookie = cookie.split("=")
    const userEmail = splitCookie[1];
    $('#loggedInDiv').css("display", "block");
    $('#notLoggedInDiv').css("display", "none");
    $loginStatusLink.text(userEmail);
  }
});

// const $loginStatusLink = $("#loginStatusLink");
// const $logoutLink = $("#logoutLink");

/*
  When the document is ready, this runs.  It sets up the part of the header
  describing whether or not the user is logged in.
*/
// $(document).ready(() => {
  // const cookie = document.cookie;
  //
  // if (cookie == "") {
  //   // the user is not logged in
  //   $('#notLoggedInDiv').css("display", "block");
  //   $('#loggedInDiv').css("display", "none");
  // } else {
  //   const splitCookie = cookie.split("=")
  //   const userEmail = splitCookie[1];
  //   $('#loggedInDiv').css("display", "block");
  //   $('#notLoggedInDiv').css("display", "none");
  //   $loginStatusLink.text(userEmail);
  // }
// });


const $loginHeaderLink = $("#loginHeaderLink");
const $logoutHeaderLink = $("#logoutHeaderLink");
const $userDropdown = $("#userDropdown");
const $userDropdownContent = $("#userDropdownContent");
const $userHeaderLink = $("#userHeaderLink");

const cookie = document.cookie;

if (cookie == "") {
  // the user is not logged in
  $loginHeaderLink.css("display", "block");
  //$logoutHeaderLink.css("display", "none");
  $userDropdown.css("display", "none");
} else {
  const splitCookie = cookie.split("=")
  const userEmail = splitCookie[1];

  $logoutHeaderLink.css("display", "block");
  $userDropdown.css("display", "block");
  //$userEmailHeaderLink.text("Logged in as " + userEmail);
  $loginHeaderLink.css("display", "none");
}

/*
  Displays the drop-down menu when the user icon is clicked.  If it is clicked
  while the drop-down is already displayed, the drop-down is hidden.
*/
// $userHeaderLink.hover(function() {
//   $userDropdownContent.css("display", "block");
//   //document.getElementById("userDropdownContent").classList.toggle("show");
// });

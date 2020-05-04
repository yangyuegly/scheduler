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
     $('#notLoggedIn').css("display", "block");
     $('#createAccount').css("display", "block");

     $('#userEmailHeaderLink').css("display", "none");
     $('#logOut').css("display", "none");
   } else {
     const splitCookie = cookie.split("=")
     const userEmail = splitCookie[1];

     $('#notLoggedIn').css("display", "none");
     $('#createAccount').css("display", "none");
     $loginStatusLink.text(userEmail);
   }
 });


const $loginHeaderLink = $("#loginHeaderLink");
const $logoutHeaderLink = $("#logoutHeaderLink");
const $userHeaderLink = $("#userHeaderLink");
const $userEmailHeaderLink = $("#userEmailHeaderLink");

const cookie = document.cookie;

if (cookie != "") {
  const splitCookie = cookie.split("=")
  const userEmail = splitCookie[1];

  $userEmailHeaderLink.text("Logged in as " + userEmail);
}

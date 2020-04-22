// this document is used for the convention_signup page in order to ensure
// no attendee signs up for more than the maximum number of events

/*
  This is run when a checkbox is selected or deselected on the convention_signup
  page.  If there are more checkboxes selected than allowed, it deselects the
  most recently selected event.
*/
$('input[type=checkbox]').on('change', function(event) {
   const maxEventsToSelect = $('#maxEventsToSelect').text();

   if($(this).siblings(':checked').length >= maxEventsToSelect) {
     if (maxEventsToSelect == 1) {
       $('#selectEventsErrorMessage').text("You cannot select more than "
          + maxEventsToSelect + " event");
     } else {
       $('#selectEventsErrorMessage').text("You cannot select more than "
          + maxEventsToSelect + " events");
     }

     this.checked = false;
   } else {
     $('#selectEventsErrorMessage').text("");
   }
});

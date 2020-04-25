// jquery fields used to access HTML objects
const $startTime = $("#startTime");
const $endTime = $("#endTime");


function validate() {
  console.log($startTime.val());
  console.log($endTime.val());
  console.log("done");
  
  
  var startTimeObject = new Date();  
  var splitTime1 = $startTime.val().split(":");
  var hour1 = splitTime1[0];
  var minute1 = splitTime1[1];

  startTimeObject.setHours(hour1, minute1, 0);

 
  var endTimeObject = new Date();
  var splitTime2 = $endTime.val().split(":");
  var hour2 = splitTime2[0];
  var minute2 = splitTime2[1];
  
  endTimeObject.setHours(hour2, minute2, 0);
 
 
  
  if(startTimeObject > endTimeObject)
 {  alert('End time should be after start time.');
 return false;
 } else {
 return true;
 }
}



 
 


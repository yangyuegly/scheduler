<#assign content>

<div class="main-content">
  <h1>Sked</h1>
  <p> Let us help you schedule the events at your next convention.
     We'll make the optimal schedule for all your attendees.
     <br>For Final Exams, Hackathons, Book Fairs, Cheese Tasting Conventions, and everything in between.</p>
 <a href="/login">Login and Start Scheduling!</a>
<!-- Slideshow container -->
<div class="slideshow-container">

  <!-- Full-width images with number and caption text -->
  <div class="mySlides fade">
    <img src="/images/attendees.png" style="width:100%">
    <div class="text">Step 1</div>
  </div>

  <div class="mySlides fade">
    <img src="/images/add_events.png" style="width:100%">
    <div class="text">Step 2</div>
  </div>


  <div class="mySlides fade">
     <img src="/images/create_convention.png" style="width:100%">

    <div class="text">Step 3</div>
  </div>



  <div class="mySlides fade">
     <img src="/images/final_schedule.png" style="width:100%">

    <div class="text">Step 4</div>
  </div>


  <div class="mySlides fade">
     <img src="/images/features.png" style="width:100%">

    <div class="text">More features</div>
  </div>


  <!-- Next and previous buttons -->
  <a class="prev" onclick="plusSlides(-1)">&#10094;</a>
  <a class="next" onclick="plusSlides(1)">&#10095;</a>
</div>
<br>

<!-- The dots/circles -->
<div style="text-align:center">
  <span class="dot" onclick="currentSlide(1)"></span>
  <span class="dot" onclick="currentSlide(2)"></span>
  <span class="dot" onclick="currentSlide(3)"></span>
  <span class="dot" onclick="currentSlide(4)"></span>
  <span class="dot" onclick="currentSlide(5)"></span>
</div>


</div>
 <script src="/js/home.js"></script>
</#assign>
<#include "main.ftl">

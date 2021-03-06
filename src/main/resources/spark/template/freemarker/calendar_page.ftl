<#assign content>

<div class="main-content">

  <html>
    <head>
      <meta charset='utf-8' />

        <style>

          html, body {
            margin: 0;
            padding: 0;
            font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
            font-size: 14px;
          }

          #calendar {
            max-width: 900px;
            margin: 40px auto;
          }

        </style>

        <link href='https://unpkg.com/@fullcalendar/core@4.4.0/main.min.css' rel='stylesheet' />
        <link href='https://unpkg.com/@fullcalendar/daygrid@4.4.0/main.min.css' rel='stylesheet' />
        <link href='https://unpkg.com/@fullcalendar/timegrid@4.4.0/main.min.css' rel='stylesheet' />

        <script src='/js/demo-to-codepen.js'></script>
        <script src='https://unpkg.com/@fullcalendar/core@4.4.0/main.min.js'></script>
        <script src='https://unpkg.com/@fullcalendar/interaction@4.4.0/main.min.js'></script>
        <script src='https://unpkg.com/@fullcalendar/daygrid@4.4.0/main.min.js'></script>
        <script src='https://unpkg.com/@fullcalendar/timegrid@4.4.0/main.min.js'></script>
        <script src="/js/calendar.js"></script>

    </head>

    <body>
      <h2>${name}</h2>
      <button id="emailAtt">Email your attendees this schedule</button>
      <p id='emailMessage'></p>


      <p id="calendarError"></p>

      <div class='demo-topbar'>
      </div>
      <div id='error'></div>
      <div id='calendar'></div>


    </body>


  </html>
</div>
</#assign>
<#include "main.ftl">

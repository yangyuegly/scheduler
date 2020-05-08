<#assign content>
<head>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.css"
    />
    <link rel="stylesheet" href="/css/term_project.css">
</head>
<body>
<div class="main-content">
<h2 class="ui header">
  Welcome
</h2>
<div class="ui link items">
  ${conventionLinks}
  <br>
  <p>Create a new convention or schedule exams:</p>
  <div class="ui buttons">
  <a class="ui teal button" href="/create_convention">Create a new convention</a>
  <div class="or"></div>
  <a href=/create_exam_conv class="ui primary button" >Schedule exams for the college of your choice</a>
</div>


  <br><br>
</div>
</div>
</body>
</#assign>
<#include "main.ftl">

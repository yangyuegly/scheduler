<#assign content>

<div class="main-content">
  <h3 for="file">Upload a file:</h3>
  <form method="POST" action="/upload_convention/${id}" enctype='multipart/form-data'>
     <input type="file" id="file" name="file" accept=".csv">
     <br><br>
     <p class="errorMessage">${message}</p>
     <br>
     <input type="submit" value="Schedule">
  </form>
</div>

</#assign>
<#include "main.ftl">

<#assign content>

<div class="main-content">
  <h2 for="file">Upload a file:</h2>
  <form method="POST" action="/upload_convention/${id}" enctype='multipart/form-data'>
     <input type="file" id="file" name="file" accept=".csv">
     <br><br>
     <p class="errorMessage">${message}</p>
     <input type="submit" value="Schedule">
  </form>
</div>

</#assign>
<#include "main.ftl">

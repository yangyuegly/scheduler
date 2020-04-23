<#assign content>

<div class="main-content">
  <p>
  <h2>Log in:</h2>
  <form method="POST" action="/account">
     <label for="email" class="sr-only">Email address</label>
     <input name="email" id="email" size="30" placeholder="Email address" required><br><br>
     <label for="password" class="sr-only">Password</label>
     <input name="password" id="password" type="password" size="30" placeholder="Password" required>
     <br><br>
     <input type="submit" value="Sign in">
  </form>
  <br>
  <p class="errorMessage">${message}</p>
  <br>
  <a href=/create_account>Make an account</a>
</div>

</#assign>
<#include "main.ftl">

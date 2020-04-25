<#assign content>

<div class="main-content">
  <p>
  <h3>Create a new account:</h3>
  <form method="POST" action="/create_account">
    <label for="email" class="sr-only">Email address</label>
    <input name="email" id="email" size="30" placeholder="Email address" required><br><br>
    <label for="password" class="sr-only">Password</label>
    <input name="password" id="password" type="password" size="30" placeholder="Password" required>
    <br>
    <br>
    <input type="submit" value="Create account">
  </form>
</div>

</#assign>
<#include "main.ftl">

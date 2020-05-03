<#assign content>
<head>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.css"
    />
</head>
<div class="main-content">
<div class="ui middle aligned center aligned grid">
<div class="column">
  <p>
  <h3>Create a new account:</h3>
  <form method="POST" action="/create_account">
      <div class="field">
          <div class="ui left icon input">
               <label for="email" class="sr-only">Email address</label>
                  <i class="user icon"></i>
              <input name="email" id="email" size="30" placeholder="Email address" required><br><br>
          </div>
        </div>

        <div class="field">
          <div class="ui left icon input">
            <i class="lock icon"></i>
                 <label for="password" class="sr-only">Password</label>

                <input name="password" id="password" type="password" size="30" placeholder="Password" required>
          </div>
        </div>
     <br>
     <input type="submit" value="Create account" size="30">
  </form>
  <br>
  <p class="errorMessage">${errorMessage}</p>
  <br>
  </div>
</div>
</div>
</#assign>
<#include "main.ftl">

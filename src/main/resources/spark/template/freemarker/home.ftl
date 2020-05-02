<#assign content>

<div class="main-content">
<head>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.css"
    />
</head>
<div class="ui middle aligned center aligned grid">
  <div class="column">
  <p>
  <h2>Sign in</h2>
  <form method="POST" action="/account">

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

     <br><br>
     <input type="submit" value="Sign in" size="30">
  </form>
  <br>
  <p class="errorMessage">${message}</p>
  <br>
      <div class="ui message">
      New to us? <a href=/create_account>Make an account</a>
    </div>
</div>
</div>
</div>
</#assign>
<#include "main.ftl">

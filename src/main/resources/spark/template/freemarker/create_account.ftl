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
                <button class="unmask" type="button" title="Mask/Unmask password to check content">
                  <svg class="bi bi-eye" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                    <path fill-rule="evenodd" d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.134 13.134 0 001.66 2.043C4.12 11.332 5.88 12.5 8 12.5c2.12 0 3.879-1.168 5.168-2.457A13.134 13.134 0 0014.828 8a13.133 13.133 0 00-1.66-2.043C11.879 4.668 10.119 3.5 8 3.5c-2.12 0-3.879 1.168-5.168 2.457A13.133 13.133 0 001.172 8z" clip-rule="evenodd"/>
                    <path fill-rule="evenodd" d="M8 5.5a2.5 2.5 0 100 5 2.5 2.5 0 000-5zM4.5 8a3.5 3.5 0 117 0 3.5 3.5 0 01-7 0z" clip-rule="evenodd"/>
                 </svg>
               </button>
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

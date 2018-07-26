<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!DOCTYPE html>
<html>
<head>
  <title>Catch-22 Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">Catch-22 Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/admin"> Admin </a>
    <a href="/activity">Activity Feed</a>
    <a href="/hashlist">Hashtags</a>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>About the Catch-22 Chat App</h1>
      <p>
        Here's what this chat app can do':
      </p>

      <ul>
        <li><strong>Admin page:</strong> If you are a user with admin status you can view statistics of our app, like the
        number of user, conversations and messages as well as showing the most active user, showing the newest user,
        and showing the wordiest user</li>
        <li><strong>Profile pages:</strong> The user has the ability to click on the username of a message inside a conversation, this
        will direct you to a profile page corresponding to that user.If the user you click on is the same as you while logedin you can change your biography.</li>
        <li><strong>Activity feed:</strong> Our application can display everything that has happened inside our app, you can see people who has joined,
        see messages and conversations created and links to the conversations mentioned. This activity feed is also displayed in chronological order </li>
        <li><strong>Hashtags:</strong> Our application can Hashtag either conversations by going to the list of conversations and write the hashtag title on the box right beside the name
         of the conversation you want to tag, or in messages if you go to a conversation and write under posting message text and at the end the hashatag you want. i.e: Message: Hello Word! #coding </li>
      </ul>

    </div>
  </div>
</body>
</html>

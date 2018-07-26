<%--
  Copyright 2018 Google Inc.

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
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
String profile = (String) request.getAttribute("profile");
String user = (String) request.getSession().getAttribute("user");
String biography = (String) request.getAttribute("biography");
String profileBio = (String) request.getAttribute("profileBio");
%>

<!DOCTYPE html>
<html>
<head>
  <title> Profile </title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
</head>
<body>
  <nav>
    <a id="navTitle" href="/">Catch-22 Chat App</a>
    <a href="/conversations">Conversations</a>
      <%if (request.getSession().getAttribute("user") != null) { %>
    <a> Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else { %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/admin"> Admin </a>
    <a href="/activity">Activity Feed</a>
    <a href="/hashlist">Hashtags</a>
  </nav>
  <div id= "container">

    <% if (user.equals(profile)) { %>
      <h1> Welcome to your Profile! </h1>
      <h2> Edit your Bio </h2>
      <form action = "/users/<%=user%>" method="POST">
        <input type="text" name="biography">
        <br/>
        <button type="submit">Send</button>
      </form>
      <p> This is your biography: <%= biography %> </p>

    <% } else { %>
      <h1> Welcome to <%=profile%>'s Profile </h1>
      <h2> Biography </h2>

      <% if (profileBio.equals("")) { %>
      <p> <%=profile%> has not set up their biography! </p>
      <% } else { %>
      <p> <%= profileBio%> </p>
      <% } %>

    <% } %>
  </div>
</body>
</html>

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
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%
List<User> users = (List<User>) request.getAttribute("users");
List<Conversation> conversations = (List<Conversation>) request.getAttribute("conversations");
%>
<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App</title>
  <link rel="stylesheet" href="/css/main.css">

  <style>
      #activity {
        background-color: white;
        height: 500px;
        overflow-y: scroll
      }
    </style>

    <script>
      // scroll the activity feed div to the bottom
      function scrollChat() {
        var chatDiv = document.getElementById('activity');
        chatDiv.scrollTop = chatDiv.scrollHeight;
      };
    </script>
</head>
<body onload="scrollChat()">

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
  </nav>

  <div id="container">

    <h1><%= "Acitivity Feed" %>
      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="activity">
      <ul>

    <%
      UserStore inst = UserStore.getInstance();
      for (Conversation conversation : conversations) {
         Instant instant = conversation.getCreationTime();
         Date myDate = Date.from(instant);
    %>
      <li><strong><%= myDate %>:</strong> <%= inst.getUser(conversation.getOwnerId()).getName() + " created a new conversation: " + conversation.getTitle() %></li>
    <%
      }
    %>
      </ul>
    </div>

    <hr/>

    <hr/>

  </div>

</body>
</html>

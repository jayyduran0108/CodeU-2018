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
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.UUID" %>
<%@ page import="codeu.controller.HashtagServlet" %>
<%@ page import="codeu.model.data.Hashtag" %>
<%@ page import="codeu.model.store.basic.HashtagStore" %>
<%@ page import="codeu.controller.ActivityFeedServlet" %>
<%@ page import="java.util.PriorityQueue" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%
String title = (String) request.getAttribute("title");
PriorityQueue<ActivityFeedServlet.Item> hashtags = (PriorityQueue<ActivityFeedServlet.Item>) request.getAttribute("hashtags");
Map<UUID, Object> ids = (Map<UUID, Object>) request.getAttribute("ids");
%>

<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body onload="scrollChat()">

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
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

    <h1>#<%= title %></h1>

    <hr/>
    <p>
      Here's everything under #<%= title %>
    </p>

    <div id="hashtag">
    <%
    ConversationStore ConvInst = ConversationStore.getInstance();
      HashtagStore hashInst = HashtagStore.getInstance();
      UserStore inst = UserStore.getInstance();
      while(!hashtags.isEmpty()){
        ActivityFeedServlet.Item hashtag = hashtags.poll();
        if(ids.get(hashtag.getId()) instanceof Message){
           Message m = (Message) ids.get(hashtag.getId());
           Instant instant = m.getCreationTime();
           Date myDate = Date.from(instant);
    %>
      <li><strong><%= ActivityFeedServlet.DateFormatter.formatDateToString(myDate) %>: </strong>Message: <%= inst.getUser(m.getAuthorId()).getName() + " sent a message in "%><a href="/chat/<%=ConvInst.getConversation(m.getConversationId()).getTitle() %>"><%= ConvInst.getConversation(m.getConversationId()).getTitle() %></a><%= ": \"" + m.getContent() +"\""  %></li>
    <%
      }else{
        Conversation c = (Conversation) ids.get(hashtag.getId());
        Instant instant = c.getCreationTime();
        Date myDate = Date.from(instant);
    %>
      <li><strong><%= ActivityFeedServlet.DateFormatter.formatDateToString(myDate) %>: </strong>Conversation: <%= inst.getUser(c.getOwnerId()).getName() + " created a new conversation: "%><a href="/chat/<%= c.getTitle() %>"><%= c.getTitle() %></a></li>
    <%
        }
      }
    %>

    </div>

    <hr/>

    <hr/>

  </div>

</body>
</html>
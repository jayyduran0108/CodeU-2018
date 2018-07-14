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
<%@ page import="codeu.controller.HashtagServlet" %>
<%@ page import="codeu.model.data.Hashtag" %>
<%@ page import="codeu.model.store.basic.HashtagStore" %>
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
  </nav>

  <div id="container">

    <h1><%= "Hashtag" %></h1>

    <hr/>
    <p>
      Here's everything under "" Tag
    </p>

    <div id="activity">
      <ul>

        <%
          ConversationStore ConvInst = ConversationStore.getInstance();
          UserStore inst = UserStore.getInstance();
          while(!FeedItem.isEmpty()){
            ActivityFeedServlet.Item activity =FeedItem.poll();
            if(ids.get(activity.getId()) instanceof User){
              User u = (User) ids.get(activity.getId());
              Instant instant = u.getCreationTime();
              Date myDate = Date.from(instant);
        %>
          <li><strong><%= ActivityFeedServlet.DateFormatter.formatDateToString(myDate) %>:</strong> <%= u.getName() + " joined! " %></li>
        <%
          }else if(ids.get(activity.getId()) instanceof Message){
            Message m = (Message) ids.get(activity.getId());
            Instant instant = m.getCreationTime();
            Date myDate = Date.from(instant);
        %>
          <li><strong><%= ActivityFeedServlet.DateFormatter.formatDateToString(myDate) %>:</strong> <%= inst.getUser(m.getAuthorId()).getName() + " sent a message in "%><a href="/chat/<%=ConvInst.getConversation(m.getConversationId()).getTitle() %>"><%= ConvInst.getConversation(m.getConversationId()).getTitle() %></a><%= ": \"" + m.getContent() +"\""  %></li>
        <%
          }else{
            Conversation c = (Conversation) ids.get(activity.getId());
            Instant instant = c.getCreationTime();
            Date myDate = Date.from(instant);
        %>
          <li><strong><%= ActivityFeedServlet.DateFormatter.formatDateToString(myDate) %>:</strong> <%= inst.getUser(c.getOwnerId()).getName() + " created a new conversation: "%><a href="/chat/<%= c.getTitle() %>"><%= c.getTitle() %></a></li>
        <%
            }
          }
        %>
      </ul>
    </div>

    <hr/>

    <hr/>

  </div>

</body>
</html>

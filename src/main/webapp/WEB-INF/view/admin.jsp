<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
  <script src= "main/java/codeu/controller/AdminServlet.java"> </script>
</head>
<body>
  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/activityfeed.jsp">Activity Feed</a>
    <a href="/admin.jsp"> Admin </a>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">
      <h1> Administration </h1>
      <hr/>
      <h2> Site Statistics </h2>
      <p> Here are some site stats:
         <ul>
          <li> <strong> Users: </strong> <%= request.getAttribute("numUsers") %> </li>
          <li> <strong> Conversations: </strong> <%= request.getAttribute("numCons") %>  </li>
          <li> <strong> Messages: </strong> <%= request.getAttribute("numMess") %> </li>
          <li> <strong> Most active user: </strong> <%= request.getAttribute("activeUser") %> </li>
          <li> <strong> Newest user: </strong> <%= request.getAttribute("newUser") %></li>
          <li> <strong> Wordiest user: </strong> <%= request.getAttribute("wordyUser") %></li>
        </ul>
      </p>
      <hr/>
  </div>

</body>
</html>

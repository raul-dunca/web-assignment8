<%--
  Created by IntelliJ IDEA.
  User: dunca
  Date: 5/22/2023
  Time: 4:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String user = session.getAttribute("user").toString();
    if (user == null || user.equals("")) {
        response.sendRedirect("/error.jsp");
        return;
    }
%>
<html>
<head>
    <title>Puzzle</title>
  <link rel="stylesheet" type="text/css" href="style.css">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>
  <script src="ajax-utils.js" defer></script>
</head>
<body>
<nav>
    <form class="continue" action="Controller" method="get">
        <input   type="submit" value="Continue the puzzle" id="continue"/>
    </form>
    <form class="reset" action="Controller" method="post">
        <input   type="submit" value="Reset the puzzle" id="reset"/>
    </form>

</nav>
</body>
</html>

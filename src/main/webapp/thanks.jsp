<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="murach.business.User" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Thank You</title>

    <link rel="stylesheet"
          href="main.css">

</head>

<body>

<div class="container">

    <%
        User user =
                (User) request.getAttribute("user");
    %>

    <h1>Thank You!</h1>

    <%
        if (user != null) {
    %>

    <p>
        Thank you,
        <strong>
            <%= user.getFirstName() %>
        </strong>
        <strong>
            <%= user.getLastName() %>
        </strong>
        for joining our email list.
    </p>

    <p>
        A confirmation email has been sent to:
    </p>

    <p class="email">
        <%= user.getEmail() %>
    </p>

    <%
        }

        String errorMessage =
                (String) request.getAttribute("errorMessage");

        if (errorMessage != null) {
    %>

    <div class="error">
        <%= errorMessage %>
    </div>

    <%
        }
    %>

    <a href="index.jsp">
        Back to registration
    </a>

</div>

</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>Email List</title>

    <link rel="stylesheet"
          href="main.css">
</head>

<body>

<div class="container">

    <h1>Join our email list</h1>

    <p>
        Please enter your information below.
    </p>

    <%
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

    <form action="emailList"
          method="post">

        <input type="hidden"
               name="action"
               value="add">

        <div class="form-group">

            <label for="firstName">
                First Name:
            </label>

            <input type="text"
                   id="firstName"
                   name="firstName"
                   required>

        </div>

        <div class="form-group">

            <label for="lastName">
                Last Name:
            </label>

            <input type="text"
                   id="lastName"
                   name="lastName"
                   required>

        </div>

        <div class="form-group">

            <label for="email">
                Email:
            </label>

            <input type="email"
                   id="email"
                   name="email"
                   required>

        </div>

        <button type="submit">
            Join Now
        </button>

    </form>

</div>

</body>
</html>
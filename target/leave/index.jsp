<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <!-- <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css"> -->
     <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">

</head>
<body class="login-page">


<!-- <div class="navbar">
    <h1>Leave Management System</h1>
</div> -->

<!-- <div class="container"> -->
    <div class="login-container">

    <form method="POST" action="<%= request.getContextPath() %>/login" class="login-box">

        <h2>Login</h2>

        <label>User ID</label>
        <input type="number" name="userId" required>

        <label>Password</label>
        <input type="password" name="password" required>

        <button type="submit" class="form-button">Login</button>

        <% String msg = (String) request.getAttribute("message"); %>
        <% if (msg != null) { %>
            <div class="alert alert-error"><%= msg %></div>
        <% } %>

    </form>
</div>

</body>
</html>

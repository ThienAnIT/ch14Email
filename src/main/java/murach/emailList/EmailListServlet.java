package murach.emailList;

import jakarta.mail.MessagingException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import murach.business.User;
import murach.data.UserDB;
import murach.email.MailUtilGmail;

import java.io.IOException;

@WebServlet("/emailList")
public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action =
                request.getParameter("action");

        if (action == null) {
            action = "join";
        }

        String url = "/index.jsp";

        if (action.equals("join")) {

            url = "/index.jsp";

        } else if (action.equals("add")) {

            String firstName =
                    request.getParameter("firstName");

            String lastName =
                    request.getParameter("lastName");

            String email =
                    request.getParameter("email");

            // Validate
            if (firstName == null ||
                    firstName.trim().isEmpty() ||
                    lastName == null ||
                    lastName.trim().isEmpty() ||
                    email == null ||
                    email.trim().isEmpty()) {

                request.setAttribute(
                        "errorMessage",
                        "Please enter all information."
                );

                url = "/index.jsp";

            } else {

                // Check whether email already exists
                if (UserDB.emailExists(email)) {

                    request.setAttribute(
                            "errorMessage",
                            "This email is already registered."
                    );

                    url = "/index.jsp";

                } else {

                    // Create User object
                    User user =
                            new User(
                                    firstName,
                                    lastName,
                                    email
                            );

                    // Save user to PostgreSQL
                    UserDB.insert(user);

                    // Send user to JSP
                    request.setAttribute(
                            "user",
                            user
                    );

                    // Email information
                    String to = email;

                    String from =
                            "YOUR_GMAIL@gmail.com";

                    String subject =
                            "Welcome to our email list";

                    String body =
                            "Dear " + firstName + ",\n\n"
                                    + "Thank you for joining our email list.\n\n"
                                    + "We will send you announcements "
                                    + "about new products and promotions.\n\n"
                                    + "Have a great day!\n\n"
                                    + "Thank you.";

                    boolean isBodyHTML = false;

                    try {

                        MailUtilGmail.sendMail(
                                to,
                                from,
                                subject,
                                body,
                                isBodyHTML
                        );

                    } catch (MessagingException e) {

                        String errorMessage =
                                "Unable to send email. "
                                        + "Please check your mail configuration.";

                        request.setAttribute(
                                "errorMessage",
                                errorMessage
                        );

                        log(
                                "Unable to send email.",
                                e
                        );
                    }

                    url = "/thanks.jsp";
                }
            }
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
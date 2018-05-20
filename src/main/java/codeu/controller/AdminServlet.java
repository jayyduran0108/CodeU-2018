package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;


public class AdminServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /**
   * Set up state for handling login-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user requests the /admin URL. It simply forwards the request to
   * login.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        // List<String> names = new ArrayList<String>();
        // names.add("anne");
        // names.add("jorge");
        // names.add("jeanette");

        String username = (String) request.getSession().getAttribute("user");
        if (username == null && username != "annepham") {
          // user is not logged in, don't let them see the admin page
          response.sendRedirect("/login");
        } else {
          request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        }

        User user = userStore.getUser(username);
        if (user == null && username != "annepham") {
          // user was not found, don't let them see the admin page
          System.out.println("Access Denied: " + username);
          response.sendRedirect("/login");
        } else {
          request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        }
  }

  /**
   * This function fires when a user submits the login form. It gets the username and password from
   * the submitted form data, checks for validity and if correct adds the username to the session so
   * we know the user is logged in.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = request.getParameter("username");
    if (username == "annepham") {
      response.sendRedirect("/admin.jsp");
    }

    /*
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if (!userStore.isUserRegistered(username)) {
      request.setAttribute("error", "That username was not found.");
      request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
      return;
    }

    User user = userStore.getUser(username);

    if (!BCrypt.checkpw(password, user.getPasswordHash())) {
      request.setAttribute("error", "Please enter a correct password.");
      request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
      return;
    }
    */

    request.getSession().setAttribute("user", username);
    response.sendRedirect("/admin.jsp");
  }
}

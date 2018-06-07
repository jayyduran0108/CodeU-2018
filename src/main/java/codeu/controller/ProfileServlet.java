package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ProfileServlet extends HttpServlet {
    
  /** Store class that gives access to Users. */
  private UserStore userStore;

  /**
  * Set up state for handling profile-related requests. This method is only called when running in a
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
  * This function fires when a user navigates to the profile page.
  */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {


    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them add a message
      response.sendRedirect("/login");
      return;
    }
        
    User user = userStore.getUser(username);
    System.out.println(username);
    if (user == null) {
      // user was not found, don't let them add a message
      response.sendRedirect("/login");
      return;
      }

    String requestUrl = request.getRequestURI();
    String profile = requestUrl.substring("/users/".length());
    System.out.println(profile);
    String biography = user.getBiography();

    request.setAttribute("biography", biography);
    request.setAttribute("profile",profile);


    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
      }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    String biography = request.getParameter("biography");

    String requestUrl = request.getRequestURI();
    String profile = requestUrl.substring("/users/".length());
    String username = (String) request.getSession().getAttribute("user");
    User user = userStore.getUser(username);

    request.setAttribute("profile",profile);

    System.out.println(biography);

    String cleanedBiography = Jsoup.clean(biography, Whitelist.none());

    user.setBiography(cleanedBiography);
    request.setAttribute("biography", biography);
    userStore.updateUser(user);

    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }
}
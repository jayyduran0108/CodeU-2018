getUsers()package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;

import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class AdminServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  // Store class that stores datastore objects
  private ConversationStore conStore;
  private MessageStore messStore;

  // Stores hard-coded list of names
  private List<String> names;


  /**
   * Set up state for handling login-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    names = new ArrayList<String>();
    names.add("annepham");
    names.add("jeanette");
    names.add("jorge");

    setConStore(ConversationStore.getInstance());
    setMessStore(MessageStore.getInstance());

  }

  /**
   * Sets the UserStore, ConversationStore, and MessasgeStore used by this servlet.
     This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  void setConStore(ConversationStore conStore) {
    this.conStore = conStore;
  }

  void setMessStore(MessageStore messStore) {
    this.messStore = messStore;
  }

  /**
   * This function fires when a user requests the /admin URL. It simply forwards the request to
   * login.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String username = (String) request.getSession().getAttribute("user");
    if (username == null || !names.contains(username)) {
      // user is not logged in, don't let them see the admin page
      response.sendRedirect("/login");
      return;
    }

    // GETTING THE DATA FOR NUMBER OF USERS, CONVERSATIONS, MESSAGES, & NEWEST USER
    int numUsers = userStore.getUsers().size();
    int numCons = conStore.getAllConversations().size();
    int numMess = messStore.getMessages().size();
    String newestUser = userStore.getNewestUser();

    // GETTING THE MOST ACTIVE USER
    List<Message> allMessages = messStore.getAllMessages();
    Map<UUID, Integer> mostActive = new HashMap<UUID, Integer>();
    for (Message message : allMessages) {
      UUID currentUser = message.getAuthorId();
      if (!mostActive.containsKey(currentUser)) {
        mostActive.put(currentUser, 1);
      } else {
        mostActive.put(currentUser, mostActive.get(currentUser) + 1);
      }
    }

    int max = 0;
    UUID activeUser = allMessages.get(0).getAuthorId();
    for (UUID user : mostActive.keySet()) {
      if (mostActive.get(user) > max) {
        max = mostActive.get(user);
        activeUser = user;
      }
    }
    User userOfActiveUser = userStore.getUser(activeUser);
    String nameOfActiveUser = userOfActiveUser.getName();

    // GETTING THE WORDIEST USER
    Map<UUID, Integer> mostWordy = new HashMap<UUID, Integer>();
    for (Message message : allMessages) {
      UUID currentUser = message.getAuthorId();
      String currentMessage = message.getContent();
      int messageLength = currentMessage.length();
      if (!mostWordy.containsKey(currentUser)) {
        mostWordy.put(currentUser, messageLength);
      } else {
        mostWordy.put(currentUser, mostWordy.get(currentUser) + messageLength);
      }
    }

    int maxWords = 0;
    UUID wordyUser = allMessages.get(0).getAuthorId();
    for (UUID user : mostWordy.keySet()) {
      if (mostWordy.get(user) > max) {
        maxWords = mostWordy.get(user);
        wordyUser = user;
      }
    }
    User userOfWordyUser = userStore.getUser(activeUser);
    String nameOfWordyUser = userOfWordyUser.getName();

    request.setAttribute("numUsers", numUsers);
    request.setAttribute("numCons", numCons);
    request.setAttribute("numMess", numMess);
    request.setAttribute("newUser", newestUser);
    request.setAttribute("activeUser", nameOfActiveUser);
    request.setAttribute("wordyUser", nameOfWordyUser);

    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }
}

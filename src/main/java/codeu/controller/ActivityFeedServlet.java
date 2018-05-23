package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;


import java.io.IOException;
import java.time.Instant;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActivityFeedServlet extends HttpServlet{
  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  @Override
  public void init() throws ServletException{
    super.init();

    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Subclass to combine activities together even if they are from different types.
   */
  public static class Item implements Comparable<Item>{
    Instant instant;
    UUID id;

    public Item(){

    }

    public Item(Message m){
      instant = m.getCreationTime();
      id = m.getId();
    }

    public Item(User u){
      instant = u.getCreationTime();
      id = u.getId();
    }

    public Item(Conversation c){
      instant = c.getCreationTime();
      id = c.getId();
    }

    @Override
    public int compareTo(Item item) {
      if (instant.compareTo(item.instant)>0) {
        return -1;
      } else if (instant.compareTo(item.instant)<0) {
        return 1;
        } else {
        return 0;
      }
    }

    public UUID getId() {
      return id;
    }
  }

  /**
   * This function fires when a user navigates to the activity feed page.
   * It then forwards to activityfeed.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    PriorityQueue<Item> fringe = new PriorityQueue<>();
    Map<UUID, Object> ids = new HashMap<>();

    List<Conversation> conversations = conversationStore.getAllConversations();
    List<User> users = userStore.getUsers();
    List<Message> messages = messageStore.getMessages();

    for(Conversation c: conversations){
      fringe.add(new Item(c));
      ids.put(c.getId(),c);
    }

    for(User u: users){
      fringe.add(new Item(u));
      ids.put(u.getId(),u);
    }

    for(Message m: messages){
      fringe.add(new Item(m));
      ids.put(m.getId(),m);
    }
    
    request.setAttribute("FeedItems", fringe);
    request.setAttribute("ids", ids);
    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
  }
}

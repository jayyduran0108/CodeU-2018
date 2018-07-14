package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Hashtag;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.HashtagStore;

import java.io.IOException;
import java.time.Instant;

import java.util.UUID;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HashtagServlet extends HttpServlet{
  /** Store class that gives access to Hashtags. */
  private HashtagStore hashtagStore;
  private ConversationStore conversationStore;
  private MessageStore messageStore;

  @Override
  public void init() throws ServletException {
    super.init();

    setMessageStore(MessageStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setHashtagStore(HashtagStore.getInstance());
  }

  /**
   * Sets the HashtagStore used by this servlet.
   */
  void setHashtagStore(HashtagStore hashtagStore) {
    this.hashtagStore = hashtagStore;
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
   * This function fires when a user requests the /hashtag URL. It simply forwards the request to
   * hashtag.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    PriorityQueue<ActivityFeedServlet.Item> fringe = new PriorityQueue<>();
    Map<UUID, Object> ids = new HashMap<>();

    List<Conversation> conversations = conversationStore.getAllConversations();
    List<Message> messages = messageStore.getMessages();

    String requestUrl = request.getRequestURI();
    String hashtagTitle = requestUrl.substring("/hashtag/".length());
    Hashtag hashtag = hashtagStore.getHashtagWithHashTitle(hashtagTitle);

    for(Conversation c: conversations){
      /*if(hashtag.getHashTitle().equals("c.getHashtag()")) {*/
        fringe.add(new ActivityFeedServlet.Item(c));
        ids.put(c.getId(), c);

    }

    for(Message m: messages){
      /*if(hashtag.getHashTitle().equals("m.getHashtag()")) {*/
        fringe.add(new ActivityFeedServlet.Item(m));
        ids.put(m.getId(), m);

    }

    if (hashtagTitle == null) {
      // No hashtag Selected
      response.sendRedirect("/conversations");
      return;
    }

    request.setAttribute("hashtags", fringe);
    request.setAttribute("ids", ids);
    request.setAttribute("title",hashtagTitle);

//    Hashtag hashtag = HashtagStore.getHashtag(hashtagTitle);
//    if (hashtag == null) {
//      // hashtag does not exist
//      response.sendRedirect("/conversations");
//      return;
//    }
//
//    // get the username of the profile
//    String requestUrl = request.getRequestURI();
//    String profile = requestUrl.substring("/users/".length());
//    String biography = user.getBiography();
//    // access profile's biography
//    User profilePage = userStore.getUser(profile);
//    String profileBio = profilePage.getBiography();
//    // send these values to jsp file
//    request.setAttribute("profileBio", profileBio);
//    request.setAttribute("biography", biography);
//    request.setAttribute("profile",profile);

    request.getRequestDispatcher("/WEB-INF/view/hashtag.jsp").forward(request, response);
  }
}
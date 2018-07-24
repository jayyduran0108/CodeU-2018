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

import java.util.*;

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

    String requestUrl = request.getRequestURI();
    String hashtagTitle = requestUrl.substring("/hashtag/".length());
    Hashtag hashtag = hashtagStore.getHashtagWithHashTitle(hashtagTitle);

    List<Conversation> conversations1 = new ArrayList<>();
    for(UUID id: hashtag.conversations){
      /*if(hashtag.getHashTitle().equals("c.getHashtag()")) {*/
        conversations1.add(conversationStore.getConversation(id));
    }

    List<Message> messages1 = new ArrayList<>();
    for(UUID id: hashtag.messages){
      /*if(hashtag.getHashTitle().equals("m.getHashtag()")) {*/
      messages1.add(messageStore.getMessage(id));
    }

    if (hashtagTitle == null) {
      // No hashtag Selected
      response.sendRedirect("/conversations");
      return;
    }

    request.setAttribute("conversations1", conversations1);
    request.setAttribute("messages1", messages1);
    request.setAttribute("title",hashtagTitle);

    request.getRequestDispatcher("/WEB-INF/view/hashtag.jsp").forward(request, response);
  }
}
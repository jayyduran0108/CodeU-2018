package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.Date;
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
     * This function fires when a user navigates to the activity feed page. It gets the conversation title from
     * the URL, finds the corresponding Conversation, and fetches the messages in that Conversation.
     * It then forwards to activityfeed.jsp for rendering.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String requestUrl = request.getRequestURI();
        String conversationTitle = requestUrl.substring("/chat/".length());

        String username = (String) request.getSession().getAttribute("user");

        Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);

        List<Message> messages = null;
        if (conversation != null) {
            UUID conversationId = conversation.getId();

            messages = messageStore.getMessagesInConversation(conversationId);


            request.setAttribute("conversation", conversation);
        }

        if (username != null) {
            // user is not logged in, don't let them create a conversation
            request.setAttribute("user", username);
        }


        request.setAttribute("messages", messages);
        request.getRequestDispatcher("/WEB-INF/activityfeed.jsp").forward(request, response);
    }

    /**
     * This function fires when a user submits the form on the chat page. It gets the logged-in
     * username from the session, the conversation title from the URL, and the chat message from the
     * submitted form data. It creates a new Message from that data, adds it to the model, and then
     * redirects back to the chat page.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String username = (String) request.getSession().getAttribute("user");

        User user = userStore.getUser(username);

        String requestUrl = request.getRequestURI();
        String conversationTitle = requestUrl.substring("/chat/".length());

        Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);

        String messageContent = request.getParameter("message");

        // this removes any HTML from the message content
        String cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());

        Message message =
                new Message(
                        UUID.randomUUID(),
                        conversation.getId(),
                        user.getId(),
                        cleanedMessageContent,
                        Instant.now());

        messageStore.addMessage(message);

        // redirect to a GET request
        response.sendRedirect("/chat/" + conversationTitle);
    }
}

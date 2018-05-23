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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ActivityFeedServletTest {
  private ActivityFeedServlet activityFeedServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    activityFeedServlet = new ActivityFeedServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    activityFeedServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    activityFeedServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    activityFeedServlet.setUserStore(mockUserStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {

    UUID fakeConversationId = UUID.randomUUID();
    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now());
    Mockito.when(mockConversationStore.getConversationWithTitle("test_conversation"))
        .thenReturn(fakeConversation);

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(
            UUID.randomUUID(),
            fakeConversationId,
            UUID.randomUUID(),
            "test message",
            Instant.now()));
    fakeMessageList = mockMessageStore.getMessages();
    Mockito.when(mockMessageStore.getMessages())
        .thenReturn(fakeMessageList);
    List<Message> fakeMessages = mockMessageStore.getMessages();
    List<Conversation> fakeConversations = mockConversationStore.getAllConversations();
    List<User> fakeUsers = mockUserStore.getUsers();
    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversations);

    PriorityQueue<ActivityFeedServlet.Item> fringe = new PriorityQueue<>();
    Map<UUID,Object> ids = new HashMap<>();

    for(Message m: fakeMessages){
      fringe.add(new ActivityFeedServlet.Item(m));
      ids.put(m.getId(),m);
    }

    for(User u: fakeUsers){
      fringe.add(new ActivityFeedServlet.Item(u));
      ids.put(u.getId(),u);
    }

    for(Conversation c: fakeConversations){
      fringe.add(new ActivityFeedServlet.Item(c));
      ids.put(c.getId(),c);
    }

    activityFeedServlet.doGet(mockRequest, mockResponse);

    //Mockito.verify(mockRequest).setAttribute("FeedItems", fringe);
    Mockito.verify(mockRequest).setAttribute("ids", ids);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}

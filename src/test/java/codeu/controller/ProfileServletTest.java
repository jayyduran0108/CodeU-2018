package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

public class ProfileServletTest {
    
  private ProfileServlet profileServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    profileServlet = new ProfileServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockUserStore = Mockito.mock(UserStore.class);
    profileServlet.setUserStore(mockUserStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now(),
            "");
    String fakeBiography = fakeUser.getBiography();

    Mockito.when(mockRequest.getSession().getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/test_profile");

    profileServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("biography", fakeBiography);
    Mockito.verify(mockRequest).setAttribute("profile", "test_profile");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);


  }

  @Test
  public void testPost_CreateBiography() throws IOException, ServletException {
    User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now(),
            "");

    User newFakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now(),
            "This is my fake biography");

    String fakeBiography = fakeUser.getBiography();

    Mockito.when(mockRequest.getParameter("biography")).thenReturn("This is my fake biography");

    Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/test_profile");
    Mockito.when(mockRequest.getSession().getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);


    profileServlet.doPost(mockRequest, mockResponse);
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    Mockito.verify(mockUserStore).updateUser(userArgumentCaptor.capture());

    Mockito.verify(mockRequest).setAttribute("profile", "test_profile");
    Mockito.verify(mockRequest).setAttribute("biography", userArgumentCaptor.getValue().getBiography());

    Assert.assertEquals(
        "This is my fake biography", userArgumentCaptor.getValue().getBiography());

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);



  }
}

// Copyright 2018 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Hashtag;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.HashtagStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the hashtags page. */
public class HashListServlet extends HttpServlet {

  /**
   * Store class that gives access to Hashtags.
   */
  private HashtagStore hashtagStore;

  /**
   * Set up state for handling hashtag-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setHashtagStore(HashtagStore.getInstance());
  }


  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setHashtagStore(HashtagStore hashtagStore) {
    this.hashtagStore = hashtagStore;
  }

  /**
   * This function fires when a user navigates to the hashtags page. It gets all of the
   * hashtags from the model and forwards to conversations.jsp for rendering the list.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    List<Hashtag> hashtags = hashtagStore.getAllHashtags();
    request.setAttribute("hashtags", hashtags);
    request.getRequestDispatcher("/WEB-INF/view/hashlist.jsp").forward(request, response);
  }

}

// Copyright 2017 Google Inc.
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

package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

/**
 * Class representing a hashtag
 */
public class Hashtag {
  public final UUID id;
  public final UUID poster;
  public final Instant creation;
  public final String hashTitle;
  public final Set<UUID> messages;
  public final Set<UUID> conversations;

  /**
   * Constructs a new Hashtag.
   *
   * @param id the ID of this Hashtag
   * @param owner the ID of the User who created this Hashtag
   * @param title the title of this Hashtag
   * @param creation the creation time of this Hashtag
   */
  public Hashtag(UUID id, UUID poster, String hashTitle, Instant creation) {
    this.id = id;
    this.poster = poster;
    this.creation = creation;
    this.hashTitle = hashTitle;
    this.conversations = new HashSet<>();
    this.messages = new HashSet<>();
  }

  /** Returns the ID of this Hashtag. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the User who created this Hashtag. */
  public UUID getPosterId() {
    return poster;
  }

  /** Returns the title of this Hashtag. */
  public String getHashTitle() {
    return hashTitle;
  }

  /** Returns the creation time of this Hashtag. */
  public Instant getCreationTime() {
    return creation;
  }

  public void addConversation(UUID id) {
    conversations.add(id);
  }

  public void addMessage(UUID id) {
    messages.add(id);
  }

  public void removeConversation(UUID id) {
    conversations.remove(id);
  }

  public void removeMessage(UUID id) {
    messages.remove(id);
  }
}
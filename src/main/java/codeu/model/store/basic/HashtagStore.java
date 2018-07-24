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

package codeu.model.store.basic;

import codeu.model.data.Hashtag;

import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class HashtagStore {

  /** Singleton instance of HashtagStore. */
  private static HashtagStore instance;

  /**
   * Returns the singleton instance of HashtagStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static HashtagStore getInstance() {
    if (instance == null) {
      instance = new HashtagStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static HashtagStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new HashtagStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Hashtags from and saving Hashtags
   * to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Hashtags. */
  private List<Hashtag> hashtags;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private HashtagStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    hashtags = new ArrayList<>();
  }

  /** Access the current set of hashtags known to the application. */
  public List<Hashtag> getAllHashtags() {
    return hashtags;
  }

  /** Add a new hashtag to the current set of hashtags known to the application. */
  public void addHashtag(Hashtag hashtag) {
    hashtags.add(hashtag);
    persistentStorageAgent.writeThrough(hashtag);
  }

  /** Check whether a Hashtag title is already known to the application. */
  public boolean isTitleTaken(String title) {
    // This approach will be pretty slow if we have many Conversations.
    for (Hashtag hashtag : hashtags) {
      if (hashtag.getHashTitle().equals(title)) {
        return true;
      }
    }
    return false;
  }

  /** Find and return the Hashtag with the given title. */
  public Hashtag getHashtagWithHashTitle(String hashTitle) {
    for (Hashtag hashtag : hashtags) {
      if (hashtag.getHashTitle().equals(hashTitle)) {
        return hashtag;
      }
    }
    return null;
  }

  /** Sets the List of hashtags stored by this HashtagStore. */
  public void setHashtags(List<Hashtag> hashtags) {
    this.hashtags = hashtags;
  }

  /**
   * Access the Hashtag object with the given UUID.
   *
   * @return null if the UUID does not match any existing Conversation.
   */
  public Hashtag getHashtag(UUID id) {
    for (Hashtag hashtag : hashtags) {
      if (hashtag.getId().equals(id)) {
        return hashtag;
      }
    }
    return null;
  }

  public List<Hashtag> getHashtagsFromContent(String title) {
    String[] hashtagList = title.split(" ");
    List<Hashtag> hashtagResults = new ArrayList();
    for (String hash : hashtagList) {
      if (hash.startsWith("#")) {
        String hashtagName = hash.substring(hash.indexOf("#")+1);
        hashtagResults.add(hashtagName);
      }
    }
    return hashtagResults;
  }

}

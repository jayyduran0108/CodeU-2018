package codeu.model.store.basic;

import codeu.model.data.Hashtag;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class HashtagStoreTest {

  private HashtagStore hashtagStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final Hashtag HASHTAG_ONE =
      new Hashtag(
          UUID.randomUUID(), UUID.randomUUID(), "hashtag_one", Instant.ofEpochMilli(1000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    hashtagStore = HashtagStore.getTestInstance(mockPersistentStorageAgent);

    final Set<Hashtag> hashtagSet = new HashSet<>();
    hashtagSet.add(HASHTAG_ONE);
    hashtagStore.setHashtags(hashtagSet);
  }

  @Test
  public void testGetHashtagWithHashTitle_found() {
    Hashtag resultHashtag =
        hashtagStore.getHashtagWithHashTitle(HASHTAG_ONE.getHashTitle());

    assertEquals(HASHTAG_ONE, resultHashtag);
  }

  @Test
  public void testGetHashtagWithHashTitle_notFound() {
    Hashtag resultHashtag = hashtagStore.getHashtagWithHashTitle("unfound_title");

    Assert.assertNull(resultHashtag);
  }

  @Test
  public void testIsTitleTaken_true() {
    boolean isTitleTaken = hashtagStore.isTitleTaken(HASHTAG_ONE.getHashTitle());

    Assert.assertTrue(isTitleTaken);
  }

  @Test
  public void testIsTitleTaken_false() {
    boolean isTitleTaken = hashtagStore.isTitleTaken("unfound_title");

    Assert.assertFalse(isTitleTaken);
  }

  @Test
  public void testAddHashtag() {
    Hashtag inputHashtag =
        new Hashtag(UUID.randomUUID(), UUID.randomUUID(), "test_hashtag", Instant.now());

    hashtagStore.addHashtag(inputHashtag);
    Hashtag resultHashtag =
        hashtagStore.getHashtagWithHashTitle("test_hashtag");

    assertEquals(inputHashtag, resultHashtag);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputHashtag);
  }

  private void assertEquals(Hashtag expectedHashtag, Hashtag actualHashtag) {
    Assert.assertEquals(expectedHashtag.getId(), actualHashtag.getId());
    Assert.assertEquals(expectedHashtag.getPosterId(), actualHashtag.getPosterId());
    Assert.assertEquals(expectedHashtag.getHashTitle(), actualHashtag.getHashTitle());
    Assert.assertEquals(
        expectedHashtag.getCreationTime(), actualHashtag.getCreationTime());
  }
}

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
import org.junit.Assert;
import org.junit.Test;

public class HashtagTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID poster = UUID.randomUUID();
    String hashTitle = "Test_HashTitle";
    Instant creation = Instant.now();

    Hashtag hashtag = new Hashtag(id, poster, hashTitle, creation);

    Assert.assertEquals(id, hashtag.getId());
    Assert.assertEquals(poster, hashtag.getPosterId());
    Assert.assertEquals(hashTitle, hashtag.getHashTitle());
    Assert.assertEquals(creation, hashtag.getCreationTime());
  }
}

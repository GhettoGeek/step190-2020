package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalURLFetchServiceTestConfig;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CandidateTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
          new LocalDatastoreServiceTestConfig(), new LocalURLFetchServiceTestConfig());

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testFromJsonObject() {
    JSONObject candidateJSON =
        new JSONObject(
            "{\""
                + Candidate.NAME_JSON_KEYWORD
                + "\":\"Jane Doe\",\""
                + Candidate.PARTY_JSON_KEYWORD
                + "\":\"Green Party\",\""
                + Candidate.CAMPAIGN_URL_JSON_KEYWORD
                + "\":\"www.janedoe.org\"}");

    Candidate newCandidate = Candidate.fromJSONObject(candidateJSON);
    Assert.assertEquals(newCandidate.getName(), "Jane Doe");
    Assert.assertEquals(newCandidate.getPartyAffiliation(), "Green Party");
    Assert.assertEquals(newCandidate.getCampaignSite(), "www.janedoe.org");
  }

  @Test
  public void testAddToDatastore() throws Exception {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Candidate newCandidate =
        Candidate.builder()
            .setName("Jane Doe")
            .setPartyAffiliation("Green")
            .setPlatformDescription("")
            .setCampaignSite("www.janedoe.org")
            .build();
    long candidateId = newCandidate.addToDatastore(ds);
    Key candidateKey = KeyFactory.createKey(Candidate.ENTITY_KIND, candidateId);
    Entity candidateEntity = ds.get(candidateKey);
    Assert.assertEquals(
        candidateEntity.getProperty(Candidate.NAME_ENTITY_KEYWORD), newCandidate.getName());
    Assert.assertEquals(
        candidateEntity.getProperty(Candidate.PARTY_ENTITY_KEYWORD),
        newCandidate.getPartyAffiliation());
    Assert.assertEquals(
        candidateEntity.getProperty(Candidate.CAMPAIGN_URL_ENTITY_KEYWORD),
        newCandidate.getCampaignSite());
    Assert.assertEquals(
        candidateEntity.getProperty(Candidate.PLATFORM_ENTITY_KEYWORD),
        newCandidate.getPlatformDescription());
  }
}
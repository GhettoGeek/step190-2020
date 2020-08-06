// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.auto.value.AutoValue;
import org.json.JSONException;
import org.json.JSONObject;

/** A candidate for a public office position that will appear on voter ballots */
@AutoValue
public abstract class Candidate {
  public abstract String getName();

  public abstract String getPartyAffiliation();

  public abstract String getCampaignSite();

  public abstract String getPlatformDescription();

  public static Builder builder() {
    return new AutoValue_Candidate.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String name);

    public abstract Builder setPartyAffiliation(String partyAffiliation);

    public abstract Builder setCampaignSite(String campaignSite);

    public abstract Builder setPlatformDescription(String platformDescription);

    public abstract Candidate build();
  }

  // creates a new Candidate object by extracting the properties from "candidateData"
  public static Candidate fromJSONObject(JSONObject candidateData) throws JSONException {
    return Candidate.builder()
        .setName(candidateData.getString("name"))
        .setPartyAffiliation(candidateData.getString("party"))
        // TODO(gianelgado): get values for campaignSite and platformDescription
        .setCampaignSite("")
        .setPlatformDescription("")
        .build();
  }

  // Converts the Candidate into a Datastore Entity and puts the Entity into the given Datastore
  // instance.
  public long addToDatastore(DatastoreService datastore) {
    Entity entity = new Entity("Candidate");
    entity.setProperty("name", this.getName());
    entity.setProperty("partyAffiliation", this.getPartyAffiliation());
    entity.setProperty("campaignSite", this.getCampaignSite());
    entity.setProperty("platformDescription", this.getPlatformDescription());
    datastore.put(entity);
    return entity.getKey().getId();
  }
}
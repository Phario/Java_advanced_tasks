package pl.pwr.ite.dynak.requester.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StarshipData(
        String name,

        @JsonProperty("crew")
        String crewCapacity
) {}
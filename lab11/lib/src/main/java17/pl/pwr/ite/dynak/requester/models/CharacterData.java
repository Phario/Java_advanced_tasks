package pl.pwr.ite.dynak.requester.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CharacterData(
        String name,
        String gender,
        String height
) {}
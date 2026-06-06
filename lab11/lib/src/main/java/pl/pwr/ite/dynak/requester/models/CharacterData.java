package pl.pwr.ite.dynak.requester.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterData {
    private String name;
    private String gender;
    private String height;

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }
}

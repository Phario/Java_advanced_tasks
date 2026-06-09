package pl.pwr.ite.dynak.lib.utils;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecallResponse {
    @XmlElement(name = "row")
    private List<Recall> recalls;

    public List<Recall> getRecalls() {
        return recalls;
    }
}

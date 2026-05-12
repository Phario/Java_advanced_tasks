package pl.pwr.ite.dynak.lib.utils;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "recall_link")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecallLink {
    @XmlAttribute(name = "description")
    private String description;

    @XmlAttribute(name = "url")
    private String url;
}

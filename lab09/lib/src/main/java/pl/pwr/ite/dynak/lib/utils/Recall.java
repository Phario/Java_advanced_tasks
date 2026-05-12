package pl.pwr.ite.dynak.lib.utils;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
public class Recall {

    @XmlElement(name = "report_received_date")
    private String reportReceivedDate;

    @XmlElement(name = "nhtsa_id")
    private String nhtsaId;

    @XmlElement(name = "recall_link")
    private RecallLink recallLink;

    @XmlElement(name = "manufacturer")
    private String manufacturer;

    @XmlElement(name = "subject")
    private String subject;

    @XmlElement(name = "component")
    private String component;

    @XmlElement(name = "mfr_campaign_number")
    private String mfrCampaignNumber;

    @XmlElement(name = "recall_type")
    private String recallType;

    @XmlElement(name = "potentially_affected")
    private int potentiallyAffected;

    @XmlElement(name = "defect_summary")
    private String defectSummary;

    @XmlElement(name = "consequence_summary")
    private String consequenceSummary;

    @XmlElement(name = "corrective_action")
    private String correctiveAction;

    @XmlElement(name = "fire_risk_when_parked")
    private String fireRiskWhenParked;

    @XmlElement(name = "do_not_drive")
    private String doNotDrive;

}
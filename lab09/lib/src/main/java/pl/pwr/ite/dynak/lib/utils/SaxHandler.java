package pl.pwr.ite.dynak.lib.utils;

import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SaxHandler extends DefaultHandler {
    private StringBuilder currentValue;
    private List<Recall> recalls = new ArrayList<>();
    private Recall currentRecall;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentValue = new StringBuilder();

        switch (qName) {
            case "row" -> currentRecall = new Recall();
            case "recall_link" -> {
                if (currentRecall != null) {
                    RecallLink link = new RecallLink();
                    link.setDescription(attributes.getValue("description"));
                    link.setUrl(attributes.getValue("url"));
                    currentRecall.setRecallLink(link);
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        currentValue.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (currentRecall == null) return;

        String value = currentValue.toString().trim();

        switch (qName) {
            case "row" -> {
                recalls.add(currentRecall);
                currentRecall = null;
            }
            case "report_received_date"  -> currentRecall.setReportReceivedDate(value);
            case "nhtsa_id"              -> currentRecall.setNhtsaId(value);
            case "manufacturer"          -> currentRecall.setManufacturer(value);
            case "subject"               -> currentRecall.setSubject(value);
            case "component"             -> currentRecall.setComponent(value);
            case "mfr_campaign_number"   -> currentRecall.setMfrCampaignNumber(value);
            case "recall_type"           -> currentRecall.setRecallType(value);
            case "potentially_affected"  -> currentRecall.setPotentiallyAffected(Integer.parseInt(value));
            case "defect_summary"        -> currentRecall.setDefectSummary(value);
            case "consequence_summary"   -> currentRecall.setConsequenceSummary(value);
            case "corrective_action"     -> currentRecall.setCorrectiveAction(value);
            case "fire_risk_when_parked" -> currentRecall.setFireRiskWhenParked(value);
            case "do_not_drive"          -> currentRecall.setDoNotDrive(value);
        }
    }

    public RecallResponse getRecalls() {
        RecallResponse recallResponse = new RecallResponse();
        recallResponse.setRecalls(recalls);
        return recallResponse;
    }
}

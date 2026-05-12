package pl.pwr.ite.dynak.lib.processors;

import jakarta.xml.bind.JAXBException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.pwr.ite.dynak.lib.utils.Recall;
import pl.pwr.ite.dynak.lib.utils.RecallResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomProcessor implements IProcessor {
    @Override
    public RecallResponse process(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("row");
        List<Recall> recalls = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            var node = nodeList.item(i);

            Recall recall = new Recall();

            Element element = (Element) node;

            recall.setReportReceivedDate(getTagValue("report_received_date", element));
            recall.setNhtsaId(getTagValue("nhtsa_id", element));
            recall.setManufacturer(getTagValue("manufacturer", element));
            recall.setSubject(getTagValue("subject", element));
            recall.setComponent(getTagValue("component", element));
            recall.setMfrCampaignNumber(getTagValue("mfr_campaign_number", element));
            recall.setRecallType(getTagValue("recall_type", element));
            recall.setDefectSummary(getTagValue("defect_summary", element));
            recall.setConsequenceSummary(getTagValue("consequence_summary", element));
            recall.setCorrectiveAction(getTagValue("corrective_action", element));
            recall.setFireRiskWhenParked(getTagValue("fire_risk_when_parked", element));
            recall.setDoNotDrive(getTagValue("do_not_drive", element));

            String affected = getTagValue("potentially_affected", element);
            if (affected != null) {
                recall.setPotentiallyAffected(Integer.parseInt(affected));
            }

            recalls.add(recall);
        }

        RecallResponse recallResponse = new RecallResponse();
        recallResponse.setRecalls(recalls);

        return recallResponse;
    }

    private String getTagValue(String attributeName, Element element) {
        NodeList nodeList = element.getElementsByTagName(attributeName);

        if (nodeList.getLength() == 0) {
            return null;
        }

        return nodeList.item(0).getTextContent();
    }
}

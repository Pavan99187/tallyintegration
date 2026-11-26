
package com.example.tallyintegration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
public class TallyMasterExistenceService {


    @Value("${companyName}")
    private String companyName;

    private final RestClient restClient;
    private final TallyXmlService tallyXmlService;

    public TallyMasterExistenceService(RestClient restClient, TallyXmlService tallyXmlService) {
        this.restClient = restClient;
        this.tallyXmlService = tallyXmlService;
    }

    public boolean unitExists(String unitName) {

        return masterExists("Unit", "PO Unit Collection", unitName);
    }

    public boolean stockGroupExists(String stockGroupName) {
        return masterExists(
                "Stock Group", "PO Stock Group Collection", stockGroupName);
    }


    public boolean godownExists(String godownName) {
        return masterExists("Godown", "PO Godown Collection", godownName);
    }


    public boolean stockItemExists(String stockItemName) {
        return masterExists(
                "Stock Item", "PO Stock Item Collection", stockItemName);
    }


    private boolean masterExists(
            String masterType,
            String collectionName,
            String masterName) {

        if (masterName == null || masterName.isBlank()) {
            throw new IllegalArgumentException(
                    masterType + " name cannot be empty");
        }

        String xml = """
            <ENVELOPE>
                <HEADER>
                    <VERSION>1</VERSION>
                    <TALLYREQUEST>EXPORT</TALLYREQUEST>
                    <TYPE>COLLECTION</TYPE>
                    <ID>%s</ID>
                </HEADER>
                <BODY>
                    <DESC>
                        <STATICVARIABLES>
                            <SVCurrentCompany>%s</SVCurrentCompany>
                            <SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT>
                        </STATICVARIABLES>
                        <TDL>
                            <TDLMESSAGE>
                                <COLLECTION NAME="%s"
                                            ISINITIALIZE="Yes">
                                    <TYPE>%s</TYPE>
                                    <NATIVEMETHOD>Name</NATIVEMETHOD>
                                </COLLECTION>
                            </TDLMESSAGE>
                        </TDL>
                    </DESC>
                </BODY>
            </ENVELOPE>
            """.formatted(
                escapeXml(collectionName),
                escapeXml(companyName),
                escapeXml(collectionName),
                escapeXml(masterType)
        );

        String response;

        try {
            response = tallyXmlService.sendRequest(xml);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to check " + masterType
                            + " in Tally: " + e.getMessage(), e);
        }

        return containsMaster(response, masterName);
    }

    private boolean containsMaster(String response, String masterName) {

        if (response == null || response.isBlank()) {
            throw new IllegalStateException(
                    "Empty response received from Tally");
        }

        try {
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            factory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true);

            factory.setFeature(
                    "http://xml.org/sax/features/external-general-entities",
                    false);

            factory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities",
                    false);

            factory.setAttribute(
                    XMLConstants.ACCESS_EXTERNAL_DTD, "");

            factory.setAttribute(
                    XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            Document document = factory.newDocumentBuilder()
                    .parse(new ByteArrayInputStream(
                            response.getBytes(StandardCharsets.UTF_8)));

            NodeList statuses = document.getElementsByTagName("STATUS");

            if (statuses.getLength() > 0
                    && "0".equals(statuses.item(0)
                    .getTextContent().trim())) {

                throw new IllegalStateException(
                        "Tally export failed: " + response);
            }

            NodeList lineErrors =
                    document.getElementsByTagName("LINEERROR");

            if (lineErrors.getLength() > 0) {
                throw new IllegalStateException(
                        "Tally error: "
                                + lineErrors.item(0).getTextContent());
            }

            // Check master NAME attributes, e.g. <UNIT NAME="Mtr">
            NodeList elements = document.getElementsByTagName("*");

            for (int i = 0; i < elements.getLength(); i++) {

                Element element = (Element) elements.item(i);

                String name = element.getAttribute("NAME");

                if (!name.isBlank()
                        && name.trim().equalsIgnoreCase(masterName.trim())) {
                    return true;
                }

                // Also support NAME child elements
                if ("NAME".equalsIgnoreCase(element.getTagName())
                        && element.getTextContent().trim()
                        .equalsIgnoreCase(masterName.trim())) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to parse Tally master response: "
                            + e.getMessage(), e);
        }
    }

    private String escapeXml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}

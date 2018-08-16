package struct;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class InitialsAdditionalServices {

    private String boardPoint;
    private String lastName;
    private String firstName;
    private String nameRefNumber;
    private String carrierCode;
    private String flightNumber;
    private String departureDate;
    private String offPoint;
    private String classOfService;
    private String owningCarrierCode;
    private String actionCode;

    public InitialsAdditionalServices(String response) {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                    parse(new ByteArrayInputStream(response.getBytes()));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        boardPoint = doc.getElementsByTagName("ns2:DepartureAirport").item(0).getTextContent();
        lastName =  doc.getElementsByTagName("ns2:LastName").item(0).getTextContent();
        firstName = doc.getElementsByTagName("ns2:FirstName").item(0).getTextContent();
        nameRefNumber = doc.getElementsByTagName("ns2:Passenger").item(0).getAttributes().getNamedItem("nameId").getTextContent();
        carrierCode = doc.getElementsByTagName("ns2:MarketingAirlineCode").item(0).getTextContent();
        flightNumber = doc.getElementsByTagName("ns2:MarketingFlightNumber").item(0).getTextContent();
        departureDate = doc.getElementsByTagName("ns2:DepartureDateTime").item(0).getTextContent().substring(0,10);
        offPoint = doc.getElementsByTagName("ns2:ArrivalAirport").item(0).getTextContent();
        classOfService = doc.getElementsByTagName("ns2:OperatingClassOfService").item(0).getTextContent();
        owningCarrierCode = doc.getElementsByTagName("ns2:OperatingAirlineCode").item(0).getTextContent();
        actionCode = doc.getElementsByTagName("ns2:ActionCode").item(0).getTextContent();
    }

    public String getBoardPoint() {
        return boardPoint;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getNameRefNumber() {
        return nameRefNumber;
    }
    public String getCarrierCode() {
        return carrierCode;
    }
    public String getFlightNumber() {
        return flightNumber;
    }
    public String getDepartureDate() {
        return departureDate;
    }
    public String getOffPoint() {
        return offPoint;
    }
    public String getClassOfService() {
        return classOfService;
    }
    public String getOwningCarrierCode() {
        return owningCarrierCode;
    }
    public String getActionCode() {
        return actionCode;
    }

    @Override
    public String toString() {
        return "InitialsAdditionalServices{" +
                "boardPoint='" + boardPoint + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", nameRefNumber='" + nameRefNumber + '\'' +
                ", carrierCode='" + carrierCode + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", offPoint='" + offPoint + '\'' +
                ", classOfService='" + classOfService + '\'' +
                ", owningCarrierCode='" + owningCarrierCode + '\'' +
                ", actionCode='" + actionCode + '\'' +
                '}';
    }
}

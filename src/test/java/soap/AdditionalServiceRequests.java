package soap;

import config.Values;

public class AdditionalServiceRequests {

    private static String ENTREE_PRICE = "1";
    private static String DESSERT_PRICE = "2";
    private static String ADDOPTION_PRICE = "3";

    public static void setPriceByCurrency() {
        switch (Values.cur) {
            case "RUB":
                ENTREE_PRICE = "1750";
                DESSERT_PRICE = "975";
                ADDOPTION_PRICE = "2000";
                break;
            case "EUR":
                ENTREE_PRICE = "17.55";
                DESSERT_PRICE = "9.72";
                ADDOPTION_PRICE = "20.01";
                break;
            case "USD":
                ENTREE_PRICE = "21.75";
                DESSERT_PRICE = "12.05";
                ADDOPTION_PRICE = "23.12";
                break;
            case "CNY":
                ENTREE_PRICE = "3500";
                DESSERT_PRICE = "1950";
                ADDOPTION_PRICE = "4000";
                break;
        }
    }
    public String getSessionCreateRQ() {
        return sessionCreateRQ;
    }
    public String getTravelItineraryReadRQ() {
        return travelItineraryReadRQ;
    }
    public String getGetReservationOperation() {
        return getReservationOperation;
    }
    public String getUpdateReservationOperation() {
        return updateReservationOperation;
    }
    public String getUpdateReservationOperation1() {
        return updateReservationOperation1;
    }
    public String getSabreCommandQ() {
        return sabreCommand;
    }

    private String sessionCreateRQ =
            "\"OTA\"~~" +
            "https://sws-crt.cert.havail.sabre.com~~" +//"https://sws-crt.cert.sabre.com~~" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sec=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:mes=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns=\"http://www.opentravel.org/OTA/2002/11\">" +
            "<soapenv:Header>" +
            "<sec:Security>" +
            "<sec:UsernameToken>" +
            "<sec:Username>970007</sec:Username>" +
            "<sec:Password>id85u314</sec:Password>" +
            "<Organization>SU</Organization>" +
            "<Domain>SU</Domain>" +
            "</sec:UsernameToken>" +
            "</sec:Security>" +
            "<mes:MessageHeader>" +
            "<mes:From>" +
            "<mes:PartyId>SoapUI</mes:PartyId>" +
            "</mes:From>" +
            "<mes:To>" +
            "<mes:PartyId>Sabre</mes:PartyId>" +
            "</mes:To>" +
            "<mes:CPAId>SU</mes:CPAId>" +
            "<mes:ConversationId>EMD tests</mes:ConversationId>" +
            "<mes:Service mes:type=\"OTA\">SessionCreateRQ</mes:Service>" +
            "<mes:Action>SessionCreateRQ</mes:Action>" +
            "<mes:MessageData>" +
            "<mes:MessageId>1000</mes:MessageId>" +
            "<mes:Timestamp>2015-04-05T12:12:12</mes:Timestamp>" +
            "</mes:MessageData>" +
            "</mes:MessageHeader>" +
            "</soapenv:Header>" +
            "<soapenv:Body>" +
            "<ns:SessionCreateRQ returnContextID=\"true\">" +
            "<ns:POS>" +
            "<ns:Source PseudoCityCode=\"SU\"/>" +
            "</ns:POS>" +
            "</ns:SessionCreateRQ>" +
            "</soapenv:Body>" +
            "</soapenv:Envelope>";

    private String travelItineraryReadRQ =
            "\"TravelItineraryReadRQ\"~~" +
            "https://sws-crt.cert.sabre.com~~" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sec=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:mes=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns=\"http://webservices.sabre.com/sabreXML/2011/10\">\n" +
            "   <soapenv:Header>\n" +
            "      <sec:Security>\n" +
            "         <sec:BinarySecurityToken>%s</sec:BinarySecurityToken>\n" + //token
            "      </sec:Security>\n" +
            "      <mes:MessageHeader>\n" +
            "         <mes:From>\n" +
            "            <mes:PartyId mes:type=\"urn:x12.org:IO5:01\">99999</mes:PartyId>\n" +
            "         </mes:From>\n" +
            "         <mes:To>\n" +
            "            <mes:PartyId mes:type=\"urn:x12.org:IO5:01\">123123</mes:PartyId>\n" +
            "            </mes:To>\n" +
            "         <mes:CPAId>ipcc</mes:CPAId>\n" +
            "         <mes:ConversationId>conversation_ID</mes:ConversationId>\n" +
            "         <mes:Service>TravelItineraryReadRQ</mes:Service>\n" +
            "         <mes:Action>TravelItineraryReadRQ</mes:Action>\n" +
            "         <mes:MessageData>\n" +
            "            <mes:MessageId>mid:20001209-133003-2333@clientofsabre.com1</mes:MessageId>\n" +
            "            <mes:Timestamp>2015-09-28T12:12:17</mes:Timestamp>\n" +
            "         </mes:MessageData>\n" +
            "      </mes:MessageHeader>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "      <ns:TravelItineraryReadRQ Version=\"3.5.0\">\n" +
            "         <ns:MessagingDetails>\n" +
            "            <ns:SubjectAreas>\n" +
            "               <ns:SubjectArea>FULL</ns:SubjectArea>\n" +
            "               <ns:SubjectArea>POPULATE_IS_PAST</ns:SubjectArea>\n" +
            "            </ns:SubjectAreas>\n" +
            "         </ns:MessagingDetails>\n" +
            "         <ns:UniqueID ID=\"%s\"/>\n" +  //pnr
            "      </ns:TravelItineraryReadRQ>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    private String getReservationOperation =
            "\"GetReservationOperation\"~~" +
            "https://sws-crt.cert.havail.sabre.com~~" +//"https://sws-crt.cert.sabre.com~~" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sec=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:mes=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:v1=\"http://webservices.sabre.com/pnrbuilder/v1_10\" xmlns:v11=\"http://services.sabre.com/res/or/v1_3\">\n" +
            "   <soapenv:Header>\n" +
            "      <sec:Security>\n" +
            "         <sec:BinarySecurityToken>%s</sec:BinarySecurityToken>\n" + //token
            "      </sec:Security>\n" +
            "      <mes:MessageHeader>\n" +
            "         <mes:From>\n" +
            "            <mes:PartyId>SoapUI</mes:PartyId>\n" +
            "         </mes:From>\n" +
            "         <mes:To>\n" +
            "            <mes:PartyId>Sabre</mes:PartyId>\n" +
            "         </mes:To>\n" +
            "         <mes:CPAId>SU</mes:CPAId>\n" +
            "         <mes:ConversationId>EMD tests</mes:ConversationId>\n" +
            "         <mes:Service mes:type=\"OTA\">getReservationRQ</mes:Service>\n" +
            "         <mes:Action>getReservationRQ</mes:Action>\n" +
            "         <mes:MessageData>\n" +
            "            <mes:MessageId>1000</mes:MessageId>\n" +
            "            <mes:Timestamp>2015-04-05T12:12:12</mes:Timestamp>\n" +
            "         </mes:MessageData>\n" +
            "      </mes:MessageHeader>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "      <v1:GetReservationRQ Version=\"1.10.0\">\n" +
            "         <v1:Locator>%s</v1:Locator>\n" +               //pnr
            "         <v1:RequestType>Stateful</v1:RequestType>\n" +
            "         <v1:ReturnOptions UnmaskCreditCard=\"false\">\n" +
            "            <v1:SubjectAreas>\n" +
            "               <v1:SubjectArea>ITINERARY</v1:SubjectArea>\n" +
            "            </v1:SubjectAreas>\n" +
            "            <v1:ViewName>Full</v1:ViewName>\n" +
            "            <v1:ResponseFormat>STL</v1:ResponseFormat>\n" +
            "         </v1:ReturnOptions>\n" +
            "      </v1:GetReservationRQ>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    private String updateReservationOperation =
            "\"UpdateReservationOperation\"~~" +
            "https://sws-crt.cert.havail.sabre.com~~" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sec=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:mes=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:v1=\"http://webservices.sabre.com/pnrbuilder/v1_16\" xmlns:v11=\"http://services.sabre.com/res/or/v1_9\">\n" +
            "   <soapenv:Header>\n" +
            "      <sec:Security>\n" +
            "         <sec:BinarySecurityToken>%s</sec:BinarySecurityToken>\n" +              //token
            "      </sec:Security>\n" +
            "      <mes:MessageHeader mes:id=\"illum exspirantem\" mes:version=\"sceptra tenens\">\n" +
            "         <mes:From>\n" +
            "            <mes:PartyId>noname@none.com</mes:PartyId>\n" +
            "         </mes:From>\n" +
            "         <mes:To>\n" +
            "            <mes:PartyId>sws-crt.cert.sabre.com</mes:PartyId>\n" +
            "         </mes:To>\n" +
            "         <mes:CPAId>DSU</mes:CPAId>\n" +
            "         <mes:ConversationId>nopool</mes:ConversationId>\n" +
            "         <mes:Service mes:type=\"OTA\">UpdateReservationRQ</mes:Service>\n" +
            "         <mes:Action>UpdateReservationRQ</mes:Action>\n" +
            "         <mes:MessageData>\n" +
            "            <mes:MessageId>mid:4533d1e1531c57b4dd867e46c378c50c@noname</mes:MessageId>\n" +
            "            <mes:Timestamp>2015-05-07T21:12:11Z</mes:Timestamp>\n" +
            "         </mes:MessageData>\n" +
            "      </mes:MessageHeader>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "      <v1:UpdateReservationRQ Version=\"1.16.0\">\n" +
            "         <v1:RequestType commitTransaction=\"false\">Stateful</v1:RequestType>\n" +
            "         <v1:ReturnOptions RetrievePNR=\"false\" IncludeUpdateDetails=\"false\">\n" +
            "            <v1:SubjectAreas>\n" +
            "               <v1:SubjectArea>ITINERARY</v1:SubjectArea>\n" +
            "            </v1:SubjectAreas>\n" +
            "         </v1:ReturnOptions>\n" +
            "         <v1:ReservationUpdateList>\n" +
            "            <v1:Locator>%s</v1:Locator>\n" +                             //pnr
            "            <v1:ReservationUpdateItem UpdateId=\"1\">\n" +
            "               <v1:AncillaryServicesUpdate id=\"1\" op=\"C\">\n" +
            "                  <v1:NameAssociationList>\n" +
            "                     <v1:NameAssociationTag>\n" +
            "                        <v1:LastName>LAST_NAME</v1:LastName>\n" +
            "                        <v1:FirstName>FIRST_NAME</v1:FirstName>\n" +
            "                        <v1:NameRefNumber>NAME_REF_NUMBER</v1:NameRefNumber>\n" +
            "                     </v1:NameAssociationTag>\n" +
            "                  </v1:NameAssociationList>\n" +
            "                 <v1:SegmentAssociationList>\n" +
            "                        <v1:SegmentAssociationTag>\n" +
            "                        <v1:CarrierCode>CARRIER_CODE</v1:CarrierCode>\n" +
            "                        <v1:FlightNumber>FLIGHT_NUMBER</v1:FlightNumber>\n" +
            "                        <v1:DepartureDate>DEPARTURE_DATE</v1:DepartureDate>\n" +
            "                        <v1:BoardPoint>BOARD_POINT</v1:BoardPoint>\n" +
            "                        <v1:OffPoint>OFF_POINT</v1:OffPoint>\n" +
            "                        <v1:ClassOfService>CLASS_OF_SERVICE</v1:ClassOfService>\n" +
            "                        <v1:BookingStatus>BOOKING_STATUS</v1:BookingStatus>\n" +
            "                     </v1:SegmentAssociationTag>\n" +
            "                  </v1:SegmentAssociationList>\n" +
            "                  <v1:CommercialName>FISH APPETIZER</v1:CommercialName>\n" +
            "                  <v1:RficCode>G</v1:RficCode>\n" +
            "                  <v1:RficSubcode>0B3</v1:RficSubcode>\n" +           //код основного блюда
            "                  <v1:SSRCode>PDML</v1:SSRCode>\n" +
            "                  <v1:OwningCarrierCode>SU</v1:OwningCarrierCode>\n" +
            "                  <v1:BookingIndicator/>\n" +
            "                  <v1:Vendor>MMGR</v1:Vendor>\n" +
            "                  <v1:EMDType>2</v1:EMDType>\n" +
            "                  <v1:Endorsements>qui foedere</v1:Endorsements>\n" +
            "                  <v1:Quantity>1</v1:Quantity>\n" +
            "                  <v1:EquivalentPrice>\n" +
            "                     <v1:Price>" + ENTREE_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:EquivalentPrice>\n" +
            "                  <v1:TTLPrice>\n" +
            "                     <v1:Price>" + ENTREE_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TTLPrice>\n" +
            "                  <v1:OriginalBasePrice>\n" +
            "                     <v1:Price>" + ENTREE_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:OriginalBasePrice>\n" +
            "                  <v1:RefundIndicator>Y</v1:RefundIndicator>\n" +
            "                  <v1:CommisionIndicator>Y</v1:CommisionIndicator>\n" +
            "                  <v1:InterlineIndicator>Y</v1:InterlineIndicator>\n" +
            "                  <v1:FeeApplicationIndicator>1</v1:FeeApplicationIndicator>\n" +
            "                  <v1:TotalOriginalBasePrice>\n" +
            "                     <v1:Price>" + ENTREE_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TotalOriginalBasePrice>\n" +
            "                  <v1:TotalEquivalentPrice>\n" +
            "                     <v1:Price>" + ENTREE_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TotalEquivalentPrice>\n" +
            "                  <v1:TotalTTLPrice>\n" +
            "                     <v1:Price>" + ENTREE_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TotalTTLPrice>\n" +
            "                  <v1:TaxExemptIndicator>N</v1:TaxExemptIndicator>\n" +
            "                  <v1:NumberOfItems>1</v1:NumberOfItems>\n" +
            "                  <v1:ActionCode>HD</v1:ActionCode>\n" +
            "                  <v1:SegmentIndicator>S</v1:SegmentIndicator>\n" +
            "                  <v1:TTYConfirmationTimestamp>2018-05-09T07:39:10</v1:TTYConfirmationTimestamp>\n" +
            "                  <v1:PurchaseTimestamp>2018-04-15T22:22:22</v1:PurchaseTimestamp>\n" +
            "                  <v1:GroupCode>ML</v1:GroupCode>\n" +
            "                  <v1:TourCode>DIS01</v1:TourCode>\n" +
            "               </v1:AncillaryServicesUpdate>\n" +
            "            </v1:ReservationUpdateItem>\n" +
            "\n" +
            "\t\t            <v1:ReservationUpdateItem UpdateId=\"2\">\n" +
            "               <v1:AncillaryServicesUpdate id=\"1\" op=\"C\">\n" +
            "                  <v1:NameAssociationList>\n" +
            "                     <v1:NameAssociationTag>\n" +
            "                        <v1:LastName>LAST_NAME</v1:LastName>\n" +
            "                        <v1:FirstName>FIRST_NAME</v1:FirstName>\n" +
            "                        <v1:NameRefNumber>NAME_REF_NUMBER</v1:NameRefNumber>\n" +
            "                     </v1:NameAssociationTag>\n" +
            "                  </v1:NameAssociationList>\n" +
            "                 <v1:SegmentAssociationList>\n" +
            "                        <v1:SegmentAssociationTag>\n" +
            "                        <v1:CarrierCode>CARRIER_CODE</v1:CarrierCode>\n" +
            "                        <v1:FlightNumber>FLIGHT_NUMBER</v1:FlightNumber>\n" +
            "                        <v1:DepartureDate>DEPARTURE_DATE</v1:DepartureDate>\n" +
            "                        <v1:BoardPoint>BOARD_POINT</v1:BoardPoint>\n" +
            "                        <v1:OffPoint>OFF_POINT</v1:OffPoint>\n" +
            "                        <v1:ClassOfService>CLASS_OF_SERVICE</v1:ClassOfService>\n" +
            "                        <v1:BookingStatus>BOOKING_STATUS</v1:BookingStatus>\n" +
            "                     </v1:SegmentAssociationTag>\n" +
            "                  </v1:SegmentAssociationList>\n" +
            "                  <v1:CommercialName>MILLEMEUILLE</v1:CommercialName>\n" +
            "                  <v1:RficCode>G</v1:RficCode>\n" +
            "                  <v1:RficSubcode>019</v1:RficSubcode>\n" +                  //КОД ДЕСЕРТА
            "                  <v1:SSRCode>PDML</v1:SSRCode>\n" +
            "                  <v1:OwningCarrierCode>SU</v1:OwningCarrierCode>\n" +
            "                  <v1:BookingIndicator/>\n" +
            "                  <v1:Vendor>MMGR</v1:Vendor>\n" +
            "                  <v1:EMDType>2</v1:EMDType>\n" +
            "                  <v1:Quantity>1</v1:Quantity>\n" +
            "                  <v1:EquivalentPrice>\n" +
            "                     <v1:Price>" + DESSERT_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:EquivalentPrice>\n" +
            "                  <v1:TTLPrice>\n" +
            "                     <v1:Price>" + DESSERT_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TTLPrice>\n" +
            "                  <v1:OriginalBasePrice>\n" +
            "                     <v1:Price>" + DESSERT_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:OriginalBasePrice>\n" +
            "                  <v1:RefundIndicator>Y</v1:RefundIndicator>\n" +
            "                  <v1:CommisionIndicator>Y</v1:CommisionIndicator>\n" +
            "                  <v1:InterlineIndicator>Y</v1:InterlineIndicator>\n" +
            "                  <v1:FeeApplicationIndicator>1</v1:FeeApplicationIndicator>\n" +
            "                  <v1:TotalOriginalBasePrice>\n" +
            "                     <v1:Price>" + DESSERT_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TotalOriginalBasePrice>\n" +
            "                  <v1:TotalEquivalentPrice>\n" +
            "                     <v1:Price>" + DESSERT_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TotalEquivalentPrice>\n" +
            "                  <v1:TotalTTLPrice>\n" +
            "                     <v1:Price>" + DESSERT_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TotalTTLPrice>\n" +
            "                  <v1:TaxExemptIndicator>N</v1:TaxExemptIndicator>\n" +
            "                  <v1:NumberOfItems>1</v1:NumberOfItems>\n" +
            "                  <v1:ActionCode>HD</v1:ActionCode>\n" +
            "                  <v1:SegmentIndicator>S</v1:SegmentIndicator>\n" +
            "                  <v1:PurchaseTimestamp>2018-04-15T22:22:22</v1:PurchaseTimestamp>\n" +
            "                  <v1:GroupCode>ML</v1:GroupCode>\n" +
            "               </v1:AncillaryServicesUpdate>\n" +
            "            </v1:ReservationUpdateItem>\n" +
            "            \n" +
            "          </v1:ReservationUpdateList>\n" +
            "      </v1:UpdateReservationRQ>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    private String updateReservationOperation1 =
            "\"UpdateReservationOperation\"~~" +
            "https://sws-crt.cert.havail.sabre.com~~" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sec=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:mes=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:v1=\"http://webservices.sabre.com/pnrbuilder/v1_16\" xmlns:v11=\"http://services.sabre.com/res/or/v1_9\">\n" +
            "   <soapenv:Header>\n" +
            "      <sec:Security>\n" +
            "         <sec:BinarySecurityToken>%s</sec:BinarySecurityToken>\n" +                    //token
            "      </sec:Security>\n" +
            "      <mes:MessageHeader mes:id=\"illum exspirantem\" mes:version=\"sceptra tenens\">\n" +
            "         <mes:From>\n" +
            "            <mes:PartyId>noname@none.com</mes:PartyId>\n" +
            "         </mes:From>\n" +
            "         <mes:To>\n" +
            "            <mes:PartyId>sws-crt.cert.sabre.com</mes:PartyId>\n" +
            "         </mes:To>\n" +
            "         <mes:CPAId>DSU</mes:CPAId>\n" +
            "         <mes:ConversationId>nopool</mes:ConversationId>\n" +
            "         <mes:Service mes:type=\"OTA\">UpdateReservationRQ</mes:Service>\n" +
            "         <mes:Action>UpdateReservationRQ</mes:Action>\n" +
            "         <mes:MessageData>\n" +
            "            <mes:MessageId>mid:4533d1e1531c57b4dd867e46c378c50c@noname</mes:MessageId>\n" +
            "            <mes:Timestamp>2015-05-07T21:12:11Z</mes:Timestamp>\n" +
            "         </mes:MessageData>\n" +
            "      </mes:MessageHeader>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "      <v1:UpdateReservationRQ Version=\"1.16.0\">\n" +
            "         <v1:RequestType commitTransaction=\"false\">Stateful</v1:RequestType>\n" +
            "         <v1:ReturnOptions RetrievePNR=\"false\" IncludeUpdateDetails=\"false\">\n" +
            "            <v1:SubjectAreas>\n" +
            "               <v1:SubjectArea>ITINERARY</v1:SubjectArea>\n" +
            "            </v1:SubjectAreas>\n" +
            "         </v1:ReturnOptions>\n" +
            "         <v1:ReservationUpdateList>\n" +
            "            <v1:Locator>%s</v1:Locator>\n" +                         //pnr
            "            <v1:ReservationUpdateItem UpdateId=\"1\">\n" +
            "               <v1:AncillaryServicesUpdate id=\"1\" op=\"C\">\n" +
            "                  <v1:NameAssociationList>\n" +
            "                     <v1:NameAssociationTag>\n" +
            "                        <v1:LastName>LAST_NAME</v1:LastName>\n" +
            "                        <v1:FirstName>FIRST_NAME</v1:FirstName>\n" +
            "                        <v1:NameRefNumber>NAME_REF_NUMBER</v1:NameRefNumber>\n" +
            "                     </v1:NameAssociationTag>\n" +
            "                  </v1:NameAssociationList>\n" +
            "                 <v1:SegmentAssociationList>\n" +
            "                        <v1:SegmentAssociationTag>\n" +
            "                        <v1:CarrierCode>CARRIER_CODE</v1:CarrierCode>\n" +
            "                        <v1:FlightNumber>FLIGHT_NUMBER</v1:FlightNumber>\n" +
            "                        <v1:DepartureDate>DEPARTURE_DATE</v1:DepartureDate>\n" +
            "                        <v1:BoardPoint>BOARD_POINT</v1:BoardPoint>\n" +
            "                        <v1:OffPoint>OFF_POINT</v1:OffPoint>\n" +
            "                        <v1:ClassOfService>CLASS_OF_SERVICE</v1:ClassOfService>\n" +
            "                        <v1:BookingStatus>BOOKING_STATUS</v1:BookingStatus>\n" +
            "                     </v1:SegmentAssociationTag>\n" +
            "                  </v1:SegmentAssociationList>\n" +
            "                  <v1:CommercialName>SEAT ASSIGNMENT</v1:CommercialName>\n" +
            "                  <v1:RficCode>A</v1:RficCode>\n" +
            "                  <v1:RficSubcode>0B5</v1:RficSubcode>\n" +           //код услуги Место в салоне
            "                  <v1:SSRCode>PDML</v1:SSRCode>\n" +
            "                  <v1:OwningCarrierCode>SU</v1:OwningCarrierCode>\n" +
            "                  <v1:BookingIndicator/>\n" +
            "                  <v1:Vendor>MMGR</v1:Vendor>\n" +
            "                  <v1:EMDType>2</v1:EMDType>\n" +                    //всегда такое значение
            "                  <v1:Quantity>1</v1:Quantity>\n" +
            "                  <v1:EquivalentPrice>\n" +
            "                     <v1:Price>" + ADDOPTION_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:EquivalentPrice>\n" +
            "                  <v1:TTLPrice>\n" +
            "                     <v1:Price>" + ADDOPTION_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TTLPrice>\n" +
            "                  <v1:OriginalBasePrice>\n" +
            "                     <v1:Price>" + ADDOPTION_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:OriginalBasePrice>\n" +
            "                  <v1:RefundIndicator>Y</v1:RefundIndicator>\n" +
            "                  <v1:CommisionIndicator>N</v1:CommisionIndicator>\n" +
            "                  <v1:InterlineIndicator>Y</v1:InterlineIndicator>\n" +
            "                  <v1:FeeApplicationIndicator>1</v1:FeeApplicationIndicator>\n" +
            "                  <v1:TotalOriginalBasePrice>\n" +
            "                     <v1:Price>" + ADDOPTION_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TotalOriginalBasePrice>\n" +
            "                  <v1:TotalEquivalentPrice>\n" +
            "                     <v1:Price>" + ADDOPTION_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TotalEquivalentPrice>\n" +
            "                  <v1:TotalTTLPrice>\n" +
            "                     <v1:Price>" + ADDOPTION_PRICE + "</v1:Price>\n" +
            "                     <v1:Currency>CURRENCY_CODE</v1:Currency>\n" +
            "                  </v1:TotalTTLPrice>\n" +
            "                  <v1:TaxExemptIndicator>N</v1:TaxExemptIndicator>\n" +
            "                  <v1:NumberOfItems>1</v1:NumberOfItems>\n" +
            "                  <v1:ActionCode>HD</v1:ActionCode>\n" +
            "                  <v1:SegmentIndicator>S</v1:SegmentIndicator>\n" +
            "                  <v1:PurchaseTimestamp>2018-04-15T22:22:22</v1:PurchaseTimestamp>\n" +
            "                  <v1:GroupCode>SA</v1:GroupCode>\n" +
            "               </v1:AncillaryServicesUpdate>\n" +
            "            </v1:ReservationUpdateItem>\n" +
            "          </v1:ReservationUpdateList>\n" +
            "      </v1:UpdateReservationRQ>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    private String sabreCommand =
            "\"OTA\"~~" +
            "https://sws-crt.cert.havail.sabre.com~~" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sec=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:mes=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns=\"http://webservices.sabre.com/sabreXML/2003/07\">\n" +
            "   <soapenv:Header>\n" +
            "      <sec:Security>\n" +
            "         <sec:BinarySecurityToken>%s</sec:BinarySecurityToken>\n" +   //token
            "      </sec:Security>\n" +
            "      <mes:MessageHeader>\n" +
            "         <mes:From>\n" +
            "            <mes:PartyId>SoapUI</mes:PartyId>\n" +
            "         </mes:From>\n" +
            "         <mes:To>\n" +
            "            <mes:PartyId>Sabre</mes:PartyId>\n" +
            "         </mes:To>\n" +
            "         <mes:CPAId>SU</mes:CPAId>\n" +
            "         <mes:ConversationId>EMD tests</mes:ConversationId>\n" +
            "         <mes:Service mes:type=\"OTA\">SabreCommandLLSRQ</mes:Service>\n" +
            "         <mes:Action>SabreCommandLLSRQ</mes:Action>\n" +
            "         <mes:MessageData>\n" +
            "            <mes:MessageId>1000</mes:MessageId>\n" +
            "            <mes:Timestamp>2015-08-31T12:12:12</mes:Timestamp>\n" +
            "         </mes:MessageData>\n" +
            "      </mes:MessageHeader>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "      <ns:SabreCommandLLSRQ Version=\"1.6.1\">\n" +
            "         <ns:Request Output=\"SCREEN\" CDATA=\"true\">\n" +
            "            <ns:HostCommand>%s</ns:HostCommand>\n" +                //command
            "         </ns:Request>\n" +
            "      </ns:SabreCommandLLSRQ>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";
}

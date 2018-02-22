package soap;

/**
 * Created by mycola on 21.02.2018.
 */
public class RequestsData {

    public static String request[][] = {
    /* 0 */ {"name", "service", "endpoint", "request"},
    /* 1 */ {"SessionCreateRQ",
            "OTA",
            "https://sws-crt.cert.sabre.com/",
            "<SOAP-ENV:Envelope " +
                    "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                    "xmlns:eb=\"http://www.ebxml.org/namespaces/messageHeader\" " +
                    "xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                    "xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">\n" +
                    "   <SOAP-ENV:Header>\n" +
                    "      <eb:MessageHeader SOAP-ENV:mustUnderstand=\"1\" eb:version=\"1.0\">\n" +
                    "         <eb:From>\n" +
                    "            <eb:PartyId type=\"urn:x12.org:IO5:01\">999999</eb:PartyId>\n" +
                    "         </eb:From>\n" +
                    "         <eb:To>\n" +
                    "            <eb:PartyId type=\"urn:x12.org:IO5:01\">123123</eb:PartyId>\n" +
                    "         </eb:To>\n" +
                    "         <eb:CPAId>ipcc</eb:CPAId>\n" +
                    "         <eb:ConversationId>conversation_ID</eb:ConversationId>\n" +
                    "         <eb:Service eb:type=\"OTA\">SessionCreateRQ</eb:Service>\n" +
                    "         <eb:Action>SessionCreateRQ</eb:Action>\n" +
                    "         <eb:MessageData>\n" +
                    "            <eb:MessageId>1000</eb:MessageId>\n" +
                    "            <eb:Timestamp>2006-10-09T15:40:30</eb:Timestamp>\n" +
                    "            <eb:TimeToLive>2007-10-09T15:40:30</eb:TimeToLive>\n" +
                    "         </eb:MessageData>\n" +
                    "      </eb:MessageHeader>\n" +
                    "      <wsse:Security xmlns:wsse=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:wsu=\"http://schemas.xmlsoap.org/ws/2002/12/utility\">\n" +
                    "         <wsse:UsernameToken>\n" +
                    "            <wsse:Username>8002</wsse:Username>\n" +
                    "            <wsse:Password>WS021510</wsse:Password>\n" +
                    "            <Organization>SU</Organization>\n" +
                    "            <Domain>SU</Domain>\n" +
                    "         </wsse:UsernameToken>\n" +
                    "      </wsse:Security>\n" +
                    "   </SOAP-ENV:Header>\n" +
                    "   <SOAP-ENV:Body>\n" +
                    "      <SessionCreateRQ>\n" +
                    "         <POS>\n" +
                    "            <Source PseudoCityCode=\"\"/>\n" +
                    "         </POS>\n" +
                    "      </SessionCreateRQ>\n" +
                    "   </SOAP-ENV:Body>\n" +
                    "</SOAP-ENV:Envelope>"},
    
    /* 2 */ {"SetPCC",
            "OTA",
            "https://sws-crt.cert.sabre.com/",
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "<soap:Header>\n" +
                    "<ns4:MessageHeader xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
                    "<ns4:From>\n" +
                    "<ns4:PartyId>99999</ns4:PartyId>\n" +
                    "</ns4:From>\n" +
                    "<ns4:To>\n" +
                    "<ns4:PartyId>123123</ns4:PartyId>\n" +
                    "</ns4:To>\n" +
                    "<ns4:ConversationId>86502144-9af8-4b24-b9a0-a2abac5fca3f</ns4:ConversationId>\n" +
                    "<ns4:Service>Session</ns4:Service>\n" +
                    "<ns4:CPAId/><ns4:Action>SabreCommandLLSRQ</ns4:Action>\n" +
                    "<ns4:MessageData>\n" +
                    "<ns4:MessageId>mid:20001209-133003-2333@clientofsabre.com1</ns4:MessageId>\n" +
                    "<ns4:Timestamp>2013-07-04Thu11:46:20</ns4:Timestamp>\n" +
                    "</ns4:MessageData>\n" +
                    "</ns4:MessageHeader>\n" +
                    "<ns8:Security xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
                    "<ns8:BinarySecurityToken></ns8:BinarySecurityToken>" +
                    "</ns8:Security>\n" +
                    "</soap:Header>\n" +
                    "<soap:Body>\n" +
                    "<ns4:SabreCommandLLSRQ TimeStamp=\"2013-07-04Thu11:46:20\" Version=\"2003A.TsabreXML1.6.1\" PrimaryLangID=\"en-us\" xmlns:ns1=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns2=\"http://www.w3.org/1999/xlink\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns4=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns5=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns6=\"http://schemas.xmlsoap.org/ws/2002/12/secext\">\n" +
                    "<ns4:Request Output=\"SCREEN\" MDRSubset=\"\" CDATA=\"true\">\n" +
                    "<ns4:HostCommand>AAADSV</ns4:HostCommand>\n" +
                    "</ns4:Request>\n" +
                    "</ns4:SabreCommandLLSRQ>\n" +
                    "</soap:Body>\n" +
                    "</soap:Envelope>"},

    /* 3 */ {"SetPCC",
            "OTA",
            "https://sws-crt.cert.sabre.com/",
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "\t<soap:Header>\n" +
                    "\t\t<ns4:MessageHeader xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
                    "\t\t\t<ns4:From>\n" +
                    "\t\t\t\t<ns4:PartyId>99999</ns4:PartyId>\n" +
                    "\t\t\t</ns4:From>\n" +
                    "\t\t\t<ns4:To>\n" +
                    "\t\t\t\t<ns4:PartyId>123123</ns4:PartyId>\n" +
                    "\t\t\t</ns4:To>\n" +
                    "\t\t\t<ns4:ConversationId>86502144-9af8-4b24-b9a0-a2abac5fca3f</ns4:ConversationId>\n" +
                    "\t\t\t<ns4:Service>Session</ns4:Service>\n" +
                    "\t\t\t<ns4:CPAId/><ns4:Action>SabreCommandLLSRQ</ns4:Action>\n" +
                    "\t\t\t<ns4:MessageData>\n" +
                    "\t\t\t\t<ns4:MessageId>mid:20001209-133003-2333@clientofsabre.com1</ns4:MessageId>\n" +
                    "\t\t\t\t<ns4:Timestamp>2013-07-04Thu11:46:20</ns4:Timestamp>\n" +
                    "\t\t\t</ns4:MessageData>\n" +
                    "\t\t</ns4:MessageHeader>\n" +
                    "\t\t<ns8:Security xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
                    "\t\t\t<ns8:BinarySecurityToken>Shared/IDL:IceSess\\/SessMgr:1\\.0.IDL/Common/!ICESMS\\/CRTG!ICESMSLB\\/CRT.LB!-3175918150659434107!340398!0</ns8:BinarySecurityToken>\n" +
                    "\t\t</ns8:Security>\n" +
                    "\t</soap:Header>\n" +
                    "\t<soap:Body>\n" +
                    "\t\t<ns4:SabreCommandLLSRQ TimeStamp=\"2013-07-04Thu11:46:20\" Version=\"2003A.TsabreXML1.6.1\" PrimaryLangID=\"en-us\" xmlns:ns1=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns2=\"http://www.w3.org/1999/xlink\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns4=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns5=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns6=\"http://schemas.xmlsoap.org/ws/2002/12/secext\">\n" +
                    "\t\t\t<ns4:Request Output=\"SCREEN\" MDRSubset=\"\" CDATA=\"true\">\n" +
                    "\t\t\t\t<ns4:HostCommand>AAADSV</ns4:HostCommand>\n" +
                    "\t\t\t</ns4:Request>\n" +
                    "\t\t</ns4:SabreCommandLLSRQ>\n" +
                    "\t</soap:Body>\n" +
                    "</soap:Envelope>"},

    /* 2 */ {"SetPCC",
            "OTA",
            "https://sws-crt.cert.sabre.com/",
            ""},

    /* 2 */ {"SetPCC",
            "OTA",
            "https://sws-crt.cert.sabre.com/",
            ""},

    /* 2 */ {"SetPCC",
            "OTA",
            "https://sws-crt.cert.sabre.com/",
            ""},

    /* 2 */ {"SetPCC",
            "OTA",
            "https://sws-crt.cert.sabre.com/",
            ""},

    };
}

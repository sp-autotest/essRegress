package soap;

import config.Values;

/**
 * Created by mycola on 21.02.2018.
 */
public class RequestsData {


    public static String request[][] = {
    /* 0 */ {"name", "service", "endpoint", "request"},
    /* 1 */ {"SessionCreateRQ",
            "\"OTA\"",
            "https://sws-crt.cert.sabre.com",
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
            "\"OTA\"",
            "https://sws-crt.cert.sabre.com",
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<soap:Header>" +
            "<ns4:MessageHeader xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">" +
            "<ns4:From>" +
            "<ns4:PartyId>99999</ns4:PartyId>" +
            "</ns4:From>" +
            "<ns4:To>" +
            "<ns4:PartyId>123123</ns4:PartyId>" +
            "</ns4:To>" +
            "<ns4:ConversationId>86502144-9af8-4b24-b9a0-a2abac5fca3f</ns4:ConversationId>" +
            "<ns4:Service>Session</ns4:Service>" +
            "<ns4:CPAId/><ns4:Action>SabreCommandLLSRQ</ns4:Action>" +
            "<ns4:MessageData>" +
            "<ns4:MessageId>mid:20001209-133003-2333@clientofsabre.com1</ns4:MessageId>" +
            "<ns4:Timestamp>2013-07-04Thu11:46:20</ns4:Timestamp>" +
            "</ns4:MessageData>" +
            "</ns4:MessageHeader>" +
            "<ns8:Security xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">" +
            "<ns8:BinarySecurityToken></ns8:BinarySecurityToken>" +
            "</ns8:Security>" +
            "</soap:Header>" +
            "<soap:Body>" +
            "<ns4:SabreCommandLLSRQ TimeStamp=\"2013-07-04Thu11:46:20\" Version=\"2003A.TsabreXML1.6.1\" PrimaryLangID=\"en-us\" xmlns:ns1=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns2=\"http://www.w3.org/1999/xlink\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns4=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns5=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns6=\"http://schemas.xmlsoap.org/ws/2002/12/secext\">" +
            "<ns4:Request Output=\"SCREEN\" MDRSubset=\"\" CDATA=\"true\">" +
            "<ns4:HostCommand>AAA</ns4:HostCommand>" +
            "</ns4:Request>" +
            "</ns4:SabreCommandLLSRQ>" +
            "</soap:Body>" +
            "</soap:Envelope>"},

    /* 3 */ {"Read PNR",
            "\"OTA\"",
            "https://sws-crt.cert.sabre.com",
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    <soap:Header>\n" +
            "        <ns4:MessageHeader xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
            "            <ns4:From>\n" +
            "                <ns4:PartyId>99999</ns4:PartyId>\n" +
            "            </ns4:From>\n" +
            "            <ns4:To>\n" +
            "                <ns4:PartyId>123123</ns4:PartyId>\n" +
            "            </ns4:To>\n" +
            "            <ns4:ConversationId>86502144-9af8-4b24-b9a0-a2abac5fca3f</ns4:ConversationId>\n" +
            "            <ns4:Service>Session</ns4:Service>\n" +
            "            <ns4:CPAId/><ns4:Action>SabreCommandLLSRQ</ns4:Action>\n" +
            "            <ns4:MessageData>\n" +
            "                <ns4:MessageId>mid:20001209-133003-2333@clientofsabre.com1</ns4:MessageId>\n" +
            "                <ns4:Timestamp>2013-07-04Thu11:46:20</ns4:Timestamp>\n" +
            "            </ns4:MessageData>\n" +
            "        </ns4:MessageHeader>\n" +
            "        <ns8:Security xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
            "            <ns8:BinarySecurityToken></ns8:BinarySecurityToken>\n" +
            "        </ns8:Security>\n" +
            "    </soap:Header>\n" +
            "    <soap:Body>\n" +
            "        <ns4:SabreCommandLLSRQ TimeStamp=\"2013-07-04Thu11:46:20\" Version=\"2003A.TsabreXML1.6.1\" PrimaryLangID=\"en-us\" xmlns:ns1=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns2=\"http://www.w3.org/1999/xlink\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns4=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns5=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns6=\"http://schemas.xmlsoap.org/ws/2002/12/secext\">\n" +
            "            <ns4:Request Output=\"SCREEN\" MDRSubset=\"\" CDATA=\"true\">\n" +
            "                <ns4:HostCommand>*"+ Values.pnr +"</ns4:HostCommand>\n" +
            "            </ns4:Request>\n" +
            "        </ns4:SabreCommandLLSRQ>\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>"},

    /* 4 */ {"DeletePQ",
            "\"OTA\"",
            "https://sws-crt.cert.sabre.com/",
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    <soap:Header>\n" +
            "        <ns4:MessageHeader xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
            "            <ns4:From>\n" +
            "                <ns4:PartyId>99999</ns4:PartyId>\n" +
            "            </ns4:From>\n" +
            "            <ns4:To>\n" +
            "                <ns4:PartyId>123123</ns4:PartyId>\n" +
            "            </ns4:To>\n" +
            "            <ns4:ConversationId>86502144-9af8-4b24-b9a0-a2abac5fca3f</ns4:ConversationId>\n" +
            "            <ns4:Service>Session</ns4:Service>\n" +
            "            <ns4:CPAId/><ns4:Action>SabreCommandLLSRQ</ns4:Action>\n" +
            "            <ns4:MessageData>\n" +
            "                <ns4:MessageId>mid:20001209-133003-2333@clientofsabre.com1</ns4:MessageId>\n" +
            "                <ns4:Timestamp>2013-07-04Thu11:46:20</ns4:Timestamp>\n" +
            "            </ns4:MessageData>\n" +
            "        </ns4:MessageHeader>\n" +
            "        <ns8:Security xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
            "            <ns8:BinarySecurityToken></ns8:BinarySecurityToken>\n" +
            "        </ns8:Security>\n" +
            "    </soap:Header>\n" +
            "    <soap:Body>\n" +
            "        <ns4:SabreCommandLLSRQ TimeStamp=\"2013-07-04Thu11:46:20\" Version=\"2003A.TsabreXML1.6.1\" PrimaryLangID=\"en-us\" xmlns:ns1=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns2=\"http://www.w3.org/1999/xlink\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns4=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns5=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns6=\"http://schemas.xmlsoap.org/ws/2002/12/secext\">\n" +
            "            <ns4:Request Output=\"SCREEN\" MDRSubset=\"\" CDATA=\"true\">\n" +
            "                <ns4:HostCommand>PQD1</ns4:HostCommand>\n" +
            "            </ns4:Request>\n" +
            "        </ns4:SabreCommandLLSRQ>\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>"},

    /* 5  {"AirPriceRQ",
            "\"OTA\"",
            "https://sws-crt.cert.sabre.com/",
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:eb=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">\n" +
            "   <SOAP-ENV:Header>\n" +
            "      <eb:MessageHeader SOAP-ENV:mustUnderstand=\"1\" eb:version=\"1.0\">\n" +
            "         <eb:From>\n" +
            "            <eb:PartyId type=\"urn:x12.org:IO5:01\">99999</eb:PartyId>\n" +
            "         </eb:From>\n" +
            "         <eb:To>\n" +
            "            <eb:PartyId type=\"urn:x12.org:IO5:01\">123123</eb:PartyId>\n" +
            "         </eb:To>\n" +
            "         <eb:CPAId>ipcc</eb:CPAId>\n" +
            "         <eb:ConversationId>conversation_ID</eb:ConversationId> \n" +
            "   <eb:Service eb:type=\"OTA\">OTA_AirPriceLLSRQ</eb:Service> \n" +
            "   <eb:Action>OTA_AirPriceLLSRQ</eb:Action> \n" +
            "          <eb:MessageData>\n" +
            "            <eb:MessageId>mid:20001209-133003-2333@clientofsabre.com</eb:MessageId>\n" +
            "            <eb:Timestamp>20011-10-05T11:15:12Z</eb:Timestamp>\n" +
            "            <eb:TimeToLive>20011-10-05T11:15:12Z</eb:TimeToLive>\n" +
            "         </eb:MessageData>\n" +
            "      </eb:MessageHeader>\n" +
            "      <wsse:Security xmlns:wsse=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:wsu=\"http://schemas.xmlsoap.org/ws/2002/12/utility\">\n" +
            "         <wsse:BinarySecurityToken></wsse:BinarySecurityToken>\n" +
            "      </wsse:Security>\n" +
            " </SOAP-ENV:Header> \n" +
            " <SOAP-ENV:Body> \n" +
            "<OTA_AirPriceRQ Version=\"1.16.1\" xmlns=\"http://webservices.sabre.com/sabreXML/2003/07\">\n" +
            "      <POS>\n" +
            "             <Source PseudoCityCode=\"ipcc\"/>\n" +
            "       </POS> \n" +
            "<PriceRequestInformation Retain=\"true\">\n" +
            "     <OptionalQualifiers>\n" +
            "         <PricingQualifiers CurrencyCode=\"" + Values.cur + "\"/>\n" +
            "     </OptionalQualifiers>\n" +
            " </PriceRequestInformation>\n" +
            " </OTA_AirPriceRQ> \n" +
            " </SOAP-ENV:Body> \n" +
            "</SOAP-ENV:Envelope>"},
    */
    /* 5 */ {"AirPriceHost",
            "\"OTA\"",
            "https://sws-crt.cert.sabre.com/",
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    <soap:Header>\n" +
            "        <ns4:MessageHeader xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
            "            <ns4:From>\n" +
            "                <ns4:PartyId>99999</ns4:PartyId>\n" +
            "            </ns4:From>\n" +
            "            <ns4:To>\n" +
            "                <ns4:PartyId>123123</ns4:PartyId>\n" +
            "            </ns4:To>\n" +
            "            <ns4:ConversationId>86502144-9af8-4b24-b9a0-a2abac5fca3f</ns4:ConversationId>\n" +
            "            <ns4:Service>Session</ns4:Service>\n" +
            "            <ns4:CPAId/><ns4:Action>SabreCommandLLSRQ</ns4:Action>\n" +
            "            <ns4:MessageData>\n" +
            "                <ns4:MessageId>mid:20001209-133003-2333@clientofsabre.com1</ns4:MessageId>\n" +
            "                <ns4:Timestamp>2013-07-04Thu11:46:20</ns4:Timestamp>\n" +
            "            </ns4:MessageData>\n" +
            "        </ns4:MessageHeader>\n" +
            "        <ns8:Security xmlns:ns8=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:ns7=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns6=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns5=\"http://www.w3.org/1999/xlink\" xmlns:ns4=\"http://www.ebxml.org/namespaces/messageHeader\">\n" +
            "            <ns8:BinarySecurityToken></ns8:BinarySecurityToken>\n" +
            "        </ns8:Security>\n" +
            "    </soap:Header>\n" +
            "    <soap:Body>\n" +
            "        <ns4:SabreCommandLLSRQ TimeStamp=\"2013-07-04Thu11:46:20\" Version=\"2003A.TsabreXML1.6.1\" PrimaryLangID=\"en-us\" xmlns:ns1=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns2=\"http://www.w3.org/1999/xlink\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ns4=\"http://webservices.sabre.com/sabreXML/2003/07\" xmlns:ns5=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns6=\"http://schemas.xmlsoap.org/ws/2002/12/secext\">\n" +
            "            <ns4:Request Output=\"SCREEN\" MDRSubset=\"\" CDATA=\"true\">\n" +
            "                <ns4:HostCommand></ns4:HostCommand>\n" +
            "            </ns4:Request>\n" +
            "        </ns4:SabreCommandLLSRQ>\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>"},

    /* 6 */ {"SetPCC",
            "\"OTA\"",
            "https://sws-crt.cert.sabre.com/",
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sec=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:mes=\"http://www.ebxml.org/namespaces/messageHeader\" xmlns:ns=\"http://webservices.sabre.com/sabreXML/2003/07\">\n" +
            "   <soapenv:Header>\n" +
            "      <sec:Security>\n" +
            "         <sec:BinarySecurityToken></sec:BinarySecurityToken>\n" +
            "      </sec:Security>\n" +
            "      <mes:MessageHeader mes:id=\"12345\" mes:version=\"1.0\">\n" +
            "         <mes:From>\n" +
            "            <mes:PartyId mes:type=\"urn:x12.org:IO5:01\">100000001</mes:PartyId>\n" +
            "         </mes:From>\n" +
            "         <mes:To>\n" +
            "            <mes:PartyId mes:type=\"urn:x12.org:IO5:01\">900000001</mes:PartyId>\n" +
            "         </mes:To>\n" +
            "         <mes:CPAId>ipcc</mes:CPAId>\n" +
            "         <mes:ConversationId>conv_id_000001</mes:ConversationId>\n" +
            "         <mes:Service mes:type=\"\">EndTransactionLLSRQ</mes:Service>\n" +
            "         <mes:Action>EndTransactionLLSRQ</mes:Action>\n" +
            "         <mes:MessageData>\n" +
            "            <mes:MessageId>mid:20111005-133003-2334@clientofsabre</mes:MessageId>\n" +
            "            <mes:Timestamp>20011-10-06T18:17:12</mes:Timestamp>\n" +
            "         </mes:MessageData>\n" +
            "      </mes:MessageHeader>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "      <ns:EndTransactionRQ TimeStamp=\"2011-10-06T18:17:12\" Version=\"1.4.1\">\n" +
            "         <ns:POS>\n" +
            "            <ns:Source PseudoCityCode=\"ipcc\"/>\n" +
            "         </ns:POS>\n" +
            "         <ns:UpdatedBy>\n" +
            "            <ns:TPA_Extensions>\n" +
            "               <ns:Access>\n" +
            "                  <ns:AccessPerson>\n" +
            "                     <ns:GivenName>TEST</ns:GivenName>\n" +
            "                  </ns:AccessPerson>\n" +
            "               </ns:Access>\n" +
            "            </ns:TPA_Extensions>\n" +
            "         </ns:UpdatedBy>\n" +
            "         <ns:EndTransaction Ind=\"true\"/>\n" +
            "      </ns:EndTransactionRQ>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>"},


    };
}

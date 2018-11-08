package soap;

import config.Values;
import org.w3c.dom.Document;
import io.qameta.allure.Step;
import org.xml.sax.SAXException;
import struct.CollectData;
import struct.InitialsAdditionalServices;
import struct.Soap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import static org.testng.AssertJUnit.assertFalse;

/**
 * Created by mycola on 21.02.2018.
 */
public class SoapRequest {

    public static String token = "";
    private CollectData collectData;

    public SoapRequest(CollectData collectData) {
        this.collectData = collectData;
    }

    public void changeCurrency() {
        try {

            callSoapWebService(1);
            callSoapWebService(2);
            callSoapWebService(3);
            callSoapWebService(4);
            callSoapWebService(5);
            callSoapWebService(6);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addAdditionalAviaServices() {
        String pnr = Values.getPNR(collectData.getTest());
        AdditionalServiceRequests add = new AdditionalServiceRequests(collectData);
        add.setPriceByCurrency();
        //1
        Soap soap = add.getSessionCreateRQ();
        String response = callSoapRequest(soap);
        String token = getToken(response);
        //2
        soap = add.getTravelItineraryReadRQ();
        soap.setRequest(String.format(soap.getRequest(), token, pnr));
        callSoapRequest(soap);
        //3
        soap = add.getGetReservationOperation();
        soap.setRequest(String.format(soap.getRequest(), token, pnr));
        response = callSoapRequest(soap);
        InitialsAdditionalServices initials = new InitialsAdditionalServices(response);
        System.out.println(initials.toString());
        //4
        soap = add.getUpdateReservationOperation();
        soap.setRequest(String.format(soap.getRequest(), token, pnr));
        soap.setRequest(replaceInitials(soap.getRequest(), initials));
        response = callSoapRequest(soap);
        assertFalse("Ошибка в 4.SOAP \"ANCS Inventory is not available\"", response.contains("ANCS Inventory is not available"));
        //5
        soap = add.getUpdateReservationOperation1();
        soap.setRequest(String.format(soap.getRequest(), token, pnr));
        soap.setRequest(replaceInitials(soap.getRequest(), initials));
        response = callSoapRequest(soap);
        assertFalse("Ошибка в 5.SOAP \"ANCS Inventory is not available\"", response.contains("ANCS Inventory is not available"));
        //6
        soap = add.getSabreCommand();
        soap.setRequest(String.format(soap.getRequest(), token, "*AES"));
        callSoapRequest(soap);
        //7
        soap = add.getSabreCommand();
        soap.setRequest(String.format(soap.getRequest(), token, "ER"));
        callSoapRequest(soap);
        //8
        callSoapRequest(soap);
    }

    public String setPNRtoSabreCommand() {
        String pnr = Values.getPNR(collectData.getTest());
        AdditionalServiceRequests add = new AdditionalServiceRequests(collectData);
        //1
        Soap soap = add.getSessionCreateRQ();
        String response = callSoapRequest(soap);
        String token = getToken(response);
        //2
        soap = add.getSabreCommand();
        soap.setRequest(String.format(soap.getRequest(), token, "*" + pnr));
        return callSoapRequest(soap);
    }

    @Step("SOAP запрос: {1}")
    private String callSoapRequest(Soap soap) {
        //System.out.println("Request: " + soap.getRequest());

        URL url = null;
        try {
            url = new URL(soap.getHost());
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        connection.setRequestProperty("SOAPAction", soap.getAction());
        connection.setRequestProperty("Content-Length", String.valueOf(soap.getRequest().length()));
        connection.setRequestProperty("Host", soap.getHost().replaceFirst("https://",""));
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
        connection.setDoOutput(true);

        String line;
        String respond = "";
        try {
            PrintWriter pw = new PrintWriter(connection.getOutputStream());
            pw.write(String.valueOf(soap.getRequest()));
            pw.flush();
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            respond = rd.readLine();
            while ((line = rd.readLine()) != null)
                respond = respond + line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Response: " + respond);
        return respond;
    }


    @Step("SOAP запрос: {0}")
    private String callSoapWebService(int n) throws Exception {
        String host = RequestsData.request[n][2];
        String request = RequestsData.request[n][3];
        System.out.println(request);
        if (n == 1) {
            int t = request.indexOf("BinarySecurityToken><")+20;
            String r1 = request.substring(0, t);
            String r2 = request.substring(t);
            request = r1.concat(token).concat(r2);
        }
        if (n == 2) {
            request = request.replaceFirst(">AAA<", ">AAA" + modifyPCC(collectData.getCur()) + "<");
        }
        if (n == 3) {
            request = request.replaceFirst("PNRPNR", Values.getPNR(collectData.getTest()));
        }
        if (n == 5) {
            request = request.replaceFirst("HostCommand>", "HostCommand>" + command(collectData.getCur()));
            //System.out.println(request);
        }

        URL url = null;
        try {
            url = new URL(host);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("host", "sws-crt.cert.sabre.com");
        connection.setRequestProperty("SOAPAction", RequestsData.request[n][1]);
        connection.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
        connection.setDoOutput(true);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(connection.getOutputStream());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        pw.write(String.valueOf(request));
        pw.flush();

        try {
            connection.connect();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String line;
        String respond = "";
        try {
            respond = rd.readLine();
            while ((line = rd.readLine()) != null)
                respond = line;

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.print(n + ". Response");
        System.out.println(": " + respond);


        if (n == 1) {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                    parse(new ByteArrayInputStream(respond.getBytes()));
            token = document.getElementsByTagName("wsse:BinarySecurityToken").item(0).getTextContent();
            //System.out.println("BinarySecurityToken = " + token);
        }
        return respond;
    }

    private String modifyPCC(String cur){
        String pcc = null;
        switch (cur) {
            case "RUB":
                pcc = "DSU";
                break;
            case "EUR":
                pcc = "UDN";
                break;
            case "USD":
                pcc = "DSV";
                break;
            case "JPY":
                pcc = "DNA";
                break;
        }
        return pcc;
    }

    private String command(String cur){
        return "WPM"+cur+"&#135;N01.01/02.01/03.01/04.01/05.01&#135;P2ADT/2CNN/1INF&#135;RQ";
    }

    private String getToken(String respond) {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                    parse(new ByteArrayInputStream(respond.getBytes()));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return document.getElementsByTagName("wsse:BinarySecurityToken").item(0).getTextContent();
    }

    private String replaceInitials(String req, InitialsAdditionalServices ini) {
        String request = req.replaceAll("LAST_NAME", ini.getLastName());
        request = request.replaceAll("FIRST_NAME", ini.getFirstName());
        request = request.replaceAll("NAME_REF_NUMBER", ini.getNameRefNumber());
        request = request.replaceAll("CARRIER_CODE", ini.getCarrierCode());
        request = request.replaceAll("FLIGHT_NUMBER", ini.getFlightNumber());
        request = request.replaceAll("DEPARTURE_DATE", ini.getDepartureDate());
        request = request.replaceAll("BOARD_POINT", ini.getBoardPoint());
        request = request.replaceAll("OFF_POINT", ini.getOffPoint());
        request = request.replaceAll("CLASS_OF_SERVICE", ini.getClassOfService());
        request = request.replaceAll("OWNING_CARRIERCODE", ini.getOwningCarrierCode());
        request = request.replaceAll("BOOKING_STATUS", ini.getActionCode());
        request = request.replaceAll("CURRENCY_CODE", collectData.getCur());
        return request;
    }


}

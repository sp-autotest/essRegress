package soap;

import config.Values;
import org.w3c.dom.Document;
import io.qameta.allure.Step;
import org.xml.sax.SAXException;
import struct.InitialsAdditionalServices;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

/**
 * Created by mycola on 21.02.2018.
 */
public class SoapRequest {

    public static String token = "";


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
        AdditionalServiceRequests.setPriceByCurrency();
        AdditionalServiceRequests add = new AdditionalServiceRequests();
        //1
        String req = add.getSessionCreateRQ();
        String response = callSoapRequest(req, req.split("~~")[0]);
        String token = getToken(response);
        //2
        req = String.format(add.getTravelItineraryReadRQ(), token, Values.pnr);
        callSoapRequest(req, req.split("~~")[0]);
        //3
        req = String.format(add.getGetReservationOperation(), token, Values.pnr);
        response = callSoapRequest(req, req.split("~~")[0]);
        InitialsAdditionalServices initials = new InitialsAdditionalServices(response);
        System.out.println(initials.toString());
        //4
        req = String.format(add.getUpdateReservationOperation(), token, Values.pnr);
        req = replaceInitials(req, initials);
        response = callSoapRequest(req, req.split("~~")[0]);
        assertFalse("Ошибка в 4.SOAP \"ANCS Inventory is not available\"", response.contains("ANCS Inventory is not available"));
        //5
        req = String.format(add.getUpdateReservationOperation1(), token, Values.pnr);
        req = replaceInitials(req, initials);
        response = callSoapRequest(req, req.split("~~")[0]);
        assertFalse("Ошибка в 5.SOAP \"ANCS Inventory is not available\"", response.contains("ANCS Inventory is not available"));
        //6
        req = String.format(add.getSabreCommandQ(), token, "*AES");
        callSoapRequest(req, req.split("~~")[0]);
        //7
        req = String.format(add.getSabreCommandQ(), token, "ER");
        callSoapRequest(req, req.split("~~")[0]);
        //8
        callSoapRequest(req, req.split("~~")[0]);
    }

    @Step("SOAP запрос: {1}")
    private String callSoapRequest(String req, String action) {
        String[] arr = req.split("~~");
        //String action = arr[0];
        String host = arr[1];
        String request = arr[2];
        //System.out.println("Request: " + request);

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

        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        connection.setRequestProperty("SOAPAction", action);
        connection.setRequestProperty("Content-Length", String.valueOf(request.length()));
        connection.setRequestProperty("Host", host.replaceFirst("https://",""));
        connection.setRequestProperty("Connection", "Keep-Alive");
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
            request = request.replaceFirst(">AAA<", ">AAA" + modifyPCC(Values.cur) + "<");
        }
        if (n == 5) {
            request = request.replaceFirst("HostCommand>", "HostCommand>" + command(Values.cur));
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
        request = request.replaceAll("CURRENCY_CODE", Values.cur);
        return request;
    }


}

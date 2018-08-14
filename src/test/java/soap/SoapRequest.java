package soap;

import config.Values;
import org.w3c.dom.Document;
//import ru.yandex.qatools.allure.annotations.Step;
import io.qameta.allure.Step;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mycola on 21.02.2018.
 */
public class SoapRequest {

    private static String  token   = null;

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
        System.out.println("SOAP start...");
        try {
            callSoapWebService(7);
            callSoapWebService(8);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Step("SOAP запрос: {0}")
    private static void callSoapWebService(int n) throws Exception {
        String host = RequestsData.request[n][2];
        String request = RequestsData.request[n][3];
        if ((n != 1) & (n != 7)) {
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
        if (n == 8) {
            request = request.replaceFirst("AAAAAA", config.Values.pnr);
            System.out.println(request);
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


        if ((n == 1)|(n == 7)) {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                    parse(new ByteArrayInputStream(respond.getBytes()));
            token = document.getElementsByTagName("wsse:BinarySecurityToken").item(0).getTextContent();
            System.out.println("BinarySecurityToken = " + token);
        }
    }

    private static String modifyPCC(String cur){
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

    private static String command(String cur){
        return "WPM"+cur+"&#135;N01.01/02.01/03.01/04.01/05.01&#135;P2ADT/2CNN/1INF&#135;RQ";
    }
}

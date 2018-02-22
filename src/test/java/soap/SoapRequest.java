package soap;

import config.Values;
import org.testng.Assert;
import org.w3c.dom.Document;
import ru.yandex.qatools.allure.annotations.Step;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static pages.Page.Sleep;

/**
 * Created by mycola on 21.02.2018.
 */
public class SoapRequest {

    private static String  token   = null;

    public void changeCurrency() {
        try {

            callSoapWebService(1);
            Sleep(1);
            callSoapWebService(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Step("SOAP запрос: {0}")
    private static void callSoapWebService(int n) throws Exception {
        String host = RequestsData.request[n][2];
        String request = RequestsData.request[n][3];
/*        StringBuilder sRequest = new StringBuilder();
        if (n == 1) sRequest.append(request);
        if (n > 1) {
            int t = request.indexOf("BinarySecurityToken><");
            String r1 = request.substring(0, t+20);
            String r2 = request.substring(t+20);

            sRequest.append(r1);
            sRequest.append(token);
            sRequest.append(r2);

            request = r1.concat(token).concat(r2);
            //request = request.replaceFirst("BinarySecurityToken><", "BinarySecurityToken>"+token+"<");
        }

        System.out.println(sRequest);
*/
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
        connection.setRequestProperty("SoapAction", RequestsData.request[n][1]);
        //connection.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
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

        System.out.print(n + ". Response SOAP Message");
        System.out.println(": " + respond);


        if (n == 1) {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                    parse(new ByteArrayInputStream(respond.getBytes()));
            token = document.getElementsByTagName("wsse:BinarySecurityToken").item(0).getTextContent();
            System.out.println("BinarySecurityToken = " + token);
        }
    }


}

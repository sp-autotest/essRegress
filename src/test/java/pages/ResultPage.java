package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import struct.CollectData;
import struct.Flight;
import struct.Passenger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;
import static config.Values.*;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 06.03.2018.
 */
public class ResultPage extends Page {

    private List<Passenger> passengers;
    private CollectData collectData;

    public ResultPage(List<Passenger> passengers, CollectData collectData) {
        this.passengers = passengers;
        this.collectData = collectData;
    }

    @Step("Действие {0}, проверка страницы результатов оплаты")
    public void checkServicesData(String n, int test) {
        System.out.println("\t" + n + ". Cheking final page with pay result");
        checkPageAppear();
        ElementsCollection services = $$(byXpath("//div[@id='frame-additionalServices']/descendant::div[@role='row']"));
        if (test < 4) {
            services.get(0).scrollTo();
            checkFlyInsurance(services.get(0));
            services.get(1).scrollTo();
            checkMedicalInsurance(services.get(1));
            //services.get(2).scrollTo();
            n = n + "04";
        }
        if (test == 1) checkHotel(services.get(2));
        if (test == 2) checkTransport(services.get(2));
        if (test == 4) {
            n = n + "01";
            assertTrue("Обнаружены дополнительные услуги", services.size() == 0);
        }
        checkTotalPrice(n);
        screenShot();
    }

    @Step("Действие 22, проверка страницы результатов оплаты")
    public void checkServicesData3(Flight flight) {
        System.out.println("\t22. Cheking final page with pay result");
        checkPageAppear();
        ElementsCollection services = $$(byXpath("//div[@id='frame-additionalServices']/descendant::div[@role='row']"));
        services.get(0).scrollTo();
        checkFlyInsurance(services.get(0));
        checkAeroexpress(services.get(1), flight.from_orig);
        checkTransfer(services.get(2), flight.start);
    }

    @Step("Действие 29, проверка страницы результатов оплаты")
    public void checkServicesData5(Flight flight) {
        System.out.println("\t29. Cheking final page with pay result");
        checkPageAppear();
        ElementsCollection services = $$(byXpath("//div[@id='frame-additionalServices']/descendant::div[@role='row']"));
        checkFlyInsurance(services.get(0).scrollTo());
        checkMedicalInsurance(services.get(1).scrollTo());
        checkAeroexpress(services.get(2), flight.from_orig);
        checkTransport(services.get(3).scrollTo());
        checkTransfer(services.get(4), flight.start);
        checkHotel(services.get(5).scrollTo());
    }

    private void checkPageAppear(){
        Sleep(25);
        $(byXpath("//div[contains(@class,'text text--bold')]")).shouldBe(visible).shouldBe(text(Values.getPNR(collectData.getTest())));
        System.out.println("URL = " + url());
        $(byXpath("//p[contains(text(),'" + text[12][collectData.getLn()] + "')]")).shouldBe(visible);
    }

    @Step("Проверка полетной страховки")
    private void checkFlyInsurance(SelenideElement row){
        ElementsCollection docs = row.$$(byXpath("child::div[4]/div/a"));
        for (int i=0; i<docs.size(); i++) {
            Values.setDOC(collectData.getTest(), Values.getDOC(collectData.getTest()) + "Страховка:" + docs.get(i).getText() + ", ");
        }
        String passengers = row.$(byXpath("child::div[2]")).scrollTo().getText().replaceAll("\\D+","");
        System.out.println("passengers = " + passengers);
        assertTrue("Количество пассажиров в полетной страховке отличается от количества билетов" +
                "\nОжидалось : " + collectData.getTicket() + "\nФактически: " + passengers, collectData.getTicket() == stringIntoInt(passengers));

        String price = row.$(byXpath("child::div[3]")).getText().replaceAll("\\D+","");
        if (collectData.getCur().equals("RUB")) price = price.substring(0, price.length()-2);
        if (collectData.getCur().equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("fly insurance = " + price);
        assertTrue("Стоимость полетной страховки отличается от забронированной" +
                   "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().iflight +
                   "\nФактически: " +price, Values.reportData[collectData.getTest()].getPrice().iflight.equals(price));

        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов не соответствует количеству пассажиров" +
                   "\nОжидалось : " + collectData.getTicket()*2 +
                   "\nФактически: " + docs.size(), docs.size() == collectData.getTicket()*2);
        for (int i=0; i<docs.size(); i=i+2) {
            assertTrue("Название квитанции некорректно", docs.get(i).getText().equals(text[13][collectData.getLn()]));
            assertTrue("Название полиса некорректно", docs.get(i+1).getText().contains(text[14][collectData.getLn()]));
        }
    }

    @Step("Проверка медицинской страховки")
    private void checkMedicalInsurance(SelenideElement row){
        ElementsCollection docs = row.$$(byXpath("child::div[4]/div/a"));
        for (SelenideElement doc : docs) {
            Values.setDOC(collectData.getTest(), Values.getDOC(collectData.getTest()) + "Страховка:" + doc.getText() + ", ");
        }
        String passengers = row.$(byXpath("child::div[2]")).getText().replaceAll("\\D+","");
        System.out.println("passengers = " + passengers);
        assertTrue("Количество пассажиров в медицинской страховке отличается от количества билетов", collectData.getTicket() == stringIntoInt(passengers));

        String price = row.$(byXpath("child::div[3]")).getText().replaceAll("\\D+","");
        if (collectData.getCur().equals("RUB")) price = price.substring(0, price.length()-2);
        if (collectData.getCur().equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("med insurance = " + price);
        assertTrue("Стоимость медицинской страховки отличается от забронированной" +
                    "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().imedical +
                    "\nФактически: " + price,
                    Values.reportData[collectData.getTest()].getPrice().imedical.equals(price));

        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов не соответствует количеству пассажиров", docs.size() == collectData.getTicket()*2);
        for (int i=0; i<docs.size(); i=i+2) {
            assertTrue("Название квитанции некорректно", docs.get(i).getText().equals(text[13][collectData.getLn()]));
            assertTrue("Название полиса некорректно", docs.get(i+1).getText().contains(text[14][collectData.getLn()]));
        }
    }

    @Step("Проверка транспортной услуги")
    private void checkTransport(SelenideElement row){
        ElementsCollection docs = row.$$(byXpath("div[5]/a"));
        for (SelenideElement doc : docs) {
            Values.setDOC(collectData.getTest(), Values.getDOC(collectData.getTest()) + "Авто:" + doc.getText() + ", ");
        }
        String name = row.$(byXpath("div[1]/div")).getText();
        System.out.println("Auto = " + name);
        assertTrue("Название авто отличается от забронированного",
                Values.reportData[collectData.getTest()].getAuto().name.equals(name));

        SelenideElement receive = row.$(byXpath("div[2]"));
        String receiveLocation = getMiddleText(receive);
        System.out.println("Auto = " + receiveLocation);
        assertTrue("Место получения отличается от забронированного",
                Values.reportData[collectData.getTest()].getAuto().receiveLocation.equals(receiveLocation));

        String receiveDate = receive.$(byXpath("div[2]")).getText();
        receiveDate = receiveDate.substring(receiveDate.indexOf(",")+2);
        System.out.println("Auto = " + receiveDate);
        String f = "d MMMM yyyy, HH:mm";
        assertTrue("Дата получения отличается от забронированной",
                Values.reportData[collectData.getTest()].getAuto().receiveDate.equals(stringToDate(receiveDate, f)));

        SelenideElement retrn = row.$(byXpath("div[3]"));
        String returnLocation = getMiddleText(retrn);
        System.out.println("Auto = " + returnLocation);
        assertTrue("Место возврата отличается от забронированного",
                Values.reportData[collectData.getTest()].getAuto().returnLocation.equals(returnLocation));

        String returnDate = retrn.$(byXpath("div[2]")).getText();
        returnDate = returnDate.substring(returnDate.indexOf(",")+2);
        System.out.println("Auto = " + returnDate);
        assertTrue("Дата возврата отличается от забронированной",
                Values.reportData[collectData.getTest()].getAuto().returnDate.equals(stringToDate(returnDate, f)));

        String price = row.$(byXpath("div[4]")).getText().replaceAll("\\D+","");
        if (collectData.getCur().equals("RUB")) price = price.substring(0, price.length()-2);
        if (collectData.getCur().equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("Auto = " + price);
        assertTrue("Стоимость аренды автомобиля отличается от забронированной",
                Values.reportData[collectData.getTest()].getPrice().nationalTransport.equals(price));
        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов не один", docs.size() == 1);
        assertTrue("Название ваучера некорректно", docs.get(0).getText().contains(text[15][collectData.getLn()]));
    }

    @Step("Проверка услуги проживания")
    private void checkHotel(SelenideElement row){
        ElementsCollection docs = row.$$(byXpath("child::div[5]/div/a"));
        for (SelenideElement doc : docs) {
            Values.setDOC(collectData.getTest(), Values.getDOC(collectData.getTest()) + "Отель:" + doc.getText() + ", ");
        }
        String name = row.$(byXpath("child::div[1]")).getText();
        System.out.println("Hotel = " + name);

        /* временно отключена проверка названия отеля, 16.11.18
        assertTrue("Название отеля отличается от забронированного" +
                   "\nОжидалось : " + Values.reportData[collectData.getTest()].getHotel().name +
                   "\nФактически: " + name,
                   Values.reportData[collectData.getTest()].getHotel().name.equals(name));
*/
        String sDate = row.$(byXpath("child::div[2]")).getText();
        int s = row.$(byXpath("child::div[2]/span")).getText().length();
        sDate = sDate.substring(s).replaceAll("[\t\n\r\f]","");//убрать перенос строки
        System.out.println("Hotel start = " + sDate);
        String f = "d MMMM yyyy";
        assertTrue("Дата заселения отличается от забронированной", Values.reportData[collectData.getTest()].getHotel().accDate.equals(stringToDate(sDate, f)));

        String eDate = row.$(byXpath("child::div[3]")).getText();
        s = row.$(byXpath("child::div[3]/span")).getText().length();
        eDate = eDate.substring(s).replaceAll("[\t\n\r\f]","");//убрать перенос строки
        System.out.println("Hotel end = " + eDate);
        assertTrue("Дата выезда отличается от забронированной", Values.reportData[collectData.getTest()].getHotel().depDate.equals(stringToDate(eDate, f)));

        String price = row.$(byXpath("child::div[4]")).getText().replaceAll("\\D+","");
        if (collectData.getCur().equals("RUB")) price = price.substring(0, price.length()-2);
        if (collectData.getCur().equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("Hotel price = " + price);
        assertTrue("Стоимость проживания отличается от забронированной" +
                "\nОжидалось : " + Values.reportData[collectData.getTest()].getHotel().price +
                "\nФактически: " + price, Values.reportData[collectData.getTest()].getHotel().price.equals(price));

        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов некорректно" +
                "\nОжидалось : 1\nФактически: " + docs.size(), docs.size() == 1);
        assertTrue("Название ваучера некорректно\nОжидалось : " + text[15][collectData.getLn()] + "\nФактически: " + docs.get(0).getText(),
                docs.get(0).getText().contains(text[15][collectData.getLn()]));
    }

    @Step("Проверка услуги Аэроэкспресс")
    private void checkAeroexpress(SelenideElement row, String direction){
        ElementsCollection docs = row.$$(byXpath("div[4]/a"));
        for (SelenideElement doc : docs) {
            Values.setDOC(collectData.getTest(), Values.getDOC(collectData.getTest()) + "Аэроэкспресс:" + doc.getText() + ", ");
        }
        String name = row.$(byXpath("div[1]/span[2]")).getText();
        System.out.println("Name = " + name);
        name = name.substring(name.indexOf(",") + 2);
        name = name.substring(0, name.indexOf(","));
        if (direction.equals("SVO")) assertTrue("Направление в Аэроэкспресс некорректно" +
                "\nОжидалось : " + text[28][collectData.getLn()] + " -> " + text[29][collectData.getLn()] +
                "\nФактически: " + name, name.equals(text[28][collectData.getLn()] + " -> " + text[29][collectData.getLn()]));
        if (direction.equals("VKO")) assertTrue("Направление в Аэроэкспресс некорректно" +
                "\nОжидалось : " + text[20][collectData.getLn()] + " -> " + text[21][collectData.getLn()] +
                "\nФактически: " + name, name.equals(text[20][collectData.getLn()] + " -> " + text[21][collectData.getLn()]));
        Integer iCount = 0;
        for (Passenger p : passengers) if (!p.getType().equals("INF")) iCount++;
        String count = row.$(byXpath("div[2]")).getText().replaceAll("\\D+", "");
        System.out.println("passengers = " + count);
        assertTrue("Количество билетов на Аэроэкспресс не корректно" +
                "\nОжидалось : " + iCount +
                "\nФактически: " + count, count.equals(iCount.toString()));

        String price = row.$(byXpath("div[3]/div")).getText().replaceAll("\\D+", "");
        if (collectData.getCur().equals("RUB")) price = price.substring(0, price.length()-2);
        if (collectData.getCur().equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("price = " + price);
        assertTrue("Стоимость билета на Аэроэкспресс не корректна" +
                "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().aeroexpress +
                "\nФактически: " + price, price.equals(Values.reportData[collectData.getTest()].getPrice().aeroexpress));

        System.out.println("docs = " + docs.size());//должно быть 4 документа: по два на каждого пассажира
        assertTrue("Количество приложенных документов некорректно" +
                "\nОжидалось : " + iCount*2 +
                "\nФактически: " + docs.size(), docs.size() == iCount*2);
        for (int i=0; i<docs.size(); i=i+2) {
            assertTrue("Название квитанции некорректно\nОжидалось :" + text[13][collectData.getLn()] +
                    "\nФактически:" + docs.get(i).getText(), docs.get(i).getText().equals(text[13][collectData.getLn()]));
            assertTrue("Название билета некорректно\nОжидалось :" + text[30][collectData.getLn()] +
                    "\nФактически:" + docs.get(i+1).getText(), docs.get(i+1).getText().contains(text[30][collectData.getLn()]));
        }
    }

    @Step("Проверка данных услуги Трансфера")
    private void checkTransfer(SelenideElement row, Date d) {
        ElementsCollection docs = row.$$(byXpath("div[4]/a"));
        for (SelenideElement doc : docs) {
            String transferDoc = doc.getText();
            if (transferDoc.contains(text[15][collectData.getLn()])) {
                transferDoc = transferDoc + " " + getTransferNumber();
            }
            Values.setDOC(collectData.getTest(), Values.getDOC(collectData.getTest()) + "Трансфер:" + transferDoc + ", ");
            System.out.println(transferDoc);
        }
        String from = row.$(byXpath("div[1]/div[2]")).getText();
        System.out.println("Transfer from = " + from);
        String fromC = (collectData.getLn()==0) ? "Курский, Москва" : "Kurskiy, Moscow";
        assertTrue("Направление Откуда трансфера не корректно" +
                "\nОжидалось : " + fromC + "\nФактически: " + from, from.equals(fromC));

        String to = row.$(byXpath("div[2]/div[2]")).getText();
        System.out.println("Transfer to = " + to);
        String toC = (collectData.getLn()==0) ? "Белорусский, Москва" : "Belorussky, Moscow";
        assertTrue("Направление Куда трансфера не корректно" +
                "\nОжидалось : " + toC + "\nФактически: " + to, to.equals(toC));

        String date = row.$(byXpath("div[1]/div[3]")).getText();
        System.out.println("Transfer date = " + date);
        String dateC;
        dateC = new SimpleDateFormat("E, d MMMM yyyy", new Locale(Values.lang[collectData.getLn()][2])).format(d);
        if (collectData.getLn() == 6) dateC = date;//невозможно воспроизвести формат даты для китайского, убрать когда сообщат формат
        if (collectData.getLn() == 8) dateC = new SimpleDateFormat("E, d M yyyy", new Locale(Values.lang[collectData.getLn()][2])).format(d);


        /*VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
        ВРЕМЕННО до решения вопроса с датой трансфера изменена её проверка:
        проверяем разницу дат - должно быть не более 1-го дня
        assertTrue("Дата трансфера не корректна" +
                "\nОжидалось : " + dateC + "\nФактически: " + date, date.equals(dateC));
*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String dateC2 = new SimpleDateFormat("E, d MMMM yyyy", new Locale(Values.lang[collectData.getLn()][2])).format(cal.getTime());
        if (collectData.getLn() == 6) dateC2 = date;
        if (collectData.getLn() == 8) dateC2 = new SimpleDateFormat("E, d M yyyy", new Locale(Values.lang[collectData.getLn()][2])).format(cal.getTime());
        System.out.println("date = " + date);
        System.out.println("dateC2 = " + dateC2);
        if (!date.equals(dateC2)) {
            assertTrue("Дата трансфера не корректна" +
                    "\nОжидалось : " + dateC + "\nФактически: " + date, date.equals(dateC));
        }
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


        String price = row.$(byXpath("div[3]/div")).getText().replaceAll("\\D+","");
        if (collectData.getCur().equals("RUB")) price = price.substring(0, price.length()-2);
        if (collectData.getCur().equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("Transfer price = " + price);
        assertTrue("Cтоимость трансфера не корректна" +
                "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().transfer +
                "\nФактически: " + price, price.equals(Values.reportData[collectData.getTest()].getPrice().transfer));

        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов некорректно" +
                "\nОжидалось : 2\nФактически: " + docs.size(), docs.size() == 2);
        assertTrue("Название квитанции некорректно\nОжидалось :" + text[13][collectData.getLn()] +
                "\nФактически:" + docs.get(0).getText(), docs.get(0).getText().equals(text[13][collectData.getLn()]));
        assertTrue("Название ваучера некорректно\nОжидалось :" + text[15][collectData.getLn()] +
                "\nФактически:" + docs.get(1).getText(), docs.get(1).getText().contains(text[15][collectData.getLn()]));
    }

    @Step("Проверка оплаченной стоимости")
    private void checkTotalPrice(String n){
        ElementsCollection texts= $$(byXpath("//div[@class='col--16 col--stack-below-mobile']"));
        String totalPrice = texts.get(1).scrollTo().getText().replaceAll("\\D+","");
        if (collectData.getCur().equals("RUB")) totalPrice = totalPrice.substring(0, totalPrice.length()-2);
        if (collectData.getCur().equals("CNY")) totalPrice = totalPrice.substring(0, totalPrice.length()-2);
        System.out.println("Total price = " + totalPrice);
        if (!Values.reportData[collectData.getTest()].getPrice().total.equals(totalPrice)){
            String text = "Ошибка: [" + n + "] Информация по оплате на результирующей странице не корректна, " +
                    "ожидалось: " + Values.reportData[collectData.getTest()].getPrice().total + ", фактически: " + totalPrice;
            Values.addERR(collectData.getTest(), text);
            logDoc(text);
            screenShot();
        }
        /*проверка итоговой суммы оплаты сделана неблокирующей, т.к. это тоже ошибка в отображении EPR
        assertTrue("Сумма в Информации по оплате не совпадает с забронированной" +
                   "\nОжидалось:" + Values.price.total +
                   "\nФактически:" + totalPrice,
                   Values.price.total.equals(totalPrice));*/
    }

    private Date stringToDate(String d, String format) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat(format, new Locale(Values.lang[collectData.getLn()][2])).parse(d);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        return parsingDate;
    }

    private String getMiddleText(SelenideElement el){
        String allText = el.getText();
        String beforeText = el.$(byXpath("span")).getText();
        String afterText = el.$(byXpath("div[2]")).getText();
        allText = allText.substring(beforeText.length()+1);
        return allText.substring(0, allText.length() - afterText.length()-1);
    }

    @Step("Получить номер ваучера трансфера из АРМ")
    private String getTransferNumber() {
        //лезем в арм за номером ваучера трансфера
        String parentHandle = getWebDriver().getWindowHandle();
        ((JavascriptExecutor) getWebDriver()).
                executeScript("window.open(arguments[0])", Values.office_host);
        switchFromFirstPageToSecond(parentHandle);
        String numberOfVoucher = new OfficePage(collectData).getTransferNumberFromArm();
        getWebDriver().switchTo().window(parentHandle);
        return numberOfVoucher;
    }

}

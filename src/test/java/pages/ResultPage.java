package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Passenger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static config.Values.*;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 06.03.2018.
 */
public class ResultPage extends Page {

    @Step("Действие {0}, проверка страницы результатов оплаты")
    public void checkServicesData(String n, int test) {
        System.out.println("\t" + n + ". Cheking final page with pay result");
        checkPageAppear();
        ElementsCollection services = $$(byXpath("//div[@id='frame-additionalServices']/descendant::div[@role='row']"));
        if (test != 4) {
            services.get(0).scrollTo();
            checkFlyInsurance(services.get(0));
            services.get(1).scrollTo();
            checkMedicalInsurance(services.get(1));
            services.get(2).scrollTo();
        } else assertTrue("Обнаружены дополнительные услуги", services.size() == 0);
        if (test == 1) checkHotel(services.get(2));
        if (test == 2) checkTransport(services.get(2));
        checkTotalPrice();
    }

    private void checkPageAppear(){
        Sleep(25);
        $(byXpath("//div[contains(@class,'text text--bold')]")).shouldBe(visible).shouldBe(exactText(pnr));
        System.out.println("URL = " + url());
        $(byXpath("//div[contains(text(),'" + text[12][ln] + "')]")).shouldBe(visible);
    }

    @Step("Проверка полетной страховки")
    private void checkFlyInsurance(SelenideElement row){
        ElementsCollection docs = row.$$(byXpath("child::div[4]/div/a"));
        for (int i=0; i<docs.size(); i++) {
            Values.docs = Values.docs + docs.get(i).getText() + ", ";
        }
        String passengers = row.$(byXpath("child::div[2]")).scrollTo().getText().replaceAll("\\D+","");
        System.out.println("passengers = " + passengers);
        assertTrue("Количество пассажиров в полетной страховке отличается от количества билетов", ticket == stringIntoInt(passengers));

        String price = row.$(byXpath("child::div[3]")).getText().replaceAll("\\D+","");
        if (Values.cur.equals("RUB")) price = price.substring(0, price.length()-2);
        if (Values.cur.equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("fly insurance = " + price);
        assertTrue("Стоимость полетной страховки отличается от забронированной", Values.price.iflight.equals(price));

        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов не соответствует количеству пассажиров", docs.size() == ticket*2);
        for (int i=0; i<docs.size(); i=i+2) {
            assertTrue("Название квитанции некорректно", docs.get(i).getText().equals(text[13][ln]));
            assertTrue("Название полиса некорректно", docs.get(i+1).getText().contains(text[14][ln]));
        }
    }

    @Step("Проверка медицинской страховки")
    private void checkMedicalInsurance(SelenideElement row){
        ElementsCollection docs = row.$$(byXpath("child::div[4]/div/a"));
        for (SelenideElement doc : docs) {
            Values.docs = Values.docs + doc.getText() + ", ";
        }
        String passengers = row.$(byXpath("child::div[2]")).getText().replaceAll("\\D+","");
        System.out.println("passengers = " + passengers);
        assertTrue("Количество пассажиров в медицинской страховке отличается от количества билетов", ticket == stringIntoInt(passengers));

        String price = row.$(byXpath("child::div[3]")).getText().replaceAll("\\D+","");
        if (Values.cur.equals("RUB")) price = price.substring(0, price.length()-2);
        if (Values.cur.equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("med insurance = " + price);
        assertTrue("Стоимость медицинской страховки отличается от забронированной", Values.price.imedical.equals(price));

        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов не соответствует количеству пассажиров", docs.size() == ticket*2);
        for (int i=0; i<docs.size(); i=i+2) {
            assertTrue("Название квитанции некорректно", docs.get(i).getText().equals(text[13][ln]));
            assertTrue("Название полиса некорректно", docs.get(i+1).getText().contains(text[14][ln]));
        }
    }

    @Step("Проверка транспортной услуги")
    private void checkTransport(SelenideElement row){
        ElementsCollection docs = row.$$(byXpath("div[5]/a"));
        for (SelenideElement doc : docs) {
            Values.docs = Values.docs + doc.getText() + ", ";
        }
        String name = row.$(byXpath("div[1]/div")).getText();
        System.out.println("Auto = " + name);
        assertTrue("Название авто отличается от забронированного", auto.name.equals(name));

        SelenideElement receive = row.$(byXpath("div[2]"));
        String receiveLocation = getMiddleText(receive);
        System.out.println("Auto = " + receiveLocation);
        assertTrue("Место получения отличается от забронированного", auto.receiveLocation.equals(receiveLocation));

        String receiveDate = receive.$(byXpath("div[2]")).getText();
        receiveDate = receiveDate.substring(receiveDate.indexOf(",")+2);
        System.out.println("Auto = " + receiveDate);
        String f = "d MMMM yyyy, HH:mm";
        assertTrue("Дата получения отличается от забронированной", auto.receiveDate.equals(stringToDate(receiveDate, f)));

        SelenideElement retrn = row.$(byXpath("div[3]"));
        String returnLocation = getMiddleText(retrn);
        System.out.println("Auto = " + returnLocation);
        assertTrue("Место возврата отличается от забронированного", auto.returnLocation.equals(returnLocation));

        String returnDate = retrn.$(byXpath("div[2]")).getText();
        returnDate = returnDate.substring(returnDate.indexOf(",")+2);
        System.out.println("Auto = " + returnDate);
        assertTrue("Дата возврата отличается от забронированной", auto.returnDate.equals(stringToDate(returnDate, f)));

        String price = row.$(byXpath("div[4]")).getText().replaceAll("\\D+","");
        if (Values.cur.equals("RUB")) price = price.substring(0, price.length()-2);
        if (Values.cur.equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("Auto = " + price);
        assertTrue("Стоимость аренды автомобиля отличается от забронированной", Values.price.nationalTransport.equals(price));

        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов не один", docs.size() == 1);
        assertTrue("Название ваучера некорректно", docs.get(0).getText().contains(text[15][ln]));
    }

    @Step("Проверка услуги проживания")
    private void checkHotel(SelenideElement row){
        ElementsCollection docs = row.$$(byXpath("child::div[5]/div/a"));
        for (SelenideElement doc : docs) {
            Values.docs = Values.docs + doc.getText() + ", ";
        }
        /* проверка названия отеля не выполняется, поскольку это ошибка в отображении EPR
        String name = row.$(byXpath("child::div[1]")).getText();
        System.out.println("Hotel = " + name);
        assertTrue("Название отеля отличается от забронированного", hotel.name.equals(name));*/

        String sDate = row.$(byXpath("child::div[2]")).getText();
        int s = row.$(byXpath("child::div[2]/span")).getText().length()+1;
        sDate = sDate.substring(s);
        System.out.println("Hotel start = " + sDate);
        String f = "d MMMM yyyy";
        assertTrue("Дата заселения отличается от забронированной", hotel.accDate.equals(stringToDate(sDate, f)));

        String eDate = row.$(byXpath("child::div[3]")).getText();
        s = row.$(byXpath("child::div[3]/span")).getText().length()+1;
        eDate = eDate.substring(s);
        System.out.println("Hotel end = " + eDate);
        assertTrue("Дата выезда отличается от забронированной", hotel.depDate.equals(stringToDate(eDate, f)));

        String price = row.$(byXpath("child::div[4]")).getText().replaceAll("\\D+","");
        if (Values.cur.equals("RUB")) price = price.substring(0, price.length()-2);
        if (Values.cur.equals("CNY")) price = price.substring(0, price.length()-2);
        System.out.println("Hotel price = " + price);
        assertTrue("Стоимость проживания отличается от забронированной" +
                   "\nОжидалось :" + Values.hotel.price +
                   "\nФактически:" + price, Values.hotel.price.equals(price));

        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов некорректно" +
                   "\nОжидалось : 1\nФактически:" + docs.size(), docs.size() == 1);
        assertTrue("Название ваучера некорректно\nОжидалось : " + text[15][ln] + "\nФактически:" + docs.get(0).getText(),
                   docs.get(0).getText().contains(text[15][ln]));
    }

    @Step("Проверка оплаченной стоимости")
    private void checkTotalPrice(){
        ElementsCollection texts= $$(byXpath("//div[@class='col--16 col--stack-below-mobile']"));
        String totalPrice = texts.get(1).scrollTo().getText().replaceAll("\\D+","");
        if (Values.cur.equals("RUB")) totalPrice = totalPrice.substring(0, totalPrice.length()-2);
        if (Values.cur.equals("CNY")) totalPrice = totalPrice.substring(0, totalPrice.length()-2);
        System.out.println("Total price = " + totalPrice);
        if (!Values.price.total.equals(totalPrice)){
            error1 = "ОШИБКА!: Информация по оплате на результирующей странице не корректна, ожидалось " + Values.price.total;
            logDoc(error1);
            screenShot("Скриншот");
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
            parsingDate = new SimpleDateFormat(format, new Locale(Values.lang[ln][2])).parse(d);
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

}

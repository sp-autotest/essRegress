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

    @Step("Действие 17, проверка страницы результатов оплаты")
    public void step17() {
        checkPageAppear();
        ElementsCollection services = $$(byXpath("//div[@id='frame-additionalServices']/descendant::div[@role='row']"));
        checkFlyInsurance(services.get(0));
        checkMedicalInsurance(services.get(1));
        checkTransport(services.get(2));
        checkTotalPrice();
    }

    private void checkPageAppear(){
        Sleep(25);
        $(byXpath("//div[contains(@class,'text text--bold')]")).shouldBe(visible).shouldBe(exactText(pnr));
        System.out.println("URL = " + url());
        $(byXpath("//div[contains(text(),'" + text[12][ln] + "')]")).shouldBe(visible);
    }

    @Step("Проверка полетной страховки")
    public void checkFlyInsurance(SelenideElement row){
        String passengers = row.$(byXpath("child::div[2]")).getText().replaceAll("\\D+","");
        System.out.println("passengers = " + passengers);
        assertTrue("Количество пассажиров в полетной страховке отличается от количества билетов", ticket == stringIntoInt(passengers));

        String price = row.$(byXpath("child::div[3]")).getText().replaceAll("\\D+","");
        price = price.substring(0, price.length()-2);
        System.out.println("fly insurance = " + price);
        assertTrue("Стоимость полетной страховки отличается от забронированной", Values.price.iflight.equals(price));

        ElementsCollection docs = row.$$(byXpath("child::div[4]/div/a"));
        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов не соответствует количеству пассажиров", docs.size() == ticket*2);
        for (int i=0; i<docs.size(); i=i+2) {
            assertTrue("Название квитанции некорректно", docs.get(i).getText().equals(text[13][ln]));
            assertTrue("Название полиса некорректно", docs.get(i+1).getText().contains(text[14][ln]));
        }
    }

    @Step("Проверка медицинской страховки")
    public void checkMedicalInsurance(SelenideElement row){
        String passengers = row.$(byXpath("child::div[2]")).getText().replaceAll("\\D+","");
        System.out.println("passengers = " + passengers);
        assertTrue("Количество пассажиров в медицинской страховке отличается от количества билетов", ticket == stringIntoInt(passengers));

        String price = row.$(byXpath("child::div[3]")).getText().replaceAll("\\D+","");
        price = price.substring(0, price.length()-2);
        System.out.println("med insurance = " + price);
        assertTrue("Стоимость медицинской страховки отличается от забронированной", Values.price.imedical.equals(price));

        ElementsCollection docs = row.$$(byXpath("child::div[4]/div/a"));
        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов не соответствует количеству пассажиров", docs.size() == ticket*2);
        for (int i=0; i<docs.size(); i=i+2) {
            assertTrue("Название квитанции некорректно", docs.get(i).getText().equals(text[13][ln]));
            assertTrue("Название полиса некорректно", docs.get(i+1).getText().contains(text[14][ln]));
        }
    }

    @Step("Проверка транспортной услуги")
    public void checkTransport(SelenideElement row){
        String name = row.$(byXpath("child::div[1]")).getText();
        System.out.println("Auto = " + name);
        assertTrue("Название авто отличается от забронированного", auto.name.equals(name));

        String receiveLocation = row.$(byXpath("child::div[2]/div/div[2]")).getText();
        System.out.println("Auto = " + receiveLocation);
        assertTrue("Место получения отличается от забронированного", auto.receiveLocation.equals(receiveLocation));

        String receiveDate = row.$(byXpath("child::div[2]/div/div[3]")).getText();
        receiveDate = receiveDate.substring(receiveDate.indexOf(",")+1);
        System.out.println("Auto = " + receiveDate);
        assertTrue("Дата получения отличается от забронированной", auto.receiveDate.equals(stringToDate(receiveDate)));

        String returnLocation = row.$(byXpath("child::div[3]/div/div[2]")).getText();
        System.out.println("Auto = " + returnLocation);
        assertTrue("Место возврата отличается от забронированного", auto.returnLocation.equals(returnLocation));

        String returnDate = row.$(byXpath("child::div[3]/div/div[3]")).getText();
        returnDate = returnDate.substring(returnDate.indexOf(",")+1);
        System.out.println("Auto = " + returnDate);
        assertTrue("Дата возврата отличается от забронированной", auto.returnDate.equals(stringToDate(returnDate)));

        String price = row.$(byXpath("child::div[4]")).getText().replaceAll("\\D+","");
        price = price.substring(0, price.length()-2);
        System.out.println("Auto = " + price);
        assertTrue("Стоимость аренды автомобиля отличается от забронированной", Values.price.nationalTransport.equals(price));

        ElementsCollection docs = row.$$(byXpath("child::div[5]/div/a"));
        System.out.println("docs = " + docs.size());
        assertTrue("Количество приложенных документов не один", docs.size() == 1);
        assertTrue("Название ваучера некорректно", docs.get(0).getText().contains(text[15][ln]));
    }

    @Step("Проверка оплаченной стоимости (без транспорта)")
    private void checkTotalPrice(){
        ElementsCollection texts= $$(byXpath("//div[@class='col--16 col--stack-below-mobile']"));
        String totalPrice = texts.get(1).getText().replaceAll("\\D+","");
        totalPrice = totalPrice.substring(0, totalPrice.length()-2);
        System.out.println("Total price = " + totalPrice);
        assertTrue("Оплаченная стоимость не совпадает с забронированной", Values.price.total.equals(totalPrice));
    }

    private Date stringToDate(String d) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat("d MMMM yyyy, HH:mm", new Locale(Values.lang[ln][2])).parse(d);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        return parsingDate;
    }
}

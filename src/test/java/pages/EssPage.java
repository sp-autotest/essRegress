package pages;

import com.codeborne.selenide.ElementsCollection;
import config.Values;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static config.Values.ln;
import static config.Values.text;
import static org.testng.AssertJUnit.assertTrue;


/**
 * Created by mycola on 22.02.2018.
 */
public class EssPage extends Page {

    @Step("Действие 6, проверка формы ESS")
    public void step6() {
        checkPageAppear();
        checkFlight();
        checkNumber();
        checkDateTime();
        checkPrice();
        checkFlightInsurance();
        checkMedicalInsurance();
        checkCart();
        checkNextButton();
        checkTransport();
        checkDwelling();
        checkTimer();
    }

    @Step("Действие 7, проверка данных в блоке «Перелет»")
    public void step7(List<Flight> flightList) {
        ElementsCollection flights = $$(byXpath("//div[@class='cart__item-details']"));
        checkPriceData();
        for (int i = 0; i < flights.size(); i++) {
            checkFlightData(i+1, flightList, flights);
            checkNumberData(i+1, flightList, flights);
            checkDateData(i+1, flightList, flights);
            checkDurationData(i+1, flightList, flights);
        }
    }

    private void checkPageAppear(){
        $(byXpath("//div[@class='cart__item-title']")).shouldBe(visible).shouldBe(text(Values.pnr)).click();
    }

    @Step("Маршрут")
    private void checkFlight(){
        $(byXpath("//div[@class='cart__item-details-item']")).shouldBe(visible);
    }

    @Step("Номер рейса")
    private void checkNumber(){
        $(byXpath("//div[@class='cart__item-details-model']")).shouldBe(visible);
    }

    @Step("Дата и время рейса")
    private void checkDateTime(){
        $(byXpath("//div[@class='h-color--gray h-mt--4']")).shouldBe(visible);
    }

    @Step("Стоимость для всех")
    private void checkPrice(){
        $(byXpath("//div[@class='cart__item-price']")).shouldBe(visible);
    }

    @Step("Блок полетной страховки")
    private void checkFlightInsurance(){
        $(byXpath("//div[contains(text(),'" + text[0][ln] + "')]")).shouldBe(visible);
    }

    @Step("Блок медицинской страховки")
    private void checkMedicalInsurance(){
        $(byXpath("//div[contains(text(),'" + text[1][ln] + "')][contains(@class,'icon-medicial')]")).shouldBe(visible);
    }

    @Step("Блок корзины")
    private void checkCart(){
        $("#window-main-cart").shouldBe(visible);
    }

    @Step("Кнопка перехода")
    private void checkNextButton(){
        $(byXpath("//div[@class='next__button-inner']")).shouldBe(visible);
    }

    @Step("Транспорт")
    private void checkTransport(){
        $("#left-column-transport").shouldBe(visible).shouldBe(exactText(text[2][ln]));
    }

    @Step("Проживание")
    private void checkDwelling(){
        $(byXpath("//div[@class='cart__item']")).shouldBe(visible).shouldBe(exactText(text[3][ln]));
    }

    @Step("Таймер")
    private void checkTimer(){
        $(byXpath("//div[@class='cart__item-counter-time']")).shouldBe(visible);
    }

    @Step("Проверка данных о стоимости")
    private void checkPriceData(){
        String price = $(byXpath("//div[@class='cart__item-price']")).getText().replace(" ", "");
        assertTrue("Стоимость не совпадает c указанной при бронировании", price.equals(Values.price));
    }

    @Step("Проверка данных о {0}-м маршруте")
    private void checkFlightData(int i, List<Flight> flightList, ElementsCollection flights){
        String flight = flights.get(i-1).$(byXpath("descendant::div[@class='cart__item-details-item']")).getText().trim();
        assertTrue("Маршрут не совпадает с забронированным", flight.equals(flightList.get(i-1).from+" → "+flightList.get(i-1).to));
    }

    @Step("Проверка данных о номере {0}-го рейса")
    private void checkNumberData(int i, List<Flight> flightList, ElementsCollection flights){
        String number = flights.get(i-1).$(byXpath("descendant::div[@class='cart__item-details-model']")).getText().trim();
        assertTrue("Номер рейса не совпадает с забронированным", number.equals(flightList.get(i-1).number));
    }

    @Step("Проверка данных о дате {0}-го авиаперелета")
    private void checkDateData(int i, List<Flight> flightList, ElementsCollection flights){
        String date = flights.get(i-1).$(byXpath("descendant::div[@class='h-color--gray h-mt--4']")).getText().replace(" ", "");
        date = date.substring(0, date.indexOf("("));
        String dd = new SimpleDateFormat(Values.lang[ln][3], new Locale(Values.lang[ln][2])).format(flightList.get(i-1).start);
        dd = dd + new SimpleDateFormat("HH:mm").format(flightList.get(i-1).end);
        System.out.println("Locale = " + dd);
        assertTrue("Дата авиаперелета не совпадает с забронированной", date.equals(dd));
    }

    @Step("Проверка данных о длительности {0}-го авиаперелета")
    private void checkDurationData(int i, List<Flight> flightList, ElementsCollection flights){
        String duration = "";
        String time = flights.get(i-1).$(byXpath("descendant::div[@class='h-color--gray h-mt--4']")).getText();
        time = time.substring(time.indexOf("("), time.indexOf(")")-1);
        for (int c = 0; c < time.length(); c++) {
            if (Character.isDigit(time.charAt(c))) {
                duration = duration + time.charAt(c);
            }
        }
        System.out.println("duration = " + duration);
        assertTrue("Длительность авиаперелета не совпадает с забронированной", duration.equals(flightList.get(i-1).duration));
    }

}

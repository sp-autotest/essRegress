package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import dict.NationalityName;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
//import ru.yandex.qatools.allure.annotations.Step;
import io.qameta.allure.Step;
import struct.Flight;
import struct.Passenger;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static config.Values.*;
import static java.lang.Math.abs;
import static org.testng.AssertJUnit.assertTrue;


/**
 * Created by mycola on 22.02.2018.
 */
public class EssPage extends Page {

    @Step("Действие 6, Проверка формы ESS")
    public void step6() {
        System.out.println("\t6. Check ESS form");
        checkPageAppear();
        countrySelect();
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $("#left-column-insurance-block").click();//раскрыть блок Страховка
            Sleep(1);
        }else moveMouseToFlight();
        screenShot("Скриншот");
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
    }

    //проверка таймера изменена на основании задачи 2663
    @Step("Действие 6, Проверка таймера")
    public boolean checkTimer() {
        ElementsCollection timers = $$(byXpath("//div[@class='cart__item-counter-time']"));
        if ((timers.size() > 0) && (timers.get(0).isDisplayed())) {
            System.out.println("Timer value on ESS: " + timers.get(0).getText());
            return true;
        }
        System.out.println("Timer value on ESS not found");
        return false;
    }

    @Step("Действие 7, Проверка данных в блоке «Перелет»")
    public void step7(List<Flight> flightList) {
        System.out.println("\t7. Check Fly group");
        ElementsCollection flights = $$(byXpath("//div[@class='cart__item-details']"));
//        checkPriceData();
        for (int i = 0; i < flights.size(); i++) {
            checkFlightData(i+1, flightList, flights);
            checkNumberData(i+1, flightList, flights);
            checkDateData(i+1, flightList, flights);
            checkDurationData(i+1, flightList, flights);
        }
    }

    @Step("Действие 8, Проверка данных в блоке «Страховка»")
    public void step8() {
        System.out.println("\t8. Check Insurance group");
        checkFlyInsuranceInCard();
        checkPriceOfFlyInsurance();
    }

    @Step("Действие 8, Проверка отсутствия/добавления полетной страховки")
    public void step8_5() {
        System.out.println("\t8. Check missing of Fly Insurance");
        checkMissFlyInsuranceInCard();
        clickAddFlyInsuranceButton();
        checkFlyInsuranceInCard();
        checkPriceOfFlyInsurance();
    }

    @Step("Действие 8, Проверка данных в блоке «Страховка»")
    public void step8_7(List<Passenger> passengerList) {
        System.out.println("\t8. Check Insurance group");
        int n = 0;
        for (Passenger p : passengerList) {
            if (!p.getNationality().equals(NationalityName.getNationalityByLanguage("us", ln))) n++;
        }
        screenShot("Скриншот");
        if (n>0) {
            String summ = $("#left-column-insurance-block").$(byXpath("descendant::" +
                    "div[@class='cart__item-priceondemand-item-price']")).getText().trim();
            Values.price.iflight = summ.replaceAll("\\D+", "");
            int s = stringIntoInt(Values.price.iflight);
            System.out.println("Summ = " + s);
            String price = $(byXpath("//div[@class='frame__heading frame__heading--icon frame__heading--icon-safe']/span")).getText();
            price = price.substring(0, price.indexOf("(")).replaceAll("\\D+", "");
            if (Values.cur.equals("EUR") | Values.cur.equals("USD")) {
                if (price.length() == 1) price = price + "00";
                if (price.length() == 2) price = price + "0";
            }
            int p = stringIntoInt(price);
            System.out.println("price = " + p);
            System.out.println("no USA passengers = " + n);
            System.out.println("price * n = " + p * n);
            assertTrue("Общая сумма страховки не равняется сумме страховок каждого пассажира" +
                    "\nОжидалось : " + p * n +
                    "\nФактически: " + Values.price.iflight , s == p * n);
        } else checkMissFlyInsuranceInCard();
    }

    @Step("Действие 9, Проверка добавления Медицинской страховки {0}")
    public void step9(String type) {
        System.out.println("\t9. Add Medical Insurance");
        if (type.equals("RANDOM")) type = getRandomMedicalInsurance();
        clickAddMedicalButton(type);
        screenShot("Скриншот");
        checkPriceOfMedicalInsurance(type);
        checkMedicalButtonName(type);
        checkTotalAndInsurensPrices();
        screenShot("Скриншот");
    }

    @Step("Действие 9, Удалить услугу «Полётная страховка»")
    public void deleteFlyInsurance() {
        System.out.println("\t9. Delete Fly Insurance");
        clickFlyInsuranceButton();
        checkMissFlyInsuranceInCard();
        checkFlyInsuranceButton(Values.text[5][ln]);
        checkTotalAndFlyPrices();
        screenShot("Скриншот");
    }

    @Step("Действие 10, Нажать Оплатить в корзине")
    public void clickPayInCart() {
        System.out.println("\t10. Click Pay in cart");
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $("#left-column-insurance-block").click();//раскрыть блок Страховка
            Sleep(1);
        }
        $(byXpath("//a[@class='cart__item-counter-link']")).click();
        waitPlane();
    }



    @Step("Нажать кнопку «Транспорт»")
    public void clickTransportButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
        waitPlane();
    }

    private void checkPageAppear(){
        //$(byXpath("//div[@class='cart__item-title']")).shouldBe(visible).shouldBe(text(pnr)).click();
        $("#left-column-insurance-block").shouldBe(visible);
        if ($$("#acceptCookiesLaw").size()>0){
            Sleep(1);
            $("#acceptCookiesLaw").click();
        }
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

    @Step("Проверка данных о стоимости")
    private void checkPriceData(){
        String price = $(byXpath("//div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        assertTrue("Стоимость не совпадает c указанной при бронировании" +
                   "\nОжидалось: " + Values.price.fly +"\nФактически: " + price,
                   price.equals(Values.price.fly));
    }

    @Step("Проверка данных о {0}-м маршруте")
    private void checkFlightData(int i, List<Flight> flightList, ElementsCollection flights){
        String flight = flights.get(i-1).$(byXpath("descendant::div[@class='cart__item-details-item']")).getText().trim();
        String expected = flightList.get(i-1).from+" → "+flightList.get(i-1).to;
        assertTrue("Маршрут не совпадает с забронированным" +
                   "\nОжидалось : " + expected +
                   "\nФактически: " + flight,
                   flight.equals(expected));
    }

    @Step("Проверка данных о номере {0}-го рейса")
    private void checkNumberData(int i, List<Flight> flightList, ElementsCollection flights){
        String number = flights.get(i-1).$(byXpath("descendant::div[@class='cart__item-details-model']")).getText().trim();
        assertTrue("Номер рейса не совпадает с забронированным" +
                   "\nОжидалось : " + flightList.get(i-1).number +
                   "\nФактически: " + number,
                   number.equals(flightList.get(i-1).number));
    }

    @Step("Проверка данных о дате {0}-го авиаперелета")
    private void checkDateData(int i, List<Flight> flightList, ElementsCollection flights){
        String date = flights.get(i-1).$(byXpath("descendant::div[@class='h-color--gray h-mt--4']")).getText().replace(" ", "");
        date = date.substring(0, date.indexOf("("));
        System.out.println("Site = " + date);
        String format = Values.lang[ln][3];
        String month = new SimpleDateFormat("MMMM", new Locale(Values.lang[ln][2])).format(flightList.get(i-1).start);
        if (month.length()>5) format = format.replaceFirst("MMMM", "MMM.");
        if (month.equals("Сентябрь")) format = format.replaceFirst("MMM", "сент");
        String dd = new SimpleDateFormat(format, new Locale(Values.lang[ln][2])).format(flightList.get(i-1).start);
        dd = dd + new SimpleDateFormat("HH:mm").format(flightList.get(i-1).end);
        System.out.println("Locale = " + dd);
        assertTrue("Дата авиаперелета не совпадает с забронированной"+
                   "\nОжидалось : " + dd + "\nФактически: " + date, date.contains(dd));
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
        assertTrue("Длительность авиаперелета не совпадает с забронированной" +
                   "\nОжидалось : " + flightList.get(i-1).duration +
                   "\nФактически: " + duration,
                   duration.equals(flightList.get(i-1).duration));
    }

    @Step("Полетная страховка в корзине")
    private void checkFlyInsuranceInCard(){
        $("#left-column-insurance-block").$(byXpath("descendant::div[@class='cart__item-priceondemand-item-title']"))
                .shouldBe(exist).shouldBe(visible).shouldBe(exactText(text[0][ln]));
    }

    @Step("Полетная страховка отсутствует в корзине")
    private void checkMissFlyInsuranceInCard(){
        $("#left-column-insurance-block").$(byXpath("descendant::div[text()='" + text[0][ln] + "']"))
                .shouldNotBe(visible).shouldNotBe(exist);
    }

    @Step("Нажать кнопку «Добавить в заказ» для полетной страховки")
    private void clickAddFlyInsuranceButton(){
        SelenideElement ins = $("#flight_insurance_select_button");
        ins.scrollTo().click();
    }

    @Step("Проверка общей суммы полетной страховки")
    private  void checkPriceOfFlyInsurance(){
        String summ = $("#left-column-insurance-block").$(byXpath("descendant::" +
                "div[@class='cart__item-priceondemand-item-price']")).getText().trim();
        Values.price.iflight = summ.replaceAll("\\D+","");
        int s = stringIntoInt(Values.price.iflight);
        System.out.println("Summ = " + s);
        String price = $(byXpath("//div[@class='frame__heading frame__heading--icon frame__heading--icon-safe']/span")).getText();
        price = price.substring(0, price.indexOf("(")).replaceAll("\\D+","");
        if (Values.cur.equals("EUR")|Values.cur.equals("USD")) {
            if (price.length() == 1) price = price + "00";
            if (price.length() == 2) price = price + "0";
        }
        int p = stringIntoInt(price);
        System.out.println("price = " + p);
        System.out.println("ticket = " + ticket);
        System.out.println("price * ticket = " + p*ticket);
        assertTrue("Общая сумма страховки не равняется сумме страховок каждого пассажира" +
                "\nОжидалось : " + Values.price.iflight +
                "\nФактически: " + p*ticket, s == p*ticket);
    }

    @Step("Нажать кнопку «Добавить в заказ»")
    private void clickAddMedicalButton(String type){
        SelenideElement ins = $("#medIns" + type);
        ins.scrollTo();
        ins.$(byXpath("descendant::a[contains(@class,'button--micro-padding')]"))
                .shouldBe(visible).shouldBe(exactText(text[5][ln])).click();
    }

    @Step("Проверка стоимости медицинской страховки")
    private void checkPriceOfMedicalInsurance(String type) {
        SelenideElement box = $("#medIns" + type);
        String price = box.$(byXpath("descendant::div[@class='tile__price']")).getText().replaceAll("\\D+","");
        String name = box.$(byXpath("descendant::div[@class='tile__title']")).getText();
        System.out.println("Type of medical insurance = " + name);
        SelenideElement p = $(byXpath("//div[@class='cart__item-priceondemand-item-title']" +
                "[contains(text(),'" + name + "')]")).shouldBe(visible);
        Values.price.imedical = p.$(byXpath("following-sibling::div[@class='cart__item-priceondemand-item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Med price = " + Values.price.imedical);
        assertTrue("Стоимость страхования в корзине не совпадает с указанной в блоке" +
                   "\nОжидалось : " + price +
                   "\nФактически: " + Values.price.imedical,
                   price.equals(Values.price.imedical));
        Sleep(3);
    }

    @Step("Проверка кнопки «В заказе»")
    private void checkMedicalButtonName(String type){
        $("#medIns" + type).$(byXpath("descendant::a[contains(@class,'button--micro-padding')]"))
                .shouldBe(visible).shouldBe(exactText(text[6][ln])).scrollTo();
    }

    @Step("Проверка общей суммы заказа (включает в себя стоимость услуг страхования)")
    private void checkTotalAndInsurensPrices(){
        String itemPrice;
        String flyPrice = $(byXpath("//div[contains(@class,'cart__item-price cart__item-price--hovered')]")).scrollTo().getText().replaceAll("\\D+","");
        int summ = stringIntoInt(flyPrice);
        System.out.println("Fly price = " + flyPrice);
        ElementsCollection items = $("#left-column-insurance-block").$$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']"));
        for (int i=0; i<items.size(); i++) {
            itemPrice = items.get(i).getText().replaceAll("\\D+","");
            summ = summ + stringIntoInt(itemPrice);
            System.out.println("Item[" + (i+1) + "] price = " + itemPrice);
        }
        Sleep(3);
        String totalPrice = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Total price = " + totalPrice);
        Values.price.total = totalPrice;
        assertTrue("Общая сумма заказа некорректна" +
                   "\nОжидалось : " + summ +
                   "\nФактически: " + Values.price.total,
                   abs(summ - stringIntoInt(Values.price.total))<2);
    }

    @Step("Проверка общей суммы заказа")
    private void checkTotalAndFlyPrices(){
        Sleep(3);
        String flyPrice = $(byXpath("//div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        Values.price.total = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Total price = " + Values.price.total);
        assertTrue("Общая сумма заказа некорректна" +
                   "\nОжидалось : " + Values.price.total +
                   "\nФактически: " + flyPrice,
                   flyPrice.equals(Values.price.total));
    }



    @Step("Нажать кнопку выбора в полетной страховке")
    private void clickFlyInsuranceButton() {
        WebElement el = $("#flight_insurance_select_button").toWebElement();
        Actions actions = new Actions(getWebDriver());
        actions.moveToElement(el).perform();
        $("#flight_insurance_deselect_button").shouldBe(visible).click();
    }

    @Step("Проверить текст кнопки в полетной страховке")
    private void checkFlyInsuranceButton(String text){
        String name = $("#flight_insurance_select_button").getText();
        assertTrue("Название кнопки в блоке полетной страховки некорректно" +
                   "\nОжидалось: " + text +
                   "\nФактически: " + name,
                   name.equals(text));
    }

    @Step("Переместить мышку в блок маршрутов")
    private void moveMouseToFlight() {
        WebElement el = $(byXpath("//div[@data-toggle-id='cart-booking']")).toWebElement();
        Actions actions = new Actions(getWebDriver());
        actions.moveToElement(el).perform();
        Sleep(1);
    }

    @Step("Выбрать страну в диалоге справа вверху")
    private void countrySelect() {
        ElementsCollection counry = $$("#countryListToggle");
        if (counry.size()>0) {
            counry.get(0).click();
            $(byXpath("//input[@id='select-country-ru']/..")).shouldBe(visible).click();
            $(byXpath("//button[contains(@class,'submitSelectedCountry')]")).shouldBe(visible).click();
            Sleep(2);
        }
    }

    private String getRandomMedicalInsurance() {
        int n = getRandomNumberLimit(3);
        switch (n) {
            case 0 : return "COMMON_SPORT";
            case 1 : return "TEAM_SPORTS";
            case 2 : return "DANGEROUS_SPORT";
            default : return "COMMON_SPORT";
        }
    }

}

package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import dict.AddService;
import dict.NationalityName;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import io.qameta.allure.Step;
import struct.CollectData;
import struct.Flight;
import struct.Passenger;
import struct.Price;

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

    private CollectData collectData;

    public EssPage(CollectData collectData) {
        this.collectData = collectData;
    }

    @Step("Действие 6, Проверка формы ESS")
    public void step6() {
        System.out.println("\t6. Check ESS form");
        checkPageAppear();
        countrySelect();
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $("#left-column-insurance-block").click();//раскрыть блок Страховка
            Sleep(1);
        }else moveMouseToFlight();
        screenShot();
        checkFlight();
        checkNumber();
        checkDateTime();
        checkPrice();
        checkFlightInsurance();
        //checkMedicalInsurance();  //временно отключено 19.10.18
        checkCart();
        checkNextButton();
        checkTransport();
        checkDwelling();
    }

    //проверка таймера изменена на основании задачи 2663
    @Step("Проверка таймера")
    public boolean checkTimer() {
        ElementsCollection timers = $$(byXpath("//div[@class='cart__item-counter-time']"));
        if ((timers.size() > 0) && (timers.get(0).isDisplayed())) {
            System.out.println("Timer value on ESS: " + timers.get(0).getText());
            return true;
        }
        System.out.println("Timer value on ESS not found");
        return false;
    }

    @Step("Действие 5, Проверка даты/времени на ESS")
    public void checkDateOnESS(List<Flight> flightList) {
        System.out.println("\t5. Check ESS form");
        checkPageAppear();
        countrySelect();
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            $("#left-column-insurance-block").click();//раскрыть блок Страховка
            Sleep(1);
        }else moveMouseToFlight();
        screenShot();
        ElementsCollection flights = $$(byXpath("//div[@class='cart__item-details']"));
        for (int i = 0; i < flights.size(); i++) {
            checkDateData(i+1, flightList, flights);
        }
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
    @Step("Действие 7, Проверка добавления дополнительных авиа услуг")
    public  void checkAdditionalServices() {
        System.out.println("\t7. Check Additional Services");
        checkSelectPlaceStep();
        //checkSelectFoodStep(); //отключено согласно задачи 3311
    }

    @Step("Проверка данных в блоке «ВЫБОР МЕСТ»")
    private void checkSelectPlaceStep() {
        System.out.println("Check Prereserved place");
        String code = "0B5"; //код услуги "Предварительный выбор места"
        String etalon = AddService.getServiceByCodeAndLanguage(code, collectData.getLn()) + " (1)";
        SelenideElement place = $(byXpath("//div[@class='cart__item cart__item--last']"));
        String nameOfService = place.$(byXpath("descendant::div[@class='cart__item-priceondemand-item-title']"))
                .shouldBe(visible).getText();
        assertTrue("Некорректное название услуги в блоке «ВЫБОР МЕСТ»" +
                    "\nОжидалось : " + etalon +
                    "\nФактически: " + nameOfService,
                    nameOfService.equals(etalon));
        Values.reportData[collectData.getTest()].getPrice().place = place.$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']"))
                .shouldBe(visible).getText().replaceAll("\\D+", "");
        System.out.println("Select place price = " + Values.reportData[collectData.getTest()].getPrice().place);
    }

    @Step("Проверка данных в блоке «ВЫБОР ПИТАНИЯ»")
    private void checkSelectFoodStep() {
        System.out.println("Check food");
        ElementsCollection cart_item = $$(byXpath("//div[@class='cart__item cart__item--last']"));
        assertTrue("Блоков с доп.услугами недостаточно" +
                   "\nОжидалось : 2" +
                   "\nФактически: " + cart_item.size(),
                   cart_item.size() == 2);
        ElementsCollection eat = cart_item.get(1).$$(byXpath("descendant::div[@class='cart__item-priceondemand-item-title']"));

        String code = "0B3"; //код услуги "Закуска Сырная"
        String etalon = AddService.getServiceByCodeAndLanguage(code, collectData.getLn()) + " (1)";
        String nameOfService = eat.get(0).shouldBe(visible).getText();
        assertTrue("Некорректное название услуги основного блюда в блоке «ВЫБОР ПИТАНИЯ»" +
                   "\nОжидалось : " + etalon +
                   "\nФактически: " + nameOfService,
                   nameOfService.equals(etalon));
        Values.reportData[collectData.getTest()].getPrice().entree = eat.get(0).$(byXpath("following-sibling::div[@class='cart__item-priceondemand-item-price']"))
                .shouldBe(visible).getText().replaceAll("\\D+", "");
        System.out.println("Price of " + nameOfService + " = " + Values.reportData[collectData.getTest()].getPrice().entree);

        code = "019"; //код услуги "Десерт Шоколадная тарталетка"
        etalon = AddService.getServiceByCodeAndLanguage(code, collectData.getLn()) + " (1)";
        nameOfService = eat.get(1).shouldBe(visible).getText();
        assertTrue("Некорректное название услуги десерта в блоке «ВЫБОР ПИТАНИЯ»" +
                   "\nОжидалось : " + etalon +
                   "\nФактически: " + nameOfService,
                   nameOfService.equals(etalon));
        Values.reportData[collectData.getTest()].getPrice().dessert = eat.get(1).$(byXpath("following-sibling::div[@class='cart__item-priceondemand-item-price']"))
                .shouldBe(visible).getText().replaceAll("\\D+", "");
        System.out.println("Price of " + nameOfService + " = " + Values.reportData[collectData.getTest()].getPrice().dessert);
    }

    @Step("Действие 8, Проверка данных в блоке «Страховка»")
    public void step8() {
        System.out.println("\t8. Check Insurance group");
        checkFlyInsuranceInCard();
        checkPriceOfFlyInsurance();
    }

    @Step("Действие 8, Проверка отсутствия и удаление полетной страховки")
    public void step8_5() {
        System.out.println("\t8. Check missing of Fly Insurance");
        SelenideElement block = $("#left-column-insurance-block");
        ElementsCollection fly = block.$$(byXpath("descendant::div[(@class='cart__item-priceondemand-item-title')" +
                " and (text()='" + text[0][collectData.getLn()] + "')]"));
        if (fly.size()>0) clickFlyInsuranceButton();
        Values.reportData[collectData.getTest()].getPrice().iflight = "0";
    }

    @Step("Действие 8, Проверка данных в блоке «Страховка»")
    public void step8_7(List<Passenger> passengerList) {
        System.out.println("\t8. Check Insurance group");
        int n = 0;
        for (Passenger p : passengerList) {
            if (!p.getNationality().equals(NationalityName.getNationalityByLanguage("us", collectData.getLn()))) n++;
        }
        screenShot();
        if (n>0) {
            String summ = $("#left-column-insurance-block").$(byXpath("descendant::" +
                    "div[@class='cart__item-priceondemand-item-price']")).getText().trim();
            Values.reportData[collectData.getTest()].getPrice().iflight = summ.replaceAll("\\D+", "");
            int s = stringIntoInt(Values.reportData[collectData.getTest()].getPrice().iflight);
            System.out.println("Summ = " + s);
            String price = $(byXpath("//div[@class='frame__heading frame__heading--icon frame__heading--icon-safe']/span")).getText();
            price = price.substring(0, price.indexOf("(")).replaceAll("\\D+", "");
            if (collectData.getCur().equals("EUR") | collectData.getCur().equals("USD")) {
                if (price.length() == 1) price = price + "00";
                if (price.length() == 2) price = price + "0";
            }
            int p = stringIntoInt(price);
            System.out.println("price = " + p);
            System.out.println("no USA passengers = " + n);
            System.out.println("price * n = " + p * n);
            assertTrue("Общая сумма страховки не равняется сумме страховок каждого пассажира" +
                    "\nОжидалось : " + p * n +
                    "\nФактически: " + Values.reportData[collectData.getTest()].getPrice().iflight , s == p * n);
        } else checkMissFlyInsuranceInCard();
    }

    @Step("Действие 9, Проверка добавления Медицинской страховки {0}")
    public void step9(String type) {
        System.out.println("\t9. Add Medical Insurance");
        if (type.equals("RANDOM")) type = getRandomMedicalInsurance();
        clickAddMedicalButton(type);
        screenShot();
        checkPriceOfMedicalInsurance(type);
        checkMedicalButtonName(type);
        checkTotalAndInsurensPrices();
        screenShot();
    }

    @Step("Действие 9, Удалить услугу «Полётная страховка»")
    public void deleteFlyInsurance() {
        System.out.println("\t9. Delete Fly Insurance");
        clickFlyInsuranceButton();
        screenShot();
        checkMissFlyInsuranceInCard();
        checkFlyInsuranceButton(Values.text[5][collectData.getLn()]);
        checkTotalAndFlyPrices();
        screenShot();
    }

    @Step("Действие {0}, Нажать Оплатить в корзине")
    public void clickPayInCart(int n) {
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
        $(byXpath("//div[contains(text(),'" + text[0][collectData.getLn()] + "')]")).shouldBe(visible);
    }

    @Step("Блок медицинской страховки")
    private void checkMedicalInsurance(){
        $(byXpath("//div[contains(text(),'" + text[1][collectData.getLn()] + "')][contains(@class,'icon-medicial')]")).shouldBe(visible);
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
        $("#left-column-transport").shouldBe(visible).shouldBe(exactText(text[2][collectData.getLn()]));
    }

    @Step("Проживание")
    private void checkDwelling(){
        $(byXpath("//div[@class='cart__item']")).shouldBe(visible).shouldBe(exactText(text[3][collectData.getLn()]));
    }

    @Step("Проверка данных о стоимости")
    private void checkPriceData(){
        String price = $(byXpath("//div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        assertTrue("Стоимость не совпадает c указанной при бронировании" +
                   "\nОжидалось: " + Values.reportData[collectData.getTest()].getPrice().fly +
                   "\nФактически: " + price,
                   price.equals(Values.reportData[collectData.getTest()].getPrice().fly));
    }

    @Step("Проверка данных о {0}-м маршруте")
    private void checkFlightData(int i, List<Flight> flightList, ElementsCollection flights){
        String flight = flights.get(i-1).$(byXpath("descendant::div[@class='cart__item-details-item']")).getText().trim();
        String expected = flightList.get(i-1).from+"—"+flightList.get(i-1).to;
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
        String format = Values.lang[collectData.getLn()][3];
        String month = new SimpleDateFormat("MMMM", new Locale(Values.lang[collectData.getLn()][2])).format(flightList.get(i-1).start);
        if (month.length()>5) format = format.replaceFirst("MMMM", "MMM.");
        if (month.equals("Сентябрь")) format = format.replaceFirst("MMM", "сент");
        if (month.equals("Ноябрь")) format = format.replaceFirst("MMM", "нояб");
        if (month.equals("Февраль")) format = format.replaceFirst("MMM", "февр");
        String dd = new SimpleDateFormat(format, new Locale(Values.lang[collectData.getLn()][2])).format(flightList.get(i-1).start);
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
        String[] t = time.split(" ");
        String min = t[1].replaceAll("\\D+","");
        duration = t[0].replaceAll("\\D+","") + min;
        if (min.equals("0")) duration = duration + "0";
        System.out.println("duration = " + duration);
        assertTrue("Длительность авиаперелета не совпадает с забронированной" +
                   "\nОжидалось : " + flightList.get(i-1).duration +
                   "\nФактически: " + duration,
                   duration.equals(flightList.get(i-1).duration));
    }

    @Step("Полетная страховка в корзине")
    private void checkFlyInsuranceInCard(){
        $("#left-column-insurance-block").$(byXpath("descendant::div[@class='cart__item-priceondemand-item-title']"))
                .shouldBe(exactText(text[0][collectData.getLn()]));
    }

    @Step("Полетная страховка отсутствует в корзине")
    private void checkMissFlyInsuranceInCard(){
        $("#left-column-insurance-block").$(byXpath("descendant::div[text()='" + text[0][collectData.getLn()] + "']"))
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
        int s = stringIntoInt(summ.replaceAll("\\D+",""));
        System.out.println("Summ = " + s);
        String price = $(byXpath("//div[@class='frame__heading frame__heading--icon frame__heading--icon-safe']/span")).getText();
        price = price.substring(0, price.indexOf("(")).replaceAll("\\D+","");
        if (collectData.getCur().equals("EUR")|collectData.getCur().equals("USD")) {
            if (price.length() == 1) price = price + "00";
            if (price.length() == 2) price = price + "0";
        }
        int p = stringIntoInt(price);
        System.out.println("price = " + p);
        System.out.println("ticket = " + collectData.getTicket());
        System.out.println("price * ticket = " + p*collectData.getTicket());
        /*выключено согласно задачи 3387
        assertTrue("Общая сумма страховки не равняется сумме страховок каждого пассажира" +
                "\nОжидалось : " + s +
                "\nФактически: " + p*collectData.getTicket(), s == p*collectData.getTicket());*/
        Values.reportData[collectData.getTest()].getPrice().iflight = "" + p * collectData.getTicket();
    }

    @Step("Нажать кнопку «Добавить в заказ»")
    private void clickAddMedicalButton(String type){
        SelenideElement ins = $("#medIns" + type);
        ins.scrollTo();
        SelenideElement button = ins.$(byXpath("descendant::a[contains(@class,'button--micro-padding')]"))
                .shouldBe(visible).shouldBe(exactText(text[5][collectData.getLn()]));
        Sleep(1);
        button.$(byXpath("parent::div")).click();
    }

    @Step("Проверка наличия и стоимости медицинской страховки в корзине")
    private void checkPriceOfMedicalInsurance(String type) {
        SelenideElement box = $("#medIns" + type);
        String price = box.$(byXpath("descendant::div[@class='tile__price']")).getText().replaceAll("\\D+","");
        String name = box.$(byXpath("descendant::div[@class='tile__title']")).getText();
        System.out.println("Type of medical insurance = " + name);
        SelenideElement p = $(byXpath("//div[@class='cart__item-priceondemand-item-title']" +
                "[contains(text(),'" + name + "')]")).shouldBe(visible);
        Values.reportData[collectData.getTest()].getPrice().imedical = p.$(byXpath("following-sibling::div[@class='cart__item-priceondemand-item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Med price = " + Values.reportData[collectData.getTest()].getPrice().imedical);
        assertTrue("Стоимость страхования в корзине не совпадает с указанной в блоке" +
                   "\nОжидалось : " + price +
                   "\nФактически: " + Values.reportData[collectData.getTest()].getPrice().imedical,
                   price.equals(Values.reportData[collectData.getTest()].getPrice().imedical));
        Sleep(3);
    }

    @Step("Проверка кнопки «В заказе»")
    private void checkMedicalButtonName(String type){
        String textButton = text[6][collectData.getLn()];
        if (collectData.getLn() == 8) textButton = "必要ない"; //костыль - тут японское название кнопки "В заказе" некорректно,но чтобы не падал тест - подменяю
        $("#medIns" + type).$(byXpath("descendant::a[contains(@class,'button--micro-padding')]"))
                .shouldBe(visible).shouldBe(exactText(textButton)).scrollTo();
    }

    @Step("Проверка общей суммы заказа (включает в себя стоимость услуг страхования)")
    private void checkTotalAndInsurensPrices(){
        Price price = Values.reportData[collectData.getTest()].getPrice();
        String itemPrice;
        String flyPrice = $(byXpath("//div[contains(@class,'cart__item-price cart__item-price--hovered')]")).scrollTo().getText().replaceAll("\\D+","");
        Values.reportData[collectData.getTest()].getPrice().fly = flyPrice;
        int summ = stringIntoInt(flyPrice);
        System.out.println("Fly price = " + flyPrice);
        ElementsCollection items = $("#left-column-insurance-block").$$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']"));
        for (int i=0; i<items.size(); i++) {
            itemPrice = items.get(i).getText().replaceAll("\\D+","");
            summ = summ + stringIntoInt(itemPrice);
            System.out.println("Item[" + (i+1) + "] price = " + itemPrice);
        }
        Sleep(3);
        /*учитываем доп. услуги питания и выбора места*/
        if (price.place != null) summ = summ + stringIntoInt(price.place);
        if (price.entree != null) summ = summ + stringIntoInt(price.entree);
        if (price.dessert != null) summ = summ + stringIntoInt(price.dessert);
        /**/
        String totalPrice = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Total price = " + totalPrice);
        Values.reportData[collectData.getTest()].getPrice().total = totalPrice;
        assertTrue("Общая сумма заказа некорректна" +
                   "\nОжидалось : " + summ +
                   "\nФактически: " + totalPrice,
                   abs(summ - stringIntoInt(totalPrice))<2);
    }

    @Step("Проверка общей суммы заказа")
    private void checkTotalAndFlyPrices(){
        Sleep(3);
        String flyPrice = $(byXpath("//div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        Values.reportData[collectData.getTest()].getPrice().total = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Total price = " + Values.reportData[collectData.getTest()].getPrice().total);
        assertTrue("Общая сумма заказа некорректна" +
                   "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().total +
                   "\nФактически: " + flyPrice,
                   flyPrice.equals(Values.reportData[collectData.getTest()].getPrice().total));
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
        WebElement el = $(byXpath("//p[@class='cart__booking-code']")).toWebElement();
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

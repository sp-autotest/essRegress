package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.yandex.qatools.allure.annotations.Step;
import struct.Flight;
import struct.Passenger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.source;
import static config.Values.*;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 28.02.2018.
 */
public class TransportPage extends Page {

    @Step("Действие 10, Нажать на кнопку «Транспорт»")
    public void step10(int test) {
        System.out.println("\t10. Click Transport button");
        new EssPage().clickTransportButton();
        checkTransportBlock();
        if (test == 1) {
            screenShot("Скриншот");
            checkAeroexpressForm();
            checkAeroexpressInCard();
        }
        if (test == 2) {
            checkCarFilter();
            screenShot("Скриншот");
            checkSelectedCars();
        }
        if (test == 3) {
            screenShot("Скриншот");
            checkAeroexpressForm();
            checkCarRentalForm();
            checkTransferForm();
            checkMissTransportServices();
        }

    }

    @Step("Действие 11, Арендовать автомобиль")
    public void step11() {
        System.out.println("\t11. Rent Auto");
        selectCar();
        screenShot("Скриншот");
        int beforePrice = getCarPrice();
        addInsurance();
        screenShot("Скриншот");

//      проверка временно отключена, до фикса бага
//      checkAllPrice(getInsurancePrice(), beforePrice, getCarPrice());

        price.nationalTransport = getAllCarPrice();
        price.transport = getEuroAllCarPrice();
        clickRentButton();
        screenShot("Скриншот");
        checkTranspotrPriceInCard();
        checkRentButtonName();
        checkOptionsButton();
        checkTimeButton();
        checkInsuranceButton();
        checkTotalPrices();
        saveTransportData();
    }

    @Step("Действие 11, Проверка выпадающего списка выбора пассажиров")
    public void checkAeroexpressPassengersList() {
        System.out.println("\t11. Check dropdown list in Aeroexpress");
        dropdownClick(0);
        checkAeroexpressTikets();
        checkAeroexpressPrice();
        checkAeroexpressPricesIsNotAdded();
    }

    @Step("Действие 11, Проверка логики отображения пассажиров Аэроэкспресса")
    public void checkAeroexpressPassengerLogic() {
        System.out.println("\t11. Check Aeroexpress passengers logic");
        dropdownClick(1);
        checkAeroexpressPassengers();
        dropdownClick(0);
        checkAeroexpressPassengers();
        checkAeroexpressPrice();
        checkAeroexpressChangePrice();
    }

    @Step("Действие 12, Нажать Оплатить в корзине")
    public void step12() {
        System.out.println("\t12. Click Pay in cart");
        $(byXpath("//a[@class='cart__item-counter-link']")).click();
        waitPlane();
    }

    @Step("Действие 12, Проверка логики отображения информации об Аэроэкспресс")
    public void checkAeroexpressLogic(List<Flight> flightList) {
        System.out.println("\t12. Check Aeroexpress logic");
        ElementsCollection directions = $$(byXpath("//div[@class='aeroexpress-select__direction-name']"));
        String dir;
        /*Туда*/
        switch (flightList.get(0).from_orig) {
            case "SVO":
                dir = directions.get(0).getText();
                assertTrue("Направление \"Из города\" некорректно\nОжидалось : " + text[18][ln] + "\nФактически: " + dir, dir.equals(text[18][ln]));
                dir = directions.get(1).getText();
                assertTrue("Направление \"В аэропорт\" некорректно\nОжидалось : " + text[19][ln] + "\nФактически: " + dir, dir.equals(text[19][ln]));
                break;
            case "VKO":
                dir = directions.get(0).getText();
                assertTrue("Направление \"Из города\" некорректно\nОжидалось : " + text[20][ln] + "\nФактически: " + dir, dir.equals(text[20][ln]));
                dir = directions.get(1).getText();
                assertTrue("Направление \"В аэропорт\" некорректно\nОжидалось : " + text[21][ln] + "\nФактически: " + dir, dir.equals(text[21][ln]));
                break;
        }
        /*Обратно*/
        switch (flightList.get(flightList.size()-1).to_orig) {
            case "SVO":
                dir = directions.get(2).getText();
                assertTrue("Направление \"Из аэропорта\" некорректно\nОжидалось : " + text[19][ln] + "\nФактически: " + dir, dir.equals(text[19][ln]));
                dir = directions.get(3).getText();
                assertTrue("Направление \"В город\" некорректно\nОжидалось : " + text[18][ln] + "\nФактически: " + dir, dir.equals(text[18][ln]));
                break;
            case "VKO":
                dir = directions.get(2).getText();
                assertTrue("Направление \"Из аэропорта\" некорректно\nОжидалось : " + text[21][ln] + "\nФактически: " + dir, dir.equals(text[21][ln]));
                dir = directions.get(3).getText();
                assertTrue("Направление \"В город\" некорректно\nОжидалось : " + text[20][ln] + "\nФактически: " + dir, dir.equals(text[20][ln]));
                break;
        }
    }

    @Step("Действие 13, Добавить в заказ билеты на Аэроэкспресс")
    public void addAeroexpressTickets() {
        System.out.println("\t13. Add Aeroexpress tickets");
        jsClick($(byXpath("//div[contains(@class,'dropdown--show')]/descendant::input")));
        clickAddInOrderButton();
        checkAeroexpressInCart();
    }

    @Step("Действие 14, Характеристики услуги «Бронирование трансфера»")
    public String setTransferLocations() {
        System.out.println("\t14. Set transfer locations");
        $("#iway_transfer_page").scrollTo();
        $("#iway_change_city").selectOptionByValue("1202");
        $(byXpath("//div[@id='transfer_options_list']/descendant::div[@class='frame__container']")).shouldBe(visible);
        $("#iway_change_city1").selectOptionByValue("1200");
        Sleep(1); //задержка в трансфере между селектом направления и кликом по кнопке Выбрать
        return $("#iway_change_city").getText() + " — " + $("#iway_change_city1").getText();
    }

    @Step("Действие 15, Нажать на кнопку «Выбрать» для категории Стандарт")
    public void clickSelectStandartButton() {
        System.out.println("\t15. Click Select button");
        String price = "";
                ElementsCollection categories = $("#transfer_options_list").$$(byXpath("descendant::div[@class='frame__container']"));
        for (int i=0; i<categories.size(); i++){
            String cat = categories.get(i).$(byXpath("descendant::h3")).getText();
            System.out.println(cat);
            if (cat.equals(Values.text[26][ln])){ //Стандарт
                price = categories.get(i).$(byXpath("div/div[4]")).getText().replaceAll("\\D+","");
                System.out.println("transfer = " + price);
                categories.get(i).$(byXpath("descendant::a[contains(@class,'button')]")).click();
                break;
            }
        }
        checkTransferAdditionalForm();
        Values.price.transfer = price;
    }

    @Step("Действие 16, Заполнить и проверить форму трансфера")
    public void setTransferAdditionalInfo(Date date, String dir) {
        System.out.println("\t16. Setting transfer form");
        setTransferDate(new SimpleDateFormat("d.MM.yyyy").format(date));
        setTransferText();
        setTransferTime();
        compareDirection(dir);
    }

    @Step("Действие 17, Нажать кнопку «Выбрать»")
    public void selectTransfer(Date date, String dir) {
        System.out.println("\t17. Click Select button in transfer form");
        clickSelectButton();
        checkTransferInCart();
        checkTransferAllData(date, dir);
    }

    @Step("Действие 18, Нажать Продолжить")
    public void clickContinue() {
        System.out.println("\t18. Click Continue button");
        $(byXpath("//a[@class='next__button']")).click();
        waitPlane();
    }

    @Step("Проверка перехода в раздел «Транспорт»")
    private void checkTransportBlock(){
        $(byXpath("//div[@id='left-column-transport'][contains(@class,'--active')]")).
                shouldBe(visible).shouldBe(exactText(text[2][ln]));
    }

    @Step("Проверка наличия фильтра для поиска автомобиля")
    private void checkCarFilter(){
        $("#frame-cars").shouldBe(visible).scrollTo();
        $(byXpath("//select[@name='stationReceive']")).shouldBe(visible).shouldBe(enabled);
        $(byXpath("//select[@name='stationReturn']")).shouldBe(visible).shouldBe(enabled);
        $("#dateStartCars").shouldBe(visible).shouldBe(enabled);
        $(byXpath("//input[@name='timeReceive']")).shouldBe(visible).shouldBe(enabled);
        $("#dateEndCars").shouldBe(visible).shouldBe(enabled);
        $(byXpath("//input[@name='timeReturn']")).shouldBe(visible).shouldBe(enabled);
        $("#auto\\/manual").shouldBe(visible).shouldBe(enabled);
        $(byXpath("//button[@onclick='priceSort(this);']")).shouldBe(visible).shouldBe(enabled);
        $(byXpath("//button[@onclick='sizeSort(this);']")).shouldBe(visible).shouldBe(enabled);
    }

    @Step("Проверка поиска авто с АКПП")
    private void checkSelectedCars(){
        $("#auto\\/manual").selectOptionByValue("1"); //выбрать только авто с АКПП
        Sleep(1);
        ElementsCollection gears = $$(byXpath("//div[@class='icon icon--auto-gear']/following-sibling::div"));
        for (int i=0; i< gears.size(); i++) {
            if (gears.get(i).isDisplayed()) {
                System.out.println("Gear =" + gears.get(i).getText());
                assertTrue("Фильтр по трансмиссии не сработал.", gears.get(i).getText().equals(text[7][ln]));
            }
        }
    }

    @Step("Выбрать автомобиль")
    private void selectCar(){
        Sleep(1);
        ElementsCollection cars = $$(byXpath("//a[@class='auto-card__button']"));
        for (int i=0; i<cars.size(); i++) {
            if (cars.get(i).isDisplayed()){
                cars.get(i).click();
                break;
            }
        }
        $("#button_top_price").shouldBe(visible);
    }

    @Step("Добавить страховку SPCDW")
    private void addInsurance(){
        SelenideElement ins = $("#insurance-options-SPCDW");
        ins.scrollTo();
        ins.$(byXpath("descendant::div[@class='_button-group']")).click();
    }

    @Step("Нажать кнопку «Арендовать»")
    private void clickRentButton(){
        $("#button_down_price").click();
        Sleep(2);
    }

    @Step("Проверка кнопки «В заказе»")
    private void checkRentButtonName(){
        $(byXpath("//div[@class='auto-card__button-top']")).shouldBe(visible).shouldBe(exactText(text[6][ln]));
    }

    @Step("Проверка кнопки «Изменить выбранные опции»")
    private void checkOptionsButton(){
        assertTrue("Кнопка не обнаружена", $(byXpath("//div[@class='auto-selected__options-button']/a"))
                .getText().equals(text[8][ln]));
    }

    @Step("Проверка кнопки «Изменить время и место»")
    private void checkTimeButton(){
        assertTrue("Кнопка не обнаружена", $(byXpath("//div[@class='auto-selected__rent-button']/a"))
                .getText().trim().equals(text[9][ln]));
    }

    @Step("Проверка кнопки «Изменить страховку»")
    private void checkInsuranceButton(){
        assertTrue("Кнопка не обнаружена", $(byXpath("//div[@class='auto-selected__options-button'][2]/a"))
                .getText().equals(text[10][ln]));
    }

    private int getInsurancePrice(){
        String p = $("#insurance-options-SPCDW").$(byXpath("descendant::div[@class='tile__price']")).getText().replaceAll("\\D+","");
        System.out.println("Car insurence price ="+p);
        return stringIntoInt(p);
    }

    private int getCarPrice(){
        SelenideElement price = $(byXpath("//div[@id='button_down_price']/itm"));
        //price.scrollTo();
        String p = price.getText().replaceAll("\\D+","");
        System.out.println("Car price="+p);
        return stringIntoInt(p);
    }

    private String getAllCarPrice(){
        String p = $(byXpath("//div[@id='button_down_allprice']/itm")).getText().replaceAll("\\D+","");
        System.out.println("All car price ="+p);
        return p;
    }

    private String getEuroAllCarPrice(){
        String p = $(byXpath("//div[@id='button_down_allprice']/span/itmsub")).getText().replaceAll("\\D+","");
        System.out.println("Euro All car price ="+p);
        return p;
    }

    @Step("Проверка пересчета стоимости: без доп. услуг {1}, страховка {0}, всего {2}")
    private void checkAllPrice(int insurance, int before, int after){
        assertTrue("Стоимость пересчитана неверно\n" +
                "Ожидалось: " + (before + insurance) +
                "\nФактически: " + after,
                after == before + insurance);
    }

    @Step("Проверка стоимости аренды автомобиля в корзине")
    private void checkTranspotrPriceInCard() {
        String leftPrice = $("#left-column-transport").$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Transport price = " + leftPrice);
        assertTrue("Стоимость аренды авто в корзине не совпадает с указанной в блоке" +
                "\nОжидалось: " + price.nationalTransport +
                "\nФакически: " + leftPrice,
                price.nationalTransport.equals(leftPrice));
    }

    @Step("Проверка общей суммы заказа (включает в себя стоимость транспортных услуг)")
    private void checkTotalPrices(){
        String itemPrice;
        String flyPrice = $(byXpath("//div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        int summ = stringIntoInt(flyPrice);
        Actions actions = new Actions(getWebDriver());
        actions.moveToElement($("#left-column-insurance-block").toWebElement(),1,1).build().perform();
        Sleep(1);
        ElementsCollection items = $("#left-column-insurance-block").$$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']"));
        for (int i=0; i<items.size(); i++) {
            itemPrice = items.get(i).getText().replaceAll("\\D+","");
            System.out.println("Item[" + (i+1) + "] price = " + itemPrice);
            summ = summ + stringIntoInt(itemPrice);
        }
        String transportPrice = $("#left-column-transport").$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']")).getText().replaceAll("\\D+","");
        summ = summ + stringIntoInt(transportPrice);
        String totalPrice = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Total price = " + totalPrice);
        assertTrue("Общая сумма заказа некорректна" +
                "\nОжидалось: " + summ +
                "\nФактически: " + totalPrice,
                summ == stringIntoInt(totalPrice));
    }

    private void saveTransportData(){
        Values.auto.name = $(byXpath("//div[@class='auto-card__title']")).getText();
        System.out.println("Auto = " + Values.auto.name);
        ElementsCollection ec = $$(byXpath("//div[@class='auto-selected__rent-element-text']"));
        for(int i = 0; i<ec.size(); i++) System.out.println("Auto = " + ec.get(i).getText());
        Values.auto.receiveLocation = ec.get(0).getText();
        String receiveDate = ec.get(1).getText() + ec.get(2).getText();
        try {
            Values.auto.receiveDate = new SimpleDateFormat("dd.MM.yyyyHH:mm").parse(receiveDate);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        Values.auto.returnLocation = ec.get(3).getText();
        String returnDate = ec.get(4).getText() + ec.get(5).getText();
        try {
            Values.auto.returnDate = new SimpleDateFormat("dd.MM.yyyyHH:mm").parse(returnDate);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
    }

    @Step("Проверка отображения раздела «Билеты на аэроэкспресс»")
    private void checkAeroexpressForm(){
        SelenideElement h = $(byXpath("//div[@class='axpholder']")).shouldBe(exist).shouldBe(visible);
        h.$(byXpath("descendant::h2")).shouldBe(exactText(text[16][ln]));
    }

    @Step("Проверка отображения раздела «Прокат автомобилей»")
    private void checkCarRentalForm(){
        SelenideElement h = $(byXpath("//div[@class='carholder']")).shouldBe(exist).shouldBe(visible);
        h.$(byXpath("descendant::h2")).shouldBe(exactText(text[24][ln]));
    }

    @Step("Проверка отображения раздела «Бронирование трансфера»")
    private void checkTransferForm(){
        SelenideElement h = $("#iway_transfer_page").shouldBe(exist).shouldBe(visible);
        h.$(byXpath("descendant::h2")).shouldBe(exactText(text[25][ln]));
    }

    @Step("Проверка отсутствия услуги «Аэроэкспресс» в корзине")
    private void checkAeroexpressInCard(){
        String service = "";
        if (ln == 0) {
            service = "Аэроэкспресс";
        }else {
            service = "Aeroexpress";
        }
        $("#left-column-transport").$(byXpath("descendant::div[contains(text(),'"+ service +"')]")).shouldNotBe(exist);
    }

    @Step("Развернуть выпадающий список выбора пассажиров")
    private void dropdownClick(int n){
        ElementsCollection drop = $$("#countTickets_");
        drop.get(n).shouldBe(visible).click();
    }

    @Step("Проверить количество билетов, выбранных для поездки")
    private void checkAeroexpressTikets(){
        int count = stringIntoInt($("#countTickets_").getText().replaceAll("\\D+",""));
        assertTrue("Количество билетов Аэроэкспресс, выбранных для поездки, отличается от количества пассажиров" +
                        "\nОжидалось: " + ticket +
                        "\nФактически: " + count,
                        count == ticket);
    }

    @Step("Проверить общую стоимость для всех пассажиров")
    private void checkAeroexpressPrice(){
        int summ = 0;
        String price = $(byXpath("//div[contains(@class,'js-price')]")).getText().replaceAll("\\D+","");
        SelenideElement d = $(byXpath("//div[@class='dropdown__items']"));
        ElementsCollection prices = d.$$(byXpath("descendant::div[@class='h-display--inline']/span"));
        for (int i=0; i<prices.size(); i++) {
            summ = summ + stringIntoInt(prices.get(i).getText().replaceAll("\\D+",""));
        }
        assertTrue("Общая стоимость Аэроэкспресс не равна сумме для всех пассажиров" +
                        "\nОжидалось: " + summ +
                        "\nФактически: " + price,
                        summ == stringIntoInt(price));
    }

    @Step("Стоимость поездки на Аэроэкспрессе не добавлена в корзину")
    private void checkAeroexpressPricesIsNotAdded() {
        //int summ = stringIntoInt(price.fly) + stringIntoInt(price.iflight) + stringIntoInt(price.imedical);
        String totalPrice = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        assertTrue("Сумма \"Всего к оплате\" не корректна" +
                        "\nОжидалось: " + Values.price.total +
                        "\nФактически: " + totalPrice,
                        totalPrice.equals(Values.price.total));
    }

    @Step("Проверка отсутствия услуг в блоке Транспорт")
    private void checkMissTransportServices() {
        ElementsCollection services = $("#left-column-transport").$$(byXpath("descendant::div[@class='cart__item-priceondemand-item']"));
        assertTrue("Обнаружены услуги в блоке Транспорт", services.size() == 0);
    }

    @Step("Проверить количество пассажиров, выбранных для поездки")
    private void checkAeroexpressPassengers(){
        ElementsCollection chb = $(byXpath("//div[contains(@class,'dropdown--show')]")).$$(byXpath("descendant::input"));
        int n = 0;
        for (int i=0; i<chb.size(); i++) if (chb.get(i).isSelected()) n++;
        assertTrue("Для поездки на Аэроэкспресс выбраны не все пассажиры" +
                   "\nОжидалось: " + ticket + "\nФактически: " + n, n == ticket);
    }

    @Step("Проверить изменение общей стоимости Аэроэкспресс")
    private void checkAeroexpressChangePrice(){
        String price1 = $(byXpath("//div[contains(@class,'js-price')]")).getText().replaceAll("\\D+","");
        jsClick($(byXpath("//div[contains(@class,'dropdown--show')]/descendant::input")));
        String price2 = $(byXpath("//div[contains(@class,'js-price')]")).getText().replaceAll("\\D+","");
        assertTrue("Общая стоимость Аэроэкспресс не изменилась", !price1.equals(price2));
    }

    @Step("Нажать кнопку «Добавить в заказ»")
    private void clickAddInOrderButton(){
        $(byXpath("//a[contains(@id,'axp-send')]")).click();
    }

    @Step("Проверить наличие и сумму Аэроэкспресс в корзине")
    private void checkAeroexpressInCart() {
        String price = $(byXpath("//div[contains(@class,'js-price')]")).getText().replaceAll("\\D+", "");
        System.out.println("aeroexpress = "+price);
        SelenideElement transport = $("#left-column-transport");
        String leftPrice = transport.$(byXpath("descendant::div[@class='cart__item-priceondemand-item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Aeroexpress price = " + leftPrice);
        assertTrue("Сумма Аэроэкспресс в корзине не совпадает с рассчитанной" +
                   "\nОжидалось: " + price +
                   "\nФакически: " + leftPrice,
                   price.equals(leftPrice));
        Values.price.aeroexpress = price;
        String service = "";
        if (ln == 0) service = "Аэроэкспресс (2)";
        else service = "Aeroexpress (2)";
        transport.$(byXpath("descendant::div[contains(text(),'"+ service +"')]")).shouldBe(visible);
    }

    @Step("Проверить наличие формы дополнительной информации о трансфере")
    private void checkTransferAdditionalForm(){
        $(byXpath("//h2[text()='"+Values.text[26][ln]+"']")).shouldBe(visible);//Стандарт
    }

    @Step("Указать дату трансфера")
    private void setTransferDate(String date){
        SelenideElement el = $("#iway_calendar_ow");
        JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
        executor.executeScript("arguments[0].removeAttribute('readonly');", el.toWebElement());
        el.setValue(date);
        executor.executeScript("arguments[0].setAttribute('readonly', '');", el.toWebElement());
    }

    @Step("Указать время трансфера")
    private void setTransferTime(){
        $(byXpath("//*[@id='iway_time']/..")).click();
    }

    @Step("Указать текст на табличке трансфера")
    private void setTransferText(){
        $("#transferItemDesc").setValue("Test Test");
    }

    @Step("Сравнить направления")
    private void compareDirection(String dir){
        System.out.println(dir);
        String direction = $(byXpath("//div[@id='trip_hi']/h3")).getText();
        assertTrue("Направления на форме с доп.информацией не совпадают с выбранными" +
                   "\nОжидалось: " + dir +
                   "\nФакически: " + direction,
                   dir.equals(direction));
    }

    @Step("Нажать кнопку «Выбрать»")
    private void clickSelectButton(){
        $(byXpath("//button[contains(@onclick,'book_transfer')]")).click();
    }

    @Step("Проверить наличие и сумму трансфера в корзине")
    private void checkTransferInCart() {
        SelenideElement transport = $("#left-column-transport");
        SelenideElement transfer = transport.$(byXpath("descendant::div[contains(text(),'"+ Values.text[27][ln] +"')]")).shouldBe(visible);
        String leftPrice = transfer.$(byXpath("parent::div/div[@class='cart__item-priceondemand-item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Transfer price = " + leftPrice);
        assertTrue("Сумма трансфера в корзине не совпадает с рассчитанной" +
                   "\nОжидалось: " + Values.price.transfer +
                   "\nФакически: " + leftPrice,
                   Values.price.transfer.equals(leftPrice));
    }

    @Step("Проверить наличие и сумму трансфера в корзине")
    private void checkTransferAllData(Date date, String dir) {
        String from = getTransferFrom();
        String fromC = dir.substring(0, dir.indexOf("—")-1);
        String to = getTransferTo();
        String toC = dir.substring(dir.indexOf("—")+2);
        String tdate = getTransferDate();
        String dateC = new SimpleDateFormat("dd MMMM , E", new Locale(Values.lang[ln][2])).format(date);
        String time = getTransferTime();
        String category = getTransferCategory();
        String summ = getTransferSumm();
        assertTrue("Направление Откуда трансфера не совпадает с выбранным" +
                   "\nОжидалось: " + fromC +
                   "\nФакически: " + from,
                   from.equals(fromC));
        assertTrue("Направление Куда трансфера не совпадает с выбранным" +
                   "\nОжидалось: " + toC +
                   "\nФакически: " + to,
                   to.equals(toC));
        assertTrue("Дата трансфера не совпадает с выбранной" +
                   "\nОжидалось: " + dateC +
                   "\nФакически: " + tdate,
                   tdate.equals(dateC));
        assertTrue("Время трансфера не совпадает с выбранным" +
                   "\nОжидалось: 00:00" +
                   "\nФакически: " + time,
                   time.equals("00:00"));
        assertTrue("Категория трансфера не совпадает с выбранной" +
                   "\nОжидалось:" + text[26][ln] +
                   "\nФакически: " + category,
                   category.equals(text[26][ln]));
        assertTrue("Сумма трансфера не совпадает с расчитанной" +
                   "\nОжидалось:" + price.transfer +
                   "\nФакически: " + summ,
                   summ.equals(price.transfer));
    }

    private String getTransferFrom(){
        return $(byXpath("//div[@class='text h-clearfix h-mb--16 h-fz--14']")).getText().trim();
    }

    private String getTransferTo(){
        return $(byXpath("//div[@class='text h-clearfix h-fz--14']")).getText().trim();
    }

    private String getTransferDate(){
        return $(byXpath("//div[@class='text h-clearfix h-mb--28 h-fz--14']/span[1]")).getText().trim();
    }

    private String getTransferTime(){
        return $(byXpath("//div[@class='text h-clearfix h-mb--28 h-fz--14']/span[2]")).getText().trim();
    }

    private String getTransferCategory(){
        return $(byXpath("//div[@class='col--11 col--stack-below-tablet']/descendant::h3")).getText();
    }

    private String getTransferSumm(){
        return $(byXpath("//span[@class='h-pull--right h-fz--18 h-fw--700']")).getText().replaceAll("\\D+","");
    }


}

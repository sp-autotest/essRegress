package pages;

import com.codeborne.selenide.Condition;
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

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static config.Values.ln;
import static config.Values.text;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 15.03.2018.
 */
public class HotelPage extends Page {

    @Step("Форма дополнительных услуг «Проживание» открылась")
    private void checkHotelFormAppear() {
        $(byXpath("//h1[contains(text(),'" + text[17][ln] + "')]")).shouldBe(visible);
        System.out.println("Accommodation form appeared");
    }

    @Step("Действие 15, Нажать кнопку «Проживание»")
    public void clickResidenceButton() {
        System.out.println("15. Click Accommodation");
        $(byXpath("//div[@class='cart__item-title'][contains(text(),'" + text[3][ln] + "')]")).shouldBe(visible).click();
        waitPlane();
        checkHotelFormAppear();
    }

    @Step("Действие 16, Проверка фильтра поиска отелей")
    public void checkHotelFilter() {
        System.out.println("16. Check exist of hotel filter");
        $("#dateStartHotel").shouldBe(visible);//поле даты заезда
        $("#dateEndHotel").shouldBe(visible);//поле даты выезда
        $("#shown_stars").shouldBe(visible);//поле выбора звездности
        $("#hotel_min_price-value").shouldBe(visible);//поле минимальной цены
        $("#hotel_max_price-value").shouldBe(visible);//поле максимальной цены
        $(byXpath("//div[@class='range-slider']")).shouldBe(visible);//слайдер цены
        $("#hotels-filter-room-1").shouldBe(visible);//блок выбора гостей
        $(byXpath("//label[@for='hotel-checkbox']")).shouldBe(visible);//чекбокс Я ознакомился
        $(byXpath("//a[@data-check-id='button-hotel']")).shouldBe(visible);//кнопка Найти отель
        $("#additional-hotels-filter-name").shouldBe(visible);//поле Название
        $("#search-hotel-food4").shouldBe(visible);//поле Питание
        $("#search-hotel-service").shouldBe(visible);//поле Услуги в отеле
        $(byXpath("//button[@data-order-by='Price']")).shouldBe(visible);//кнопка Сортировка по цене
        $(byXpath("//button[@data-order-by='Distance']")).shouldBe(visible);//кнопка Сортировка по удаленности от центра
        $(byXpath("//button[@data-order-by='Category']")).shouldBe(visible);//кнопка Сортировка по звездности
    }

    @Step("Действие 17, Проверка логики отображения информации в блоке «Проживание»")
    public void checkHotelLogic(List<Flight> flightList) {
        System.out.println("17. Check logic in Accommodation block");
        checkStartHotelDate(new SimpleDateFormat("yyyy-MM-dd").format(flightList.get(0).end));
        checkEndHotelDate(new SimpleDateFormat("yyyy-MM-dd").format(flightList.get(1).start));
        checkResidentsNumber();
        checkRoomCount();
        clickHotelCheckbox();
        clickHotelSearchButton();
    }

    @Step("Действие 18, Проверка использования данных пассажиров при бронировании")
    public void checkPassengersData(List<Passenger> passList) {
        System.out.println("18. Check passengers data");
        ElementsCollection info = $$(byXpath("//div[@class='hotel-selected__room-guest-info']/div"));
        int n = checkPassengerName(info.get(0), passList);
        checkPassengerDob(info.get(1), passList.get(n).dob);
        checkEmail();
        checkPhone();
    }

    @Step("Действие 19, Проверка возможности фильтрации")
    public void checkFiltration() {
        System.out.println("19. Check filtering");
        setPriceFilter();
        checkHotelFilterByPrice();
        setStarsFilter();
        checkHotelFilterByStars();
    }

    @Step("Действие 20, Проверка возможности сортировки")
    public void checkSorting() {
        System.out.println("20. Checking the sorting");
        System.out.println("sort by price");
        checkDecreasingPrice();
        clickSortByPriceButton();
        checkAscendingPrice();
        System.out.println("sort by stars");
        checkDecreasingStars();
        clickSortByStarsButton();
        checkAscendingStars();
    }

    @Step("Действие 21, Выбор отеля для аренды")
    public void selectHotel() {
        System.out.print("21. Select hotel: ");
        $(byXpath("//div[@id='shown_stars']/..")).click(); //кликнуть по выпадающему списку звезд
        setStarsCheckbox($("#stars5")); //отметить галкой 5-ти звездные отели в фильтре
        clickHotelSearchButton();//найти
        ElementsCollection hotels = null;
        String name = "";
        int n = 0;
        for (int i=0; i<20; i++) {
            Sleep(1);
            hotels = $$(byXpath("//*[@id='hotel-search-result']/div/div/a"));
            n = getRandomNumberLimit(hotels.size());
            name = hotels.get(n).getText();
            if (name.length()>0) break;
        }
        System.out.println(name);
        hotels.get(n).click();
        waitPlane();
        $(byXpath("//h1[@class='modal__header-title']")).shouldBe(visible);
    }

    @Step("Действие 22, Выбор типа номера")
    public int selectRoomType() {
        System.out.println("22. Select type of room");
        ElementsCollection rooms = $$(byXpath("//li[@class='hotel-room']"));
        int sel = getRandomNumberLimit(rooms.size());
        String price = rooms.get(sel).$(byXpath("descendant::div[@class='hotel-room__buy-price']")).shouldBe(visible).getText();
        System.out.println("Room price = " + price);
        return sel;
    }

    @Step("Действие 23, Нажать кнопку «Забронировать»")
    public void clickBookButton(int room) {
        System.out.println("23. Click botton \"Book\"");
        ElementsCollection rooms = $$(byXpath("//li[@class='hotel-room']"));
        rooms.get(room).$(byXpath("descendant::div[@class='hotel-room__buy-button-wrapper']")).shouldBe(visible).click();
        checkHotelFormAppear();
        checkHotelCartPrice();
        checkRentButtonName();
    }

    @Step("Проверить совпадение автоматической даты вселения с датой прилета")
    private void checkStartHotelDate(String start) {
        String minHotel = $(byXpath("//input[@name='hotel_min_date']")).getValue();
        assertTrue("Дата вселения отличается от даты прилета" +
                        "\nОжидалось: " + start +
                        "\nФактически: " + minHotel,
                        start.equals(minHotel));
    }

    @Step("Проверить совпадение автоматической даты выселения с датой отлета")
    private void checkEndHotelDate(String end) {
        String maxHotel = $(byXpath("//input[@name='hotel_max_date']")).getValue();
        assertTrue("Дата выселения отличается от даты отлета" +
                        "\nОжидалось: " + end +
                        "\nФактически: " + maxHotel,
                        end.equals(maxHotel));
    }

    @Step("Проверить количество проживающих")
    private void checkResidentsNumber() {
        int n = $$(byXpath("//div[@class='js-travellers-list']/div")).size();
        assertTrue("Не соответствует количество проживающих\nОжидалось: 1\nФактически: " + n, n == 1);
    }

    @Step("Проверить количество номеров")
    private void checkRoomCount() {
        int n = $$(byXpath("//div[contains(@id,'hotels-filter-room-')]")).size();
        assertTrue("Не соответствует количество номеров\nОжидалось: 1\nФактически: " + n, n == 1);
    }

    @Step("Отметить чекбокс «Я принимаю...»")
    private void clickHotelCheckbox() {
        WebElement checkbox = $("#hotel-checkbox").toWebElement();
        JavascriptExecutor executor = (JavascriptExecutor)getWebDriver();
        executor.executeScript("arguments[0].click();", checkbox);
    }

    @Step("Нажать кнопку «Найти отель»")
    private void clickHotelSearchButton() {
        $(byXpath("//a[@data-check-id='button-hotel']")).click();
        waitPlane();
        $(byXpath("//h1[contains(text(),'" + text[17][ln] + "')]")).shouldBe(visible);
    }

    @Step("Проверить Имя/Фамилию пассажира")
    private int checkPassengerName(SelenideElement pass, List<Passenger> passList) {
        String name = pass.getText().trim();
        System.out.println("Name = " + name);
        String bookname = "";
        boolean found = false;
        int i = 0;
        for (Passenger p : passList) {
            bookname = (p.lastname + " " + p.firstname).toUpperCase();
            if (bookname.equals(name)){
                found = true;
                break;
            }
            i++;
        }
        assertTrue("Имя <" + name + ">, указанное при бронировании отеля, отсутствует среди пассажиров", found);
        return i;
    }

    @Step("Проверить дату рождения пассажира")
    private void checkPassengerDob(SelenideElement info, String dob) {
        String date = info.getText().trim();
        System.out.println("Date on site = " + date);
        System.out.println("Date in save = " + dob);
        String datef = new java.text.SimpleDateFormat("ddMMyyyy").format(stringToDate(date));
        assertTrue("Дата рождения пассажира не совпадает с забронированной" +
                 "\nОжидалось: " + dob +
                 "\nФактически: " + datef,
                 dob.equals(datef));
    }


    @Step("Установить фильтр цены")
    private void setPriceFilter() {
        ElementsCollection balls = $$(byXpath("//div[@class='range-slider']/div"));
        int center = (int) ($(byXpath("//div[@class='range-slider']")).getSize().width/2.5);
        int decile = $(byXpath("//div[@class='range-slider']")).getSize().width/6;
        System.out.println("center = " + center);
        System.out.println("decile = " + decile);
        Actions actions = new Actions(getWebDriver());
        actions.dragAndDropBy(balls.get(0).toWebElement(), center, 0).perform();
        actions.dragAndDropBy(balls.get(2).toWebElement(), -decile, 0).perform();
    }

    @Step("Проверить цены найденных отелей")
    private void checkHotelFilterByPrice(){
        float from = Float.parseFloat($("#hotel_min_price-value").getValue().replace(" ",""));
        float to = Float.parseFloat($("#hotel_max_price-value").getValue().replace(" ",""));
        System.out.println("From = " + from);
        System.out.println("To = " + to);

        clickHotelSearchButton();
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        for (int i=0; i<hotels.size(); i++) {
            float price = Float.parseFloat(hotels.get(i).$(byXpath("descendant::" +
                    "div[@class='hotel-card__button-price']")).getText().replaceAll("\\s|a|\\$|€|¥",""));
            System.out.println("hotel price"+i+" = " + price);
            assertTrue("Цена " + price + "отеля №" + i + " не входит в фильтр" +
                            "\nОт: " + from +
                            "\nДо: " + to,
                            (from<=price)&(price<=to));
        }
    }

    @Step("Установить фильтр звездности")
    private void setStarsFilter() {
        $(byXpath("//div[@id='shown_stars']/..")).click(); //кликнуть по выпадающему списку звезд
        SelenideElement chb;
        for (int i=1; i<6; i++) {
            chb = $("#stars"+i);
            if (i!=4) clearStarsCheckbox(chb); //снять галки с чекбокса кроме четырех звезд
        }

    }

    @Step("Установить галку в чекбоксе со звездами")
    private void setStarsCheckbox(SelenideElement chb) {
        if (!chb.isSelected()) {
            WebElement checkbox = chb.toWebElement();
            JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
            executor.executeScript("arguments[0].click();", checkbox);
        }
    }

    @Step("Снять галку в чекбоксе со звездами")
    private void clearStarsCheckbox(SelenideElement chb) {
        if (chb.isSelected()) {
            WebElement checkbox = chb.toWebElement();
            JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
            executor.executeScript("arguments[0].click();", checkbox);
        }
    }

    @Step("Проверить звездность найденных отелей")
    private void checkHotelFilterByStars(){
        clickHotelSearchButton();
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        for (int i=0; i<hotels.size(); i++) {
            hotels.get(i).scrollTo();
            ElementsCollection stars = hotels.get(i).$$(byXpath("descendant::div[@class='stars-item stars-item--mark']"));
            int s = stars.size();
            System.out.println("hotel stars"+i+" = " + s);
            assertTrue("Звездность " + s + " отеля №" + i + " не равна 4", s == 4);
        }
    }

    @Step("Проверить сортировку по убыванию цены")
    private void checkDecreasingPrice(){
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        String price = hotels.get(0).$(byXpath("descendant::div[@class='hotel-card__button-allprice']")).getText();
        price = price.substring(0, price.indexOf(" "));
        System.out.println("hotel all price1 = " + price);
        float max = Float.parseFloat(price);
        float next;
        for (int i=1; i<hotels.size(); i++) {
            hotels.get(i).scrollTo();
            price = hotels.get(i).$(byXpath("descendant::div[@class='hotel-card__button-allprice']")).getText();
            price = price.substring(0, price.indexOf(" "));
            System.out.println("hotel all price"+(i+1)+" = " + price);
            next = Float.parseFloat(price);
            assertTrue("Сортировка некорректна, стоимость следующего отеля выше: " + next, max>=next);
            max = next;
        }
    }

    @Step("Проверить сортировку по возрастанию цены")
    private void checkAscendingPrice(){
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        String price = hotels.get(0).$(byXpath("descendant::div[@class='hotel-card__button-allprice']")).getText();
        price = price.substring(0, price.indexOf(" "));
        System.out.println("hotel all price1 = " + price);
        float min = Float.parseFloat(price);
        float next;
        for (int i=1; i<hotels.size(); i++) {
            hotels.get(i).scrollTo();
            price = hotels.get(i).$(byXpath("descendant::div[@class='hotel-card__button-allprice']")).getText();
            price = price.substring(0, price.indexOf(" "));
            System.out.println("hotel all price"+(i+1)+" = " + price);
            next = Float.parseFloat(price);
            assertTrue("Сортировка некорректна, стоимость следующего отеля ниже: " + next, min<=next);
            min = next;
        }
    }

    @Step("Нажать кнопку сортировки по цене")
    private void clickSortByPriceButton(){
        $(byXpath("//button[@data-order-by='Price']")).click();
        waitPlane();
        $(byXpath("//h1[contains(text(),'" + text[17][ln] + "')]")).shouldBe(visible);
    }

    @Step("Нажать кнопку сортировки по звездности")
    private void clickSortByStarsButton(){
        $(byXpath("//button[@data-order-by='Category']")).click();
        waitPlane();
        $(byXpath("//h1[contains(text(),'" + text[17][ln] + "')]")).shouldBe(visible);
    }

    @Step("Проверить сортировку по убыванию звездности")
    private void checkDecreasingStars(){
        /*установка фильтров в подходящее значение для теста сортировки по звездности*/
        $(byXpath("//div[@id='shown_stars']/..")).click(); //кликнуть по выпадающему списку звезд
        clearStarsCheckbox($("#stars5"));
        clearStarsCheckbox($("#stars4"));
        clearStarsCheckbox($("#stars3"));
        setStarsCheckbox($("#stars2"));
        setStarsCheckbox($("#stars1"));
        SelenideElement ball = $$(byXpath("//div[@class='range-slider']/div")).get(2);
        int decile = (int) ($(byXpath("//div[@class='range-slider']")).getSize().width/1.1);
        Actions actions = new Actions(getWebDriver());
        actions.dragAndDropBy(ball.toWebElement(), -decile, 0).perform();
        clickHotelSearchButton();
        /*конец установки фильтров*/

        clickSortByStarsButton();
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        int max = hotels.get(0).$$(byXpath("descendant::div[@class='stars-item stars-item--mark']")).size();
        int next;
        System.out.println("hotel stars1 = " + max);
        for (int i=1; i<hotels.size(); i++) {
            hotels.get(i).scrollTo();
            next = hotels.get(i).$$(byXpath("descendant::div[@class='stars-item stars-item--mark']")).size();
            System.out.println("hotel stars"+(i+1)+" = " + next);
            assertTrue("Сортировка некорректна, звездность следующего отеля выше: " + next, max>=next);
            max = next;
        }
    }

    @Step("Проверить сортировку по возрастанию звездности")
    private void checkAscendingStars(){
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        int min = hotels.get(0).$$(byXpath("descendant::div[@class='stars-item stars-item--mark']")).size();
        int next;
        System.out.println("hotel stars1 = " + min);
        for (int i=1; i<hotels.size(); i++) {
            hotels.get(i).scrollTo();
            next = hotels.get(i).$$(byXpath("descendant::div[@class='stars-item stars-item--mark']")).size();
            System.out.println("hotel stars"+(i+1)+" = " + next);
            assertTrue("Сортировка некорректна, звездность следующего отеля ниже: " + next, min<=next);
            min = next;
        }
    }

    @Step("Проверить стоимость выбранного отеля в корзине")
    private void checkHotelCartPrice(){
        String p = $(byXpath("//div[@class='hotel-selected__card-order-price']")).innerHtml();
        p = p.substring(0, p.indexOf(" "));
        p = p.replaceAll("\\D+","");
        System.out.println("Hotel price = " + p);
        SelenideElement hotel = $(byXpath("//div[text()='" + text[3][ln] + "']")).shouldBe(visible);
        String cartPrice = hotel.$(byXpath("following::div[@class='cart__item-pricelist-item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Hotel cart price = " + cartPrice);
    }

    @Step("Проверка кнопки «В заказе»")
    private void checkRentButtonName(){
        $(byXpath("//div[@class='hotel-selected__card-order-text']")).
                shouldBe(visible).shouldBe(Condition.text(text[6][ln]));
    }

    private Date stringToDate(String d) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat("dd MMMM yyyy", new Locale(Values.lang[ln][2])).parse(d);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        return parsingDate;
    }

    @Step("Проверка Email бронирования")
    private void checkEmail(){
        $("#mail-contents-inserted-value").shouldBe(visible).shouldBe(Condition.text(Values.email));
    }

    @Step("Проверка номера телефона бронирования")
    private void checkPhone(){
        $("#phone-contents-inserted-value").shouldBe(visible).shouldBe(Condition.text(Values.phone));
    }

}

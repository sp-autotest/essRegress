package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import io.qameta.allure.Step;
import struct.CollectData;
import struct.Flight;
import struct.InitialData;
import struct.Passenger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static config.Values.text;
import static java.lang.String.format;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 15.03.2018.
 */
public class HotelPage extends Page {

    private InitialData initData;
    private CollectData collectData;

    public HotelPage(InitialData initData, CollectData collectData) {
        this.initData = initData;
        this.collectData = collectData;
    }

    @Step("Форма дополнительных услуг «Проживание» открылась")
    private void checkHotelFormAppear() {
        String header = format(text[17][collectData.getLn()], Values.city.getCity(initData.getCityTo(), collectData.getLn()));
        $(byXpath("//h1[contains(text(),'" + header + "')]")).shouldBe(visible);
        System.out.println("Accommodation form appeared");
    }

    @Step("Действие {0}, Нажать кнопку «Проживание»")
    public void clickResidenceButton(int n) {
        System.out.println("\t" + n + ". Click Accommodation");
        //$(byXpath("//div[@class='cart__item-title'][contains(text(),'" + text[3][ln] + "')]")).shouldBe(visible).click();
        SelenideElement acc = $(byXpath("//div[@class='cart__item']")).shouldBe(visible);
        Sleep(2);
        acc.click();
        Sleep(2);
        acc.click();
        waitPlane();
        checkHotelFormAppear();
    }

    @Step("Действие {0}, Проверка фильтра поиска отелей")
    public void checkHotelFilter(int n) {
        System.out.println("\t" + n + ". Check exist of hotel filter");
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
        String d1 = $(byXpath("//input[@name='hotel_min_date']")).getValue();
        String d2 = $(byXpath("//input[@name='hotel_max_date']")).getValue();
        System.out.println("booking period = " + d1 + " - " + d2);
        try {
            Values.reportData[collectData.getTest()].getHotel().accDate = new SimpleDateFormat("yyyy-MM-dd").parse(d1);
            Values.reportData[collectData.getTest()].getHotel().depDate = new SimpleDateFormat("yyyy-MM-dd").parse(d2);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
    }

    @Step("Действие {0}, Проверка логики отображения информации в блоке «Проживание»")
    public void checkHotelLogic(int n, List<Flight> flightList, List<Passenger> passList) {
        System.out.println("\t" + n + ". Check logic in Accommodation block");
        checkStartHotelDate(new SimpleDateFormat("yyyy-MM-d").format(flightList.get(0).end));
        if (null != initData.getDateBack()) {
            checkEndHotelDate(new SimpleDateFormat("yyyy-MM-d").format(flightList.get(1).start));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(flightList.get(0).end);//в случае OneWay дата окончания бронирования
            cal.add(Calendar.DAY_OF_MONTH, 3); //должна быть на трое суток позже даты начала
            checkEndHotelDate(new SimpleDateFormat("yyyy-MM-d").format(cal.getTime()));
        }
        checkResidentsNumber(passList.size());//проверка количества пассажиров
        checkRoomCount();
        clickHotelCheckbox();
        clickHotelSearchButton();
    }

    @Step("Действие {1}, Проверка использования данных пассажиров при бронировании")
    public void checkPassengersData(List<Passenger> passList, int s) {
        System.out.println("\t" + s + ". Check passengers data");
        ElementsCollection info = $$(byXpath("//div[@class='hotel-selected__room-guest-info']/div"));
        int n = checkPassengerName(info.get(0), passList);
        checkPassengerDob(info.get(1), passList.get(n).getDob());
        checkEmail();
        checkPhone();
    }

    @Step("Действие 16, Проверка возможности фильтрации")
    public void checkFiltration() {
        System.out.println("\t16. Check filtering");
        setPriceFilter();
        checkHotelFilterByPrice();
        setStarsFilter();
        checkHotelFilterByStars();
    }

    @Step("Действие 17, Проверка возможности сортировки")
    public void checkSorting() {
        System.out.println("\t17. Checking the sorting");
        System.out.println("sort by price");
        checkDecreasingPrice();
        clickSortByPriceButton();
        checkAscendingPrice();
        System.out.println("sort by stars");
        checkDecreasingStars();
        clickSortByStarsButton();
        checkAscendingStars();
    }

    @Step("Действие {1}, Выбор отеля для аренды ({0})")
    public void selectHotel(int n, int step) {
        System.out.print("\t" + step + ". Select hotel: ");
        if (n==0) clickSortByStarsButton();//отсортировать по убыванию звездности
        ElementsCollection hotels = null;
        String name = null;
        for (int i=0; i<20; i++) {
            Sleep(1);
            hotels = $$(byXpath("//*[@id='hotel-search-result']/div/div/a"));
            name = hotels.get(n).getText();
            if (name.length()>0) break;
        }
        System.out.print(n + " ");
        assertTrue("Неудалось найти отель с подходящими штрафами", n < hotels.size());
        //name = hotels.get(n).getText();
        System.out.println(name);//вывести в лог имя отеля
        hotels.get(n).click();
        waitPlane();
        $(byXpath("//h1[@class='modal__header-title']")).shouldBe(visible);
    }

    @Step("Действие {0}, Выбор типа номера")
    public int selectRoomType(int s) {
        System.out.println("\t" + s + ". Select type of room");
        ElementsCollection rooms = $$(byXpath("//li[@class='hotel-room']"));
        int sel = -1;
        System.out.println("Rooms = " + rooms.size());

        /*понажимать во всех комнатах линк 'Условия отмены'*/
        for (int i=0; i<rooms.size(); i++){
            rooms.get(i).$(byXpath("descendant::div[@class='h-fz--12 h-color--blue h-display--inline-block h-mr--4']")).click();
            Sleep(1);
        }

        /*подождать пока прогрузятся условия отмены во всех комнатах*/
        int n;//число прогрузившихся условий отмены
        int t=0;//число секунд ожидания прогрузки всех, максимум 30
        do {
            n = $(byXpath("//div[@class='modal__frame modal__frame--no-padding']")).$$(byXpath("descendant::div[@class='wrapper']")).size();
            Sleep(1);
            t++;
        } while ((rooms.size()>n)&(t<30));

        /*найти условия отмены без штрафа на текущее число*/
        String period;
        for (int i=0; i<rooms.size(); i++){
            ElementsCollection rules = rooms.get(i).$$(byXpath("descendant::div[@class='wrapper']"));
            if (rules.size()>0) {
                System.out.println("Room" + i + " Cancel Rules = " + rules.get(0).getAttribute("textContent"));
                if (rules.get(0).getText().contains(Values.text[22][collectData.getLn()])) {//если условие отмены содержит текст "Бесплатно"
                    period = rules.get(0).$(byXpath("//div[text()='" + Values.text[22][collectData.getLn()] + "']/..")).getAttribute("textContent");
                    period = period.replaceFirst(Values.text[22][collectData.getLn()], "");
                    System.out.println("period = " + period);
                    if (period.contains(Values.text[23][collectData.getLn()])) {//если условие отмены содержит текст "До"
                        period = period.replaceFirst(Values.text[23][collectData.getLn()], "");
                        period = period.substring(period.indexOf(":")+2);
                        System.out.println("period = " + period);
                        if (sTd(period.trim()).after(new Date())) {//если условие отмены содержит дату после текущей
                            sel = i;
                            break;
                        }
                    }
                }
            }
        }
        if (sel < 0) clickHotelCardClose();//если комната с условиями отмены не найдена - нажать крестик, закрыть карточку отеля
        return sel;
    }

    @Step("Действие {1}, Нажать кнопку «Забронировать»")
    public void clickBookButton(int room, int n) {
        System.out.println("\t" + n + ". Click botton \"Book\"");
        ElementsCollection rooms = $$(byXpath("//li[@class='hotel-room']"));
        SelenideElement textprice = rooms.get(room).$(byXpath("descendant::div[@class='hotel-room__buy-price']")).shouldBe(visible);
        textprice.scrollTo();
        screenShot();
        System.out.println("Room text price = " + textprice.getAttribute("textContent"));
        String price = "";
        if (collectData.getCur().equals("RUB")) {
            price = textprice.innerHtml();
            price = price.substring(0, price.indexOf("<"));
        } else {
            price = textprice.getText();
            price = price.substring(price.indexOf("/"));
            price = price.substring(0, price.indexOf(","));
        }
        price = price.replaceAll("\\D+","");
        System.out.println("Room price = " + price);
        Values.reportData[collectData.getTest()].getPrice().hotel = price;
        checkCancelCurrency(textprice);
        rooms.get(room).$(byXpath("descendant::div[@class='hotel-room__buy-button-wrapper']")).shouldBe(visible).click();
        checkHotelFormAppear();
        checkHotelCartPrice();
        checkRentButtonName();
        saveHotelData();
        screenShot();
    }

    @Step("Проверить валюту отмены проживания")
    private void checkCancelCurrency(SelenideElement price) {
        String etalon = price.getText();

        if (collectData.getCur().equals("RUB")) {
            String nights = price.$(byXpath("descendant::div[@class='hotel-room__buy-price-subtitle h-text--nowrap']")).getText();
            etalon = etalon.replaceFirst(nights, "");
            etalon = etalon.replaceAll("\\d+","").replace(",", "").trim();
        } else {
            etalon = etalon.substring(0, etalon.indexOf("/")).replaceAll("\\d+","").replace(",", "").trim();
        }
        ElementsCollection canceled = $$(byXpath("//div[@class='h-fz--14 h-mt--8 h-fw--700']"));
        for (SelenideElement el : canceled) {
            String cancel = el.getText().replaceAll("\\d+","").replace(".", "").trim();
            if (cancel.contains(etalon)) return;
        }
        assertTrue("Валюта штрафа при отмене проживания не соответствует валюте бронирования", false);
    }

    @Step("Действие {0}, Нажать Оплатить в корзине")
    public void clickPayInCart(int n) {
        System.out.println("\t" + n + ". Click Pay in cart");
        $(byXpath("//a[@class='cart__item-counter-link']")).click();
        waitPlane();
    }

    @Step("Нажать Продолжить")
    public void clickContinue() {
        System.out.println("\tClick Continue button");
        $(byXpath("//a[@class='next__button']")).click();
        waitPlane();
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
    private void checkResidentsNumber(int passCount) {
        int n = $$(byXpath("//div[@class='js-travellers-list']/div")).size();
        if (passCount == 1)
            assertTrue("Не соответствует количество мест размещения\nОжидалось: 1\nФактически: " + n, n == 1);
        if (passCount > 1)
            assertTrue("Не соответствует количество мест размещения\nОжидалось: 2\nФактически: " + n, n == 2);
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
        checkHotelFormAppear();
    }

    @Step("Проверить Имя/Фамилию пассажира")
    private int checkPassengerName(SelenideElement pass, List<Passenger> passList) {
        String name = pass.getText().trim();
        System.out.println("Name = " + name);
        String bookname = "";
        boolean found = false;
        int i = 0;
        for (Passenger p : passList) {
            bookname = (p.getLastname() + " " + p.getFirstname()).toUpperCase();
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
        int delta  = $(byXpath("//div[@class='range-slider']")).getSize().width/11;
        System.out.println("delta = " + delta);
        Actions actions = new Actions(getWebDriver());
        actions.dragAndDropBy(balls.get(0).toWebElement(), delta, 0).perform();
        actions.dragAndDropBy(balls.get(2).toWebElement(), -delta, 0).perform();
    }

    @Step("Проверить цены найденных отелей")
    private void checkHotelFilterByPrice(){
        float from = Float.parseFloat($("#hotel_min_price-value").getValue().replace(" ",""));
        float to = Float.parseFloat($("#hotel_max_price-value").getValue().replace(" ",""));
        System.out.println("From = " + from);
        System.out.println("To = " + to);

        clickHotelSearchButton();
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        assertTrue("Отели не найдены", hotels.size()>0);
        for (int i=0; i<hotels.size(); i++) {
            float price = Float.parseFloat(hotels.get(i).$(byXpath("descendant::" +
                    "div[@class='hotel-card__button-price']")).getText().replaceAll("\\s|a|\\$|€|¥|CNY",""));
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
        if (!chb.isSelected()) jsClick(chb);
    }

    @Step("Снять галку в чекбоксе со звездами")
    private void clearStarsCheckbox(SelenideElement chb) {
        if (chb.isSelected()) jsClick(chb);
    }

    @Step("Проверить звездность найденных отелей")
    private void checkHotelFilterByStars(){
        clickHotelSearchButton();
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        assertTrue("Отели не найдены", hotels.size()>0);
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
        assertTrue("Отели не найдены", hotels.size()>0);
        String price = hotels.get(0).$(byXpath("descendant::div[@class='hotel-card__button-allprice']")).getText();
        price = price.substring(0, price.indexOf(" "));
        float max = Float.parseFloat(price);
        float next;
        for (int i=1; i<hotels.size(); i++) {
            hotels.get(i).scrollTo();
            price = hotels.get(i).$(byXpath("descendant::div[@class='hotel-card__button-allprice']")).getText();
            price = price.substring(0, price.indexOf(" "));
            next = Float.parseFloat(price);
            assertTrue("Сортировка некорректна, стоимость следующего отеля выше: " + next, max>=next);
            max = next;
        }
    }

    @Step("Проверить сортировку по возрастанию цены")
    private void checkAscendingPrice(){
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        assertTrue("Отели не найдены", hotels.size()>0);
        String price = hotels.get(0).$(byXpath("descendant::div[@class='hotel-card__button-allprice']")).getText();
        price = price.substring(0, price.indexOf(" "));
        float min = Float.parseFloat(price);
        float next;
        for (int i=1; i<hotels.size(); i++) {
            hotels.get(i).scrollTo();
            price = hotels.get(i).$(byXpath("descendant::div[@class='hotel-card__button-allprice']")).getText();
            price = price.substring(0, price.indexOf(" "));
            next = Float.parseFloat(price);
            assertTrue("Сортировка некорректна, стоимость следующего отеля ниже: " + next, min<=next);
            min = next;
        }
    }

    @Step("Нажать кнопку сортировки по цене")
    private void clickSortByPriceButton(){
        $(byXpath("//button[@data-order-by='Price']")).click();
        waitPlane();
        checkHotelFormAppear();
    }

    @Step("Нажать кнопку сортировки по звездности")
    private void clickSortByStarsButton(){
        $(byXpath("//button[@data-order-by='Category']")).scrollTo().shouldBe(visible).click();
        waitPlane();
        checkHotelFormAppear();
    }

    @Step("Проверить сортировку по убыванию звездности")
    private void checkDecreasingStars(){
        /*установка фильтров в подходящее значение для теста сортировки по звездности*/
        $(byXpath("//div[@id='shown_stars']/..")).click(); //кликнуть по выпадающему списку звезд
        setStarsCheckbox($("#stars5"));

        clickSortByStarsButton();
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        assertTrue("Отели не найдены", hotels.size()>0);
        int max = hotels.get(0).$$(byXpath("descendant::div[@class='stars-item stars-item--mark']")).size();
        int next;
        for (int i=1; i<hotels.size(); i++) {
            hotels.get(i).scrollTo();
            next = hotels.get(i).$$(byXpath("descendant::div[@class='stars-item stars-item--mark']")).size();
            assertTrue("Сортировка некорректна, звездность следующего отеля выше: " + next, max>=next);
            max = next;
        }
    }

    @Step("Проверить сортировку по возрастанию звездности")
    private void checkAscendingStars(){
        ElementsCollection hotels = $$(byXpath("//div[@id='hotel-search-result']/div"));
        assertTrue("Отели не найдены", hotels.size()>0);
        int min = hotels.get(0).$$(byXpath("descendant::div[@class='stars-item stars-item--mark']")).size();
        int next;
        for (int i=1; i<hotels.size(); i++) {
            hotels.get(i).scrollTo();
            next = hotels.get(i).$$(byXpath("descendant::div[@class='stars-item stars-item--mark']")).size();
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
        Values.reportData[collectData.getTest()].getHotel().price = p;
        SelenideElement hotel = $(byXpath("//div[text()='" + text[3][collectData.getLn()] + "']")).shouldBe(visible);
        String cartPrice = hotel.$(byXpath("following::div[@class='cart__item-pricelist-item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Hotel cart price = " + cartPrice);
        String totalPrice = $("#cart-total-incarts").$(byXpath("descendant::div[@class='cart__item-price']")).getText().replaceAll("\\D+","");
        System.out.println("Total price = " + totalPrice);
        Values.reportData[collectData.getTest()].getPrice().total = totalPrice;
    }

    @Step("Проверка кнопки «В заказе»")
    private void checkRentButtonName(){
        $(byXpath("//div[@class='hotel-selected__card-order-text']")).
                shouldBe(visible).shouldBe(Condition.text(text[6][collectData.getLn()]));
    }

    private Date stringToDate(String d) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat("dd MMMM yyyy", new Locale(Values.lang[collectData.getLn()][2])).parse(d);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        return parsingDate;
    }

    private Date sTd(String d) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat("dd.MM.yyyy", new Locale(Values.lang[collectData.getLn()][2])).parse(d);
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
        $("#phone-contents-inserted-value").shouldBe(visible).shouldBe(Condition.text(collectData.getPhone()));
    }

    private void saveHotelData(){
        Values.reportData[collectData.getTest()].getHotel().name = $(byXpath("//a[@class='hotel-selected__card-title']")).getText();
        Values.reportData[collectData.getTest()].getHotel().star = $$(byXpath("//div[@class='hotel-selected__card-stars']" +
                "/descendant::div[@class='stars-item stars-item--mark']")).size();
    }

    private void clickHotelCardClose(){
        $(byXpath("//a[@class='modal__close']")).shouldBe(visible).click();
    }


}

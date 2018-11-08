package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import dict.AddService;
import io.qameta.allure.Step;
import struct.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static config.Values.text;
import static java.lang.Math.abs;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 01.03.2018.
 */
public class EprPage extends Page {

    private static int MINUTES_LIMIT = 100;
    private CollectData collectData;

    public EprPage(CollectData collectData) {
        this.collectData = collectData;
    }

    @Step("Действие {0}, проверка данных на странице оплаты")
    public void checkDataOnPayPage(int n, List<Flight> flyList, List<Passenger> passList, int test, boolean timer) {
        System.out.println("\t" + n + ". Checking data on Pay page");
        screenShot();
        if (!timer) checkTimer();//добавлена проверка значения таймера согласно задачи 2663
        ElementsCollection passengers = $$(byXpath("//div[contains(@ng-repeat,'passenger')]"));
        for (int i = 0; i < passengers.size(); i++) {
            checkPassengerName(i + 1, passList.get(i), passengers.get(i));
        }

        ElementsCollection flights = null;
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            SelenideElement el = $(byXpath("//div[@data-toggle-target='toggle-flight']")).shouldBe(visible);
            scrollWithOffset(el, 0, -100);
            el.click(); //раскрыть блок Перелет
        }
        for (int i=0; i<20; i++) {
            Sleep(1);
            flights = $$(byXpath("//div[@class='flight__row']"));
            if (flights.size()>0) break;
            assertTrue("Не обнаружено данных о перелете на странице оплаты", i!=19);
        }
        screenShot();
        for (int i = 0; i < flights.size(); i++) {
            checkFlight(i + 1, flyList.get(i), flights.get(i));
            checkFlightDate(i + 1, flyList.get(i), flights.get(i));
        }
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            SelenideElement el = $(byXpath("//div[@data-toggle-target='toggle-safe']")).shouldBe(visible);
            scrollWithOffset(el, 0, -100);
            el.click(); //раскрыть блок Страховка
        }
        if (test<4) {
            screenShot();
            checkFlyInsurance(passList);
        }
        if (test<3) {
            checkMedicalInsurance(passList);
            checkAllInsurancePrice();
        }
        if (test == 5) {
            screenShot();
            checkFlyInsurance(passList);
            checkMedicalInsurance(passList);
            checkAllInsurancePrice();
            checkAccommodation();
        }
        if (test == 8) {
            screenShot();
            checkFlyInsurance(passList);
            checkMedicalInsurance(passList);
            checkAllInsurancePrice();
            checkSelectPlaceService();
            checkEntreeService();
            checkDessertService();
            checkTotalCount();
        }

        if (test == 1) checkAccommodation();
        if (getWebDriver().manage().window().getSize().getWidth() < 1280) {
            SelenideElement el = $(byXpath("//div[@data-toggle-target='toggle-TRANSPORT']")).shouldBe(visible);
            scrollWithOffset(el, 0, -100);
            el.click(); //раскрыть блок Транспорт
        }
        if ((test == 2)|(test == 5)|(test == 8)) checkTransport();
        if ((test == 3)|(test == 5)|(test == 8)) {
            checkAeroexpress(flyList.get(0).from_orig, passList.size());
            checkTransfer(flyList.get(0).start);
        }
    }

    @Step("Действие 13, проверка даты/времени на EPR")
    public void checkDateOnEPR(List<Flight> flyList) {
        System.out.println("\t13. Checking data on Pay page");
        for (int i=0; i<30; i++) {
            Sleep(1);
            if ($(byXpath("//div[@class='flight__row']")).exists()) break;
            assertTrue("Не обнаружено данных о перелете на странице оплаты", i!=19);
        }
        screenShot();
        ElementsCollection flights = $$(byXpath("//div[@class='flight__row']"));
        for (int i = 0; i < flights.size(); i++) {
            checkFlightDateNew(i + 1, flyList.get(i), flights.get(i));
        }

    }

    @Step("Действие 14, Проверка даты услуги Трансфера")
    public void checkTransferDate(Date d) {
        System.out.println("\t13. Checking transfer date");
        SelenideElement group = $(byXpath("//div[@ng-switch-when='Transfer'][@class='ng-scope']"));
        group.scrollTo();
        screenShot();
        String date = group.$(byXpath("descendant::div[@ng-bind='item.details.date']")).getText();
        System.out.println("Transfer date = " + date);
        String dateC;
        dateC = new SimpleDateFormat("E, dd MMMM", new Locale(Values.lang[collectData.getLn()][2])).format(d);
        assertTrue("Дата трансфера не корректна" +
                "\nОжидалось : " + dateC + "\nФактически: " + date, date.equals(dateC));
    }

    @Step("Проверка фамилии и имени {0}-го пассажира")
    private void checkPassengerName(int i, Passenger p, SelenideElement passenger){
        String pass = passenger.$(byXpath("child::div[2]")).getText();
        System.out.println(pass);
        assertTrue("Фамилия и/или имя пассажира не совпадает с забронированным",
                pass.equals((p.getLastname()+" "+p.getFirstname()).toUpperCase()));
    }

    @Step("Проверка данных о {0}-м маршруте")
    private void checkFlight(int i, Flight f, SelenideElement flight){
        String from = flight.$(byXpath("descendant::div[@class='flight__direction-airport-code ng-binding']")).getText();
        if (from.equals("SVO")|from.equals("VKO")) from = "MOW";
        System.out.print(from + " / ");
        assertTrue("Направление «Откуда» в маршруте отличается от забронированного" +
                   "\nОжидалось : " + f.from + "\nФактически: " + from, from.equals(f.from));

        String to = flight.$(byXpath("descendant::div[@class='flight__direction-airport-code ng-binding'][2]")).getText();
        if (to.equals("SVO")|to.equals("VKO")) to = "MOW";
        System.out.print(to + " / ");
        assertTrue("Направление «Куда» в маршруте отличается от забронированного" +
                   "\nОжидалось : " + f.to + "\nФактически: " + to, to.equals(f.to));

        String number = flight.$(byXpath("descendant::div[@class='flight__flight ng-binding']")).getText();
        number = number.substring(0, number.indexOf("\n"));
        System.out.print(number + "  / ");
        assertTrue("Номер рейса в маршруте отличается от забронированного" +
                   "\nОжидалось : " + f.number.replace(" ", "") + "\nФактически: " + number,
                   number.equals(f.number.replace(" ", "")));

        String dur = flight.$(byXpath("descendant::span[@duration='route.duration']")).getText();
        String duration = dur.replaceAll("[^0-9]", "");
        if (dur.contains(" 0 ")) duration = duration + "0";
        System.out.print(duration + " ");
        assertTrue("Длительность перелета в маршруте отличается от забронированного" +
                   "\nОжидалось : " + f.duration + "\nФактически: " + duration, duration.equals(f.duration));
    }

    @Step("Проверка даты/времени в {0}-м маршруте")
    private void checkFlightDate(int i, Flight f, SelenideElement flight){
        boolean overnight = false;
        String start = flight.$(byXpath("descendant::div[@class='flight__date ng-binding']")).getText();
        start = start.substring(start.indexOf(",")+1) + " " + new SimpleDateFormat("yyyy").format(f.start);
        Date dStart = new Date();
        try {
            dStart = new SimpleDateFormat("dd MMMM HH:mm yyyy", new Locale(Values.lang[collectData.getLn()][2])).parse(start);
            System.out.print("["+dStart+"] ");
        }catch (ParseException e) {
            System.out.println("Дата нераспаршена");
        }
        assertTrue("Время/дата вылета отличается от забронированного" +
                   "\nОжидалось : " + f.start + "\nФактически: " + dStart, dStart.equals(f.start));

        String end = flight.$(byXpath("descendant::div[@class='flight__date ng-binding'][2]")).getText();
        if (end.contains("+1")) overnight = true;
        end = end.substring(end.indexOf(",")+1);
        end = end.substring(0, end.indexOf(",")) + " " + new SimpleDateFormat("yyyy").format(f.end);
        Date dEnd = new Date();
        try {
            dEnd = new SimpleDateFormat("dd MMMM HH:mm yyyy", new Locale(Values.lang[collectData.getLn()][2])).parse(end);
            System.out.println("["+dEnd+"] ");
        }catch (ParseException e) {
            System.out.println("Дата нераспаршена");
        }
        Date expectedDate;
        if (overnight) expectedDate = overNight(f.end);
        else expectedDate = f.end;
        assertTrue("Время/дата прилета отличается от забронированного" +
                   "\nОжидалось : " + expectedDate + "\nФактически: " + dEnd, dEnd.equals(expectedDate));
    }

    @Step("Проверка даты/времени в {0}-м маршруте")
    private void checkFlightDateNew(int i, Flight f, SelenideElement flight){
        String start = flight.$(byXpath("descendant::div[@class='flight__date ng-binding']")).getText()
                + " " + new SimpleDateFormat("yyyy").format(f.start);
        Date dStart = new Date();
        try {
            dStart = new SimpleDateFormat("E, dd MMMM HH:mm yyyy", new Locale("ru")).parse(start);
            System.out.println("["+dStart+"] ");
        }catch (ParseException e) {
            System.out.println("Дата вылета нераспаршена");
        }
        assertTrue("Время/дата вылета отличается от забронированного" +
                "\nОжидалось : " + f.start + "\nФактически: " + dStart, dStart.equals(f.start));

        SelenideElement endElement = flight.$(byXpath("descendant::div[@class='flight__date ng-binding'][2]"));
        String end = endElement.$(byXpath("descendant::span[@class='h-text--nowrap ng-binding']")).getText()
                + " " + endElement.$(byXpath("descendant::span[@class='h-text--bold h-color--black ng-binding']")).getText()
                + " " + new SimpleDateFormat("yyyy").format(f.end);
        Date dEnd = new Date();
        try {
            dEnd = new SimpleDateFormat("E, dd MMMM HH:mm yyyy", new Locale("ru")).parse(end);
            System.out.println("["+dEnd+"] ");
        }catch (ParseException e) {
            System.out.println("Дата прилета нераспаршена");
        }
        assertTrue("Время/дата прилета отличается от забронированного" +
                "\nОжидалось : " + f.end + "\nФактически: " + dEnd, dEnd.equals(f.end));
    }

    @Step("Проверка полетной страховки")
    private void checkFlyInsurance(List<Passenger> passList){
        SelenideElement row = $(byXpath("//div[@data-toggle-id='toggle-safe']/descendant::div[@role='row'][1]"));
        row.scrollTo();
        String insurance = row.getText();
        String iprice = row.$(byXpath("descendant::span[contains(@class,'__item-price')]")).getText().replaceAll("\\D+","");
        System.out.println("fly insurance = " + iprice);
        String fullName;
        for (int i=0; i<passList.size(); i++){
            fullName = (passList.get(i).getLastname() + " " + passList.get(i).getFirstname()).toUpperCase();
            assertTrue("Полетная страховка не содержит пассажира " + fullName, insurance.contains(fullName));
        }
        System.out.println(Values.reportData[collectData.getTest()].getPrice().iflight);
        assertTrue("Стоимость полетной страховки отличается от забронированной" +
                   "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().iflight +
                   "\nФактически: " + iprice,
                   iprice.equals(Values.reportData[collectData.getTest()].getPrice().iflight));
    }

    @Step("Проверка медицинской страховки")
    private void checkMedicalInsurance(List<Passenger> passList){
        SelenideElement row = $(byXpath("//div[@data-toggle-id='toggle-safe']/descendant::div[@role='row'][2]"));
        row.scrollTo();
        String insurance = row.getText();
        String price = row.$(byXpath("descendant::span[contains(@class,'__item-price')]")).getText().replaceAll("\\D+","");
        System.out.println("med insurance = " + price);
        String fullName;
        for (int i=0; i<passList.size(); i++){
            fullName = (passList.get(i).getLastname() + " " + passList.get(i).getFirstname()).toUpperCase();
            assertTrue("Медицинская страховка не содержит пассажира " + fullName, insurance.contains(fullName));
        }
        assertTrue("Стоимость медицинской страховки отличается от забронированной" +
                   "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().imedical +
                   "\nФактически: " + price,
                   Values.reportData[collectData.getTest()].getPrice().imedical.equals(price));
        assertTrue("Название медицинской страховки не корректно" +
                   "\nОжидалось : " + Values.text[1][collectData.getLn()] +
                   "\nФактически: " + insurance,
                   insurance.contains(Values.text[1][collectData.getLn()]));
    }

    @Step("Проверка общей стоимости всех страховок")
    private void checkAllInsurancePrice() {
        String price = $(byXpath("//div[@data-toggle-id='toggle-safe']/descendant::" +
                "div[@class='checkout-item__left-container']")).getText().replaceAll("\\D+","");
        System.out.println("all insurances= " + price);
        int allPrice = stringIntoInt(Values.reportData[collectData.getTest()].getPrice().iflight) +
                stringIntoInt(Values.reportData[collectData.getTest()].getPrice().imedical);
        assertTrue("Общая стоимость всех страховок некорректна" +
                   "\nОжидалось : " + allPrice + "\nФактически: " + price, stringIntoInt(price) == allPrice);
    }

    @Step("Проверка данных транспортной услуги")
    private void checkTransport(){
        ReportData rData = Values.reportData[collectData.getTest()];
        SelenideElement row = $(byXpath("//div[@ng-switch-when='Europcar'][@class='ng-scope']"));

        row.scrollTo();
        String name = row.$(byXpath("descendant::div[@ng-bind='item.details.carName']")).getText();
        System.out.println(name);
        assertTrue("Название авто отличается от забронированного" +
                   "\nОжидалось : " + rData.getAuto().name + "\nФактически: " + name,
                   rData.getAuto().name.equals(name));

        String receiveLocation = row.$(byXpath("descendant::div[@ng-bind='item.details.receiveLocation']")).getText();
        System.out.println(receiveLocation);
        assertTrue("Место получения отличается от забронированного" +
                   "\nОжидалось : " + rData.getAuto().receiveLocation + "\nФактически: " + receiveLocation,
                   rData.getAuto().receiveLocation.equals(receiveLocation));

        String receiveDate = row.$(byXpath("descendant::div[@ng-bind='item.details.receiveDate']")).getText();
        receiveDate = receiveDate.substring(receiveDate.indexOf(",")+1);
        System.out.println(receiveDate);
        assertTrue("Дата получения отличается от забронированной",
                rData.getAuto().receiveDate.equals(stringToDate(receiveDate)));

        String returnLocation = row.$(byXpath("descendant::div[@ng-bind='item.details.returnLocation']")).getText();
        System.out.println(returnLocation);
        assertTrue("Место возврата отличается от забронированного",
                rData.getAuto().returnLocation.equals(returnLocation));

        String returnDate = row.$(byXpath("descendant::div[@ng-bind='item.details.returnDate']")).getText();
        returnDate = returnDate.substring(returnDate.indexOf(",")+1);
        System.out.println(returnDate);
        assertTrue("Дата возврата отличается от забронированной",
                rData.getAuto().returnDate.equals(stringToDate(returnDate)));

        String price = row.$(byXpath("descendant::span[contains(@class,'__item-price')]")).getText().replaceAll("\\D+","");
        System.out.println(price);
        assertTrue("Стоимость аренды автомобиля отличается от забронированной" +
                   "\nОжидалось :" + rData.getPrice().transport +
                   "\nФактически:" + price, rData.getPrice().transport.equals(price));
    }

    @Step("Проверка данных услуги Аэроэкспресс")
    private void checkAeroexpress(String aero, int tickets) {
        SelenideElement row = $(byXpath("//div[@ng-switch-when='AeroExpress'][@class='ng-scope']"));
        row.scrollTo();
            String name = row.$(byXpath("descendant::div[@ng-bind='item.details.description']")).getText();
            name = name.substring(name.indexOf(",") + 2);
            name = name.substring(0, name.indexOf(","));
            System.out.println(name);
            if (aero.equals("SVO")) assertTrue("Направление в Аэроэкспресс некорректно" +
                    "\nОжидалось : " + text[28][collectData.getLn()] + " -> " + text[29][collectData.getLn()] +
                    "\nФактически: " + name, name.equals(text[28][collectData.getLn()] + " -> " + text[29][collectData.getLn()]));
            if (aero.equals("VKO")) assertTrue("Направление в Аэроэкспресс некорректно" +
                    "\nОжидалось : " + text[20][collectData.getLn()] + " -> " + text[21][collectData.getLn()] +
                    "\nФактически: " + name, name.equals(text[20][collectData.getLn()] + " -> " + text[21][collectData.getLn()]));

            String count = row.$(byXpath("descendant::span[@translate-plural='paymentPage.passengers']/..")).getText().replaceAll("\\D+", "");
            System.out.println("passengers = " + count);
            assertTrue("Количество билетов на Аэроэкспресс не корректно" +
                    "\nОжидалось : " + tickets + "\nФактически: " + count, stringIntoInt(count) == tickets);

            String price = row.$(byXpath("descendant::span[contains(@class,'checkout-item__item-price')]")).getText().replaceAll("\\D+", "");
            System.out.println("price = " + price);
            assertTrue("Стоимость билета на Аэроэкспресс не корректна" +
                       "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().aeroexpress +
                       "\nФактически: " + price, price.equals(Values.reportData[collectData.getTest()].getPrice().aeroexpress));
    }

    @Step("Проверка данных услуги Трансфера")
    private void checkTransfer(Date d) {
        SelenideElement group = $(byXpath("//div[@ng-switch-when='Transfer'][@class='ng-scope']"));
        String from = group.$(byXpath("descendant::div[@ng-bind='item.details.from']")).getText();
        System.out.println("Transfer from = " + from);
        String fromC = (collectData.getLn()==0) ? "Курский, Москва" : "Kurskiy, Moscow";
        assertTrue("Направление Откуда трансфера не корректно" +
                   "\nОжидалось : " + fromC + "\nФактически: " + from, from.equals(fromC));

        String to = group.$(byXpath("descendant::div[@ng-bind='item.details.to']")).getText();
        System.out.println("Transfer to = " + to);
        String toC = (collectData.getLn()==0) ? "Белорусский, Москва" : "Belorussky, Moscow";
        assertTrue("Направление Куда трансфера не корректно" +
                   "\nОжидалось : " + toC + "\nФактически: " + to, to.equals(toC));

        String date = group.$(byXpath("descendant::div[@ng-bind='item.details.date']")).getText();
        System.out.println("Transfer date = " + date);
        String dateC;
        dateC = new SimpleDateFormat("E, dd MMMM", new Locale(Values.lang[collectData.getLn()][2])).format(d);
        if (collectData.getLn() == 6) dateC = date;//невозможно воспроизвести формат даты для китайского, убрать когда сообщат формат
        assertTrue("Дата трансфера не корректна" +
                   "\nОжидалось : " + dateC + "\nФактически: " + date, date.equals(dateC));

        String price = group.$(byXpath("descendant::span[@class='h-text--bold checkout-item__item-price ng-binding']")).getText().replaceAll("\\D+","");
        System.out.println("Transfer price = " + price);
        assertTrue("Cтоимость трансфера не корректна" +
                   "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().transfer +
                   "\nФактически: " + price, price.equals(Values.reportData[collectData.getTest()].getPrice().transfer));
    }

    @Step("Проверка данных услуги проживания")
    private void checkAccommodation(){
        SelenideElement row = $(byXpath("//div[@data-toggle-id='toggle-Hotel']"));
        row.scrollTo();
        String name = row.$$(byXpath("descendant::div[@ng-bind='categoryItem.details.name']")).get(1).getText();
        String sDate = row.$(byXpath("descendant::div[@ng-bind='categoryItem.details.dateFrom']")).getText();
        String eDate = row.$(byXpath("descendant::div[@ng-bind='categoryItem.details.dateTo']")).getText();
        String price = row.$(byXpath("descendant::span[contains(@class,'__item-price')]")).getText().replaceAll("\\D+","");
        System.out.println("Cheking accommodation service: \n"+name+"\n"+sDate+"\n"+eDate+"\n"+price);
        assertTrue("Название отеля отличается от забронированного", name.contains(Values.reportData[collectData.getTest()].getHotel().name));
//        assertTrue("Звездность отеля отличается от забронированной", name.contains(" " + Values.hotel.star + "*"));
        assertTrue("Дата заселения отличается от забронированной", Values.reportData[collectData.getTest()].getHotel().accDate.equals(sTd(sDate)));
        assertTrue("Дата выезда отличается от забронированной", Values.reportData[collectData.getTest()].getHotel().depDate.equals(sTd(eDate)));
        int delta = abs(stringIntoInt(price)-stringIntoInt(Values.reportData[collectData.getTest()].getPrice().hotel));
        assertTrue("Стоимость проживания отличается от забронированной" +
                "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().hotel +
                "\nФактически: " + price, delta <= 2);
    }

    private Date stringToDate(String d) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat("dd MMMM yyyy, HH:mm", new Locale(Values.lang[collectData.getLn()][2])).parse(d);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        return parsingDate;
    }

    private Date sTd(String d) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat("dd MMMM yyyy", new Locale(Values.lang[collectData.getLn()][2])).parse(d);
        }catch (ParseException e) {
            System.out.println("Parsing date error");
        }
        return parsingDate;
    }

    @Step("Нажать кнопку «Оплатить»")
    public void clickPayButton() {
        $(byXpath("//div[@class='next__button-inner ng-scope']")).shouldBe(visible).click();
        waitPlane();
    }

    @Step("Проверка таймера")
    private void checkTimer(){
        String timer = $(byXpath("//div[@class='counter__value limited_time']")).shouldBe(visible).getText();
        System.out.println("Timer value on EPR: " + timer);
        String[] array = timer.split(":");
        int minutes = Integer.parseInt(array[0]) * 60;
        minutes = minutes + Integer.parseInt(array[1]);
        assertTrue("Количество оставшихся минут в таймере меньше лимита" +
                "\nОжидалось : " + MINUTES_LIMIT +
                "\nФактически: " + minutes, minutes >= MINUTES_LIMIT);
    }

    private Date overNight(Date d)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    @Step("Проверка «К оплате сейчас»")
    private void checkTotalCount(){
        Price price = Values.reportData[collectData.getTest()].getPrice();
        String totalCount = $(byXpath("//div[@class='cart__item-priceondemand-item-price ng-binding']"))
                .getText().replaceAll("\\D+","");//к оплате сейчас
        int total = stringIntoInt(price.fly) +
                stringIntoInt(price.iflight) +
                stringIntoInt(price.imedical) +
                stringIntoInt(price.aeroexpress) +
                stringIntoInt(price.transfer) +
                stringIntoInt(price.place) +
                stringIntoInt(price.entree) +
                stringIntoInt(price.dessert);

        System.out.println("-----------------------");
        System.out.println("TOTAL COUNT = " + totalCount);
        System.out.println("total summ = " + total);
        System.out.println(price.fly);
        System.out.println(price.iflight);
        System.out.println(price.imedical);
        System.out.println(price.aeroexpress);
        System.out.println(price.transfer);
        System.out.println(price.place);
        System.out.println(price.entree);
        System.out.println(price.dessert);
        System.out.println("-----------------------");

    }

    @Step("Проверка услуги «Выбор мест»")
    public void checkSelectPlaceService() {
        System.out.println("Check Prereserved place");
        String code = "0B5"; //код услуги "Предварительный выбор места"
        String etalon = AddService.getServiceByCodeAndLanguage(code, collectData.getLn());
        SelenideElement place = $(byText(etalon)).shouldBe(visible);
        String price = place.$(byXpath("../../../preceding-sibling::div[@class='checkout-item__left-container']"))
                .shouldBe(visible).getText().replaceAll("\\D+","");
        System.out.println("Prereserved place count = " + price);
        assertTrue("Стоимость услуги выбора метса некорректна" +
                   "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().place +
                   "\nФактически: " + price,
                   price.equals(Values.reportData[collectData.getTest()].getPrice().place));
    }

    @Step("Проверка услуги «Основное блюдо»")
    public void checkEntreeService() {
        System.out.println("Check Entree");
        String code = "0B3"; //код услуги "Закуска Сырная"
        String etalon = AddService.getServiceByCodeAndLanguage(code, collectData.getLn());
        SelenideElement entree = $(byText(etalon)).shouldBe(visible);
        String price = entree.$(byXpath("../../../preceding-sibling::div[@class='checkout-item__left-container']"))
                .shouldBe(visible).getText().replaceAll("\\D+","");
        System.out.println("Entree count = " + price);
        assertTrue("Стоимость основного блюда некорректна" +
                   "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().entree +
                   "\nФактически: " + price,
                   price.equals(Values.reportData[collectData.getTest()].getPrice().entree));
    }

    @Step("Проверка услуги «Основное блюдо»")
    public void checkDessertService() {
        System.out.println("Check Dessert");
        String code = "019"; //код услуги "Десерт Шоколадная тарталетка"
        String etalon = AddService.getServiceByCodeAndLanguage(code, collectData.getLn());
        SelenideElement entree = $(byText(etalon)).shouldBe(visible);
        String price = entree.$(byXpath("../../../preceding-sibling::div[@class='checkout-item__left-container']"))
                .shouldBe(visible).getText().replaceAll("\\D+","");
        System.out.println("Dessert count = " + price);
        assertTrue("Стоимость десерта некорректна" +
                   "\nОжидалось : " + Values.reportData[collectData.getTest()].getPrice().dessert +
                   "\nФактически: " + price,
                   price.equals(Values.reportData[collectData.getTest()].getPrice().dessert));
    }

}

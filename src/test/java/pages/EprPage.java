package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
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
import static config.Values.ln;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by mycola on 01.03.2018.
 */
public class EprPage extends Page {

    @Step("Действие 14, проверка данных на странице оплаты")
    public void step14(List<Flight> flyList, List<Passenger> passList) {

        ElementsCollection passengers = $$(byXpath("//div[contains(@ng-repeat,'passenger')]"));
        for (int i = 0; i < passengers.size(); i++) {
            checkPassengerName(i + 1, passList.get(i), passengers.get(i));
        }

        ElementsCollection flights = $$(byXpath("//div[@class='flight__row']"));
        for (int i = 0; i < flights.size(); i++) {
            checkFlight(i + 1, flyList.get(i), flights.get(i));
        }

        checkFlyInsurance(passList);
        checkMedicalInsurance(passList);
        checkAllInsurancePrice();
        checkTransport();
    }

    @Step("Проверка фамилии и имени {0}-го пассажира")
    private void checkPassengerName(int i, Passenger p, SelenideElement passenger){
        String pass = passenger.$(byXpath("child::div[2]")).getText();
        System.out.println(pass);
        assertTrue("Фамилия и/или имя пассажира не совпадает с забронированным",
                pass.equals((p.lastname+" "+p.firstname).toUpperCase()));
    }

    @Step("Проверка данных о {0}-м маршруте")
    private void checkFlight(int i, Flight f, SelenideElement flight){
        String from = flight.$(byXpath("descendant::div[@class='flight__direction-airport-code ng-binding']")).getText();
        if (from.equals("SVO")|from.equals("VKO")) from = "MOW";
        System.out.print(from + " / ");
        assertTrue("Направление «Откуда» в маршруте отличается от забронированного", from.equals(f.from));

        String to = flight.$(byXpath("descendant::div[@class='flight__direction-airport-code ng-binding'][2]")).getText();
        if (to.equals("SVO")|to.equals("VKO")) to = "MOW";
        System.out.print(to + " / ");
        assertTrue("Направление «Куда» в маршруте отличается от забронированного", to.equals(f.to));

        String number = flight.$(byXpath("descendant::div[@class='flight__flight ng-binding']")).getText();
        number = number.substring(0, number.indexOf("\n"));
        System.out.print(number + "  / ");
        assertTrue("Номер рейса в маршруте отличается от забронированного", number.equals(f.number.replace(" ", "")));

        String duration = flight.$(byXpath("descendant::span[@duration='route.duration']")).getText().replaceAll("[^0-9]", "");
        System.out.print(duration + " ");
        assertTrue("Длительность перелета в маршруте отличается от забронированного", duration.equals(f.duration));

        String start = flight.$(byXpath("descendant::div[@class='flight__date ng-binding']")).getText();
        start = start.substring(start.indexOf(",")+1) + " " + new SimpleDateFormat("yyyy").format(f.start);
        Date dStart = new Date();
        try {
            dStart = new SimpleDateFormat("dd MMMM HH:mm yyyy", new Locale(Values.lang[ln][2])).parse(start);
            System.out.print("["+dStart+"] ");
        }catch (ParseException e) {
            System.out.println("Дата нераспаршена");
        }
        assertTrue("Время/дата вылета отличается от забронированного", dStart.equals(f.start));

        String end = flight.$(byXpath("descendant::div[@class='flight__date ng-binding'][2]")).getText();
        end = end.substring(end.indexOf(",")+1);
        end = end.substring(0, end.indexOf(",")) + " " + new SimpleDateFormat("yyyy").format(f.end);
        Date dEnd = new Date();
        try {
            dEnd = new SimpleDateFormat("dd MMMM HH:mm yyyy", new Locale(Values.lang[ln][2])).parse(end);
            System.out.println("["+dEnd+"] ");
        }catch (ParseException e) {
            System.out.println("Дата нераспаршена");
        }
        assertTrue("Время/дата прилета отличается от забронированного", dEnd.equals(f.end));
    }

    @Step("Проверка полетной страховки")
    public void checkFlyInsurance(List<Passenger> passList){
        SelenideElement row = $(byXpath("//div[@data-toggle-id='toggle-safe']/descendant::div[@role='row'][1]"));
        String insurance = row.getText();
        String price = row.$(byXpath("descendant::span[contains(@class,'__item-price')]")).getText().replace(" ", "");
        price = price.substring(0, price.length()-1);
        System.out.println("fly insurance = " + price);
        String fullName;
        for (int i=0; i<passList.size(); i++){
            fullName = (passList.get(i).lastname + " " + passList.get(i).firstname).toUpperCase();
            assertTrue("Полетная страховка не содержит пассажира " + fullName, insurance.contains(fullName));
        }
        assertTrue("Стоимость полетной страховки отличается от забронированной", Values.price.iflight.equals(price));
    }

    @Step("Проверка медицинской страховки")
    public void checkMedicalInsurance(List<Passenger> passList){
        SelenideElement row = $(byXpath("//div[@data-toggle-id='toggle-safe']/descendant::div[@role='row'][2]"));
        String insurance = row.getText();
        String price = row.$(byXpath("descendant::span[contains(@class,'__item-price')]")).getText().replace(" ", "");
        price = price.substring(0, price.length()-1);
        System.out.println("med insurance = " + price);
        String fullName;
        for (int i=0; i<passList.size(); i++){
            fullName = (passList.get(i).lastname + " " + passList.get(i).firstname).toUpperCase();
            assertTrue("Медицинская страховка не содержит пассажира " + fullName, insurance.contains(fullName));
        }
        assertTrue("Стоимость медицинской страховки отличается от забронированной", Values.price.imedical.equals(price));
        assertTrue("Название медицинской страховки не корректно", insurance.contains(Values.text[1][ln]));
    }

    @Step("Проверка общей стоимости всех страховок")
    public void checkAllInsurancePrice() {
        String price = $(byXpath("//div[@data-toggle-id='toggle-safe']/descendant::" +
                "div[@class='checkout-item__left-container']")).getText().replace(" ", "");
        price = price.substring(0, price.length()-1);
        System.out.println("all insurances= " + price);
        int allPrice = stringIntoInt(Values.price.iflight) + stringIntoInt(Values.price.imedical);
        assertTrue("Общая стоимость всех страховок некорректна", stringIntoInt(price) == allPrice);
    }

    @Step("Проверка данных транспортной услуги")
    public void checkTransport(){
        SelenideElement row = $(byXpath("//div[@data-toggle-id='toggle-TRANSPORT']"));
        String name = row.$(byXpath("descendant::div[@ng-bind='item.details.carName']")).getText();
        System.out.println(name);
        assertTrue("Название авто отличается от забронированного", Values.auto.name.equals(name));

        String receiveLocation = row.$(byXpath("descendant::div[@ng-bind='item.details.receiveLocation']")).getText();
        System.out.println(receiveLocation);
        assertTrue("Место получения отличается от забронированного", Values.auto.receiveLocation.equals(receiveLocation));

        String receiveDate = row.$(byXpath("descendant::div[@ng-bind='item.details.receiveDate']")).getText();
        receiveDate = receiveDate.substring(receiveDate.indexOf(",")+1);
        System.out.println(receiveDate);
        assertTrue("Дата получения отличается от забронированной", Values.auto.receiveDate.equals(stringToDate(receiveDate)));

        String returnLocation = row.$(byXpath("descendant::div[@ng-bind='item.details.returnLocation']")).getText();
        System.out.println(returnLocation);
        assertTrue("Место возврата отличается от забронированного", Values.auto.returnLocation.equals(returnLocation));

        String returnDate = row.$(byXpath("descendant::div[@ng-bind='item.details.returnDate']")).getText();
        returnDate = returnDate.substring(returnDate.indexOf(",")+1);
        System.out.println(returnDate);
        assertTrue("Дата возврата отличается от забронированной", Values.auto.returnDate.equals(stringToDate(returnDate)));

        String price = row.$(byXpath("descendant::span[contains(@class,'__item-price')]")).getText().replace(" ", "");
        price = price.substring(0, price.length()-1);
        System.out.println(price);
        assertTrue("Стоимость аренды автомобиля отличается от забронированной", Values.price.transport.equals(price.replace(".",",")));
    }

    private Date stringToDate(String d) {
        Date parsingDate=null;
        try {
            parsingDate = new SimpleDateFormat("dd MMMM yyyy, HH:mm", new Locale(Values.lang[ln][2])).parse(d);
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


}

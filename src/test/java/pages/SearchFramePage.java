package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import config.Values;
import io.qameta.allure.Step;
import struct.CollectData;
import struct.Flight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by mycola on 10.10.2018.
 */
public class SearchFramePage extends Page {

    private CollectData collectData;

    public SearchFramePage(CollectData collectData) {
        this.collectData = collectData;
    }

    @Step("Действие 2, Поиск рейсов")
    public List<Flight> searchFlight(int caseNumber) {
        boolean result = false;
        List<Flight> flightList = new ArrayList<Flight>();
        switch (caseNumber) {
            case 1:
                result = searchFlight1();
                break;
            case 2:
                result = searchFlight1();
                break;
            case 3:
                result = searchFlight7("00:","00:");
                break;
            case 4:
                result = searchFlight7("00:", "00:");
                break;
            case 5:
                result = searchFlight7("23:","23:");
                break;
            case 6:
                result = searchFlight7("23:", "23:");
                break;
            case 7:
                result = searchFlight7("23:", "00:");
                break;
            case 8:
                result = searchFlight7("23:", "00:");
                break;
        }
        if (result) {
            flightList = getFlightData();
            clickBuyButton();
            clickPassengersButton();
        }
        return flightList;
    }

    @Step("Вылет до полуночи, прилет после полуночи")
    private boolean searchFlight1() {
        $(byXpath("//div[contains(@class,'frame__heading')]")).shouldBe(visible);
        Sleep(2);
        ElementsCollection flights = $$(byXpath("//div[@class='flight-search__inner']"));
        if (flights.size() > 0) {
            for (SelenideElement flight : flights) {
                if (!flight.$(byXpath("descendant::button")).exists()) continue; //Пропускаем рейсы без кнопки <ВЫБРАТЬ РЕЙС>
                if (flight.$(byXpath("descendant::span[@class='time-destination__plusday']")).exists()) {
                    flight.click();
                    return true;
                }
                if (flight.$(byXpath("descendant::span[@class='h-color--orange']")).exists()) {
                    flight.click();
                    return true;
                }
            }
        }
        return false;
    }

    @Step("Вылет после {0}00, но не позднее {1}59 по Москве")
    private boolean searchFlight7(String leftTime, String rightTime) {
        $(byXpath("//div[contains(@class,'frame__heading')]")).shouldBe(visible);
        Sleep(2);
        ElementsCollection flights = $$(byXpath("//div[@class='flight-search__inner']"));
        if (flights.size() > 0) {
            for (SelenideElement flight : flights) {
                if (!flight.$(byXpath("descendant::button")).exists()) continue; //Пропускаем рейсы без кнопки <ВЫБРАТЬ РЕЙС>
                SelenideElement time = flight.$(byXpath("descendant::div[@class='time-destination__from']/div"));
                if (time.getText().contains(leftTime) | time.getText().contains(rightTime)) {
                    flight.click();
                    return true;
                }
            }
        }
        return false;
    }

    @Step("Извлечь дату/время перелета")
    private List<Flight> getFlightData(){
        $(byXpath("//a[contains(@class,'modal__close')]")).shouldBe(visible);
        List<Flight> flightList = new ArrayList<Flight>();
        ElementsCollection flights = $$(byXpath("//div[@class='flight__row']/descendant::div[@class='flight__date']"));
        for (int i = 0; i<flights.size(); i = i+2) {
            Flight flight = new Flight();
            String start = flights.get(i).text();
            System.out.println(start);
            try {
                flight.start = new SimpleDateFormat("dd MMMM yyyy г., в HH:mm", new Locale("ru")).parse(start);
                System.out.println("[" + flight.start + "] ");
            } catch (ParseException e) {
                System.out.println("Дата вылета нераспаршена: " + start);
            }
            flight.from = flights.get(i).$(byXpath("preceding-sibling::div[@class='flight__direction']" +
                    "/div[@class='flight__direction-city']")).getText().replace(",", "");

            String stop = getNodeText(flights.get(i+1))
                    + flights.get(i+1).$(byXpath("descendant::span[@class='h-text--bold h-color--black']")).getText();
            System.out.println(stop);
            try {
                flight.end = new SimpleDateFormat("dd MMMM yyyy г., в HH:mm", new Locale("ru")).parse(stop);
                System.out.println("[" + flight.end + "] ");
            } catch (ParseException e) {
                System.out.println("Дата прилета нераспаршена: " + stop);
            }
            flight.to = flights.get(i+1).$(byXpath("preceding-sibling::div[@class='flight__direction']" +
                    "/div[@class='flight__direction-city']")).getText().replace(",", "");
            flightList.add(flight);
        }
        Values.setDOC(collectData.getTest(),  flightList.get(0).from + " -> " +
                flightList.get(flightList.size()-1).to + ", вылет " +
                new SimpleDateFormat("dd MMM. HH:mm -> ").format(flightList.get(0).start) +
                new SimpleDateFormat("dd MMM. HH:mm").format(flightList.get(flightList.size()-1).end));
        System.out.println(Values.getDOC(9));
        return flightList;
    }

    @Step("Нажать \"Купить\"")
    private void clickBuyButton() {
        $(byXpath("//a[contains(@class,'modal__close')]")).shouldBe(visible);
        ElementsCollection buttons = $$(byXpath("//a[contains(@class,'button--bordered')][@aria-disabled='false']"));
        if (buttons.size()>0) {
            buttons.get(0).shouldBe(visible).click();
        } else {
            $(byXpath("//a[contains(@class,'button--bordered')]")).shouldBe(visible).click();
        }
    }

    @Step("Нажать \"Указать пассажиров\"")
    private void clickPassengersButton() {
        $(byXpath("//a[@class='next__button']")).shouldBe(visible).click();
    }

}

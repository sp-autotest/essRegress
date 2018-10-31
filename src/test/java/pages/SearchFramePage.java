package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Created by mycola on 10.10.2018.
 */
public class SearchFramePage extends Page {

    @Step("Действие 3, поиск рейсов")
    public boolean searchFlight(int caseNumber) {
        boolean result = false;
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
            clickBuyButton();
            clickPassengersButton();
        }
        return result;
    }

    @Step("Вылет до полуночи, прилет после полуночи")
    private boolean searchFlight1() {
        boolean result = false;
        $(byXpath("//div[contains(@class,'frame__heading')]")).shouldBe(visible);
        Sleep(2);
        ElementsCollection flights = $$(byXpath("//div[@class='flight-search__inner']"));
        if (flights.size() > 0) {
            for (SelenideElement flight : flights) {
                if (flight.$$(byXpath("descendant::button")).size() == 0) continue; //Пропускаем рейсы без кнопки <ВЫБРАТЬ РЕЙС>
                ElementsCollection plusDay = flight
                        .$$(byXpath("descendant::span[@class='time-destination__plusday']"));
                if (plusDay.size() > 0) {
                    flight.click();
                    result = true;
                    break;
                }
                ElementsCollection overDay = flight
                        .$$(byXpath("descendant::span[@class='h-color--orange']"));
                if (overDay.size() > 0) {
                    flight.click();
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    @Step("Вылет после {0}00, но не позднее {1}59 по Москве")
    private boolean searchFlight7(String leftTime, String rightTime) {
        boolean result = false;
        $(byXpath("//div[contains(@class,'frame__heading')]")).shouldBe(visible);
        Sleep(2);
        ElementsCollection flights = $$(byXpath("//div[@class='flight-search__inner']"));
        if (flights.size() > 0) {
            for (SelenideElement flight : flights) {
                if (flight.$$(byXpath("descendant::button")).size() == 0) continue; //Пропускаем рейсы без кнопки <ВЫБРАТЬ РЕЙС>
                SelenideElement time = flight.$(byXpath("descendant::div[@class='time-destination__from']/div"));
                System.out.println("Time = " + time.getText());
                if (time.getText().contains(leftTime) | time.getText().contains(rightTime)) {
                    System.out.println("Time exist!!!!!");
                    flight.click();
                    result = true;
                    break;
                }
            }
        }
        return result;
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

package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import struct.City;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

/**
 * Created by mycola on 11.10.2018.
 */
public class CityPage extends Page {

    @Step("Действие 0, выбор рейсов")
    public List<City> getCityList() {
        List<City> cities = new ArrayList<>();
        SelenideElement table = $(byXpath("//table[@class='tariff_list']")).shouldBe(visible);
        ElementsCollection rows = table.$$(byXpath("descendant::tr"));
        for (SelenideElement row : rows) {
            ElementsCollection cells = row.$$(byXpath("descendant::td"));
            if (cells.size() == 7) {
                if (cells.get(6).getText().trim().length() == 3) {
                    City city = new City();
                    city.setCode(cells.get(0).getText());
                    city.setHour(stringIntoInt(cells.get(6).getText()));
                    cities.add(city);
                }
            }
        }
        return cities;
    }

}

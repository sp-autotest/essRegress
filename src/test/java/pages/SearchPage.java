package pages;

import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

/**
 * Created by mycola on 20.02.2018.
 */
public class SearchPage extends Page {

    @Step("Проверить загрузку страницы \"Укажите пассажиров\"")
    public void selectLocale(String locale) {
        $(byXpath("//div[@class='header__select-items']")).shouldBe(visible).click();
        $(byXpath("//div[text()='" + locale + "']")).shouldBe(visible).click();
    }

}

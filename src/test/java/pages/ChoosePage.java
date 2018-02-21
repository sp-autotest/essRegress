package pages;

import ru.yandex.qatools.allure.annotations.Step;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;


/**
 * Created by mycola on 21.02.2018.
 */
public class ChoosePage extends Page {

    private String env = System.getProperty("area", "RC");//получить имя площадки из дженкинса, при неудаче площадка=RC


    @Step("Действие 4, выбор стенда")
    public void step4() {
        checkChoosePage();
        clickEnvironment();
    }

    @Step("Подождать страницу выбора стенда")
    private void checkChoosePage(){
        waitPlane();
        waitPlane();
        $("h1").shouldBe(exactText("Вход в тестовую среду системы ЕПР"));
    }

    @Step("Выбрать стенд")
    private void clickEnvironment() {
        $(byXpath("//div[contains(text(),'Cтенд ЕПР TEST-RC (realease candidate) с перенаправлением на витрину ESS')]")).click();
        //временно, нужно решить проблему с <BR> в тексте
        //$(byXpath("//span[contains(text(),'TEST-RC')][contains(text(),'ESS')]")).shouldBe(visible).click();
        waitPlane();
    }



}

package listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import io.qameta.allure.Attachment;
import java.io.IOException;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Created by mycola on 20.02.2018.
 */
public class ScreenShoter {

    private  ScreenShoter(){};

    @Attachment(value = "Скриншот", type = "image/png")
    public static byte[] makeScreenshot() throws IOException {
        return ((TakesScreenshot)getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

}

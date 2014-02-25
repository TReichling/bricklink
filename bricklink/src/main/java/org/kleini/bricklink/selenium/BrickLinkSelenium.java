/*
 * GPLv3
 */

package org.kleini.bricklink.selenium;

import static org.kleini.bricklink.api.ConfigurationProperty.LOGIN;
import static org.kleini.bricklink.api.ConfigurationProperty.PASSWORD;
import org.kleini.bricklink.api.Configuration;
import org.kleini.bricklink.data.Condition;
import org.kleini.bricklink.data.GuideType;
import org.kleini.bricklink.data.ItemType;
import org.kleini.bricklink.data.PriceGuide;
import org.kleini.bricklink.selenium.catalog.PriceGuidePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * {@link BrickLinkSelenium}
 *
 * @author <a href="mailto:himself@kleini.org">Marcus Klein</a>
 */
public final class BrickLinkSelenium {

    private static final String URL = "https://www.bricklink.com";

    private final WebDriver driver;

    private final LoginPage loginPage;

    public BrickLinkSelenium(String login, String password) throws Exception {
        super();
        driver = new FirefoxDriver();
        driver.get(URL);
        loginPage = new LoginPage(driver);
        loginPage.login(login, password);
    }

    public BrickLinkSelenium(Configuration configuration) throws Exception {
        this(configuration.getProperty(LOGIN), configuration.getProperty(PASSWORD));
    }

    public void close() {
        loginPage.logout();
    }

    public PriceGuide getPriceGuide(ItemType itemType, String itemID, int colorID, GuideType sold, Condition condition) throws Exception {
        return new PriceGuidePage(driver).getPriceGuide(itemType, itemID, colorID, sold, condition);
    }
}
package org.kleini.lego;

import static org.kleini.selenium.Utils.headlessChrome;
import java.io.Closeable;
import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.kleini.selenium.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * set nummer span[class='item-code']
 * *
 * @author <a href="mailto:himself@kleini.org">Marcus Klein</a>
 */
public class LEGOShopSelenium implements Closeable {

    private static final String URL = "https://shop.lego.com/de-DE/";

    private final WebDriver driver;

    public LEGOShopSelenium() {
        super();
        String browser = System.getProperty("browser");
        if ("firefox".equals(browser)) {
            driver = new FirefoxDriver();
        } else if ("chrome".equals(browser)) {
            driver = new ChromeDriver();
        } else {
            driver = headlessChrome();
        }
        driver.manage().window().setSize(new Dimension(1980, 1500));
    }

    @Override
    public void close() {
        driver.close();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public File getScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }

    public List<Set> getAvailableSets() throws Exception {
        driver.get(URL);
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-test='age-gate-grown-up-cta']"))).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-test='cookie-accept-all']"))).click();
        driver.findElement(By.cssSelector("button[data-analytics-title='themes']")).click();
        List<WebElement> categories = driver.findElements(By.cssSelector("button[data-analytics-title='themes'] + div > div > ul > li > a"));
        List<String> urls = new LinkedList<String>();
        for (WebElement a : categories) {
            urls.add(a.getAttribute("href"));
        }
        List<Set> retval = new LinkedList<Set>();
        for (String url : urls) {
            if (url.endsWith("/about")) continue;
            driver.get(url);
            System.out.print("Category: " + (url.substring(url.lastIndexOf('/') + 1)) + " ");
            final List<Set> found;
            if (false) {
                found = allSetsOnOnePage();
            } else {
                found = allSetsByPagination();
            }
            System.out.println(found.size());
            retval.addAll(found);
        }
        return retval;
    }

    private List<Set> allSetsByPagination() {
        List<Set> retval = new LinkedList<Set>();
        List<WebElement> nextButton = Collections.emptyList();
        do {
            if (!nextButton.isEmpty() && nextButton.get(0).getAttribute("disabled") == null) {
                nextButton.get(0).click();
            }
            new WebDriverWait(driver, 10).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div[data-test='product-leaf']"), 0));
            retval.addAll(readSetsFromPage());
            nextButton = driver.findElements(By.cssSelector("a[data-test='pagination-next']"));
        } while (!nextButton.isEmpty() && nextButton.get(0).getAttribute("disabled") == null);
        return retval;
    }

    private static final Pattern shownNumber = Pattern.compile("ANZEIGE: (\\d+) VON (\\d+)\n.*", Pattern.MULTILINE);

    private List<Set> allSetsOnOnePage() throws Exception {
        List<WebElement> all = driver.findElements(By.cssSelector("button[data-test='pagination-show-all']"));
        if (!all.isEmpty()) {
            try {
                all.get(0).click();
                WebElement endButton = driver.findElement(By.cssSelector("button[class^='Scrollstyles__Button']"));
                boolean finished = false;
                do {
                    Utils.scrollTo(driver, endButton);
                    Matcher matcher = shownNumber.matcher(endButton.getText());
                    if (matcher.matches()) {
                        finished = matcher.group(1).equals(matcher.group(2));
                    } else {
                        throw new Exception("Text for testing scrolling and article loading was not found.");
                    }
                } while (!finished);
            } catch (StaleElementReferenceException | InterruptedException e) {
                // if button is not attached to page, we have less than 15 elements
            }
        }
        return readSetsFromPage();
    }

    private static final Pattern pattern = Pattern.compile("(?:Price\\r?\\n)?([\\d\\,]+) €");

    private List<Set> readSetsFromPage() {
        List<Set> retval = new LinkedList<Set>();
        List<WebElement> products = new WebDriverWait(driver, 10).until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div[data-test='product-leaf']"), 0));
        for (WebElement product : products) {
            WebElement productLink = product.findElement(By.cssSelector("div > a"));
            String href = productLink.getAttribute("href");
            int setNummer = Integer.parseInt(href.substring(href.lastIndexOf('-') + 1));
            Set set = new Set(setNummer);

            WebElement spanWithName = product.findElement(By.cssSelector("div > div > a[data-test='product-leaf-title-link'] > h2[data-test='product-leaf-title'] > span"));
            String name = spanWithName.getText();
            set.setName(name);

            final WebElement priceElement;
            List<WebElement> saleElements = product.findElements(By.cssSelector("div > div > div[data-test='product-leaf-price'] > div > span"));
            if (!saleElements.isEmpty()) {
                priceElement = saleElements.get(0);
            } else {
                System.out.println("No retail price for Set " + setNummer);
                continue;
            }
            String toParse = priceElement.getText();
            Matcher matcher = pattern.matcher(toParse);
            if (matcher.matches()) {
                String value = matcher.group(1).replace(',', '.');
                set.setRetailPrice(new BigDecimal(value));
            } else {
                throw new RuntimeException("Can not parse set retail price \"" + toParse.replace('\n', '_') + "\".");
            }
            retval.add(set);
        }
        return retval;
    }
}

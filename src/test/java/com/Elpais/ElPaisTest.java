package com.Elpais;

import DriverSetup.BaseTest;
import GoogleTranslate.GoogleTranslator;
import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ElPaisTest extends BaseTest {
    private WebDriver driver;

    public static void main(String[] args) {

    }

    static String baseURL = "https://elpais.com/";
    static List<WebElement> articleLists;

    @BeforeClass
    public void setUpBeforeClass() {
        driver = getDriver();

    }

    @Test
    public void LaunchElPaisPage() {
        driver.get(baseURL);
    }

    @Test
    public void checkAlert() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("" +
                    "//*[contains(text(), 'Accept')]"))).click();
        } catch (Exception e) {
            System.out.println("Alert is not present");
        }
    }


    @Test
    //Checking for page in spanish
    public void verifyWordsInSpanish() {
        //1. Get the first html tag, which has all the content and attribute of lang

        WebElement htmlElement = driver.findElement(By.xpath("//html"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String pageLanguage = (String) js.executeScript("return arguments[0].getAttribute('lang');", htmlElement);
        System.out.println("Page language is: " + pageLanguage);
        assert pageLanguage != null;
        if (pageLanguage.contains("es"))
            System.out.println("Page language is ES");
        else
            System.out.println("page language is not ES. Page language is: " + pageLanguage);
    }

    @Test()
    public void translateFromSpainToEnglishAndCountRepeatedWords() {
        LinkedHashMap<String, Integer> countMap = new LinkedHashMap<>();
        articleLists = driver.findElements(By.xpath("//article"));
        System.out.println("Total articles : " + articleLists.size());
        for (WebElement article : articleLists) {
            String title = article.findElement(By.tagName("h2")).getText();
            String titleInEnglish = GoogleTranslator.translateText(title);
            System.out.println("Spanish title - " + title + " : English translation - " + titleInEnglish);
            String[] words = titleInEnglish.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+");
            for (String word : words) {
                countMap.put(word, countMap.getOrDefault(word, 0) + 1);
            }
        }

        System.out.println("Printing repeated words more than twice");
        boolean found = false;
        for (String title : countMap.keySet()) {
            if (countMap.get(title) > 2) {
                System.out.println(title + " - " + countMap.get(title));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No words repeated more than twice");
        }

    }

    @Test
    public void printTitleAndContent() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //In mobile opinion link is not present
        try {
            WebElement opinionLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[contains(text(), 'Opinión')])[1]")));
            opinionLink.click();
        }
        catch (Exception e){
            driver.navigate().to("https://elpais.com/opinion/");
        }
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//article")));
        //Looping through the articles and printing title and content
        for (int i = 0; i < 5; i++) {
            //To avoid Stale Element Exception
            List<WebElement> articleLists = driver.findElements(By.xpath("//article"));
            if (i >= articleLists.size()) break;
            WebElement articleList = articleLists.get(i);
            String articleHeading = "";
            String articleContent = "";

            //We are facing issues of Stale Element in Mobile OS since it reloads after every scroll
            for(int j=0; j<3; j++) {
                try {
                    articleHeading = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy
                            (articleList, By.tagName("h2"))).getText();
                    articleContent = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(articleList, By.tagName("p"))).getText();
                    break;
                }
                catch (StaleElementReferenceException e){
                    Thread.sleep(1000);
                    articleLists = driver.findElements(By.xpath("//article"));
                    articleList = articleLists.get(i);
                }
                }
            System.out.println(articleHeading + " : " + articleContent);
            fetchImage(articleHeading, i);
            System.out.println("\n");

        }

    }


    public void fetchImage(String articleHeading, int count) {

        try {
            String url = "";
            List<WebElement> articleLists;
            WebElement article = null;
            for (int attempt = 0; attempt < 3; attempt++) {
                try {
                    articleLists = driver.findElements(By.xpath("//article"));
                    if (count >= articleLists.size()) return;
                    article = articleLists.get(count);
                    try {
                        url = article.findElement(By.tagName("img")).getAttribute("src");
                    } catch (Exception e) {
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        WebElement img1 = article.findElement(By.tagName("img"));
                        url = (String) js.executeScript("return arguments[0].getAttribute('src');", img1);
                    }
                    break; // success
                } catch (StaleElementReferenceException e) {
                    Thread.sleep(1000);
                }
            }

            System.out.println("URL is : " + url);
            URL imageurl = new URL(url);
            BufferedImage img = ImageIO.read(imageurl);

            if (img != null) {
                String savePath = System.getProperty("user.dir") + "/target/images/";
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String fileName = savePath + "article" + count + ".jpg";
                ImageIO.write(img, "jpg", new File(fileName));
                System.out.println("Image downloaded: " + savePath);
            } else {
                System.out.println("Image could not be read (null): " + imageurl);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Image not present for the article : " + articleHeading);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


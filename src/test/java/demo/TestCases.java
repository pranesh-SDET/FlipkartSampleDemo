    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     * 
     * Testcase01:  Go to www.flipkart.com. Search "Washing Machine". Sort by popularity and print the count of items with rating less than or equal to 4 stars.
     * 
     * 1. List<WebElement> productRatings = driver.findElements(By.xpath("//span[contains[@id='productRating']"));
     * int count = 0;
     * for (WebElement productRating : productRatings) {
     *  double ratingDouble = Double.parseOf(productRating.getText());
     *  if (ratingDouble >= 4.0) {
     *  Count++;
     * 
     * Challenge :
     * By taking wrong class name in the xpath was giving 1 of 39 which is incorrect product rating elements
     * so post that we have taken proper id attribute value for xpath and get 1 of 24 in the washing machine page
    * 
     */



    /*
        * Testcase02:  Search "iPhone", print the Titles and discount % of items with more than 17% discount
     *   Parent : //div[@class='jIjQ8S'] 24 elements or iphones
     * 
     *   for (WebElement iphone : iphones) {
     *        String discountPercentage = iphone.findElement(By.xpath("//div[@class='HQe8jr']")).getText();
     *        // further condition for the discount percentage and then print iphone title and discount percentage
     *          At 7th iteration, selenium will throw no such element exception as the 7th element does not have discount percentage and we need to handle that exception
     * WorkAround:-
     * List<WebElement> discountPercentage = driver.findElements(By.xpath("//div[@class='HQe8jr']"));
     * for(WebElement discountPercentage : discountPercentages){
     *     String discountPercentageText = discountPercentage.getText(); //12% off
     *     int discountPercentageInteger = Integer.parseOf(discountPercentageText.replaceAll("[^\\d]",""));
     *     if(discountPercentageInteger> 17){
     *          String title = discountPercentage.findElement("./ancestor::div[contains(@class,'ZFwe0M')]//div[@class='RG5SIk']")
     *      print(title);
     *      print(discountPercentageText);
     * }    
     */



    /**
     *  Testcase03: Search "Coffee Mug", select 4 stars and above, and print the Title and image URL of the 5 items with highest number of reviews
     * How to sort 40 coffee mugs in page no.1 ?
     * 
     * Collections class
     * 1st we will do a sort method and then we will reverse it as we need it in descending order
     * method --> sort() will accept ArrayList
     * method --> reverse()
     * 
     * String title = //span[contains(text(),'"+userRating"')]/ancestor::div[@class="RGLWAk"]//a[@class="pIpigb"]
     * String url = //span[contains(text(),'"+userRating"')]/ancestor::div[@class="RGLWAk"]//img[@class='UCc1II']
     * 
     */

     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
package demo;

import java.time.Duration;
import java.util.logging.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;

    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @Test(enabled = true)
    public void testCase01() throws InterruptedException {
        System.out.println("Beginning Test Case 01");

        double starRating = 4.0;
        driver.get("https://www.flipkart.com/");

        // Search for Washing Machine
        Wrappers.enterTextWrapper(driver, By.xpath("//input[@name='q']"), "Washing Machine");
        Thread.sleep(3000);

        // Sort by Popularity
        Wrappers.clickOnElementWapper(driver, By.xpath("//div[contains(text(),'Popularity')]"));
        Thread.sleep(3000);

        // Count items with rating <= 4.0
        Boolean status = Wrappers.searchStarRatingAndPrintCount(
                driver,
                By.xpath("//span[contains(@id,'productRating')]/div | //div[contains(@class,'XQDdHH')] | //div[contains(@class,'_3LWZlK')]"),
                starRating);
        Assert.assertTrue(status);

        System.out.println("Ending Test Case 01");
    }

    @Test(enabled = true)
    public void testCase02() throws InterruptedException {
        System.out.println("Beginning Test Case 02");

        int discount = 17;
        driver.get("https://www.flipkart.com/");

        // Search for iPhone
        Wrappers.enterTextWrapper(driver, By.xpath("//input[@name='q']"), "iPhone");
        Thread.sleep(5000);

        // Print titles and discount percentage for items with discount > 17%
        Boolean status = Wrappers.printTitleAndDiscountIphone(
                driver,
                By.xpath("//div[@class='_3Ay6Sb']//span | //div[contains(@class,'UkUFwK')]//span | //div[contains(text(),'% off')] | //span[contains(text(),'% off')]"),
                discount);
        Assert.assertTrue(status);

        System.out.println("Ending Test Case 02");
    }

    @Test(enabled = true)
    public void testCase03() throws InterruptedException {
        System.out.println("Beginning Test Case 03");

        driver.get("https://www.flipkart.com/");

        // Search for Coffee Mug
        Wrappers.enterTextWrapper(driver, By.xpath("//input[@name='q']"), "Coffee Mug");
        Thread.sleep(3000);

        // Filter by 4★ & above
        Wrappers.clickOnElementWapper(driver, By.xpath("//div[contains(text(),'4') and contains(text(),'above')]"));
        Thread.sleep(3000);

        // Extract top 5 review count products with titles and image URLs
        Boolean status = Wrappers.printTitleAndImageUrlOfCoffeeMug(
                driver,
                By.xpath("//div[contains(@class,'slAVV4')]//span[contains(text(),'(')] | //span[contains(@class,'Wphh3N')]"));
        Assert.assertTrue(status);

        System.out.println("Ending Test Case 03");
    }

    @AfterTest
    public void endTest() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}
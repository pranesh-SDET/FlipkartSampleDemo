package demo.wrappers;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Wrappers {

    public static void enterTextWrapper(WebDriver driver, By locator, String textToEnter) {
        System.out.println("Sending Keys");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement inputBox = driver.findElement(locator);
            inputBox.clear();
            inputBox.sendKeys(textToEnter);
            inputBox.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            System.out.println("Exception Occured! " + e.getMessage());
        }
    }

    public static void clickOnElementWapper(WebDriver driver, By locator) {
        System.out.println("Clicking");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement clickableElement = driver.findElement(locator);
            clickableElement.click();
        } catch (Exception e) {
            System.out.println("Exception Occured! " + e.getMessage());
        }
    }

    // Alias in case TestCases.java uses standard spelling
    public static void clickOnElementWrapper(WebDriver driver, By locator) {
        clickOnElementWapper(driver, locator);
    }

    public static Boolean searchStarRatingAndPrintCount(WebDriver driver, By locator, double starRating) {
        int washingMachineCount = 0;
        Boolean success;
        try {
            List<WebElement> starRatingElements = driver.findElements(locator);
            for (WebElement starRatingElement : starRatingElements) {
                if (Double.parseDouble(starRatingElement.getText().trim()) <= starRating) {
                    washingMachineCount++;
                }
            }
            System.out.println("Count of washing machine which has star rating less than or equal to " + starRating
                    + ": " + washingMachineCount);
            success = true;
        } catch (Exception e) {
            System.out.println("Exception Occured! ");
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public static Boolean printTitleAndDiscountIphone(WebDriver driver, By locator, int discount) {
        Boolean success;
        try {
            HashMap<String, String> iphoneTitleDiscountMap = new HashMap<>();
            List<WebElement> discountPercentageList = driver.findElements(locator);
            for (WebElement productRow : discountPercentageList) {
                String discountPercent = productRow.getText();
                int discountValue = Integer.parseInt(discountPercent.replaceAll("[^\\d]", ""));
                if (discountValue > discount) {
                    String iphoneTitle;
                    try {
                        iphoneTitle = productRow.findElement(By.xpath("./../../../../..//div[contains(@class,'KzDlHZ') or contains(@class,'_4rR01T')]")).getText();
                    } catch (Exception ex) {
                        iphoneTitle = productRow.findElement(By.xpath("./ancestor::div[contains(@class,'_1AtVbE') or contains(@class,'_75nlfW') or contains(@class,'cPHDOP')]//div[contains(@class,'KzDlHZ') or contains(@class,'_4rR01T')]")).getText();
                    }
                    iphoneTitleDiscountMap.put(iphoneTitle, discountPercent);
                }
            }

            for (Map.Entry<String, String> iphoneTitleDiscounts : iphoneTitleDiscountMap.entrySet()) {
                System.out.println("Iphone discount percentage :: " + iphoneTitleDiscounts.getValue()
                        + " and its title :: " + iphoneTitleDiscounts.getKey());
            }
            success = true;
        } catch (Exception e) {
            System.out.println("Exception Occured! ");
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public static Boolean printTitleAndImageUrlOfCoffeeMug(WebDriver driver, By locator) {
        Boolean success;
        try {
            List<WebElement> userReviewElements = driver.findElements(locator);
            Set<Integer> userReviewSet = new HashSet<>();
            for (WebElement userReviewElement : userReviewElements) {
                int userReview = Integer.parseInt(userReviewElement.getText().replaceAll("[^\\d]", ""));
                userReviewSet.add(userReview);
            }

            List<Integer> userReviewCountList = new ArrayList<>(userReviewSet);
            Collections.sort(userReviewCountList, Collections.reverseOrder());
            System.out.println(userReviewCountList);

            NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
            LinkedHashMap<String, String> productDetailsMap = new LinkedHashMap<>();

            int topCount = Math.min(5, userReviewCountList.size());
            for (int i = 0; i < topCount; i++) {
                String formattedUserReviewCount = "(" + numberFormat.format(userReviewCountList.get(i)) + ")";
                
                String productTitle;
                try {
                    productTitle = driver.findElement(By.xpath("//div[contains(@class,'RGLWAk')]//span[contains(text(),'" + formattedUserReviewCount + "')]/../../a[contains(@class,'piPigb') or contains(@class,'wjcEIp')]")).getText();
                } catch (Exception ex) {
                    productTitle = driver.findElement(By.xpath("//span[contains(text(),'" + formattedUserReviewCount + "')]/ancestor::div[contains(@class,'_1sdMkc') or contains(@class,'slAVV4')]//a")).getText();
                }

                String productImgURL;
                try {
                    productImgURL = driver.findElement(By.xpath("//div[contains(@class,'sLAVV4') or contains(@class,'slAVV4')]//span[contains(text(),'" + formattedUserReviewCount + "')]/../../../img[contains(@class,'DByuf4') or contains(@class,'_53J4C-')]")).getAttribute("src");
                } catch (Exception ex) {
                    productImgURL = driver.findElement(By.xpath("//span[contains(text(),'" + formattedUserReviewCount + "')]/ancestor::div[contains(@class,'_1sdMkc') or contains(@class,'slAVV4')]//img")).getAttribute("src");
                }

                String highestReviewCountAndProductTitle = String.valueOf(i + 1) + " highest review count: " + formattedUserReviewCount + " and title: " + productTitle;
                productDetailsMap.put(highestReviewCountAndProductTitle, productImgURL);
            }

            for (Map.Entry<String, String> productDetails : productDetailsMap.entrySet()) {
                System.out.println(productDetails.getKey() + " and Product image url: " + productDetails.getValue());
            }
            success = true;
        } catch (Exception e) {
            System.out.println("Exception Occured! ");
            e.printStackTrace();
            success = false;
        }
        return success;
    }
}
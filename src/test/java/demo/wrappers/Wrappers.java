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
import org.openqa.selenium.JavascriptExecutor;
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
            WebElement clickableElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", clickableElement);
            wait.until(ExpectedConditions.elementToBeClickable(clickableElement));
            clickableElement.click();
        } catch (Exception e) {
            try {
                WebElement clickableElement = driver.findElement(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickableElement);
            } catch (Exception ex) {
                System.out.println("Exception Occured! " + e.getMessage());
            }
        }
    }

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
            List<WebElement> userReviewElements = driver.findElements(locator); // 40 coffee mugs
            Set<Integer> userReviewSet = new HashSet<>(); // we will store unique number of user rating
            for (WebElement userReviewElement : userReviewElements) {
                String text = userReviewElement.getText().replaceAll("[^\\d]", "");
                if (!text.isEmpty()) {
                    userReviewSet.add(Integer.parseInt(text));
                }
            }

            List<Integer> userReviewCountList = new ArrayList<>(userReviewSet); // converting set into list
            Collections.sort(userReviewCountList, Collections.reverseOrder()); // sorting into descending order
            System.out.println(userReviewCountList); // [1,2,3,4,.......] 2000 -> logging

            NumberFormat numberFormat = NumberFormat.getInstance(Locale.US); // which will add a comma
            LinkedHashMap<String, String> productDetailsMap = new LinkedHashMap<>(); // i am using this for storing

            int topCount = Math.min(5, userReviewCountList.size());
            for (int i = 0; i < topCount; i++) {
                int reviewNum = userReviewCountList.get(i);
                String formattedUserReviewCount = "(" + numberFormat.format(reviewNum) + ")"; // (2,000)
                String rawNumWithComma = numberFormat.format(reviewNum);
                String rawNumWithoutComma = String.valueOf(reviewNum);

                WebElement reviewSpan;
                try {
                    reviewSpan = driver.findElement(By.xpath(
                            "//span[contains(., '" + formattedUserReviewCount + "') or contains(., '" + rawNumWithComma + "') or contains(., '" + rawNumWithoutComma + "')]"));
                } catch (Exception ex) {
                    reviewSpan = driver.findElement(By.xpath(
                            "//*[contains(text(), '" + rawNumWithComma + "') or contains(text(), '" + rawNumWithoutComma + "')]"));
                }

                WebElement card = reviewSpan.findElement(By.xpath(
                        "./ancestor::div[contains(@class,'slAVV4') or contains(@class,'_1sdMkc') or contains(@class,'_4ddWXP') or contains(@class,'_75nlfW') or contains(@class,'cPHDOP')][1]"));

                String productTitle;
                try {
                    productTitle = card.findElement(By.xpath(
                            ".//a[contains(@class,'wjcEIp') or contains(@class,'piPigb') or contains(@class,'VJA3rP') or @title]")).getText();
                    if (productTitle == null || productTitle.trim().isEmpty()) {
                        productTitle = card.findElement(By.xpath(
                                ".//a[contains(@class,'wjcEIp') or contains(@class,'piPigb') or contains(@class,'VJA3rP') or @title]")).getAttribute("title");
                    }
                } catch (Exception ex) {
                    productTitle = card.findElement(By.xpath(".//a")).getText();
                }

                String productImgURL;
                try {
                    productImgURL = card.findElement(By.xpath(
                            ".//img[contains(@class,'DByuf4') or contains(@class,'_53J4C-') or contains(@class,'_396cs4') or @src]")).getAttribute("src");
                } catch (Exception ex) {
                    productImgURL = card.findElement(By.xpath(".//img")).getAttribute("src");
                }

                String highestReviewCountAndProductTitle = String.valueOf(i + 1) + " highest review count: " + formattedUserReviewCount + " and title: " + productTitle;
                productDetailsMap.put(highestReviewCountAndProductTitle, productImgURL);
            }

            // print title and image url of coffee mug
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
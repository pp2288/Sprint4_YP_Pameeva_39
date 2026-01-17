package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 1ая страница заказа "Для кого самокат"
    private final By firstNameInput = By.xpath(".//input[@placeholder='* Имя']");
    private final By lastNameInput  = By.xpath(".//input[@placeholder='* Фамилия']");
    private final By addressInput   = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroInput     = By.xpath(".//input[@placeholder='* Станция метро']");
    private final By phoneInput     = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton     = By.xpath(".//button[text()='Далее']");

    // 2ая страница заказа "Про аренду"
    private final By dateInput = By.xpath(".//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodDropdown = By.className("Dropdown-control");
    private final By commentInput = By.xpath(".//input[@placeholder='Комментарий для курьера']");
    // Кнопка "Заказать" на 2-й странице заказа
    private By orderButton = By.xpath("//div[contains(@class,'Order_Buttons')]//button[text()='Заказать']");

    // Подтверждение
    private final By confirmYesButton = By.xpath(".//button[text()='Да']");
    private final By orderSuccessModal = By.cssSelector(".Order_Modal__YZ-d3"); // если не найдётся — скажешь, подберём
    private final By orderSuccessHeader = By.xpath(".//*[contains(text(),'Заказ оформлен')]");
    // ВСПЛЫВАЮЩЕЕ ОКНО ПОДТВЕРЖДЕНИЯ
    private By confirmModal = By.cssSelector(".Order_Modal__YZ-d3");

    // Методы 1ой страницы

    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput)).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameInput)).sendKeys(lastName);
    }

    public void enterAddress(String address) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput)).sendKeys(address);
    }

    public void selectMetroStation(String station) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(metroInput));
        input.click();
        input.sendKeys(station);

        By stationOption = By.xpath(String.format(".//div[@class='select-search__select']//div[text()='%s']", station));
        wait.until(ExpectedConditions.elementToBeClickable(stationOption)).click();
    }

    public void enterPhone(String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput)).sendKeys(phone);
    }

    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    // методы 2ой страницы

    public void enterDate(String date) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(dateInput));
        input.click();
        input.sendKeys(date);
        input.sendKeys(Keys.ENTER);
    }

    public void selectRentalPeriod(String period) {
        wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodDropdown)).click();
        By option = By.xpath(String.format(".//div[@class='Dropdown-option' and text()='%s']", period));
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

    public void selectColor(String color) {
        // black или grey
        By checkbox = By.id(color);
        wait.until(ExpectedConditions.elementToBeClickable(checkbox)).click();
    }

    public void enterComment(String comment) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(commentInput)).sendKeys(comment);
    }

    public void submitOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(orderButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmModal));

        // Кликнуть да
        wait.until(ExpectedConditions.elementToBeClickable(confirmYesButton)).click();
    }

    public boolean isOrderSuccessDisplayed() {
        // Проверяем, что модалка/заголовок с "Заказ оформлен" появился
        return wait.until(ExpectedConditions.visibilityOfElementLocated(orderSuccessHeader)).isDisplayed();
    }
}
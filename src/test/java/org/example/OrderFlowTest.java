package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.MainPage;
import org.example.pages.OrderPage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderFlowTest {

    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;

    private final Browser browser;
    private final EntryPoint entryPoint;

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String date;
    private final String rentalPeriod;
    private final String color;   // "black" или "grey"
    private final String comment;

    public OrderFlowTest(Browser browser, EntryPoint entryPoint,
                         String firstName, String lastName, String address, String metroStation, String phone,
                         String date, String rentalPeriod, String color, String comment) {
        this.browser = browser;
        this.entryPoint = entryPoint;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.date = date;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
    }

    public enum Browser { CHROME, FIREFOX }
    public enum EntryPoint { HEADER, MIDDLE }

    @Parameterized.Parameters(name = "{index}: {0}, {1}, {2} {3}")
    public static Collection<Object[]> data() {

        // 2 набора данных
        Object[] d1 = new Object[]{"Полина", "Тестова", "Тестовая 1", "Черкизовская", "+79990000001",
                "20.01.2026", "сутки", "black", "Позвоните за 10 минут"};

        Object[] d2 = new Object[]{"Дарина", "Тестовачич", "Тестовая 2", "Сокольники", "+79990000002",
                "21.01.2026", "двое суток", "grey", "Оставьте у двери"};

        // 2 входа × 2 набора × 2 браузера = 8
        return Arrays.asList(new Object[][]{
                {Browser.CHROME,  EntryPoint.HEADER, d1[0], d1[1], d1[2], d1[3], d1[4], d1[5], d1[6], d1[7], d1[8]},
                {Browser.CHROME,  EntryPoint.HEADER, d2[0], d2[1], d2[2], d2[3], d2[4], d2[5], d2[6], d2[7], d2[8]},
                {Browser.CHROME,  EntryPoint.MIDDLE, d1[0], d1[1], d1[2], d1[3], d1[4], d1[5], d1[6], d1[7], d1[8]},
                {Browser.CHROME,  EntryPoint.MIDDLE, d2[0], d2[1], d2[2], d2[3], d2[4], d2[5], d2[6], d2[7], d2[8]},
                {Browser.FIREFOX, EntryPoint.HEADER, d1[0], d1[1], d1[2], d1[3], d1[4], d1[5], d1[6], d1[7], d1[8]},
                {Browser.FIREFOX, EntryPoint.HEADER, d2[0], d2[1], d2[2], d2[3], d2[4], d2[5], d2[6], d2[7], d2[8]},
                {Browser.FIREFOX, EntryPoint.MIDDLE, d1[0], d1[1], d1[2], d1[3], d1[4], d1[5], d1[6], d1[7], d1[8]},
                {Browser.FIREFOX, EntryPoint.MIDDLE, d2[0], d2[1], d2[2], d2[3], d2[4], d2[5], d2[6], d2[7], d2[8]},
        });
    }

    @Before
    public void setUp() {
        if (browser == Browser.CHROME) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }

        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void shouldCreateOrderSuccessfully() {

        // 1) Вход в сценарий (две кнопки)
        if (entryPoint == EntryPoint.HEADER) {
            mainPage.clickOrderButtonHeader();
        } else {
            mainPage.clickOrderButtonMiddle();
        }

        // 2) "Для кого самокат"
        orderPage.enterFirstName(firstName);
        orderPage.enterLastName(lastName);
        orderPage.enterAddress(address);
        orderPage.selectMetroStation(metroStation);
        orderPage.enterPhone(phone);
        orderPage.clickNext();

        // 3) "Про аренду"
        orderPage.enterDate(date);
        orderPage.selectRentalPeriod(rentalPeriod);
        orderPage.selectColor(color);
        orderPage.enterComment(comment);

        // 4) Заказать + подтвердить
        orderPage.submitOrder();

        // 5) Успех
        assertTrue("Должно появиться сообщение об успешном создании заказа",
                orderPage.isOrderSuccessDisplayed());
    }
}
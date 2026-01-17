package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Конструктор страницы
    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Кнопка "Заказать" в шапке страницы
    private By orderButtonHeader = By.xpath("(//button[text()='Заказать'])[1]");

    // Кнопка "Заказать" в середине страницы
    private By orderButtonMiddle = By.xpath("(//button[text()='Заказать'])[2]");

    // Локатор впросов в части Вопросы о важном
    private By questions = By.cssSelector("div.accordion__button[role='button']");

    // Метод для клика на кнопку "Заказать" в шапке страницы
    public void clickOrderButtonHeader() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(orderButtonHeader));
        scrollToElement(button);
        button.click();
    }

    // Метод для клика на кнопку "Заказать" на середине страницы
    public void clickOrderButtonMiddle() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(orderButtonMiddle));
        scrollToElement(button);
        button.click();
    }

    // Метод для клика на вопрос по порядковому номеру (0–7)
    public void clickQuestion(int index) {


        List<WebElement> questionList =
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(questions));

        if (index < 0 || index >= questionList.size()) {
            throw new IllegalArgumentException(
                    "Вопроса с индексом " + index + " не существует. Всего вопросов: " + questionList.size()
            );
        }

        WebElement question = questionList.get(index);
        scrollToElement(question);

        wait.until(ExpectedConditions.elementToBeClickable(question)).click();

        String answerPanelId = question.getAttribute("aria-controls");
        if (answerPanelId != null && !answerPanelId.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(answerPanelId)));
        }
    }

    // Метод для получения текста ответа на вопрос
    public String getAnswerText(int index) {

        List<WebElement> questionList =
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(questions));

        if (index < 0 || index >= questionList.size()) {
            throw new IllegalArgumentException(
                    "Вопроса с индексом " + index + " не существует. Всего вопросов: " + questionList.size()
            );
        }

        WebElement question = questionList.get(index);

        String answerPanelId = question.getAttribute("aria-controls");

        if (answerPanelId == null || answerPanelId.isEmpty()) {
            throw new IllegalStateException(
                    "Не удалось определить панель ответа для вопроса с индексом " + index
            );
        }

        WebElement answerPanel =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(answerPanelId)));

        return answerPanel.getText().trim();
    }

    // Метод для прокрутки страницы к нужному элементу
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                element
        );
    }
}
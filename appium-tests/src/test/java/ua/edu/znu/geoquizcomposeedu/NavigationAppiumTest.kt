
package ua.edu.znu.geoquizcomposeedu

import io.appium.java_client.AppiumBy
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import ua.edu.znu.geoquizcomposeedu.util.TestTags
import java.time.Duration

class NavigationAppiumTest : BaseTest() {

    private fun waitForElement(locator: By) =
        WebDriverWait(driver, Duration.ofSeconds(15))
            .until(
                ExpectedConditions.visibilityOfElementLocated(locator)
            )

    private val questionsButton = AppiumBy.xpath(
        "//*[@text='Questions' or @text='Питання' " +
                "or @content-desc='Questions' or @content-desc='Питання']"
    )

    private val quizButton = AppiumBy.xpath(
        "//*[@text='Quiz' or @text='Тест' " +
                "or @content-desc='Quiz' or @content-desc='Тест']"
    )

    private val questionListTitle = AppiumBy.xpath(
        "//*[@text='Question text' or @text='Текст питання']"
    )

    @Test
    fun navigateFromQuizToQuestions() {

        // Перехід до списку питань
        waitForElement(questionsButton).click()

        // Перевірка відкриття списку
        val title = waitForElement(questionListTitle)

        assertTrue(
            title.isDisplayed,
            "Екран Questions не відкрився"
        )
    }

    @Test
    fun navigateFromQuestionsToQuiz() {

        // Відкриття списку
        waitForElement(questionsButton).click()

        waitForElement(questionListTitle)

        // Повернення до Quiz
        waitForElement(quizButton).click()

        // Перевірка головного екрана
        val question = waitForElement(
            AppiumBy.accessibilityId(
                TestTags.QUESTION_TEXT
            )
        )

        assertTrue(
            question.isDisplayed,
            "Екран Quiz не відкрився"
        )
    }
}

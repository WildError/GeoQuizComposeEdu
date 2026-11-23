
package ua.edu.znu.geoquizcomposeedu

import io.appium.java_client.AppiumBy
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import ua.edu.znu.geoquizcomposeedu.util.TestTags
import java.time.Duration

class QuestionListAppiumTest : BaseTest() {

    private val wait by lazy {
        WebDriverWait(driver, Duration.ofSeconds(15))
    }

    @Test
    fun questionListScreenDisplaysQuestions() {

        // Отримуємо текст першого запитання
        val firstQuestion = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.accessibilityId(
                    TestTags.QUESTION_TEXT
                )
            )
        ).text

        // Переходимо до списку запитань
        val questionsButton = AppiumBy.xpath(
            "//*[@text='Questions' or @text='Питання' " +
                    "or @content-desc='Questions' " +
                    "or @content-desc='Питання']"
        )

        wait.until(
            ExpectedConditions.elementToBeClickable(
                questionsButton
            )
        ).click()

        // Перевіряємо заголовок списку
        val header = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath(
                    "//*[@text='Question text' or " +
                            "@text='Текст питання']"
                )
            )
        )

        assertTrue(
            header.isDisplayed,
            "Заголовок списку не відображається"
        )

        // Перевіряємо наявність запитання у списку
        val questionDisplayed = wait.until { d ->
            d.findElements(
                AppiumBy.className("android.widget.TextView")
            ).any {
                it.text == firstQuestion && it.isDisplayed
            }
        }

        assertTrue(
            questionDisplayed,
            "Перше запитання відсутнє у списку"
        )
    }
}

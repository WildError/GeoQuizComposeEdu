
package ua.edu.znu.geoquizcomposeedu

import io.appium.java_client.AppiumBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.openqa.selenium.support.ui.WebDriverWait
import ua.edu.znu.geoquizcomposeedu.util.TestTags
import java.time.Duration

class MainScreenCheckAnswerParameterizedTest : BaseTest() {

    @ParameterizedTest(name = "Question {0}, answer={2}, correct={3}")
    @CsvSource(
        value = [
            "0, Канберра - це столиця Австралії, Так, true",
            "1, Канберра - це столиця Австралії, Ні, false",
            "2, Суецький канал поєднує Червоне море та Індійський океан, Ні, true",
            "3, Суецький канал поєднує Червоне море та Індійський океан, Так, false"
        ]
    )
    fun checkIfAnswerParameterizedTest(
        index: Int,
        questionText: String,
        userAnswer: String,
        expectedCorrect: Boolean
    ) {
        val wait = WebDriverWait(
            driver,
            Duration.ofSeconds(10)
        )

        // Перехід до третього запитання
        if (index >= 2) {
            repeat(2) {
                driver.findElement(
                    AppiumBy.accessibilityId(
                        TestTags.NEXT_QUESTION_BUTTON
                    )
                ).click()
            }
        }

        // Перевірка тексту запитання
        val questionElement = driver.findElement(
            AppiumBy.accessibilityId(
                TestTags.QUESTION_TEXT
            )
        )

        assertEquals(
            questionText,
            questionElement.text
        )

        // Натискання True або False
        val buttonTag = if (userAnswer == "Так") {
            TestTags.TRUE_BUTTON
        } else {
            TestTags.FALSE_BUTTON
        }

        driver.findElement(
            AppiumBy.accessibilityId(buttonTag)
        ).click()

        // Допустимі локалізовані повідомлення
        val expectedMessages = if (expectedCorrect) {
            listOf("Correct!", "Правильно!")
        } else {
            listOf("Incorrect!", "Неправильно!")
        }

        // Очікування повідомлення Snackbar
        val snackbar = wait.until { currentDriver ->
            currentDriver.findElements(
                AppiumBy.xpath(
                    "//*[@text='Correct!' or " +
                            "@text='Правильно!' or " +
                            "@text='Incorrect!' or " +
                            "@text='Неправильно!']"
                )
            ).firstOrNull { it.isDisplayed }
        }

        // Перевірка правильності результату
        assertTrue(
            snackbar.text in expectedMessages,
            "Неочікуване повідомлення: ${snackbar.text}"
        )
    }
}

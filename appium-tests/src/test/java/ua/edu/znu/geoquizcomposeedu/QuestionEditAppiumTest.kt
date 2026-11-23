
package ua.edu.znu.geoquizcomposeedu

import io.appium.java_client.AppiumBy
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class QuestionEditAppiumTest : BaseTest() {

    private val wait by lazy {
        WebDriverWait(driver, Duration.ofSeconds(20))
    }

    private val listHeader = AppiumBy.xpath(
        "//*[@text='Question text' or @text='Текст питання']"
    )

    private val inputField =
        AppiumBy.className("android.widget.EditText")

    private fun waitForText(text: String): WebElement {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[@text='$text']")
            )
        )
    }

    private fun clickButton(
        english: String,
        ukrainian: String
    ) {
        val locator = AppiumBy.xpath(
            "//*[@text='$english' or @text='$ukrainian']"
        )

        wait.until(
            ExpectedConditions.elementToBeClickable(locator)
        ).click()
    }

    private fun hideKeyboardIfNeeded() {
        runCatching {
            driver.hideKeyboard()
        }
    }

    private fun waitForEditor(): WebElement {
        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                inputField
            )
        )
    }

    private fun waitForList() {
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                listHeader
            )
        )
    }

    private fun findQuestion(text: String): WebElement {

        val locator = AppiumBy.xpath(
            "//*[@text='$text']"
        )

        // Пошук із прокручуванням списку
        repeat(8) {
            val found = driver.findElements(locator)
                .firstOrNull { element ->
                    runCatching {
                        element.isDisplayed
                    }.getOrDefault(false)
                }

            if (found != null) return found

            val size = driver.manage().window().size

            driver.executeScript(
                "mobile: scrollGesture",
                mapOf(
                    "left" to size.width / 10,
                    "top" to size.height / 4,
                    "width" to size.width * 8 / 10,
                    "height" to size.height / 2,
                    "direction" to "down",
                    "percent" to 0.7
                )
            )
        }

        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                locator
            )
        )
    }

    @Test
    fun questionCanBeAddedEditedAndRemoved() {

        val newQuestion =
            "Appium test ${System.currentTimeMillis()}"

        val updatedQuestion = "$newQuestion updated"

        // 1. Перехід до списку запитань
        val questionsTab = AppiumBy.xpath(
            "//*[@text='Questions' or @text='Питання' " +
                    "or @content-desc='Questions' " +
                    "or @content-desc='Питання']"
        )

        wait.until(
            ExpectedConditions.elementToBeClickable(
                questionsTab
            )
        ).click()

        waitForList()

        // 2. Відкриття форми додавання
        val addButton = AppiumBy.xpath(
            "//*[@content-desc='Add' or " +
                    "@content-desc='Додати']"
        )

        wait.until(
            ExpectedConditions.elementToBeClickable(
                addButton
            )
        ).click()

        // 3. Введення нового запитання
        waitForEditor().sendKeys(newQuestion)

        hideKeyboardIfNeeded()

        clickButton("Add", "Додати")

        waitForList()

        // 4. Перевірка додавання
        assertTrue(
            findQuestion(newQuestion).isDisplayed,
            "Запитання не додано"
        )

        // 5. Відкриття редагування
        findQuestion(newQuestion).click()

        val editor = waitForEditor()

        // 6. Зміна тексту
        editor.clear()
        editor.sendKeys(updatedQuestion)

        hideKeyboardIfNeeded()

        clickButton("Update", "Оновити")

        waitForList()

        // 7. Перевірка редагування
        assertTrue(
            findQuestion(updatedQuestion).isDisplayed,
            "Запитання не оновлено"
        )

        // 8. Видалення запитання
        findQuestion(updatedQuestion).click()

        waitForEditor()

        clickButton("Remove", "Видалити")

        waitForList()

        // 9. Перевірка видалення
        val deletedQuestionLocator =
            AppiumBy.xpath(
                "//*[@text='$updatedQuestion']"
            )

        val stillPresent = driver
            .findElements(deletedQuestionLocator)
            .any { it.isDisplayed }

        assertFalse(
            stillPresent,
            "Запитання не видалено"
        )
    }
}

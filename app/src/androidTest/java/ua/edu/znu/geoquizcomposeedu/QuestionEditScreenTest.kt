
package ua.edu.znu.geoquizcomposeedu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import org.junit.Rule
import org.junit.Test

class QuestionEditScreenTest {

    @get:Rule
    val composeTestRule =
        createAndroidComposeRule<MainActivity>()

    private fun waitForText(text: String) {
        composeTestRule.waitUntil(timeoutMillis = 20000) {
            composeTestRule
                .onAllNodesWithText(text)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun waitForEditor() {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodes(
                    hasSetTextAction(),
                    useUnmergedTree = true
                )
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun waitForEditorGone() {
        composeTestRule.waitUntil(timeoutMillis = 20000) {
            composeTestRule
                .onAllNodes(
                    hasSetTextAction(),
                    useUnmergedTree = true
                )
                .fetchSemanticsNodes()
                .isEmpty()
        }
    }

    private fun waitForEditorWithText(text: String) {
        composeTestRule.waitUntil(timeoutMillis = 20000) {
            composeTestRule
                .onAllNodes(
                    hasSetTextAction() and hasText(text),
                    useUnmergedTree = true
                )
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Test
    fun questionCanBeAddedEditedAndRemoved() {

        val newQuestion =
            "UI test question ${System.currentTimeMillis()}"

        val updatedQuestion = "$newQuestion updated"

        val questionsButton =
            composeTestRule.activity.getString(
                R.string.questions
            )

        val addButton =
            composeTestRule.activity.getString(
                R.string.add_question
            )

        val updateButton =
            composeTestRule.activity.getString(
                R.string.update_question
            )

        val removeButton =
            composeTestRule.activity.getString(
                R.string.remove_question
            )

        // 1. Перехід до списку запитань
        composeTestRule
            .onNodeWithText(questionsButton)
            .performClick()

        // 2. Відкриття форми додавання
        composeTestRule
            .onNodeWithContentDescription(addButton)
            .performClick()

        // Очікування поля введення
        waitForEditor()

        // 3. Введення нового запитання
        composeTestRule
            .onNode(
                hasSetTextAction(),
                useUnmergedTree = true
            )
            .performTextInput(newQuestion)

        // Збереження запитання
        composeTestRule
            .onNodeWithText(addButton)
            .performClick()

        // Очікування закриття форми
        waitForEditorGone()

        // 4. Перевірка доданого запитання
        waitForText(newQuestion)

        composeTestRule
            .onNodeWithText(newQuestion)
            .assertIsDisplayed()

        // 5. Відкриття форми редагування
        composeTestRule
            .onNodeWithText(newQuestion)
            .performClick()

        // Очікування завантаження тексту
        waitForEditorWithText(newQuestion)

        // 6. Зміна тексту запитання
        composeTestRule
            .onNode(
                hasSetTextAction(),
                useUnmergedTree = true
            )
            .performTextReplacement(updatedQuestion)

        // Збереження змін
        composeTestRule
            .onNodeWithText(updateButton)
            .performClick()

        // Очікування закриття форми
        waitForEditorGone()

        // 7. Перевірка оновлення
        waitForText(updatedQuestion)

        composeTestRule
            .onNodeWithText(updatedQuestion)
            .assertIsDisplayed()

        // 8. Відкриття запитання для видалення
        composeTestRule
            .onNodeWithText(updatedQuestion)
            .performClick()

        waitForEditorWithText(updatedQuestion)

        // 9. Видалення запитання
        composeTestRule
            .onNodeWithText(removeButton)
            .performClick()

        waitForEditorGone()

        // 10. Перевірка відсутності запитання
        composeTestRule.waitUntil(timeoutMillis = 20000) {
            composeTestRule
                .onAllNodesWithText(updatedQuestion)
                .fetchSemanticsNodes()
                .isEmpty()
        }
    }
}

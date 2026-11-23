
package ua.edu.znu.geoquizcomposeedu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import ua.edu.znu.geoquizcomposeedu.util.TestTags

@RunWith(Parameterized::class)
class MainScreenCheckAnswerParameterizedTest(
    private val questionIndex: Int,
    private val expectedCorrect: Boolean
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(
            name = "Question {0}, correct answer: {1}"
        )
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf<Any>(0, true),
                arrayOf<Any>(0, false),
                arrayOf<Any>(2, true),
                arrayOf<Any>(2, false)
            )
        }
    }

    @get:Rule
    val composeTestRule =
        createAndroidComposeRule<MainActivity>()

    @Test
    fun checkIfAnswer(): Unit = with(composeTestRule) {

        val repository =
            (activity.application as GeoQuizApplication)
                .questionRepository

        // Очікування завантаження запитань
        waitUntil(timeoutMillis = 10_000) {
            repository.getQuestionBankSize() > questionIndex
        }

        val question =
            repository.getQuestionByIndex(questionIndex)

        // Перехід до потрібного запитання
        repeat(questionIndex) {
            onNodeWithContentDescription(
                TestTags.NEXT_QUESTION_BUTTON
            ).performClick()
        }

        // Перевірка відображення запитання
        waitUntil(timeoutMillis = 10_000) {
            onAllNodesWithText(question.questionText)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        onNodeWithText(question.questionText)
            .assertIsDisplayed()

        // Вибір правильної або неправильної відповіді
        val userAnswer = if (expectedCorrect) {
            question.answer
        } else {
            !question.answer
        }

        val buttonDescription = if (userAnswer) {
            TestTags.TRUE_BUTTON
        } else {
            TestTags.FALSE_BUTTON
        }

        onNodeWithContentDescription(buttonDescription)
            .performClick()

        // Очікуване повідомлення Snackbar
        val messageId = if (expectedCorrect) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        val expectedMessage = activity.getString(messageId)

        waitUntil(timeoutMillis = 10_000) {
            onAllNodesWithText(expectedMessage)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Перевірка результату
        onNodeWithText(expectedMessage)
            .assertIsDisplayed()
    }
}


package ua.edu.znu.geoquizcomposeedu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import ua.edu.znu.geoquizcomposeedu.data.QuestionRepository
import ua.edu.znu.geoquizcomposeedu.util.TestTags

class MainScreenTest {

    @get:Rule
    val composeTestRule =
        createAndroidComposeRule<MainActivity>()

    private fun repository(): QuestionRepository {
        return (composeTestRule.activity.application
                as GeoQuizApplication).questionRepository
    }

    private fun waitForQuestions(minCount: Int = 1) {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            repository().getQuestionBankSize() >= minCount
        }
    }

    @Test
    fun ifFirstQuestionDisplayed() {
        waitForQuestions()

        val firstQuestion =
            repository().getQuestionByIndex(0).questionText

        composeTestRule
            .onNodeWithText(firstQuestion)
            .assertIsDisplayed()
    }

    @Test
    fun checkIfSecondQuestionDisplayedAfterClickingNextButton() {
        waitForQuestions(2)

        val secondQuestion =
            repository().getQuestionByIndex(1).questionText

        composeTestRule
            .onNodeWithContentDescription(
                TestTags.NEXT_QUESTION_BUTTON
            )
            .performClick()

        composeTestRule
            .onNodeWithText(secondQuestion)
            .assertIsDisplayed()
    }

    @Test
    fun checkIfFirstQuestionDisplayedAfterClickingNextButtonOnLastQuestion() {
        waitForQuestions()

        val count = repository().getQuestionBankSize()

        val firstQuestion =
            repository().getQuestionByIndex(0).questionText

        repeat(count) {
            composeTestRule
                .onNodeWithContentDescription(
                    TestTags.NEXT_QUESTION_BUTTON
                )
                .performClick()
        }

        composeTestRule
            .onNodeWithText(firstQuestion)
            .assertIsDisplayed()
    }
}

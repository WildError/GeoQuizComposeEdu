
package ua.edu.znu.geoquizcomposeedu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class NavigationUITest {

    @get:Rule
    val composeTestRule =
        createAndroidComposeRule<MainActivity>()

    @Test
    fun navigateFromQuizToQuestions() {
        val questionsButton =
            composeTestRule.activity.getString(R.string.questions)

        val questionListTitle =
            composeTestRule.activity.getString(R.string.question_text)

        composeTestRule
            .onNodeWithText(questionsButton)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithText(questionListTitle)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithText(questionListTitle)
            .assertIsDisplayed()
    }

    @Test
    fun navigateFromQuestionsToQuiz() {
        val questionsButton =
            composeTestRule.activity.getString(R.string.questions)

        val quizButton =
            composeTestRule.activity.getString(R.string.home)

        val repository =
            (composeTestRule.activity.application
                    as GeoQuizApplication).questionRepository

        composeTestRule.waitUntil(timeoutMillis = 10000) {
            repository.getQuestionBankSize() > 0
        }

        val firstQuestion =
            repository.getQuestionByIndex(0).questionText

        composeTestRule
            .onNodeWithText(questionsButton)
            .performClick()

        composeTestRule
            .onNodeWithText(quizButton)
            .performClick()

        composeTestRule
            .onNodeWithText(firstQuestion)
            .assertIsDisplayed()
    }
}

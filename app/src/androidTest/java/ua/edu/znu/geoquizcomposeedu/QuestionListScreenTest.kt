
package ua.edu.znu.geoquizcomposeedu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class QuestionListScreenTest {

    @get:Rule
    val composeTestRule =
        createAndroidComposeRule<MainActivity>()

    @Test
    fun questionListScreen_displaysQuestions() {

        val repository =
            (composeTestRule.activity.application
                    as GeoQuizApplication).questionRepository

        composeTestRule.waitUntil(timeoutMillis = 10000) {
            repository.getQuestionListState().value.isNotEmpty()
        }

        val firstQuestion =
            repository.getQuestionByIndex(0).questionText

        val questionsButton =
            composeTestRule.activity.getString(R.string.questions)

        val questionListTitle =
            composeTestRule.activity.getString(R.string.question_text)

        val isTrueTitle =
            composeTestRule.activity.getString(R.string.is_true)

        composeTestRule
            .onNodeWithText(questionsButton)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithText(firstQuestion)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithText(questionListTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(isTrueTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(firstQuestion)
            .assertIsDisplayed()
    }
}

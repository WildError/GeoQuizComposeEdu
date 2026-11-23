
package ua.edu.znu.geoquizcomposeedu.viewmodel

import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.edu.znu.geoquizcomposeedu.data.Question

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: FakeQuestionRepository
    private lateinit var viewModel: QuestionViewModel

    private val initialQuestion = Question(
        id = 1,
        questionText = "Q1",
        answer = true
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = spyk(
            FakeQuestionRepository(
                listOf(initialQuestion)
            )
        )

        viewModel = QuestionViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onAddQuestionClick_addsQuestionToRepository(): Unit = runTest {
        val question = Question(
            id = 2,
            questionText = "Q2",
            answer = false
        )

        viewModel.onAddQuestionClick(question)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            repository.addQuestion(question)
        }

        assertEquals(
            2,
            repository.getQuestionBankSize()
        )
    }

    @Test
    fun onUpdateQuestionClick_updatesQuestionInRepository(): Unit = runTest {
        val updatedQuestion = Question(
            id = 1,
            questionText = "Updated Q1",
            answer = false
        )

        viewModel.onUpdateQuestionClick(updatedQuestion)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            repository.updateQuestion(updatedQuestion)
        }

        assertEquals(
            updatedQuestion,
            repository.getQuestionByIndex(0)
        )
    }

    @Test
    fun onRemoveQuestionClick_removesQuestionFromRepository(): Unit = runTest {
        viewModel.onRemoveQuestionClick(initialQuestion)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            repository.removeQuestion(initialQuestion)
        }

        assertEquals(
            0,
            repository.getQuestionBankSize()
        )
    }
}

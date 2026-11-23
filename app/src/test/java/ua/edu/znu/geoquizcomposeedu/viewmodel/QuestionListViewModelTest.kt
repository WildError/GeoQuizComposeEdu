
package ua.edu.znu.geoquizcomposeedu.viewmodel

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import ua.edu.znu.geoquizcomposeedu.data.Question
import ua.edu.znu.geoquizcomposeedu.data.QuestionRepository

class QuestionListViewModelTest {

    private lateinit var repository: QuestionRepository
    private lateinit var repositoryFlow: MutableStateFlow<List<Question>>
    private lateinit var viewModel: QuestionListViewModel

    private val initialQuestions = listOf(
        Question(id = 1, questionText = "Q1", answer = true)
    )

    @Before
    fun setUp() {
        repositoryFlow = MutableStateFlow(initialQuestions)
        repository = mockk()

        every {
            repository.getQuestionListState()
        } returns repositoryFlow

        viewModel = QuestionListViewModel(repository)
    }

    @Test
    fun questionListFlow_exposesRepositoryState() {
        assertSame(
            repositoryFlow,
            viewModel.questionListFlow
        )
    }

    @Test
    fun questionListFlow_emitsUpdates(): Unit = runTest {
        val updatedQuestions = initialQuestions + Question(
            id = 2,
            questionText = "Q2",
            answer = false
        )

        viewModel.questionListFlow.test {
            assertEquals(initialQuestions, awaitItem())

            repositoryFlow.value = updatedQuestions

            assertEquals(updatedQuestions, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}

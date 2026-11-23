
package ua.edu.znu.geoquizcomposeedu.viewmodel

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.edu.znu.geoquizcomposeedu.R
import ua.edu.znu.geoquizcomposeedu.data.Question
import ua.edu.znu.geoquizcomposeedu.data.QuestionRepository

class MainViewModelTest {

    private lateinit var repository: QuestionRepository
    private lateinit var viewModel: MainViewModel

    private val questions = listOf(
        Question(id = 1, questionText = "Q1", answer = true),
        Question(id = 2, questionText = "Q2", answer = false)
    )

    @Before
    fun setUp() {
        repository = mockk()

        every {
            repository.getQuestionByIndex(0)
        } returns questions[0]

        every {
            repository.getQuestionByIndex(1)
        } returns questions[1]

        every {
            repository.getQuestionBankSize()
        } returns questions.size

        viewModel = MainViewModel(repository)
    }

    @Test
    fun getQuestionByIndex_returnsQuestion() {
        val result = viewModel.getQuestionByIndex(1)

        assertEquals(questions[1], result)

        verify(exactly = 1) {
            repository.getQuestionByIndex(1)
        }
    }

    @Test
    fun onAnswerButtonClick_correctAnswer() {
        val result = viewModel.onAnswerButtonClick(true)

        assertEquals(R.string.correct_toast, result)
    }

    @Test
    fun onAnswerButtonClick_incorrectAnswer() {
        val result = viewModel.onAnswerButtonClick(false)

        assertEquals(R.string.incorrect_toast, result)
    }

    @Test
    fun onNextQuestionButtonClick_advancesIndex() {
        val spyRepository = spyk(
            FakeQuestionRepository(questions)
        )

        val model = MainViewModel(spyRepository)

        model.onNextQuestionButtonClick()

        assertEquals(
            1,
            model.mainScreenState.value.currentIndex
        )

        verify(exactly = 1) {
            spyRepository.getQuestionBankSize()
        }
    }

    @Test
    fun onNextQuestionButtonClick_wrapsAround() {
        val spyRepository = spyk(
            FakeQuestionRepository(questions)
        )

        val model = MainViewModel(spyRepository)

        model.onNextQuestionButtonClick()
        model.onNextQuestionButtonClick()

        assertEquals(
            0,
            model.mainScreenState.value.currentIndex
        )

        verify(exactly = 2) {
            spyRepository.getQuestionBankSize()
        }
    }
}

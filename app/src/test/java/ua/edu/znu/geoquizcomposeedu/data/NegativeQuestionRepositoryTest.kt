
package ua.edu.znu.geoquizcomposeedu.data

import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NegativeQuestionRepositoryTest : QuestionRepositoryBaseTest() {

    // Перевірка генерації виключення
    @Test(expected = IllegalArgumentException::class)
    fun updateQuestion_emptyText_throwsException(): Unit = runTest {
        val question = Question(
            id = 1,
            questionText = "",
            answer = true
        )

        questionRepository.updateQuestion(question)
    }

    // Перевірка перевищення таймауту
    @Test(timeout = 300)
    fun addQuestion_exceedsTimeout(): Unit = runBlocking {
        val question = Question(
            id = 1,
            questionText = "Test question",
            answer = true
        )

        coEvery {
            questionDao.addQuestion(question)
        } returns Unit

        questionRepository.addQuestion(question)
    }
}

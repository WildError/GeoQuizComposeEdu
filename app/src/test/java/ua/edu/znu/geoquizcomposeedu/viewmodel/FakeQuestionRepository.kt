
package ua.edu.znu.geoquizcomposeedu.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.edu.znu.geoquizcomposeedu.data.Question
import ua.edu.znu.geoquizcomposeedu.data.QuestionRepository

class FakeQuestionRepository(
    initialQuestions: List<Question> = emptyList()
) : QuestionRepository {

    private val questions = MutableStateFlow(initialQuestions)

    override fun getQuestionByIndex(index: Int): Question {
        return questions.value[index]
    }

    override fun getQuestionBankSize(): Int {
        return questions.value.size
    }

    override fun getQuestionListState(): StateFlow<List<Question>> {
        return questions
    }

    override suspend fun addQuestion(question: Question) {
        questions.value = questions.value + question
    }

    override suspend fun updateQuestion(updatedQuestion: Question) {
        questions.value = questions.value.map {
            if (it.id == updatedQuestion.id) updatedQuestion else it
        }
    }

    override suspend fun removeQuestion(question: Question) {
        questions.value = questions.value.filterNot {
            it.id == question.id
        }
    }
}

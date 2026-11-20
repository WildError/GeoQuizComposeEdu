
package ua.edu.znu.geoquizcomposeedu.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val TAG = "QuestionRepositoryImpl"

class QuestionRepositoryImpl private constructor(
    private val questionDataSource: QuestionDao
) : QuestionRepository {

    companion object {
        @Volatile
        private var instance: QuestionRepositoryImpl? = null

        private val applicationScope =
            CoroutineScope(Dispatchers.IO + SupervisorJob())

        fun getInstance(questionDao: QuestionDao): QuestionRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: QuestionRepositoryImpl(questionDao).also {
                    instance = it
                }
            }
        }
    }

    init {
        Log.d(TAG, "initialized QuestionRepositoryImpl")
    }

    override fun getQuestionByIndex(index: Int): Question {
        val list = getQuestionListState().value

        if (list.isEmpty()) {
            Log.w(
                TAG,
                "getQuestionByIndex: question list empty"
            )
            return Question(
                -1,
                "Жодного питання не було створено",
                true
            )
        }

        return list[index]
    }

    override fun getQuestionBankSize(): Int {
        return getQuestionListState().value.size
    }

    private val questionsStateFlow: StateFlow<List<Question>> =
        questionDataSource.getQuestions()
            .stateIn(
                scope = applicationScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    override fun getQuestionListState(): StateFlow<List<Question>> {
        return questionsStateFlow
    }

    // Лабораторна №1: штучна затримка
    override suspend fun addQuestion(question: Question) {
        delay(1000L)

        questionDataSource.addQuestion(question)

        Log.d(
            TAG,
            "addQuestion: inserted=$question, totalAfter=${questionsStateFlow.value.size} (may not reflect the latest state yet)"
        )
    }

    // Лабораторна №1: генерація виключення
    override suspend fun updateQuestion(updatedQuestion: Question) {
        require(updatedQuestion.questionText.isNotBlank()) {
            "Question text must not be blank"
        }

        questionDataSource.updateQuestion(updatedQuestion)

        Log.d(
            TAG,
            "updateQuestion: updated=$updatedQuestion, totalNow(before update propagates)=${questionsStateFlow.value.size}"
        )
    }

    override suspend fun removeQuestion(question: Question) {
        questionDataSource.removeQuestion(question)

        Log.d(
            TAG,
            "removeQuestion: removed=$question, totalAfter (may not reflect removal yet)=${questionsStateFlow.value.size}"
        )
    }
}

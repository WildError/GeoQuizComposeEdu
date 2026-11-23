
package ua.edu.znu.geoquizcomposeedu.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import ua.edu.znu.geoquizcomposeedu.data.Question
import ua.edu.znu.geoquizcomposeedu.viewmodel.QuestionViewModel

@Composable
fun QuestionScreenHost(
    initialQuestion: Question?,
    buttonTextRes: Int,
    questionViewModel: QuestionViewModel,
    onDone: () -> Unit,
    onRemoveDone: (() -> Unit)? = null
) {
    var textValue by rememberSaveable {
        mutableStateOf(initialQuestion?.questionText ?: "")
    }

    var isAnswerTrue by rememberSaveable {
        mutableStateOf(initialQuestion?.answer ?: false)
    }

    val scope = rememberCoroutineScope()

    val onSave: () -> Unit = {
        val question = initialQuestion?.copy(
            questionText = textValue,
            answer = isAnswerTrue
        ) ?: Question(
            questionText = textValue,
            answer = isAnswerTrue
        )

        scope.launch {
            if (initialQuestion == null) {
                questionViewModel
                    .onAddQuestionClick(question)
                    .join()
            } else {
                questionViewModel
                    .onUpdateQuestionClick(question)
                    .join()
            }

            // Повернення після завершення операції
            onDone()
        }
        Unit
    }

    val onRemove: (() -> Unit)? = initialQuestion?.let { q ->
        {
            scope.launch {
                questionViewModel
                    .onRemoveQuestionClick(q)
                    .join()

                onRemoveDone?.invoke()
            }
            Unit
        }
    }

    QuestionScreen(
        textValue = textValue,
        onTextChange = { textValue = it },
        isAnswerTrue = isAnswerTrue,
        onCheckedChange = { isAnswerTrue = it },
        buttonTextRes = buttonTextRes,
        onSave = onSave,
        onRemove = onRemove
    )
}

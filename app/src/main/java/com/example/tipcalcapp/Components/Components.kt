package com.example.tipcalcapp.Components



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    modifier: Modifier = Modifier,
    isSingleLine: Boolean = true,
    onAction: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
) {
    var hasFocus by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .background(
                color = if (hasFocus) Color(0xFFF0F0F0) else Color(0xFFFFFFFF),
                shape = MaterialTheme.shapes.medium
            )
            .border(
                width = 2.dp,
                color = if (hasFocus) MaterialTheme.colorScheme.primary else Color(0xFFB0B0B0),
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            BasicTextField(
                value = valueState.value,
                onValueChange = {
                    valueState.value = it
                    onValueChange(it)
                },
                singleLine = isSingleLine,
                keyboardActions = onAction,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                decorationBox = { innerTextField ->
                    if (valueState.value.isEmpty()) {
                        Text(
                            text = labelId,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        hasFocus = focusState.isFocused
                    }
                    .fillMaxWidth()
            )
        }
    }
}

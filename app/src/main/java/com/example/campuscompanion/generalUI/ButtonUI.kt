package com.example.campuscompanion.generalUi

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonUI(
    modifier: Modifier = Modifier,
    text: String = "Next",
    backgroundColor: Color = Color(0xFF902A1D),
    textColor: Color = Color.White,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    fontSize: Int = 14,
    shape: Int = 10,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
        containerColor = backgroundColor,
        contentColor = textColor,
        disabledContainerColor = Color(0xFFB4A6A4), // Customize this
        disabledContentColor = Color.White),
        contentPadding = PaddingValues(horizontal = 50.dp, vertical = 10.dp),
        shape = RoundedCornerShape(shape.dp),
        enabled = enabled,

    ) {
        Text(
            text = text,
            style = textStyle,
            fontSize = fontSize.sp
        )
    }
}


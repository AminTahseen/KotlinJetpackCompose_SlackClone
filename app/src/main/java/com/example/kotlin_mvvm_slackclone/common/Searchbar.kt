package com.example.kotlin_mvvm_slackclone.common


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import  com.example.kotlin_mvvm_slackclone.ui.theme.SearchBar

@Composable
fun SearchBar(placeholder:String){
    TextField(
        value = "",
        modifier = Modifier
            .padding(
                start = 10.dp,
                end = 10.dp
            ).fillMaxWidth(),
        onValueChange = {
        },
        colors= TextFieldDefaults.textFieldColors(
            backgroundColor= Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color.White ,
        ),
        singleLine = true,
        placeholder = {
            Text(placeholder,
                color = Color(0xFF7B59A8),
                fontSize = 16.sp,
                modifier = Modifier
                    . fillMaxWidth(),
            )
        }
    )
}
package com.example.campuscompanion.presentation.feature.auth.signin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.graphics.Color
import com.example.campuscompanion.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campuscompanion.generalUi.ButtonUI

@Composable
fun SignInScreen(navController: NavController, modifier: Modifier = Modifier) {

    val viewModel: SignInViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value){
        when(uiState.value) {
            is SignInState.Success -> {
                navController.navigate("main")
            }
            is SignInState.Error -> {
                // Show error
                Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 180.dp)
                .padding(horizontal = 45.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo: red box + text
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.iconlogo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "RLifeHub",
                    color = Color.White,
                    fontWeight = FontWeight.W900,
                    fontSize = 26.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Sign in to your \n Account",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))
            Row(){
                Text(
                    text = "Don't have any account? ",
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text= "Connect IT",
                    fontSize = 16.sp,
                    color = Color.Red,
                    style = TextStyle(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
            Spacer(modifier = Modifier.height(22.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                placeholder = {Text("Email")},
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(18.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it},
                placeholder = {Text("Password")},
                visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                },
            )
            Spacer(modifier = Modifier.height(18.dp))
            if(uiState.value == SignInState.Loading){
                CircularProgressIndicator()
            }else {
                ButtonUI(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Sign In",
                    fontSize = 16,
                    onClick = {
                        viewModel.signIn(email, password)
                    },
                    enabled = email.isNotEmpty() && password.isNotEmpty() && (uiState.value == SignInState.Nothing || uiState.value == SignInState.Error)
                )
            }
        }
    }
}


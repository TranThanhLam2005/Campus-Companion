package com.example.campuscompanion.onboard

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.campuscompanion.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuscompanion.generalUi.ButtonUI

@Composable
fun OnboardingGraphUI(
    onboardingModel: OnboardingModel,
    pageIndex: Int,
    lastPageIndex: Int,
    onFinished: () -> Unit,
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = onboardingModel.painter),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 700f
                    )
                )
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo: red box + text
            Row(verticalAlignment = Alignment.CenterVertically) {

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
                text = onboardingModel.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp)
            ){
                Text(
                    text = onboardingModel.description,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            ButtonUI(
                text = if (pageIndex == lastPageIndex) "Get Started" else "Next",
                shape = 50,
                fontSize = 18,
                onClick = {
                    if (pageIndex == lastPageIndex) {
                        onFinished()
                    } else {
                        onNext()
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingGraphUIPreview() {
    OnboardingGraphUI(OnboardingModel.ThirdPages, pageIndex = 3, lastPageIndex = 4, onFinished = {}, onNext = {})
}


package com.example.campuscompanion.onboard
import android.R.color.transparent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton


@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pages: List<OnboardingModel> = listOf(
        OnboardingModel.FirstPages,
        OnboardingModel.SecondPages,
        OnboardingModel.ThirdPages,
        OnboardingModel.FourthPages
        )

    val pagerState: PagerState = rememberPagerState(initialPage = 0){
        pages.size
    }
    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        topBar = {},
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Back Button or Spacer (to keep layout consistent)
                if (pagerState.currentPage != 0) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        Text("Back")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(48.dp)
                    )
                }

                // Center: Indicator inside Box with weight(1f)
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IndicatorUI(pageSize = pages.size, currentPage = pagerState.currentPage)
                }

                // Right: Spacer to balance the Back button visually
                Spacer(modifier = Modifier.width(100.dp)) // Same as left to maintain symmetry
            }
        }, content = {
        Box(modifier = Modifier.padding(it)) {
            HorizontalPager(state = pagerState,) { index ->
                OnboardingGraphUI(
                    onboardingModel = pages[index],
                    pageIndex = index,
                    lastPageIndex = pages.lastIndex,
                    onFinished = onFinished,
                    onNext = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index + 1)
                        }
                    }

                )
            }
        }
    })

}

@Preview
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(onFinished = {})
}
package com.example.campuscompanion.onboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

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
                .padding(10.dp, 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            IndicatorUI(pageSize = pages.size, currentPage = pagerState.currentPage)
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
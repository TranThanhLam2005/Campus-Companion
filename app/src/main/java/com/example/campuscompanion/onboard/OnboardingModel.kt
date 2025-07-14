package com.example.campuscompanion.onboard

import androidx.annotation.DrawableRes
import com.example.campuscompanion.R

sealed class OnboardingModel (
    @DrawableRes val painter: Int,
    val title: String,
    val description: String
) {
    data object FirstPages: OnboardingModel(
        painter = R.drawable.onboarding1,
        title = "Welcome to RMIT LifeHub",
        description = "Your Student Life Companion Clubs, Events, Study Spots, And More!"
    )
    data object SecondPages: OnboardingModel(
        painter = R.drawable.onboarding2,
        title = "Find Your People",
        description = "Discover Student Clubs, Join Events, And Connect With The Campus Community."
    )
    data object ThirdPages: OnboardingModel(
        painter = R.drawable.onboarding3,
        title = "Know Where to Go",
        description = "Check Live Study Room Availability, Maps, And Cafeteria Menus — Anytime."
    )
    data object FourthPages: OnboardingModel(
        painter = R.drawable.onboarding4,
        title = "Discuss & Discover",
        description = "Share Your Thoughts On Food And Events — See What Others Are Saying Too."
    )
}
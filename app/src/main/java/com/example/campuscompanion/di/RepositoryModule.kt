package com.example.campuscompanion.di

import com.example.campuscompanion.data.repository.CafeteriaRepositoryImpl
import com.example.campuscompanion.data.repository.ClubRepositoryImpl
import com.example.campuscompanion.data.repository.EventRepositoryImpl
import com.example.campuscompanion.data.repository.NewFeedRepositoryImpl
import com.example.campuscompanion.data.repository.OrderRepositoryImpl
import com.example.campuscompanion.data.repository.PlaceRepositoryImpl
import com.example.campuscompanion.data.repository.RoomRepositoryImpl
import com.example.campuscompanion.data.repository.UserRepositoryImpl
import com.example.campuscompanion.data.repository.CartRepositoryImpl
import com.example.campuscompanion.domain.repository.CafeteriaRepository
import com.example.campuscompanion.domain.repository.ClubRepository
import com.example.campuscompanion.domain.repository.EventRepository
import com.example.campuscompanion.domain.repository.NewFeedRepository
import com.example.campuscompanion.domain.repository.OrderRepository
import com.example.campuscompanion.domain.repository.PlaceRepository
import com.example.campuscompanion.domain.repository.RoomRepository
import com.example.campuscompanion.domain.repository.UserRepository
import com.example.campuscompanion.domain.repository.CartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindClubRepository(
        impl: ClubRepositoryImpl
    ): ClubRepository

    @Binds
    @Singleton
    abstract fun bindCafeteriaRepository(
        impl: CafeteriaRepositoryImpl
    ): CafeteriaRepository

    @Binds
    @Singleton
    abstract fun bindRoomRepository(
        impl: RoomRepositoryImpl
    ): RoomRepository

    @Binds
    @Singleton
    abstract fun bindNewFeedRepository(
        impl: NewFeedRepositoryImpl
    ): NewFeedRepository

    @Binds
    @Singleton
    abstract fun bindPlaceRepository(
        impl: PlaceRepositoryImpl
    ): PlaceRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        impl: EventRepositoryImpl
    ): EventRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        impl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        impl: CartRepositoryImpl
    ): CartRepository
}
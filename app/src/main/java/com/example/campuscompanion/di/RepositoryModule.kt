package com.example.campuscompanion.di

import com.example.campuscompanion.data.repository.CafeteriaRepositoryImpl
import com.example.campuscompanion.data.repository.ClubRepositoryImpl
import com.example.campuscompanion.data.repository.RoomRepositoryImpl
import com.example.campuscompanion.data.repository.UserRepositoryImpl
import com.example.campuscompanion.domain.repository.CafeteriaRepository
import com.example.campuscompanion.domain.repository.ClubRepository
import com.example.campuscompanion.domain.repository.RoomRepository
import com.example.campuscompanion.domain.repository.UserRepository
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

}
package com.example.campuscompanion.di

import android.content.Context
import androidx.room.Room
import com.example.campuscompanion.data.local.AppDatabase
import com.example.campuscompanion.data.local.dao.CartDao
import com.example.campuscompanion.data.local.dao.OrderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "campus_companion.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()
}

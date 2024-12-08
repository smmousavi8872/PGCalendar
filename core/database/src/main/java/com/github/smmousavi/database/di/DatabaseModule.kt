package com.github.smmousavi.database.di

import android.content.Context
import androidx.room.Room
import com.github.smmousavi.database.SampleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context,
    ): SampleDatabase = Room.databaseBuilder(
        context,
        SampleDatabase::class.java,
        "sample-database",
    )
        .fallbackToDestructiveMigration()
        .build()
}
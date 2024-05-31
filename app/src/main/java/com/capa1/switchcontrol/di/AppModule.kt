package com.capa1.switchcontrol.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesKeepSwData(
        @ApplicationContext context: Context
    ): KeepSwData
    {
        return KeepSwData(context)
    }
}

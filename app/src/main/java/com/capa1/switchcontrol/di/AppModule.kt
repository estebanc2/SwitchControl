package com.capa1.switchcontrol.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.capa1.switchcontrol.data.Controller
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
    fun providesController(
        @ApplicationContext context: Context
    ): Controller
    {
        return Controller(context)
    }

}

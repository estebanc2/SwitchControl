package com.capa1.switchcontrol.di

import android.content.Context
import com.capa1.switchcontrol.data.SwDataStore
import com.capa1.switchcontrol.data.mqtt.MqttListener
import com.capa1.switchcontrol.data.mqtt.MqttManager
import com.capa1.switchcontrol.data.wifi.EspTouch
import com.capa1.switchcontrol.data.wifi.WifiCredentials
import com.capa1.switchcontrol.data.wifi.WifiListener
import com.capa1.switchcontrol.ui.SwViewModel
import dagger.Binds
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
    fun providesSwdDtaStore (
        @ApplicationContext context: Context
    ): SwDataStore {
        return SwDataStore(context)
    }

    @Provides
    @Singleton
    fun providesMqttManager (
    ): MqttManager {
        return MqttManager()
    }

    @Provides
    @Singleton
    fun providesEspTouch (
        @ApplicationContext context: Context
    ): EspTouch {
        return EspTouch(context)
    }

    @Provides
    @Singleton
    fun providesWifiCredentials (
        @ApplicationContext context: Context
    ): WifiCredentials {
        return WifiCredentials(context)
    }
}

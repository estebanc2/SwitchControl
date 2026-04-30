package com.capa1.switchcontrol.ui;

import com.capa1.switchcontrol.data.LegendMaker;
import com.capa1.switchcontrol.data.SwDataStore;
import com.capa1.switchcontrol.data.mqtt.MqttManager;
import com.capa1.switchcontrol.data.wifi.EspTouch;
import com.capa1.switchcontrol.data.wifi.WifiCredentials;
import com.google.gson.Gson;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class SwViewModel_Factory implements Factory<SwViewModel> {
  private final Provider<MqttManager> mqttManagerProvider;

  private final Provider<WifiCredentials> wifiCredentialsProvider;

  private final Provider<SwDataStore> swDataStoreProvider;

  private final Provider<EspTouch> espTouchProvider;

  private final Provider<LegendMaker> legendMakerProvider;

  private final Provider<Gson> gsonProvider;

  private SwViewModel_Factory(Provider<MqttManager> mqttManagerProvider,
      Provider<WifiCredentials> wifiCredentialsProvider, Provider<SwDataStore> swDataStoreProvider,
      Provider<EspTouch> espTouchProvider, Provider<LegendMaker> legendMakerProvider,
      Provider<Gson> gsonProvider) {
    this.mqttManagerProvider = mqttManagerProvider;
    this.wifiCredentialsProvider = wifiCredentialsProvider;
    this.swDataStoreProvider = swDataStoreProvider;
    this.espTouchProvider = espTouchProvider;
    this.legendMakerProvider = legendMakerProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public SwViewModel get() {
    return newInstance(mqttManagerProvider.get(), wifiCredentialsProvider.get(), swDataStoreProvider.get(), espTouchProvider.get(), legendMakerProvider.get(), gsonProvider.get());
  }

  public static SwViewModel_Factory create(Provider<MqttManager> mqttManagerProvider,
      Provider<WifiCredentials> wifiCredentialsProvider, Provider<SwDataStore> swDataStoreProvider,
      Provider<EspTouch> espTouchProvider, Provider<LegendMaker> legendMakerProvider,
      Provider<Gson> gsonProvider) {
    return new SwViewModel_Factory(mqttManagerProvider, wifiCredentialsProvider, swDataStoreProvider, espTouchProvider, legendMakerProvider, gsonProvider);
  }

  public static SwViewModel newInstance(MqttManager mqttManager, WifiCredentials wifiCredentials,
      SwDataStore swDataStore, EspTouch espTouch, LegendMaker legendMaker, Gson gson) {
    return new SwViewModel(mqttManager, wifiCredentials, swDataStore, espTouch, legendMaker, gson);
  }
}

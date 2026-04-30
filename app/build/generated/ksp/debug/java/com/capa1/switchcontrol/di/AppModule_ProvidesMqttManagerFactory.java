package com.capa1.switchcontrol.di;

import com.capa1.switchcontrol.data.mqtt.MqttManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvidesMqttManagerFactory implements Factory<MqttManager> {
  @Override
  public MqttManager get() {
    return providesMqttManager();
  }

  public static AppModule_ProvidesMqttManagerFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MqttManager providesMqttManager() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providesMqttManager());
  }

  private static final class InstanceHolder {
    static final AppModule_ProvidesMqttManagerFactory INSTANCE = new AppModule_ProvidesMqttManagerFactory();
  }
}

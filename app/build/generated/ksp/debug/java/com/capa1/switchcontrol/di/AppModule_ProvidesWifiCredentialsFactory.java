package com.capa1.switchcontrol.di;

import android.content.Context;
import com.capa1.switchcontrol.data.wifi.WifiCredentials;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvidesWifiCredentialsFactory implements Factory<WifiCredentials> {
  private final Provider<Context> contextProvider;

  private AppModule_ProvidesWifiCredentialsFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public WifiCredentials get() {
    return providesWifiCredentials(contextProvider.get());
  }

  public static AppModule_ProvidesWifiCredentialsFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvidesWifiCredentialsFactory(contextProvider);
  }

  public static WifiCredentials providesWifiCredentials(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providesWifiCredentials(context));
  }
}

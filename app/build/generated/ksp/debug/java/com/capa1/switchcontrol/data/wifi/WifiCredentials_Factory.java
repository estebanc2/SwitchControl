package com.capa1.switchcontrol.data.wifi;

import android.content.Context;
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
public final class WifiCredentials_Factory implements Factory<WifiCredentials> {
  private final Provider<Context> contextProvider;

  private WifiCredentials_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public WifiCredentials get() {
    return newInstance(contextProvider.get());
  }

  public static WifiCredentials_Factory create(Provider<Context> contextProvider) {
    return new WifiCredentials_Factory(contextProvider);
  }

  public static WifiCredentials newInstance(Context context) {
    return new WifiCredentials(context);
  }
}

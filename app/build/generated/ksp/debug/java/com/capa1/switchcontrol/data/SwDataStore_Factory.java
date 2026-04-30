package com.capa1.switchcontrol.data;

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
public final class SwDataStore_Factory implements Factory<SwDataStore> {
  private final Provider<Context> contextProvider;

  private SwDataStore_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SwDataStore get() {
    return newInstance(contextProvider.get());
  }

  public static SwDataStore_Factory create(Provider<Context> contextProvider) {
    return new SwDataStore_Factory(contextProvider);
  }

  public static SwDataStore newInstance(Context context) {
    return new SwDataStore(context);
  }
}

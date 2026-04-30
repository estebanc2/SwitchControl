package com.capa1.switchcontrol.di;

import com.google.gson.Gson;
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
public final class AppModule_ProvidesGsonFactory implements Factory<Gson> {
  @Override
  public Gson get() {
    return providesGson();
  }

  public static AppModule_ProvidesGsonFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Gson providesGson() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providesGson());
  }

  private static final class InstanceHolder {
    static final AppModule_ProvidesGsonFactory INSTANCE = new AppModule_ProvidesGsonFactory();
  }
}

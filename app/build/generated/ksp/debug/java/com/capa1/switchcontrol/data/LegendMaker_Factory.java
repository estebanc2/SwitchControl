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
public final class LegendMaker_Factory implements Factory<LegendMaker> {
  private final Provider<Context> contextProvider;

  private LegendMaker_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LegendMaker get() {
    return newInstance(contextProvider.get());
  }

  public static LegendMaker_Factory create(Provider<Context> contextProvider) {
    return new LegendMaker_Factory(contextProvider);
  }

  public static LegendMaker newInstance(Context context) {
    return new LegendMaker(context);
  }
}

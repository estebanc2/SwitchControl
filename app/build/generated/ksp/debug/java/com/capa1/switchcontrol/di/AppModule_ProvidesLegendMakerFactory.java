package com.capa1.switchcontrol.di;

import android.content.Context;
import com.capa1.switchcontrol.data.LegendMaker;
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
public final class AppModule_ProvidesLegendMakerFactory implements Factory<LegendMaker> {
  private final Provider<Context> contextProvider;

  private AppModule_ProvidesLegendMakerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LegendMaker get() {
    return providesLegendMaker(contextProvider.get());
  }

  public static AppModule_ProvidesLegendMakerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvidesLegendMakerFactory(contextProvider);
  }

  public static LegendMaker providesLegendMaker(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providesLegendMaker(context));
  }
}

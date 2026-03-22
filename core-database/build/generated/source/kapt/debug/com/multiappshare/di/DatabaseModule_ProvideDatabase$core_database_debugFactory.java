package com.multiappshare.di;

import android.content.Context;
import com.multiappshare.data.local.AppDatabase;
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
public final class DatabaseModule_ProvideDatabase$core_database_debugFactory implements Factory<AppDatabase> {
  private final Provider<Context> contextProvider;

  private DatabaseModule_ProvideDatabase$core_database_debugFactory(
      Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AppDatabase get() {
    return provideDatabase$core_database_debug(contextProvider.get());
  }

  public static DatabaseModule_ProvideDatabase$core_database_debugFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideDatabase$core_database_debugFactory(contextProvider);
  }

  public static AppDatabase provideDatabase$core_database_debug(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDatabase$core_database_debug(context));
  }
}

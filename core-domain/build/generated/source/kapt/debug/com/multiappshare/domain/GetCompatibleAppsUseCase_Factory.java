package com.multiappshare.domain;

import android.content.pm.PackageManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
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
public final class GetCompatibleAppsUseCase_Factory implements Factory<GetCompatibleAppsUseCase> {
  private final Provider<PackageManager> packageManagerProvider;

  private GetCompatibleAppsUseCase_Factory(Provider<PackageManager> packageManagerProvider) {
    this.packageManagerProvider = packageManagerProvider;
  }

  @Override
  public GetCompatibleAppsUseCase get() {
    return newInstance(packageManagerProvider.get());
  }

  public static GetCompatibleAppsUseCase_Factory create(
      Provider<PackageManager> packageManagerProvider) {
    return new GetCompatibleAppsUseCase_Factory(packageManagerProvider);
  }

  public static GetCompatibleAppsUseCase newInstance(PackageManager packageManager) {
    return new GetCompatibleAppsUseCase(packageManager);
  }
}

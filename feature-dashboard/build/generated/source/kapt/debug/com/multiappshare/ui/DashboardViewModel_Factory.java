package com.multiappshare.ui;

import com.multiappshare.domain.CreateAutoGroupsUseCase;
import com.multiappshare.domain.GetCompatibleAppsUseCase;
import com.multiappshare.domain.GroupsRepository;
import com.multiappshare.domain.HistoryRepository;
import com.multiappshare.domain.SettingsRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<GroupsRepository> groupsRepositoryProvider;

  private final Provider<HistoryRepository> historyRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<CreateAutoGroupsUseCase> createAutoGroupsUseCaseProvider;

  private final Provider<GetCompatibleAppsUseCase> getCompatibleAppsUseCaseProvider;

  private DashboardViewModel_Factory(Provider<GroupsRepository> groupsRepositoryProvider,
      Provider<HistoryRepository> historyRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<CreateAutoGroupsUseCase> createAutoGroupsUseCaseProvider,
      Provider<GetCompatibleAppsUseCase> getCompatibleAppsUseCaseProvider) {
    this.groupsRepositoryProvider = groupsRepositoryProvider;
    this.historyRepositoryProvider = historyRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.createAutoGroupsUseCaseProvider = createAutoGroupsUseCaseProvider;
    this.getCompatibleAppsUseCaseProvider = getCompatibleAppsUseCaseProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(groupsRepositoryProvider.get(), historyRepositoryProvider.get(), settingsRepositoryProvider.get(), createAutoGroupsUseCaseProvider.get(), getCompatibleAppsUseCaseProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<GroupsRepository> groupsRepositoryProvider,
      Provider<HistoryRepository> historyRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<CreateAutoGroupsUseCase> createAutoGroupsUseCaseProvider,
      Provider<GetCompatibleAppsUseCase> getCompatibleAppsUseCaseProvider) {
    return new DashboardViewModel_Factory(groupsRepositoryProvider, historyRepositoryProvider, settingsRepositoryProvider, createAutoGroupsUseCaseProvider, getCompatibleAppsUseCaseProvider);
  }

  public static DashboardViewModel newInstance(GroupsRepository groupsRepository,
      HistoryRepository historyRepository, SettingsRepository settingsRepository,
      CreateAutoGroupsUseCase createAutoGroupsUseCase,
      GetCompatibleAppsUseCase getCompatibleAppsUseCase) {
    return new DashboardViewModel(groupsRepository, historyRepository, settingsRepository, createAutoGroupsUseCase, getCompatibleAppsUseCase);
  }
}

package com.multiappshare.domain;

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
public final class CreateAutoGroupsUseCase_Factory implements Factory<CreateAutoGroupsUseCase> {
  private final Provider<GroupsRepository> groupsRepositoryProvider;

  private CreateAutoGroupsUseCase_Factory(Provider<GroupsRepository> groupsRepositoryProvider) {
    this.groupsRepositoryProvider = groupsRepositoryProvider;
  }

  @Override
  public CreateAutoGroupsUseCase get() {
    return newInstance(groupsRepositoryProvider.get());
  }

  public static CreateAutoGroupsUseCase_Factory create(
      Provider<GroupsRepository> groupsRepositoryProvider) {
    return new CreateAutoGroupsUseCase_Factory(groupsRepositoryProvider);
  }

  public static CreateAutoGroupsUseCase newInstance(GroupsRepository groupsRepository) {
    return new CreateAutoGroupsUseCase(groupsRepository);
  }
}

package com.multiappshare.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J6\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\u0006\u0010\n\u001a\u00020\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\rH\u0086B\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/multiappshare/domain/CreateAutoGroupsUseCase;", "", "groupsRepository", "Lcom/multiappshare/domain/GroupsRepository;", "(Lcom/multiappshare/domain/GroupsRepository;)V", "invoke", "", "Lcom/multiappshare/model/AppGroup;", "allApps", "Lcom/multiappshare/model/AppInfo;", "append", "", "singleCategoryOnly", "", "(Ljava/util/List;ZLjava/lang/Integer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "core-domain_debug"})
public final class CreateAutoGroupsUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.multiappshare.domain.GroupsRepository groupsRepository = null;
    
    @javax.inject.Inject()
    public CreateAutoGroupsUseCase(@org.jetbrains.annotations.NotNull()
    com.multiappshare.domain.GroupsRepository groupsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object invoke(@org.jetbrains.annotations.NotNull()
    java.util.List<com.multiappshare.model.AppInfo> allApps, boolean append, @org.jetbrains.annotations.Nullable()
    java.lang.Integer singleCategoryOnly, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.multiappshare.model.AppGroup>> $completion) {
        return null;
    }
}
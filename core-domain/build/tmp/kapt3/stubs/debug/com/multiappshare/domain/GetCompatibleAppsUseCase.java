package com.multiappshare.domain;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\n\u001a\u00020\u000bJ\u001f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\t2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0086\u0002R,\u0010\u0005\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/multiappshare/domain/GetCompatibleAppsUseCase;", "", "packageManager", "Landroid/content/pm/PackageManager;", "(Landroid/content/pm/PackageManager;)V", "compatiblePackagesCache", "", "Lkotlin/Pair;", "", "", "clearCache", "", "invoke", "action", "mime", "core-domain_debug"})
public final class GetCompatibleAppsUseCase {
    @org.jetbrains.annotations.NotNull()
    private final android.content.pm.PackageManager packageManager = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<kotlin.Pair<java.lang.String, java.lang.String>, java.util.Set<java.lang.String>> compatiblePackagesCache = null;
    
    @javax.inject.Inject()
    public GetCompatibleAppsUseCase(@org.jetbrains.annotations.NotNull()
    android.content.pm.PackageManager packageManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> invoke(@org.jetbrains.annotations.NotNull()
    java.lang.String action, @org.jetbrains.annotations.NotNull()
    java.lang.String mime) {
        return null;
    }
    
    public final void clearCache() {
    }
}
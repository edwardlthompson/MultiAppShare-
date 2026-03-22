package com.multiappshare.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u001c\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/multiappshare/data/local/HistoryDao;", "", "getAllHistory", "", "Lcom/multiappshare/model/HistoryItem;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertHistory", "", "history", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertHistoryItem", "item", "(Lcom/multiappshare/model/HistoryItem;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "core-database_release"})
@androidx.room.Dao()
public abstract interface HistoryDao {
    
    @androidx.room.Query(value = "SELECT * FROM history ORDER BY timestamp DESC LIMIT 50")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllHistory(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.multiappshare.model.HistoryItem>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertHistory(@org.jetbrains.annotations.NotNull()
    java.util.List<com.multiappshare.model.HistoryItem> history, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertHistoryItem(@org.jetbrains.annotations.NotNull()
    com.multiappshare.model.HistoryItem item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
package com.multiappshare.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.multiappshare.model.AppGroup;
import com.multiappshare.model.AppInfo;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GroupDao_Impl implements GroupDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppGroup> __insertionAdapterOfAppGroup;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<AppGroup> __deletionAdapterOfAppGroup;

  public GroupDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppGroup = new EntityInsertionAdapter<AppGroup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `groups` (`name`,`apps`,`isExpanded`,`usageCount`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppGroup entity) {
        if (entity.getName() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getName());
        }
        final String _tmp = __converters.fromAppInfoList(entity.getApps());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final int _tmp_1 = entity.isExpanded() ? 1 : 0;
        statement.bindLong(3, _tmp_1);
        statement.bindLong(4, entity.getUsageCount());
      }
    };
    this.__deletionAdapterOfAppGroup = new EntityDeletionOrUpdateAdapter<AppGroup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `groups` WHERE `name` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppGroup entity) {
        if (entity.getName() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getName());
        }
      }
    };
  }

  @Override
  public Object insertGroups(final List<AppGroup> groups,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppGroup.insert(groups);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertGroup(final AppGroup group, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppGroup.insert(group);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteGroup(final AppGroup group, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAppGroup.handle(group);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllGroups(final Continuation<? super List<AppGroup>> $completion) {
    final String _sql = "SELECT * FROM groups";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AppGroup>>() {
      @Override
      @NonNull
      public List<AppGroup> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfApps = CursorUtil.getColumnIndexOrThrow(_cursor, "apps");
          final int _cursorIndexOfIsExpanded = CursorUtil.getColumnIndexOrThrow(_cursor, "isExpanded");
          final int _cursorIndexOfUsageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "usageCount");
          final List<AppGroup> _result = new ArrayList<AppGroup>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppGroup _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final List<AppInfo> _tmpApps;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfApps)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfApps);
            }
            _tmpApps = __converters.toAppInfoList(_tmp);
            final boolean _tmpIsExpanded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsExpanded);
            _tmpIsExpanded = _tmp_1 != 0;
            final int _tmpUsageCount;
            _tmpUsageCount = _cursor.getInt(_cursorIndexOfUsageCount);
            _item = new AppGroup(_tmpName,_tmpApps,_tmpIsExpanded,_tmpUsageCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

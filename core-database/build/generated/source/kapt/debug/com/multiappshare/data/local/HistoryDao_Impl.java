package com.multiappshare.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.multiappshare.model.HistoryItem;
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
public final class HistoryDao_Impl implements HistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HistoryItem> __insertionAdapterOfHistoryItem;

  public HistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHistoryItem = new EntityInsertionAdapter<HistoryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `history` (`id`,`timestamp`,`groupName`,`contentDescription`,`status`,`isError`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoryItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        if (entity.getGroupName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getGroupName());
        }
        if (entity.getContentDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getContentDescription());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        final int _tmp = entity.isError() ? 1 : 0;
        statement.bindLong(6, _tmp);
      }
    };
  }

  @Override
  public Object insertHistory(final List<HistoryItem> history,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHistoryItem.insert(history);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertHistoryItem(final HistoryItem item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHistoryItem.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllHistory(final Continuation<? super List<HistoryItem>> $completion) {
    final String _sql = "SELECT * FROM history ORDER BY timestamp DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HistoryItem>>() {
      @Override
      @NonNull
      public List<HistoryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfContentDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "contentDescription");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfIsError = CursorUtil.getColumnIndexOrThrow(_cursor, "isError");
          final List<HistoryItem> _result = new ArrayList<HistoryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoryItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpGroupName;
            if (_cursor.isNull(_cursorIndexOfGroupName)) {
              _tmpGroupName = null;
            } else {
              _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            }
            final String _tmpContentDescription;
            if (_cursor.isNull(_cursorIndexOfContentDescription)) {
              _tmpContentDescription = null;
            } else {
              _tmpContentDescription = _cursor.getString(_cursorIndexOfContentDescription);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final boolean _tmpIsError;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsError);
            _tmpIsError = _tmp != 0;
            _item = new HistoryItem(_tmpId,_tmpTimestamp,_tmpGroupName,_tmpContentDescription,_tmpStatus,_tmpIsError);
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

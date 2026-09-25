package com.pos.pik.data.local;

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
public final class RoleDao_Impl implements RoleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RoleEntity> __insertionAdapterOfRoleEntity;

  public RoleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRoleEntity = new EntityInsertionAdapter<RoleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `roles` (`rol_id`,`rol_name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RoleEntity entity) {
        statement.bindLong(1, entity.getRolId());
        statement.bindString(2, entity.getRolName());
      }
    };
  }

  @Override
  public Object insertAll(final List<RoleEntity> roles,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRoleEntity.insert(roles);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllRoles(final Continuation<? super List<RoleEntity>> $completion) {
    final String _sql = "SELECT * FROM roles";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RoleEntity>>() {
      @Override
      @NonNull
      public List<RoleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRolId = CursorUtil.getColumnIndexOrThrow(_cursor, "rol_id");
          final int _cursorIndexOfRolName = CursorUtil.getColumnIndexOrThrow(_cursor, "rol_name");
          final List<RoleEntity> _result = new ArrayList<RoleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RoleEntity _item;
            final int _tmpRolId;
            _tmpRolId = _cursor.getInt(_cursorIndexOfRolId);
            final String _tmpRolName;
            _tmpRolName = _cursor.getString(_cursorIndexOfRolName);
            _item = new RoleEntity(_tmpRolId,_tmpRolName);
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

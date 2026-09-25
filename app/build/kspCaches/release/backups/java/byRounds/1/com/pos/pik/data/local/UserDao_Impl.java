package com.pos.pik.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserEntity> __insertionAdapterOfUserEntity;

  private final EntityDeletionOrUpdateAdapter<UserEntity> __updateAdapterOfUserEntity;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserEntity = new EntityInsertionAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`usr_id`,`usr_role_id`,`usr_name`,`usr_username`,`usr_password`,`usr_phone`,`usr_is_active`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindLong(1, entity.getUsrId());
        statement.bindLong(2, entity.getUsrRoleId());
        statement.bindString(3, entity.getUsrName());
        statement.bindString(4, entity.getUsrUsername());
        statement.bindString(5, entity.getUsrPassword());
        if (entity.getUsrPhone() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUsrPhone());
        }
        statement.bindLong(7, entity.getUsrIsActive());
      }
    };
    this.__updateAdapterOfUserEntity = new EntityDeletionOrUpdateAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `users` SET `usr_id` = ?,`usr_role_id` = ?,`usr_name` = ?,`usr_username` = ?,`usr_password` = ?,`usr_phone` = ?,`usr_is_active` = ? WHERE `usr_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindLong(1, entity.getUsrId());
        statement.bindLong(2, entity.getUsrRoleId());
        statement.bindString(3, entity.getUsrName());
        statement.bindString(4, entity.getUsrUsername());
        statement.bindString(5, entity.getUsrPassword());
        if (entity.getUsrPhone() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUsrPhone());
        }
        statement.bindLong(7, entity.getUsrIsActive());
        statement.bindLong(8, entity.getUsrId());
      }
    };
  }

  @Override
  public Object insertUser(final UserEntity user, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfUserEntity.insertAndReturnId(user);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<UserEntity> users,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserEntity.insert(users);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateUser(final UserEntity user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserEntity.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<UserWithRole>> getAllUsers() {
    final String _sql = "SELECT u.*, r.rol_name FROM users u JOIN roles r ON u.usr_role_id = r.rol_id ORDER BY u.usr_name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users",
        "roles"}, new Callable<List<UserWithRole>>() {
      @Override
      @NonNull
      public List<UserWithRole> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsrId = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_id");
          final int _cursorIndexOfUsrRoleId = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_role_id");
          final int _cursorIndexOfUsrName = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_name");
          final int _cursorIndexOfUsrUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_username");
          final int _cursorIndexOfUsrPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_password");
          final int _cursorIndexOfUsrPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_phone");
          final int _cursorIndexOfUsrIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_is_active");
          final int _cursorIndexOfRolName = CursorUtil.getColumnIndexOrThrow(_cursor, "rol_name");
          final List<UserWithRole> _result = new ArrayList<UserWithRole>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserWithRole _item;
            final int _tmpUsrId;
            _tmpUsrId = _cursor.getInt(_cursorIndexOfUsrId);
            final int _tmpUsrRoleId;
            _tmpUsrRoleId = _cursor.getInt(_cursorIndexOfUsrRoleId);
            final String _tmpUsrName;
            _tmpUsrName = _cursor.getString(_cursorIndexOfUsrName);
            final String _tmpUsrUsername;
            _tmpUsrUsername = _cursor.getString(_cursorIndexOfUsrUsername);
            final String _tmpUsrPassword;
            _tmpUsrPassword = _cursor.getString(_cursorIndexOfUsrPassword);
            final String _tmpUsrPhone;
            if (_cursor.isNull(_cursorIndexOfUsrPhone)) {
              _tmpUsrPhone = null;
            } else {
              _tmpUsrPhone = _cursor.getString(_cursorIndexOfUsrPhone);
            }
            final int _tmpUsrIsActive;
            _tmpUsrIsActive = _cursor.getInt(_cursorIndexOfUsrIsActive);
            final String _tmpRolName;
            _tmpRolName = _cursor.getString(_cursorIndexOfRolName);
            _item = new UserWithRole(_tmpUsrId,_tmpUsrRoleId,_tmpUsrName,_tmpUsrUsername,_tmpUsrPassword,_tmpUsrPhone,_tmpUsrIsActive,_tmpRolName);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object login(final String username, final String password,
      final Continuation<? super UserWithRole> $completion) {
    final String _sql = "SELECT u.*, r.rol_name FROM users u JOIN roles r ON u.usr_role_id = r.rol_id WHERE u.usr_username = ? AND u.usr_password = ? AND u.usr_is_active = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, username);
    _argIndex = 2;
    _statement.bindString(_argIndex, password);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserWithRole>() {
      @Override
      @Nullable
      public UserWithRole call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsrId = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_id");
          final int _cursorIndexOfUsrRoleId = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_role_id");
          final int _cursorIndexOfUsrName = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_name");
          final int _cursorIndexOfUsrUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_username");
          final int _cursorIndexOfUsrPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_password");
          final int _cursorIndexOfUsrPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_phone");
          final int _cursorIndexOfUsrIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_is_active");
          final int _cursorIndexOfRolName = CursorUtil.getColumnIndexOrThrow(_cursor, "rol_name");
          final UserWithRole _result;
          if (_cursor.moveToFirst()) {
            final int _tmpUsrId;
            _tmpUsrId = _cursor.getInt(_cursorIndexOfUsrId);
            final int _tmpUsrRoleId;
            _tmpUsrRoleId = _cursor.getInt(_cursorIndexOfUsrRoleId);
            final String _tmpUsrName;
            _tmpUsrName = _cursor.getString(_cursorIndexOfUsrName);
            final String _tmpUsrUsername;
            _tmpUsrUsername = _cursor.getString(_cursorIndexOfUsrUsername);
            final String _tmpUsrPassword;
            _tmpUsrPassword = _cursor.getString(_cursorIndexOfUsrPassword);
            final String _tmpUsrPhone;
            if (_cursor.isNull(_cursorIndexOfUsrPhone)) {
              _tmpUsrPhone = null;
            } else {
              _tmpUsrPhone = _cursor.getString(_cursorIndexOfUsrPhone);
            }
            final int _tmpUsrIsActive;
            _tmpUsrIsActive = _cursor.getInt(_cursorIndexOfUsrIsActive);
            final String _tmpRolName;
            _tmpRolName = _cursor.getString(_cursorIndexOfRolName);
            _result = new UserWithRole(_tmpUsrId,_tmpUsrRoleId,_tmpUsrName,_tmpUsrUsername,_tmpUsrPassword,_tmpUsrPhone,_tmpUsrIsActive,_tmpRolName);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object verifyAdminPassword(final String password,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM users u \n"
            + "        JOIN roles r ON u.usr_role_id = r.rol_id \n"
            + "        WHERE LOWER(r.rol_name) = 'admin' AND u.usr_password = ? AND u.usr_is_active = 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, password);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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

package com.pos.pik.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppSettingDao_Impl implements AppSettingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppSettingEntity> __insertionAdapterOfAppSettingEntity;

  public AppSettingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppSettingEntity = new EntityInsertionAdapter<AppSettingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `app_settings` (`set_id`,`set_warung_name`,`set_address`,`set_phone`,`set_default_printer`,`set_paper_size`,`set_margin`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppSettingEntity entity) {
        statement.bindLong(1, entity.getSetId());
        statement.bindString(2, entity.getSetWarungName());
        statement.bindString(3, entity.getSetAddress());
        statement.bindString(4, entity.getSetPhone());
        if (entity.getSetDefaultPrinter() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSetDefaultPrinter());
        }
        statement.bindLong(6, entity.getSetPaperSize());
        statement.bindDouble(7, entity.getSetMargin());
      }
    };
  }

  @Override
  public Object updateSettings(final AppSettingEntity setting,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppSettingEntity.insert(setting);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<AppSettingEntity> getSettings() {
    final String _sql = "SELECT * FROM app_settings WHERE set_id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_settings"}, new Callable<AppSettingEntity>() {
      @Override
      @Nullable
      public AppSettingEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSetId = CursorUtil.getColumnIndexOrThrow(_cursor, "set_id");
          final int _cursorIndexOfSetWarungName = CursorUtil.getColumnIndexOrThrow(_cursor, "set_warung_name");
          final int _cursorIndexOfSetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "set_address");
          final int _cursorIndexOfSetPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "set_phone");
          final int _cursorIndexOfSetDefaultPrinter = CursorUtil.getColumnIndexOrThrow(_cursor, "set_default_printer");
          final int _cursorIndexOfSetPaperSize = CursorUtil.getColumnIndexOrThrow(_cursor, "set_paper_size");
          final int _cursorIndexOfSetMargin = CursorUtil.getColumnIndexOrThrow(_cursor, "set_margin");
          final AppSettingEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpSetId;
            _tmpSetId = _cursor.getInt(_cursorIndexOfSetId);
            final String _tmpSetWarungName;
            _tmpSetWarungName = _cursor.getString(_cursorIndexOfSetWarungName);
            final String _tmpSetAddress;
            _tmpSetAddress = _cursor.getString(_cursorIndexOfSetAddress);
            final String _tmpSetPhone;
            _tmpSetPhone = _cursor.getString(_cursorIndexOfSetPhone);
            final String _tmpSetDefaultPrinter;
            if (_cursor.isNull(_cursorIndexOfSetDefaultPrinter)) {
              _tmpSetDefaultPrinter = null;
            } else {
              _tmpSetDefaultPrinter = _cursor.getString(_cursorIndexOfSetDefaultPrinter);
            }
            final int _tmpSetPaperSize;
            _tmpSetPaperSize = _cursor.getInt(_cursorIndexOfSetPaperSize);
            final double _tmpSetMargin;
            _tmpSetMargin = _cursor.getDouble(_cursorIndexOfSetMargin);
            _result = new AppSettingEntity(_tmpSetId,_tmpSetWarungName,_tmpSetAddress,_tmpSetPhone,_tmpSetDefaultPrinter,_tmpSetPaperSize,_tmpSetMargin);
          } else {
            _result = null;
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
  public Object getSettingsSync(final Continuation<? super AppSettingEntity> $completion) {
    final String _sql = "SELECT * FROM app_settings WHERE set_id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AppSettingEntity>() {
      @Override
      @Nullable
      public AppSettingEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSetId = CursorUtil.getColumnIndexOrThrow(_cursor, "set_id");
          final int _cursorIndexOfSetWarungName = CursorUtil.getColumnIndexOrThrow(_cursor, "set_warung_name");
          final int _cursorIndexOfSetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "set_address");
          final int _cursorIndexOfSetPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "set_phone");
          final int _cursorIndexOfSetDefaultPrinter = CursorUtil.getColumnIndexOrThrow(_cursor, "set_default_printer");
          final int _cursorIndexOfSetPaperSize = CursorUtil.getColumnIndexOrThrow(_cursor, "set_paper_size");
          final int _cursorIndexOfSetMargin = CursorUtil.getColumnIndexOrThrow(_cursor, "set_margin");
          final AppSettingEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpSetId;
            _tmpSetId = _cursor.getInt(_cursorIndexOfSetId);
            final String _tmpSetWarungName;
            _tmpSetWarungName = _cursor.getString(_cursorIndexOfSetWarungName);
            final String _tmpSetAddress;
            _tmpSetAddress = _cursor.getString(_cursorIndexOfSetAddress);
            final String _tmpSetPhone;
            _tmpSetPhone = _cursor.getString(_cursorIndexOfSetPhone);
            final String _tmpSetDefaultPrinter;
            if (_cursor.isNull(_cursorIndexOfSetDefaultPrinter)) {
              _tmpSetDefaultPrinter = null;
            } else {
              _tmpSetDefaultPrinter = _cursor.getString(_cursorIndexOfSetDefaultPrinter);
            }
            final int _tmpSetPaperSize;
            _tmpSetPaperSize = _cursor.getInt(_cursorIndexOfSetPaperSize);
            final double _tmpSetMargin;
            _tmpSetMargin = _cursor.getDouble(_cursorIndexOfSetMargin);
            _result = new AppSettingEntity(_tmpSetId,_tmpSetWarungName,_tmpSetAddress,_tmpSetPhone,_tmpSetDefaultPrinter,_tmpSetPaperSize,_tmpSetMargin);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

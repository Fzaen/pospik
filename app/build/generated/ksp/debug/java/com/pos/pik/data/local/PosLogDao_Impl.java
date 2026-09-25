package com.pos.pik.data.local;

import android.database.Cursor;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PosLogDao_Impl implements PosLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PosLogEntity> __insertionAdapterOfPosLogEntity;

  public PosLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPosLogEntity = new EntityInsertionAdapter<PosLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pos_logs` (`log_id`,`log_user_id`,`log_prd_sku`,`log_action`,`log_old_qty`,`log_new_qty`,`log_description`,`log_timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PosLogEntity entity) {
        statement.bindLong(1, entity.getLogId());
        statement.bindLong(2, entity.getLogUserId());
        statement.bindString(3, entity.getLogPrdSku());
        statement.bindString(4, entity.getLogAction());
        statement.bindLong(5, entity.getLogOldQty());
        statement.bindLong(6, entity.getLogNewQty());
        if (entity.getLogDescription() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLogDescription());
        }
        statement.bindString(8, entity.getLogTimestamp());
      }
    };
  }

  @Override
  public Object insertLog(final PosLogEntity log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPosLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PosLogWithDetails>> getLogs(final String startDate, final String endDate) {
    final String _sql = "\n"
            + "        SELECT l.*, u.usr_name, p.prd_name \n"
            + "        FROM pos_logs l\n"
            + "        JOIN users u ON l.log_user_id = u.usr_id\n"
            + "        JOIN products p ON l.log_prd_sku = p.prd_sku\n"
            + "        WHERE DATE(l.log_timestamp) BETWEEN DATE(?) AND DATE(?)\n"
            + "        ORDER BY l.log_timestamp DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pos_logs", "users",
        "products"}, new Callable<List<PosLogWithDetails>>() {
      @Override
      @NonNull
      public List<PosLogWithDetails> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLogId = CursorUtil.getColumnIndexOrThrow(_cursor, "log_id");
          final int _cursorIndexOfLogUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "log_user_id");
          final int _cursorIndexOfLogPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "log_prd_sku");
          final int _cursorIndexOfLogAction = CursorUtil.getColumnIndexOrThrow(_cursor, "log_action");
          final int _cursorIndexOfLogOldQty = CursorUtil.getColumnIndexOrThrow(_cursor, "log_old_qty");
          final int _cursorIndexOfLogNewQty = CursorUtil.getColumnIndexOrThrow(_cursor, "log_new_qty");
          final int _cursorIndexOfLogDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "log_description");
          final int _cursorIndexOfLogTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "log_timestamp");
          final int _cursorIndexOfUsrName = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_name");
          final int _cursorIndexOfPrdName = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_name");
          final List<PosLogWithDetails> _result = new ArrayList<PosLogWithDetails>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PosLogWithDetails _item;
            final int _tmpLogId;
            _tmpLogId = _cursor.getInt(_cursorIndexOfLogId);
            final int _tmpLogUserId;
            _tmpLogUserId = _cursor.getInt(_cursorIndexOfLogUserId);
            final String _tmpLogPrdSku;
            _tmpLogPrdSku = _cursor.getString(_cursorIndexOfLogPrdSku);
            final String _tmpLogAction;
            _tmpLogAction = _cursor.getString(_cursorIndexOfLogAction);
            final int _tmpLogOldQty;
            _tmpLogOldQty = _cursor.getInt(_cursorIndexOfLogOldQty);
            final int _tmpLogNewQty;
            _tmpLogNewQty = _cursor.getInt(_cursorIndexOfLogNewQty);
            final String _tmpLogDescription;
            if (_cursor.isNull(_cursorIndexOfLogDescription)) {
              _tmpLogDescription = null;
            } else {
              _tmpLogDescription = _cursor.getString(_cursorIndexOfLogDescription);
            }
            final String _tmpLogTimestamp;
            _tmpLogTimestamp = _cursor.getString(_cursorIndexOfLogTimestamp);
            final String _tmpUsrName;
            _tmpUsrName = _cursor.getString(_cursorIndexOfUsrName);
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            _item = new PosLogWithDetails(_tmpLogId,_tmpLogUserId,_tmpLogPrdSku,_tmpLogAction,_tmpLogOldQty,_tmpLogNewQty,_tmpLogDescription,_tmpLogTimestamp,_tmpUsrName,_tmpPrdName);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

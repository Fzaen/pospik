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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class InventoryDamagedDao_Impl implements InventoryDamagedDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<InventoryDamagedEntity> __insertionAdapterOfInventoryDamagedEntity;

  public InventoryDamagedDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfInventoryDamagedEntity = new EntityInsertionAdapter<InventoryDamagedEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `inventory_damaged` (`dmg_id`,`dmg_date`,`dmg_user_id`,`dmg_prd_sku`,`dmg_qty`,`dmg_reason`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InventoryDamagedEntity entity) {
        statement.bindLong(1, entity.getDmgId());
        statement.bindString(2, entity.getDmgDate());
        statement.bindLong(3, entity.getDmgUserId());
        statement.bindString(4, entity.getDmgPrdSku());
        statement.bindLong(5, entity.getDmgQty());
        if (entity.getDmgReason() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDmgReason());
        }
      }
    };
  }

  @Override
  public Object insertDamaged(final InventoryDamagedEntity damaged,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfInventoryDamagedEntity.insertAndReturnId(damaged);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<InventoryDamagedWithDetails>> getDamagedHistory(final String startDate,
      final String endDate) {
    final String _sql = "\n"
            + "        SELECT d.*, p.prd_name, u.usr_name\n"
            + "        FROM inventory_damaged d\n"
            + "        JOIN products p ON d.dmg_prd_sku = p.prd_sku\n"
            + "        JOIN users u ON d.dmg_user_id = u.usr_id\n"
            + "        WHERE DATE(d.dmg_date) BETWEEN DATE(?) AND DATE(?)\n"
            + "        ORDER BY d.dmg_date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"inventory_damaged", "products",
        "users"}, new Callable<List<InventoryDamagedWithDetails>>() {
      @Override
      @NonNull
      public List<InventoryDamagedWithDetails> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDmgId = CursorUtil.getColumnIndexOrThrow(_cursor, "dmg_id");
          final int _cursorIndexOfDmgDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dmg_date");
          final int _cursorIndexOfDmgUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "dmg_user_id");
          final int _cursorIndexOfDmgPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "dmg_prd_sku");
          final int _cursorIndexOfDmgQty = CursorUtil.getColumnIndexOrThrow(_cursor, "dmg_qty");
          final int _cursorIndexOfDmgReason = CursorUtil.getColumnIndexOrThrow(_cursor, "dmg_reason");
          final int _cursorIndexOfPrdName = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_name");
          final int _cursorIndexOfUsrName = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_name");
          final List<InventoryDamagedWithDetails> _result = new ArrayList<InventoryDamagedWithDetails>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final InventoryDamagedWithDetails _item;
            final int _tmpDmgId;
            _tmpDmgId = _cursor.getInt(_cursorIndexOfDmgId);
            final String _tmpDmgDate;
            _tmpDmgDate = _cursor.getString(_cursorIndexOfDmgDate);
            final int _tmpDmgUserId;
            _tmpDmgUserId = _cursor.getInt(_cursorIndexOfDmgUserId);
            final String _tmpDmgPrdSku;
            _tmpDmgPrdSku = _cursor.getString(_cursorIndexOfDmgPrdSku);
            final int _tmpDmgQty;
            _tmpDmgQty = _cursor.getInt(_cursorIndexOfDmgQty);
            final String _tmpDmgReason;
            if (_cursor.isNull(_cursorIndexOfDmgReason)) {
              _tmpDmgReason = null;
            } else {
              _tmpDmgReason = _cursor.getString(_cursorIndexOfDmgReason);
            }
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            final String _tmpUsrName;
            _tmpUsrName = _cursor.getString(_cursorIndexOfUsrName);
            _item = new InventoryDamagedWithDetails(_tmpDmgId,_tmpDmgDate,_tmpDmgUserId,_tmpDmgPrdSku,_tmpDmgQty,_tmpDmgReason,_tmpPrdName,_tmpUsrName);
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
  public Object getTotalDamagedQty(final String sku, final int year,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(dmg_qty), 0)\n"
            + "        FROM inventory_damaged\n"
            + "        WHERE dmg_prd_sku = ? AND strftime('%Y', dmg_date) = CAST(? AS TEXT)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sku);
    _argIndex = 2;
    _statement.bindLong(_argIndex, year);
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

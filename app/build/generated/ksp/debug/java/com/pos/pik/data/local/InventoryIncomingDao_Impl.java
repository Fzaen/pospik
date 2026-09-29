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
public final class InventoryIncomingDao_Impl implements InventoryIncomingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<InventoryIncomingEntity> __insertionAdapterOfInventoryIncomingEntity;

  public InventoryIncomingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfInventoryIncomingEntity = new EntityInsertionAdapter<InventoryIncomingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `inventory_incoming` (`inc_id`,`inc_date`,`inc_user_id`,`inc_prd_sku`,`inc_package_qty`,`inc_fraction`,`inc_total_qty`,`inc_total_cost`,`inc_unit_cost`,`inc_note`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InventoryIncomingEntity entity) {
        statement.bindLong(1, entity.getIncId());
        statement.bindString(2, entity.getIncDate());
        statement.bindLong(3, entity.getIncUserId());
        statement.bindString(4, entity.getIncPrdSku());
        statement.bindLong(5, entity.getIncPackageQty());
        statement.bindLong(6, entity.getIncFraction());
        statement.bindLong(7, entity.getIncTotalQty());
        statement.bindDouble(8, entity.getIncTotalCost());
        statement.bindDouble(9, entity.getIncUnitCost());
        if (entity.getIncNote() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getIncNote());
        }
      }
    };
  }

  @Override
  public Object insertIncoming(final InventoryIncomingEntity incoming,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfInventoryIncomingEntity.insertAndReturnId(incoming);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<InventoryIncomingWithDetails>> getIncomingHistory(final String startDate,
      final String endDate) {
    final String _sql = "\n"
            + "        SELECT i.*, p.prd_name, u.usr_name\n"
            + "        FROM inventory_incoming i\n"
            + "        JOIN products p ON i.inc_prd_sku = p.prd_sku\n"
            + "        JOIN users u ON i.inc_user_id = u.usr_id\n"
            + "        WHERE DATE(i.inc_date) BETWEEN DATE(?) AND DATE(?)\n"
            + "        ORDER BY i.inc_date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"inventory_incoming", "products",
        "users"}, new Callable<List<InventoryIncomingWithDetails>>() {
      @Override
      @NonNull
      public List<InventoryIncomingWithDetails> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfIncId = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_id");
          final int _cursorIndexOfIncDate = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_date");
          final int _cursorIndexOfIncUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_user_id");
          final int _cursorIndexOfIncPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_prd_sku");
          final int _cursorIndexOfIncPackageQty = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_package_qty");
          final int _cursorIndexOfIncFraction = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_fraction");
          final int _cursorIndexOfIncTotalQty = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_total_qty");
          final int _cursorIndexOfIncTotalCost = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_total_cost");
          final int _cursorIndexOfIncUnitCost = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_unit_cost");
          final int _cursorIndexOfIncNote = CursorUtil.getColumnIndexOrThrow(_cursor, "inc_note");
          final int _cursorIndexOfPrdName = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_name");
          final int _cursorIndexOfUsrName = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_name");
          final List<InventoryIncomingWithDetails> _result = new ArrayList<InventoryIncomingWithDetails>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final InventoryIncomingWithDetails _item;
            final int _tmpIncId;
            _tmpIncId = _cursor.getInt(_cursorIndexOfIncId);
            final String _tmpIncDate;
            _tmpIncDate = _cursor.getString(_cursorIndexOfIncDate);
            final int _tmpIncUserId;
            _tmpIncUserId = _cursor.getInt(_cursorIndexOfIncUserId);
            final String _tmpIncPrdSku;
            _tmpIncPrdSku = _cursor.getString(_cursorIndexOfIncPrdSku);
            final int _tmpIncPackageQty;
            _tmpIncPackageQty = _cursor.getInt(_cursorIndexOfIncPackageQty);
            final int _tmpIncFraction;
            _tmpIncFraction = _cursor.getInt(_cursorIndexOfIncFraction);
            final int _tmpIncTotalQty;
            _tmpIncTotalQty = _cursor.getInt(_cursorIndexOfIncTotalQty);
            final double _tmpIncTotalCost;
            _tmpIncTotalCost = _cursor.getDouble(_cursorIndexOfIncTotalCost);
            final double _tmpIncUnitCost;
            _tmpIncUnitCost = _cursor.getDouble(_cursorIndexOfIncUnitCost);
            final String _tmpIncNote;
            if (_cursor.isNull(_cursorIndexOfIncNote)) {
              _tmpIncNote = null;
            } else {
              _tmpIncNote = _cursor.getString(_cursorIndexOfIncNote);
            }
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            final String _tmpUsrName;
            _tmpUsrName = _cursor.getString(_cursorIndexOfUsrName);
            _item = new InventoryIncomingWithDetails(_tmpIncId,_tmpIncDate,_tmpIncUserId,_tmpIncPrdSku,_tmpIncPackageQty,_tmpIncFraction,_tmpIncTotalQty,_tmpIncTotalCost,_tmpIncUnitCost,_tmpIncNote,_tmpPrdName,_tmpUsrName);
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
  public Object getTotalIncomingQty(final String sku, final int year,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(inc_total_qty), 0)\n"
            + "        FROM inventory_incoming\n"
            + "        WHERE inc_prd_sku = ? AND strftime('%Y', inc_date) = CAST(? AS TEXT)\n"
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

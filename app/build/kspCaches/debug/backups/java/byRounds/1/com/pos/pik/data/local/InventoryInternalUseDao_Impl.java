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
public final class InventoryInternalUseDao_Impl implements InventoryInternalUseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<InventoryInternalUseEntity> __insertionAdapterOfInventoryInternalUseEntity;

  public InventoryInternalUseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfInventoryInternalUseEntity = new EntityInsertionAdapter<InventoryInternalUseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `inventory_internal_use` (`use_id`,`use_date`,`use_user_id`,`use_prd_sku`,`use_qty`,`use_note`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InventoryInternalUseEntity entity) {
        statement.bindLong(1, entity.getUseId());
        statement.bindString(2, entity.getUseDate());
        statement.bindLong(3, entity.getUseUserId());
        statement.bindString(4, entity.getUsePrdSku());
        statement.bindLong(5, entity.getUseQty());
        if (entity.getUseNote() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUseNote());
        }
      }
    };
  }

  @Override
  public Object insertInternalUse(final InventoryInternalUseEntity internalUse,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfInventoryInternalUseEntity.insertAndReturnId(internalUse);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<InventoryInternalUseWithDetails>> getInternalUseHistory(final String startDate,
      final String endDate) {
    final String _sql = "\n"
            + "        SELECT u.*, p.prd_name, us.usr_name, c.cat_name, c.cat_subname\n"
            + "        FROM inventory_internal_use u\n"
            + "        JOIN products p ON u.use_prd_sku = p.prd_sku\n"
            + "        JOIN categories c ON p.prd_category_id = c.cat_id\n"
            + "        JOIN users us ON u.use_user_id = us.usr_id\n"
            + "        WHERE DATE(u.use_date) BETWEEN DATE(?) AND DATE(?)\n"
            + "        ORDER BY u.use_date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"inventory_internal_use",
        "products", "categories", "users"}, new Callable<List<InventoryInternalUseWithDetails>>() {
      @Override
      @NonNull
      public List<InventoryInternalUseWithDetails> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUseId = CursorUtil.getColumnIndexOrThrow(_cursor, "use_id");
          final int _cursorIndexOfUseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "use_date");
          final int _cursorIndexOfUseUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "use_user_id");
          final int _cursorIndexOfUsePrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "use_prd_sku");
          final int _cursorIndexOfUseQty = CursorUtil.getColumnIndexOrThrow(_cursor, "use_qty");
          final int _cursorIndexOfUseNote = CursorUtil.getColumnIndexOrThrow(_cursor, "use_note");
          final int _cursorIndexOfPrdName = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_name");
          final int _cursorIndexOfUsrName = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_name");
          final int _cursorIndexOfCatName = CursorUtil.getColumnIndexOrThrow(_cursor, "cat_name");
          final int _cursorIndexOfCatSubname = CursorUtil.getColumnIndexOrThrow(_cursor, "cat_subname");
          final List<InventoryInternalUseWithDetails> _result = new ArrayList<InventoryInternalUseWithDetails>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final InventoryInternalUseWithDetails _item;
            final int _tmpUseId;
            _tmpUseId = _cursor.getInt(_cursorIndexOfUseId);
            final String _tmpUseDate;
            _tmpUseDate = _cursor.getString(_cursorIndexOfUseDate);
            final int _tmpUseUserId;
            _tmpUseUserId = _cursor.getInt(_cursorIndexOfUseUserId);
            final String _tmpUsePrdSku;
            _tmpUsePrdSku = _cursor.getString(_cursorIndexOfUsePrdSku);
            final int _tmpUseQty;
            _tmpUseQty = _cursor.getInt(_cursorIndexOfUseQty);
            final String _tmpUseNote;
            if (_cursor.isNull(_cursorIndexOfUseNote)) {
              _tmpUseNote = null;
            } else {
              _tmpUseNote = _cursor.getString(_cursorIndexOfUseNote);
            }
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            final String _tmpUsrName;
            _tmpUsrName = _cursor.getString(_cursorIndexOfUsrName);
            final String _tmpCatName;
            _tmpCatName = _cursor.getString(_cursorIndexOfCatName);
            final String _tmpCatSubname;
            _tmpCatSubname = _cursor.getString(_cursorIndexOfCatSubname);
            _item = new InventoryInternalUseWithDetails(_tmpUseId,_tmpUseDate,_tmpUseUserId,_tmpUsePrdSku,_tmpUseQty,_tmpUseNote,_tmpPrdName,_tmpUsrName,_tmpCatName,_tmpCatSubname);
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
  public Object getTotalInternalUseQty(final String sku, final int year,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(use_qty), 0)\n"
            + "        FROM inventory_internal_use\n"
            + "        WHERE use_prd_sku = ? AND strftime('%Y', use_date) = CAST(? AS TEXT)\n"
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

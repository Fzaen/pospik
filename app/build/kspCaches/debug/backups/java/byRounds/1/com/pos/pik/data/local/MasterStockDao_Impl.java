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
import androidx.room.SharedSQLiteStatement;
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
public final class MasterStockDao_Impl implements MasterStockDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MasterStockEntity> __insertionAdapterOfMasterStockEntity;

  private final EntityDeletionOrUpdateAdapter<MasterStockEntity> __updateAdapterOfMasterStockEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteStockByYear;

  public MasterStockDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMasterStockEntity = new EntityInsertionAdapter<MasterStockEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `master_stock` (`st_id`,`st_prd_sku`,`st_year`,`st_initial_qty`,`st_incoming_qty`,`st_sales_qty`,`st_damaged_qty`,`st_internal_use_qty`,`st_final_qty`,`st_last_updated`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MasterStockEntity entity) {
        statement.bindLong(1, entity.getStId());
        statement.bindString(2, entity.getStPrdSku());
        statement.bindLong(3, entity.getStYear());
        statement.bindLong(4, entity.getStInitialQty());
        statement.bindLong(5, entity.getStIncomingQty());
        statement.bindLong(6, entity.getStSalesQty());
        statement.bindLong(7, entity.getStDamagedQty());
        statement.bindLong(8, entity.getStInternalUseQty());
        statement.bindLong(9, entity.getStFinalQty());
        statement.bindString(10, entity.getStLastUpdated());
      }
    };
    this.__updateAdapterOfMasterStockEntity = new EntityDeletionOrUpdateAdapter<MasterStockEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `master_stock` SET `st_id` = ?,`st_prd_sku` = ?,`st_year` = ?,`st_initial_qty` = ?,`st_incoming_qty` = ?,`st_sales_qty` = ?,`st_damaged_qty` = ?,`st_internal_use_qty` = ?,`st_final_qty` = ?,`st_last_updated` = ? WHERE `st_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MasterStockEntity entity) {
        statement.bindLong(1, entity.getStId());
        statement.bindString(2, entity.getStPrdSku());
        statement.bindLong(3, entity.getStYear());
        statement.bindLong(4, entity.getStInitialQty());
        statement.bindLong(5, entity.getStIncomingQty());
        statement.bindLong(6, entity.getStSalesQty());
        statement.bindLong(7, entity.getStDamagedQty());
        statement.bindLong(8, entity.getStInternalUseQty());
        statement.bindLong(9, entity.getStFinalQty());
        statement.bindString(10, entity.getStLastUpdated());
        statement.bindLong(11, entity.getStId());
      }
    };
    this.__preparedStmtOfDeleteStockByYear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM master_stock WHERE st_year = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertStock(final MasterStockEntity stock,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMasterStockEntity.insert(stock);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStock(final MasterStockEntity stock,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMasterStockEntity.handle(stock);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteStockByYear(final int year, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteStockByYear.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, year);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteStockByYear.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MasterStockWithProduct>> getMasterStockByYear(final int year,
      final String query) {
    final String _sql = "\n"
            + "        SELECT s.*, p.prd_name, p.prd_cost_price, p.prd_selling_price, c.cat_name\n"
            + "        FROM master_stock s\n"
            + "        JOIN products p ON s.st_prd_sku = p.prd_sku\n"
            + "        JOIN categories c ON p.prd_category_id = c.cat_id\n"
            + "        WHERE s.st_year = ?\n"
            + "          AND (? IS NULL OR ? = '' OR p.prd_name LIKE '%' || ? || '%' OR p.prd_sku LIKE '%' || ? || '%')\n"
            + "        ORDER BY p.prd_name ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, year);
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 3;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 4;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 5;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"master_stock", "products",
        "categories"}, new Callable<List<MasterStockWithProduct>>() {
      @Override
      @NonNull
      public List<MasterStockWithProduct> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfStId = CursorUtil.getColumnIndexOrThrow(_cursor, "st_id");
          final int _cursorIndexOfStPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "st_prd_sku");
          final int _cursorIndexOfStYear = CursorUtil.getColumnIndexOrThrow(_cursor, "st_year");
          final int _cursorIndexOfStInitialQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_initial_qty");
          final int _cursorIndexOfStIncomingQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_incoming_qty");
          final int _cursorIndexOfStSalesQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_sales_qty");
          final int _cursorIndexOfStDamagedQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_damaged_qty");
          final int _cursorIndexOfStInternalUseQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_internal_use_qty");
          final int _cursorIndexOfStFinalQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_final_qty");
          final int _cursorIndexOfStLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "st_last_updated");
          final int _cursorIndexOfPrdName = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_name");
          final int _cursorIndexOfPrdCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_cost_price");
          final int _cursorIndexOfPrdSellingPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_selling_price");
          final int _cursorIndexOfCatName = CursorUtil.getColumnIndexOrThrow(_cursor, "cat_name");
          final List<MasterStockWithProduct> _result = new ArrayList<MasterStockWithProduct>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MasterStockWithProduct _item;
            final int _tmpStId;
            _tmpStId = _cursor.getInt(_cursorIndexOfStId);
            final String _tmpStPrdSku;
            _tmpStPrdSku = _cursor.getString(_cursorIndexOfStPrdSku);
            final int _tmpStYear;
            _tmpStYear = _cursor.getInt(_cursorIndexOfStYear);
            final int _tmpStInitialQty;
            _tmpStInitialQty = _cursor.getInt(_cursorIndexOfStInitialQty);
            final int _tmpStIncomingQty;
            _tmpStIncomingQty = _cursor.getInt(_cursorIndexOfStIncomingQty);
            final int _tmpStSalesQty;
            _tmpStSalesQty = _cursor.getInt(_cursorIndexOfStSalesQty);
            final int _tmpStDamagedQty;
            _tmpStDamagedQty = _cursor.getInt(_cursorIndexOfStDamagedQty);
            final int _tmpStInternalUseQty;
            _tmpStInternalUseQty = _cursor.getInt(_cursorIndexOfStInternalUseQty);
            final int _tmpStFinalQty;
            _tmpStFinalQty = _cursor.getInt(_cursorIndexOfStFinalQty);
            final String _tmpStLastUpdated;
            _tmpStLastUpdated = _cursor.getString(_cursorIndexOfStLastUpdated);
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            final double _tmpPrdCostPrice;
            _tmpPrdCostPrice = _cursor.getDouble(_cursorIndexOfPrdCostPrice);
            final double _tmpPrdSellingPrice;
            _tmpPrdSellingPrice = _cursor.getDouble(_cursorIndexOfPrdSellingPrice);
            final String _tmpCatName;
            _tmpCatName = _cursor.getString(_cursorIndexOfCatName);
            _item = new MasterStockWithProduct(_tmpStId,_tmpStPrdSku,_tmpStYear,_tmpStInitialQty,_tmpStIncomingQty,_tmpStSalesQty,_tmpStDamagedQty,_tmpStInternalUseQty,_tmpStFinalQty,_tmpStLastUpdated,_tmpPrdName,_tmpPrdCostPrice,_tmpPrdSellingPrice,_tmpCatName);
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
  public Object getStockBySkuAndYear(final String sku, final int year,
      final Continuation<? super MasterStockEntity> $completion) {
    final String _sql = "SELECT * FROM master_stock WHERE st_prd_sku = ? AND st_year = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, sku);
    _argIndex = 2;
    _statement.bindLong(_argIndex, year);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MasterStockEntity>() {
      @Override
      @Nullable
      public MasterStockEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfStId = CursorUtil.getColumnIndexOrThrow(_cursor, "st_id");
          final int _cursorIndexOfStPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "st_prd_sku");
          final int _cursorIndexOfStYear = CursorUtil.getColumnIndexOrThrow(_cursor, "st_year");
          final int _cursorIndexOfStInitialQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_initial_qty");
          final int _cursorIndexOfStIncomingQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_incoming_qty");
          final int _cursorIndexOfStSalesQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_sales_qty");
          final int _cursorIndexOfStDamagedQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_damaged_qty");
          final int _cursorIndexOfStInternalUseQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_internal_use_qty");
          final int _cursorIndexOfStFinalQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_final_qty");
          final int _cursorIndexOfStLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "st_last_updated");
          final MasterStockEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpStId;
            _tmpStId = _cursor.getInt(_cursorIndexOfStId);
            final String _tmpStPrdSku;
            _tmpStPrdSku = _cursor.getString(_cursorIndexOfStPrdSku);
            final int _tmpStYear;
            _tmpStYear = _cursor.getInt(_cursorIndexOfStYear);
            final int _tmpStInitialQty;
            _tmpStInitialQty = _cursor.getInt(_cursorIndexOfStInitialQty);
            final int _tmpStIncomingQty;
            _tmpStIncomingQty = _cursor.getInt(_cursorIndexOfStIncomingQty);
            final int _tmpStSalesQty;
            _tmpStSalesQty = _cursor.getInt(_cursorIndexOfStSalesQty);
            final int _tmpStDamagedQty;
            _tmpStDamagedQty = _cursor.getInt(_cursorIndexOfStDamagedQty);
            final int _tmpStInternalUseQty;
            _tmpStInternalUseQty = _cursor.getInt(_cursorIndexOfStInternalUseQty);
            final int _tmpStFinalQty;
            _tmpStFinalQty = _cursor.getInt(_cursorIndexOfStFinalQty);
            final String _tmpStLastUpdated;
            _tmpStLastUpdated = _cursor.getString(_cursorIndexOfStLastUpdated);
            _result = new MasterStockEntity(_tmpStId,_tmpStPrdSku,_tmpStYear,_tmpStInitialQty,_tmpStIncomingQty,_tmpStSalesQty,_tmpStDamagedQty,_tmpStInternalUseQty,_tmpStFinalQty,_tmpStLastUpdated);
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
  public Object getAllStockByYear(final int year,
      final Continuation<? super List<MasterStockEntity>> $completion) {
    final String _sql = "SELECT * FROM master_stock WHERE st_year = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, year);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MasterStockEntity>>() {
      @Override
      @NonNull
      public List<MasterStockEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfStId = CursorUtil.getColumnIndexOrThrow(_cursor, "st_id");
          final int _cursorIndexOfStPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "st_prd_sku");
          final int _cursorIndexOfStYear = CursorUtil.getColumnIndexOrThrow(_cursor, "st_year");
          final int _cursorIndexOfStInitialQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_initial_qty");
          final int _cursorIndexOfStIncomingQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_incoming_qty");
          final int _cursorIndexOfStSalesQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_sales_qty");
          final int _cursorIndexOfStDamagedQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_damaged_qty");
          final int _cursorIndexOfStInternalUseQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_internal_use_qty");
          final int _cursorIndexOfStFinalQty = CursorUtil.getColumnIndexOrThrow(_cursor, "st_final_qty");
          final int _cursorIndexOfStLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "st_last_updated");
          final List<MasterStockEntity> _result = new ArrayList<MasterStockEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MasterStockEntity _item;
            final int _tmpStId;
            _tmpStId = _cursor.getInt(_cursorIndexOfStId);
            final String _tmpStPrdSku;
            _tmpStPrdSku = _cursor.getString(_cursorIndexOfStPrdSku);
            final int _tmpStYear;
            _tmpStYear = _cursor.getInt(_cursorIndexOfStYear);
            final int _tmpStInitialQty;
            _tmpStInitialQty = _cursor.getInt(_cursorIndexOfStInitialQty);
            final int _tmpStIncomingQty;
            _tmpStIncomingQty = _cursor.getInt(_cursorIndexOfStIncomingQty);
            final int _tmpStSalesQty;
            _tmpStSalesQty = _cursor.getInt(_cursorIndexOfStSalesQty);
            final int _tmpStDamagedQty;
            _tmpStDamagedQty = _cursor.getInt(_cursorIndexOfStDamagedQty);
            final int _tmpStInternalUseQty;
            _tmpStInternalUseQty = _cursor.getInt(_cursorIndexOfStInternalUseQty);
            final int _tmpStFinalQty;
            _tmpStFinalQty = _cursor.getInt(_cursorIndexOfStFinalQty);
            final String _tmpStLastUpdated;
            _tmpStLastUpdated = _cursor.getString(_cursorIndexOfStLastUpdated);
            _item = new MasterStockEntity(_tmpStId,_tmpStPrdSku,_tmpStYear,_tmpStInitialQty,_tmpStIncomingQty,_tmpStSalesQty,_tmpStDamagedQty,_tmpStInternalUseQty,_tmpStFinalQty,_tmpStLastUpdated);
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

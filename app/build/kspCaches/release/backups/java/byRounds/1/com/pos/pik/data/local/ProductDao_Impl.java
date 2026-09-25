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
public final class ProductDao_Impl implements ProductDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProductEntity> __insertionAdapterOfProductEntity;

  private final EntityDeletionOrUpdateAdapter<ProductEntity> __updateAdapterOfProductEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteProductBySku;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllProducts;

  public ProductDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProductEntity = new EntityInsertionAdapter<ProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `products` (`prd_sku`,`prd_category_id`,`prd_name`,`prd_cost_price`,`prd_selling_price`,`prd_image`,`prd_is_active`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductEntity entity) {
        statement.bindString(1, entity.getPrdSku());
        statement.bindLong(2, entity.getPrdCategoryId());
        statement.bindString(3, entity.getPrdName());
        statement.bindDouble(4, entity.getPrdCostPrice());
        statement.bindDouble(5, entity.getPrdSellingPrice());
        if (entity.getPrdImage() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPrdImage());
        }
        statement.bindLong(7, entity.getPrdIsActive());
      }
    };
    this.__updateAdapterOfProductEntity = new EntityDeletionOrUpdateAdapter<ProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `products` SET `prd_sku` = ?,`prd_category_id` = ?,`prd_name` = ?,`prd_cost_price` = ?,`prd_selling_price` = ?,`prd_image` = ?,`prd_is_active` = ? WHERE `prd_sku` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductEntity entity) {
        statement.bindString(1, entity.getPrdSku());
        statement.bindLong(2, entity.getPrdCategoryId());
        statement.bindString(3, entity.getPrdName());
        statement.bindDouble(4, entity.getPrdCostPrice());
        statement.bindDouble(5, entity.getPrdSellingPrice());
        if (entity.getPrdImage() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPrdImage());
        }
        statement.bindLong(7, entity.getPrdIsActive());
        statement.bindString(8, entity.getPrdSku());
      }
    };
    this.__preparedStmtOfDeleteProductBySku = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM products WHERE prd_sku = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllProducts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM products";
        return _query;
      }
    };
  }

  @Override
  public Object insertProduct(final ProductEntity product,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProductEntity.insert(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<ProductEntity> products,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProductEntity.insert(products);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProduct(final ProductEntity product,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProductEntity.handle(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProductBySku(final String sku, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteProductBySku.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, sku);
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
          __preparedStmtOfDeleteProductBySku.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllProducts(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllProducts.acquire();
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
          __preparedStmtOfDeleteAllProducts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProductWithCategory>> getAllProducts(final String mainCat, final String query) {
    final String _sql = "\n"
            + "        SELECT p.*, c.cat_name, c.cat_subname \n"
            + "        FROM products p\n"
            + "        JOIN categories c ON p.prd_category_id = c.cat_id\n"
            + "        WHERE (? IS NULL OR c.cat_name = ?)\n"
            + "          AND (? IS NULL OR ? = '' OR p.prd_name LIKE '%' || ? || '%' OR p.prd_sku LIKE '%' || ? || '%')\n"
            + "        ORDER BY p.prd_sku ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 6);
    int _argIndex = 1;
    if (mainCat == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, mainCat);
    }
    _argIndex = 2;
    if (mainCat == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, mainCat);
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
    _argIndex = 6;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products",
        "categories"}, new Callable<List<ProductWithCategory>>() {
      @Override
      @NonNull
      public List<ProductWithCategory> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_sku");
          final int _cursorIndexOfPrdCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_category_id");
          final int _cursorIndexOfPrdName = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_name");
          final int _cursorIndexOfPrdCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_cost_price");
          final int _cursorIndexOfPrdSellingPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_selling_price");
          final int _cursorIndexOfPrdImage = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_image");
          final int _cursorIndexOfPrdIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_is_active");
          final int _cursorIndexOfCatName = CursorUtil.getColumnIndexOrThrow(_cursor, "cat_name");
          final int _cursorIndexOfCatSubname = CursorUtil.getColumnIndexOrThrow(_cursor, "cat_subname");
          final List<ProductWithCategory> _result = new ArrayList<ProductWithCategory>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductWithCategory _item;
            final String _tmpPrdSku;
            _tmpPrdSku = _cursor.getString(_cursorIndexOfPrdSku);
            final int _tmpPrdCategoryId;
            _tmpPrdCategoryId = _cursor.getInt(_cursorIndexOfPrdCategoryId);
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            final double _tmpPrdCostPrice;
            _tmpPrdCostPrice = _cursor.getDouble(_cursorIndexOfPrdCostPrice);
            final double _tmpPrdSellingPrice;
            _tmpPrdSellingPrice = _cursor.getDouble(_cursorIndexOfPrdSellingPrice);
            final String _tmpPrdImage;
            if (_cursor.isNull(_cursorIndexOfPrdImage)) {
              _tmpPrdImage = null;
            } else {
              _tmpPrdImage = _cursor.getString(_cursorIndexOfPrdImage);
            }
            final int _tmpPrdIsActive;
            _tmpPrdIsActive = _cursor.getInt(_cursorIndexOfPrdIsActive);
            final String _tmpCatName;
            _tmpCatName = _cursor.getString(_cursorIndexOfCatName);
            final String _tmpCatSubname;
            _tmpCatSubname = _cursor.getString(_cursorIndexOfCatSubname);
            _item = new ProductWithCategory(_tmpPrdSku,_tmpPrdCategoryId,_tmpPrdName,_tmpPrdCostPrice,_tmpPrdSellingPrice,_tmpPrdImage,_tmpPrdIsActive,_tmpCatName,_tmpCatSubname);
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
  public Flow<List<ProductWithCategory>> getActiveProducts(final String mainCat,
      final String query) {
    final String _sql = "\n"
            + "        SELECT p.*, c.cat_name, c.cat_subname \n"
            + "        FROM products p\n"
            + "        JOIN categories c ON p.prd_category_id = c.cat_id\n"
            + "        WHERE p.prd_is_active = 1\n"
            + "          AND (? IS NULL OR c.cat_name = ?)\n"
            + "          AND (? IS NULL OR ? = '' OR p.prd_name LIKE '%' || ? || '%' OR p.prd_sku LIKE '%' || ? || '%')\n"
            + "        ORDER BY p.prd_sku ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 6);
    int _argIndex = 1;
    if (mainCat == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, mainCat);
    }
    _argIndex = 2;
    if (mainCat == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, mainCat);
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
    _argIndex = 6;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products",
        "categories"}, new Callable<List<ProductWithCategory>>() {
      @Override
      @NonNull
      public List<ProductWithCategory> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_sku");
          final int _cursorIndexOfPrdCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_category_id");
          final int _cursorIndexOfPrdName = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_name");
          final int _cursorIndexOfPrdCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_cost_price");
          final int _cursorIndexOfPrdSellingPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_selling_price");
          final int _cursorIndexOfPrdImage = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_image");
          final int _cursorIndexOfPrdIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_is_active");
          final int _cursorIndexOfCatName = CursorUtil.getColumnIndexOrThrow(_cursor, "cat_name");
          final int _cursorIndexOfCatSubname = CursorUtil.getColumnIndexOrThrow(_cursor, "cat_subname");
          final List<ProductWithCategory> _result = new ArrayList<ProductWithCategory>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductWithCategory _item;
            final String _tmpPrdSku;
            _tmpPrdSku = _cursor.getString(_cursorIndexOfPrdSku);
            final int _tmpPrdCategoryId;
            _tmpPrdCategoryId = _cursor.getInt(_cursorIndexOfPrdCategoryId);
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            final double _tmpPrdCostPrice;
            _tmpPrdCostPrice = _cursor.getDouble(_cursorIndexOfPrdCostPrice);
            final double _tmpPrdSellingPrice;
            _tmpPrdSellingPrice = _cursor.getDouble(_cursorIndexOfPrdSellingPrice);
            final String _tmpPrdImage;
            if (_cursor.isNull(_cursorIndexOfPrdImage)) {
              _tmpPrdImage = null;
            } else {
              _tmpPrdImage = _cursor.getString(_cursorIndexOfPrdImage);
            }
            final int _tmpPrdIsActive;
            _tmpPrdIsActive = _cursor.getInt(_cursorIndexOfPrdIsActive);
            final String _tmpCatName;
            _tmpCatName = _cursor.getString(_cursorIndexOfCatName);
            final String _tmpCatSubname;
            _tmpCatSubname = _cursor.getString(_cursorIndexOfCatSubname);
            _item = new ProductWithCategory(_tmpPrdSku,_tmpPrdCategoryId,_tmpPrdName,_tmpPrdCostPrice,_tmpPrdSellingPrice,_tmpPrdImage,_tmpPrdIsActive,_tmpCatName,_tmpCatSubname);
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
  public Object getLastSkuWithPrefix(final String prefix,
      final Continuation<? super String> $completion) {
    final String _sql = "SELECT MAX(prd_sku) FROM products WHERE prd_sku LIKE ? || '%' AND length(prd_sku) = 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, prefix);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<String>() {
      @Override
      @Nullable
      public String call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final String _result;
          if (_cursor.moveToFirst()) {
            final String _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(0);
            }
            _result = _tmp;
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

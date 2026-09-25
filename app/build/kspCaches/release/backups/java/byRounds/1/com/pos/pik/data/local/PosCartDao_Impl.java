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
public final class PosCartDao_Impl implements PosCartDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PosCartEntity> __insertionAdapterOfPosCartEntity;

  private final EntityDeletionOrUpdateAdapter<PosCartEntity> __updateAdapterOfPosCartEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCartItem;

  private final SharedSQLiteStatement __preparedStmtOfCheckoutCart;

  public PosCartDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPosCartEntity = new EntityInsertionAdapter<PosCartEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pos_cart` (`cart_id`,`cart_user_id`,`cart_prd_sku`,`cart_qty`,`cart_price`,`cart_cost_price`,`cart_subtotal`,`cart_status`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PosCartEntity entity) {
        statement.bindLong(1, entity.getCartId());
        statement.bindLong(2, entity.getCartUserId());
        statement.bindString(3, entity.getCartPrdSku());
        statement.bindLong(4, entity.getCartQty());
        statement.bindDouble(5, entity.getCartPrice());
        statement.bindDouble(6, entity.getCartCostPrice());
        statement.bindDouble(7, entity.getCartSubtotal());
        statement.bindLong(8, entity.getCartStatus());
      }
    };
    this.__updateAdapterOfPosCartEntity = new EntityDeletionOrUpdateAdapter<PosCartEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pos_cart` SET `cart_id` = ?,`cart_user_id` = ?,`cart_prd_sku` = ?,`cart_qty` = ?,`cart_price` = ?,`cart_cost_price` = ?,`cart_subtotal` = ?,`cart_status` = ? WHERE `cart_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PosCartEntity entity) {
        statement.bindLong(1, entity.getCartId());
        statement.bindLong(2, entity.getCartUserId());
        statement.bindString(3, entity.getCartPrdSku());
        statement.bindLong(4, entity.getCartQty());
        statement.bindDouble(5, entity.getCartPrice());
        statement.bindDouble(6, entity.getCartCostPrice());
        statement.bindDouble(7, entity.getCartSubtotal());
        statement.bindLong(8, entity.getCartStatus());
        statement.bindLong(9, entity.getCartId());
      }
    };
    this.__preparedStmtOfDeleteCartItem = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pos_cart WHERE cart_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfCheckoutCart = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE pos_cart SET cart_status = 1 WHERE cart_user_id = ? AND cart_status = 0";
        return _query;
      }
    };
  }

  @Override
  public Object insertCart(final PosCartEntity cart, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPosCartEntity.insert(cart);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCart(final PosCartEntity cart, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPosCartEntity.handle(cart);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCartItem(final int cartId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCartItem.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cartId);
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
          __preparedStmtOfDeleteCartItem.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object checkoutCart(final int userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCheckoutCart.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
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
          __preparedStmtOfCheckoutCart.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CartItemWithProduct>> getActiveCart(final int userId) {
    final String _sql = "\n"
            + "        SELECT c.*, p.prd_name, p.prd_image \n"
            + "        FROM pos_cart c\n"
            + "        JOIN products p ON c.cart_prd_sku = p.prd_sku\n"
            + "        WHERE c.cart_user_id = ? AND c.cart_status = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pos_cart",
        "products"}, new Callable<List<CartItemWithProduct>>() {
      @Override
      @NonNull
      public List<CartItemWithProduct> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCartId = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_id");
          final int _cursorIndexOfCartUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_user_id");
          final int _cursorIndexOfCartPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_prd_sku");
          final int _cursorIndexOfCartQty = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_qty");
          final int _cursorIndexOfCartPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_price");
          final int _cursorIndexOfCartCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_cost_price");
          final int _cursorIndexOfCartSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_subtotal");
          final int _cursorIndexOfCartStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_status");
          final int _cursorIndexOfPrdName = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_name");
          final int _cursorIndexOfPrdImage = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_image");
          final List<CartItemWithProduct> _result = new ArrayList<CartItemWithProduct>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CartItemWithProduct _item;
            final int _tmpCartId;
            _tmpCartId = _cursor.getInt(_cursorIndexOfCartId);
            final int _tmpCartUserId;
            _tmpCartUserId = _cursor.getInt(_cursorIndexOfCartUserId);
            final String _tmpCartPrdSku;
            _tmpCartPrdSku = _cursor.getString(_cursorIndexOfCartPrdSku);
            final int _tmpCartQty;
            _tmpCartQty = _cursor.getInt(_cursorIndexOfCartQty);
            final double _tmpCartPrice;
            _tmpCartPrice = _cursor.getDouble(_cursorIndexOfCartPrice);
            final double _tmpCartCostPrice;
            _tmpCartCostPrice = _cursor.getDouble(_cursorIndexOfCartCostPrice);
            final double _tmpCartSubtotal;
            _tmpCartSubtotal = _cursor.getDouble(_cursorIndexOfCartSubtotal);
            final int _tmpCartStatus;
            _tmpCartStatus = _cursor.getInt(_cursorIndexOfCartStatus);
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            final String _tmpPrdImage;
            if (_cursor.isNull(_cursorIndexOfPrdImage)) {
              _tmpPrdImage = null;
            } else {
              _tmpPrdImage = _cursor.getString(_cursorIndexOfPrdImage);
            }
            _item = new CartItemWithProduct(_tmpCartId,_tmpCartUserId,_tmpCartPrdSku,_tmpCartQty,_tmpCartPrice,_tmpCartCostPrice,_tmpCartSubtotal,_tmpCartStatus,_tmpPrdName,_tmpPrdImage);
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
  public Object getCartItem(final int userId, final String sku,
      final Continuation<? super PosCartEntity> $completion) {
    final String _sql = "SELECT * FROM pos_cart WHERE cart_user_id = ? AND cart_prd_sku = ? AND cart_status = 0 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindString(_argIndex, sku);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PosCartEntity>() {
      @Override
      @Nullable
      public PosCartEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCartId = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_id");
          final int _cursorIndexOfCartUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_user_id");
          final int _cursorIndexOfCartPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_prd_sku");
          final int _cursorIndexOfCartQty = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_qty");
          final int _cursorIndexOfCartPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_price");
          final int _cursorIndexOfCartCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_cost_price");
          final int _cursorIndexOfCartSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_subtotal");
          final int _cursorIndexOfCartStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_status");
          final PosCartEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpCartId;
            _tmpCartId = _cursor.getInt(_cursorIndexOfCartId);
            final int _tmpCartUserId;
            _tmpCartUserId = _cursor.getInt(_cursorIndexOfCartUserId);
            final String _tmpCartPrdSku;
            _tmpCartPrdSku = _cursor.getString(_cursorIndexOfCartPrdSku);
            final int _tmpCartQty;
            _tmpCartQty = _cursor.getInt(_cursorIndexOfCartQty);
            final double _tmpCartPrice;
            _tmpCartPrice = _cursor.getDouble(_cursorIndexOfCartPrice);
            final double _tmpCartCostPrice;
            _tmpCartCostPrice = _cursor.getDouble(_cursorIndexOfCartCostPrice);
            final double _tmpCartSubtotal;
            _tmpCartSubtotal = _cursor.getDouble(_cursorIndexOfCartSubtotal);
            final int _tmpCartStatus;
            _tmpCartStatus = _cursor.getInt(_cursorIndexOfCartStatus);
            _result = new PosCartEntity(_tmpCartId,_tmpCartUserId,_tmpCartPrdSku,_tmpCartQty,_tmpCartPrice,_tmpCartCostPrice,_tmpCartSubtotal,_tmpCartStatus);
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
  public Object getCartItemById(final int cartId,
      final Continuation<? super PosCartEntity> $completion) {
    final String _sql = "SELECT * FROM pos_cart WHERE cart_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cartId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PosCartEntity>() {
      @Override
      @Nullable
      public PosCartEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCartId = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_id");
          final int _cursorIndexOfCartUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_user_id");
          final int _cursorIndexOfCartPrdSku = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_prd_sku");
          final int _cursorIndexOfCartQty = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_qty");
          final int _cursorIndexOfCartPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_price");
          final int _cursorIndexOfCartCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_cost_price");
          final int _cursorIndexOfCartSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_subtotal");
          final int _cursorIndexOfCartStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "cart_status");
          final PosCartEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpCartId;
            _tmpCartId = _cursor.getInt(_cursorIndexOfCartId);
            final int _tmpCartUserId;
            _tmpCartUserId = _cursor.getInt(_cursorIndexOfCartUserId);
            final String _tmpCartPrdSku;
            _tmpCartPrdSku = _cursor.getString(_cursorIndexOfCartPrdSku);
            final int _tmpCartQty;
            _tmpCartQty = _cursor.getInt(_cursorIndexOfCartQty);
            final double _tmpCartPrice;
            _tmpCartPrice = _cursor.getDouble(_cursorIndexOfCartPrice);
            final double _tmpCartCostPrice;
            _tmpCartCostPrice = _cursor.getDouble(_cursorIndexOfCartCostPrice);
            final double _tmpCartSubtotal;
            _tmpCartSubtotal = _cursor.getDouble(_cursorIndexOfCartSubtotal);
            final int _tmpCartStatus;
            _tmpCartStatus = _cursor.getInt(_cursorIndexOfCartStatus);
            _result = new PosCartEntity(_tmpCartId,_tmpCartUserId,_tmpCartPrdSku,_tmpCartQty,_tmpCartPrice,_tmpCartCostPrice,_tmpCartSubtotal,_tmpCartStatus);
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

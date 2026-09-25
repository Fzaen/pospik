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
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
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
public final class SaleDao_Impl implements SaleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SaleEntity> __insertionAdapterOfSaleEntity;

  private final EntityInsertionAdapter<SaleItemEntity> __insertionAdapterOfSaleItemEntity;

  public SaleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSaleEntity = new EntityInsertionAdapter<SaleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sales` (`sls_invoice_number`,`sls_transaction_date`,`sls_user_id`,`sls_subtotal`,`sls_discount_amount`,`sls_grand_total`,`sls_paid_amount`,`sls_change_amount`,`sls_payment_method`,`sls_total_item`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SaleEntity entity) {
        statement.bindString(1, entity.getSlsInvoiceNumber());
        statement.bindString(2, entity.getSlsTransactionDate());
        statement.bindLong(3, entity.getSlsUserId());
        statement.bindDouble(4, entity.getSlsSubtotal());
        statement.bindDouble(5, entity.getSlsDiscountAmount());
        statement.bindDouble(6, entity.getSlsGrandTotal());
        statement.bindDouble(7, entity.getSlsPaidAmount());
        statement.bindDouble(8, entity.getSlsChangeAmount());
        statement.bindString(9, entity.getSlsPaymentMethod());
        statement.bindLong(10, entity.getSlsTotalItem());
      }
    };
    this.__insertionAdapterOfSaleItemEntity = new EntityInsertionAdapter<SaleItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sale_items` (`itm_id`,`itm_sale_id`,`itm_sku`,`itm_discount`,`itm_cashback`,`itm_quantity`,`itm_unit_price`,`itm_cost_price`,`itm_subtotal`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SaleItemEntity entity) {
        statement.bindLong(1, entity.getItmId());
        statement.bindString(2, entity.getItmSaleId());
        statement.bindString(3, entity.getItmSku());
        statement.bindDouble(4, entity.getItmDiscount());
        statement.bindDouble(5, entity.getItmCashback());
        statement.bindLong(6, entity.getItmQuantity());
        statement.bindDouble(7, entity.getItmUnitPrice());
        statement.bindDouble(8, entity.getItmCostPrice());
        statement.bindDouble(9, entity.getItmSubtotal());
      }
    };
  }

  @Override
  public Object insertSale(final SaleEntity sale, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSaleEntity.insert(sale);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSaleItems(final List<SaleItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSaleItemEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLastSale(final Continuation<? super SaleEntity> $completion) {
    final String _sql = "SELECT * FROM sales ORDER BY sls_transaction_date DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SaleEntity>() {
      @Override
      @Nullable
      public SaleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSlsInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_invoice_number");
          final int _cursorIndexOfSlsTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_transaction_date");
          final int _cursorIndexOfSlsUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_user_id");
          final int _cursorIndexOfSlsSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_subtotal");
          final int _cursorIndexOfSlsDiscountAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_discount_amount");
          final int _cursorIndexOfSlsGrandTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_grand_total");
          final int _cursorIndexOfSlsPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_paid_amount");
          final int _cursorIndexOfSlsChangeAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_change_amount");
          final int _cursorIndexOfSlsPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_payment_method");
          final int _cursorIndexOfSlsTotalItem = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_total_item");
          final SaleEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSlsInvoiceNumber;
            _tmpSlsInvoiceNumber = _cursor.getString(_cursorIndexOfSlsInvoiceNumber);
            final String _tmpSlsTransactionDate;
            _tmpSlsTransactionDate = _cursor.getString(_cursorIndexOfSlsTransactionDate);
            final int _tmpSlsUserId;
            _tmpSlsUserId = _cursor.getInt(_cursorIndexOfSlsUserId);
            final double _tmpSlsSubtotal;
            _tmpSlsSubtotal = _cursor.getDouble(_cursorIndexOfSlsSubtotal);
            final double _tmpSlsDiscountAmount;
            _tmpSlsDiscountAmount = _cursor.getDouble(_cursorIndexOfSlsDiscountAmount);
            final double _tmpSlsGrandTotal;
            _tmpSlsGrandTotal = _cursor.getDouble(_cursorIndexOfSlsGrandTotal);
            final double _tmpSlsPaidAmount;
            _tmpSlsPaidAmount = _cursor.getDouble(_cursorIndexOfSlsPaidAmount);
            final double _tmpSlsChangeAmount;
            _tmpSlsChangeAmount = _cursor.getDouble(_cursorIndexOfSlsChangeAmount);
            final String _tmpSlsPaymentMethod;
            _tmpSlsPaymentMethod = _cursor.getString(_cursorIndexOfSlsPaymentMethod);
            final int _tmpSlsTotalItem;
            _tmpSlsTotalItem = _cursor.getInt(_cursorIndexOfSlsTotalItem);
            _result = new SaleEntity(_tmpSlsInvoiceNumber,_tmpSlsTransactionDate,_tmpSlsUserId,_tmpSlsSubtotal,_tmpSlsDiscountAmount,_tmpSlsGrandTotal,_tmpSlsPaidAmount,_tmpSlsChangeAmount,_tmpSlsPaymentMethod,_tmpSlsTotalItem);
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
  public Flow<List<SaleWithUser>> getSalesHistory(final String startDate, final String endDate) {
    final String _sql = "\n"
            + "        SELECT s.*, u.usr_username \n"
            + "        FROM sales s\n"
            + "        JOIN users u ON s.sls_user_id = u.usr_id\n"
            + "        WHERE DATE(s.sls_transaction_date) BETWEEN DATE(?) AND DATE(?)\n"
            + "        ORDER BY s.sls_transaction_date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sales",
        "users"}, new Callable<List<SaleWithUser>>() {
      @Override
      @NonNull
      public List<SaleWithUser> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSlsInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_invoice_number");
          final int _cursorIndexOfSlsTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_transaction_date");
          final int _cursorIndexOfSlsUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_user_id");
          final int _cursorIndexOfSlsSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_subtotal");
          final int _cursorIndexOfSlsDiscountAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_discount_amount");
          final int _cursorIndexOfSlsGrandTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_grand_total");
          final int _cursorIndexOfSlsPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_paid_amount");
          final int _cursorIndexOfSlsChangeAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_change_amount");
          final int _cursorIndexOfSlsPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_payment_method");
          final int _cursorIndexOfSlsTotalItem = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_total_item");
          final int _cursorIndexOfUsrUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_username");
          final List<SaleWithUser> _result = new ArrayList<SaleWithUser>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SaleWithUser _item;
            final String _tmpSlsInvoiceNumber;
            _tmpSlsInvoiceNumber = _cursor.getString(_cursorIndexOfSlsInvoiceNumber);
            final String _tmpSlsTransactionDate;
            _tmpSlsTransactionDate = _cursor.getString(_cursorIndexOfSlsTransactionDate);
            final int _tmpSlsUserId;
            _tmpSlsUserId = _cursor.getInt(_cursorIndexOfSlsUserId);
            final double _tmpSlsSubtotal;
            _tmpSlsSubtotal = _cursor.getDouble(_cursorIndexOfSlsSubtotal);
            final double _tmpSlsDiscountAmount;
            _tmpSlsDiscountAmount = _cursor.getDouble(_cursorIndexOfSlsDiscountAmount);
            final double _tmpSlsGrandTotal;
            _tmpSlsGrandTotal = _cursor.getDouble(_cursorIndexOfSlsGrandTotal);
            final double _tmpSlsPaidAmount;
            _tmpSlsPaidAmount = _cursor.getDouble(_cursorIndexOfSlsPaidAmount);
            final double _tmpSlsChangeAmount;
            _tmpSlsChangeAmount = _cursor.getDouble(_cursorIndexOfSlsChangeAmount);
            final String _tmpSlsPaymentMethod;
            _tmpSlsPaymentMethod = _cursor.getString(_cursorIndexOfSlsPaymentMethod);
            final int _tmpSlsTotalItem;
            _tmpSlsTotalItem = _cursor.getInt(_cursorIndexOfSlsTotalItem);
            final String _tmpUsrUsername;
            _tmpUsrUsername = _cursor.getString(_cursorIndexOfUsrUsername);
            _item = new SaleWithUser(_tmpSlsInvoiceNumber,_tmpSlsTransactionDate,_tmpSlsUserId,_tmpSlsSubtotal,_tmpSlsDiscountAmount,_tmpSlsGrandTotal,_tmpSlsPaidAmount,_tmpSlsChangeAmount,_tmpSlsPaymentMethod,_tmpSlsTotalItem,_tmpUsrUsername);
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
  public Object getSaleByInvoice(final String invoiceNumber,
      final Continuation<? super SaleWithUser> $completion) {
    final String _sql = "\n"
            + "        SELECT s.*, u.usr_username \n"
            + "        FROM sales s\n"
            + "        JOIN users u ON s.sls_user_id = u.usr_id\n"
            + "        WHERE s.sls_invoice_number = ?\n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, invoiceNumber);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SaleWithUser>() {
      @Override
      @Nullable
      public SaleWithUser call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSlsInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_invoice_number");
          final int _cursorIndexOfSlsTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_transaction_date");
          final int _cursorIndexOfSlsUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_user_id");
          final int _cursorIndexOfSlsSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_subtotal");
          final int _cursorIndexOfSlsDiscountAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_discount_amount");
          final int _cursorIndexOfSlsGrandTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_grand_total");
          final int _cursorIndexOfSlsPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_paid_amount");
          final int _cursorIndexOfSlsChangeAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_change_amount");
          final int _cursorIndexOfSlsPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_payment_method");
          final int _cursorIndexOfSlsTotalItem = CursorUtil.getColumnIndexOrThrow(_cursor, "sls_total_item");
          final int _cursorIndexOfUsrUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "usr_username");
          final SaleWithUser _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSlsInvoiceNumber;
            _tmpSlsInvoiceNumber = _cursor.getString(_cursorIndexOfSlsInvoiceNumber);
            final String _tmpSlsTransactionDate;
            _tmpSlsTransactionDate = _cursor.getString(_cursorIndexOfSlsTransactionDate);
            final int _tmpSlsUserId;
            _tmpSlsUserId = _cursor.getInt(_cursorIndexOfSlsUserId);
            final double _tmpSlsSubtotal;
            _tmpSlsSubtotal = _cursor.getDouble(_cursorIndexOfSlsSubtotal);
            final double _tmpSlsDiscountAmount;
            _tmpSlsDiscountAmount = _cursor.getDouble(_cursorIndexOfSlsDiscountAmount);
            final double _tmpSlsGrandTotal;
            _tmpSlsGrandTotal = _cursor.getDouble(_cursorIndexOfSlsGrandTotal);
            final double _tmpSlsPaidAmount;
            _tmpSlsPaidAmount = _cursor.getDouble(_cursorIndexOfSlsPaidAmount);
            final double _tmpSlsChangeAmount;
            _tmpSlsChangeAmount = _cursor.getDouble(_cursorIndexOfSlsChangeAmount);
            final String _tmpSlsPaymentMethod;
            _tmpSlsPaymentMethod = _cursor.getString(_cursorIndexOfSlsPaymentMethod);
            final int _tmpSlsTotalItem;
            _tmpSlsTotalItem = _cursor.getInt(_cursorIndexOfSlsTotalItem);
            final String _tmpUsrUsername;
            _tmpUsrUsername = _cursor.getString(_cursorIndexOfUsrUsername);
            _result = new SaleWithUser(_tmpSlsInvoiceNumber,_tmpSlsTransactionDate,_tmpSlsUserId,_tmpSlsSubtotal,_tmpSlsDiscountAmount,_tmpSlsGrandTotal,_tmpSlsPaidAmount,_tmpSlsChangeAmount,_tmpSlsPaymentMethod,_tmpSlsTotalItem,_tmpUsrUsername);
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
  public Object getSaleItemsByInvoice(final String invoiceNumber,
      final Continuation<? super List<SaleItemWithProduct>> $completion) {
    final String _sql = "\n"
            + "        SELECT si.*, p.prd_name \n"
            + "        FROM sale_items si\n"
            + "        JOIN products p ON si.itm_sku = p.prd_sku\n"
            + "        WHERE si.itm_sale_id = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, invoiceNumber);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SaleItemWithProduct>>() {
      @Override
      @NonNull
      public List<SaleItemWithProduct> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfItmId = CursorUtil.getColumnIndexOrThrow(_cursor, "itm_id");
          final int _cursorIndexOfItmSaleId = CursorUtil.getColumnIndexOrThrow(_cursor, "itm_sale_id");
          final int _cursorIndexOfItmSku = CursorUtil.getColumnIndexOrThrow(_cursor, "itm_sku");
          final int _cursorIndexOfItmDiscount = CursorUtil.getColumnIndexOrThrow(_cursor, "itm_discount");
          final int _cursorIndexOfItmCashback = CursorUtil.getColumnIndexOrThrow(_cursor, "itm_cashback");
          final int _cursorIndexOfItmQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "itm_quantity");
          final int _cursorIndexOfItmUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "itm_unit_price");
          final int _cursorIndexOfItmCostPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "itm_cost_price");
          final int _cursorIndexOfItmSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "itm_subtotal");
          final int _cursorIndexOfPrdName = CursorUtil.getColumnIndexOrThrow(_cursor, "prd_name");
          final List<SaleItemWithProduct> _result = new ArrayList<SaleItemWithProduct>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SaleItemWithProduct _item;
            final int _tmpItmId;
            _tmpItmId = _cursor.getInt(_cursorIndexOfItmId);
            final String _tmpItmSaleId;
            _tmpItmSaleId = _cursor.getString(_cursorIndexOfItmSaleId);
            final String _tmpItmSku;
            _tmpItmSku = _cursor.getString(_cursorIndexOfItmSku);
            final double _tmpItmDiscount;
            _tmpItmDiscount = _cursor.getDouble(_cursorIndexOfItmDiscount);
            final double _tmpItmCashback;
            _tmpItmCashback = _cursor.getDouble(_cursorIndexOfItmCashback);
            final int _tmpItmQuantity;
            _tmpItmQuantity = _cursor.getInt(_cursorIndexOfItmQuantity);
            final double _tmpItmUnitPrice;
            _tmpItmUnitPrice = _cursor.getDouble(_cursorIndexOfItmUnitPrice);
            final double _tmpItmCostPrice;
            _tmpItmCostPrice = _cursor.getDouble(_cursorIndexOfItmCostPrice);
            final double _tmpItmSubtotal;
            _tmpItmSubtotal = _cursor.getDouble(_cursorIndexOfItmSubtotal);
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            _item = new SaleItemWithProduct(_tmpItmId,_tmpItmSaleId,_tmpItmSku,_tmpItmDiscount,_tmpItmCashback,_tmpItmQuantity,_tmpItmUnitPrice,_tmpItmCostPrice,_tmpItmSubtotal,_tmpPrdName);
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

  @Override
  public Flow<List<ProfitReportRow>> getProfitReport(final String startDate, final String endDate) {
    final String _sql = "\n"
            + "        SELECT DATE(sls_transaction_date) as date, COUNT(*) as total_invoices, SUM(sls_grand_total) as total_revenue,\n"
            + "        SUM((SELECT SUM(itm_quantity * itm_cost_price) FROM sale_items WHERE itm_sale_id = sls_invoice_number)) as total_cost\n"
            + "        FROM sales \n"
            + "        WHERE DATE(sls_transaction_date) BETWEEN DATE(?) AND DATE(?)\n"
            + "        GROUP BY DATE(sls_transaction_date) \n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sale_items",
        "sales"}, new Callable<List<ProfitReportRow>>() {
      @Override
      @NonNull
      public List<ProfitReportRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = 0;
          final int _cursorIndexOfTotalInvoices = 1;
          final int _cursorIndexOfTotalRevenue = 2;
          final int _cursorIndexOfTotalCost = 3;
          final List<ProfitReportRow> _result = new ArrayList<ProfitReportRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProfitReportRow _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpTotalInvoices;
            _tmpTotalInvoices = _cursor.getInt(_cursorIndexOfTotalInvoices);
            final double _tmpTotalRevenue;
            _tmpTotalRevenue = _cursor.getDouble(_cursorIndexOfTotalRevenue);
            final double _tmpTotalCost;
            _tmpTotalCost = _cursor.getDouble(_cursorIndexOfTotalCost);
            _item = new ProfitReportRow(_tmpDate,_tmpTotalInvoices,_tmpTotalRevenue,_tmpTotalCost);
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
  public Flow<List<ItemSalesReportRow>> getSalesByItemReport(final String startDate,
      final String endDate) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "          si.itm_sku, \n"
            + "          p.prd_name,\n"
            + "          c.cat_name,\n"
            + "          c.cat_subname,\n"
            + "          p.prd_cost_price as cost_price,\n"
            + "          p.prd_selling_price as selling_price,\n"
            + "          SUM(si.itm_quantity) as total_qty,\n"
            + "          SUM(si.itm_quantity * si.itm_cost_price) as total_cost,\n"
            + "          SUM(si.itm_subtotal) as total_sales,\n"
            + "          SUM(si.itm_subtotal - (si.itm_quantity * si.itm_cost_price)) as total_profit,\n"
            + "          COUNT(DISTINCT si.itm_sale_id) as total_trx\n"
            + "        FROM sale_items si\n"
            + "        JOIN products p ON si.itm_sku = p.prd_sku\n"
            + "        JOIN categories c ON p.prd_category_id = c.cat_id\n"
            + "        JOIN sales s ON si.itm_sale_id = s.sls_invoice_number\n"
            + "        WHERE DATE(s.sls_transaction_date) BETWEEN DATE(?) AND DATE(?)\n"
            + "        GROUP BY si.itm_sku, p.prd_name, c.cat_name, c.cat_subname, p.prd_cost_price, p.prd_selling_price\n"
            + "        ORDER BY total_qty DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sale_items", "products",
        "categories", "sales"}, new Callable<List<ItemSalesReportRow>>() {
      @Override
      @NonNull
      public List<ItemSalesReportRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfItmSku = 0;
          final int _cursorIndexOfPrdName = 1;
          final int _cursorIndexOfCatName = 2;
          final int _cursorIndexOfCatSubname = 3;
          final int _cursorIndexOfCostPrice = 4;
          final int _cursorIndexOfSellingPrice = 5;
          final int _cursorIndexOfTotalQty = 6;
          final int _cursorIndexOfTotalCost = 7;
          final int _cursorIndexOfTotalSales = 8;
          final int _cursorIndexOfTotalProfit = 9;
          final int _cursorIndexOfTotalTrx = 10;
          final List<ItemSalesReportRow> _result = new ArrayList<ItemSalesReportRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ItemSalesReportRow _item;
            final String _tmpItmSku;
            _tmpItmSku = _cursor.getString(_cursorIndexOfItmSku);
            final String _tmpPrdName;
            _tmpPrdName = _cursor.getString(_cursorIndexOfPrdName);
            final String _tmpCatName;
            _tmpCatName = _cursor.getString(_cursorIndexOfCatName);
            final String _tmpCatSubname;
            _tmpCatSubname = _cursor.getString(_cursorIndexOfCatSubname);
            final double _tmpCostPrice;
            _tmpCostPrice = _cursor.getDouble(_cursorIndexOfCostPrice);
            final double _tmpSellingPrice;
            _tmpSellingPrice = _cursor.getDouble(_cursorIndexOfSellingPrice);
            final int _tmpTotalQty;
            _tmpTotalQty = _cursor.getInt(_cursorIndexOfTotalQty);
            final double _tmpTotalCost;
            _tmpTotalCost = _cursor.getDouble(_cursorIndexOfTotalCost);
            final double _tmpTotalSales;
            _tmpTotalSales = _cursor.getDouble(_cursorIndexOfTotalSales);
            final double _tmpTotalProfit;
            _tmpTotalProfit = _cursor.getDouble(_cursorIndexOfTotalProfit);
            final int _tmpTotalTrx;
            _tmpTotalTrx = _cursor.getInt(_cursorIndexOfTotalTrx);
            _item = new ItemSalesReportRow(_tmpItmSku,_tmpPrdName,_tmpCatName,_tmpCatSubname,_tmpCostPrice,_tmpSellingPrice,_tmpTotalQty,_tmpTotalCost,_tmpTotalSales,_tmpTotalProfit,_tmpTotalTrx);
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
  public Flow<List<CategorySalesReportRow>> getSalesByCategoryReport(final String startDate,
      final String endDate) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "          c.cat_name,\n"
            + "          SUM(si.itm_quantity) as total_qty,\n"
            + "          SUM(si.itm_quantity * si.itm_cost_price) as total_cost,\n"
            + "          SUM(si.itm_subtotal) as total_sales,\n"
            + "          SUM(si.itm_subtotal - (si.itm_quantity * si.itm_cost_price)) as total_profit,\n"
            + "          COUNT(DISTINCT si.itm_sale_id) as total_trx\n"
            + "        FROM sale_items si\n"
            + "        JOIN products p ON si.itm_sku = p.prd_sku\n"
            + "        JOIN categories c ON p.prd_category_id = c.cat_id\n"
            + "        JOIN sales s ON si.itm_sale_id = s.sls_invoice_number\n"
            + "        WHERE DATE(s.sls_transaction_date) BETWEEN DATE(?) AND DATE(?)\n"
            + "        GROUP BY c.cat_name\n"
            + "        ORDER BY total_sales DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sale_items", "products",
        "categories", "sales"}, new Callable<List<CategorySalesReportRow>>() {
      @Override
      @NonNull
      public List<CategorySalesReportRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCatName = 0;
          final int _cursorIndexOfTotalQty = 1;
          final int _cursorIndexOfTotalCost = 2;
          final int _cursorIndexOfTotalSales = 3;
          final int _cursorIndexOfTotalProfit = 4;
          final int _cursorIndexOfTotalTrx = 5;
          final List<CategorySalesReportRow> _result = new ArrayList<CategorySalesReportRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategorySalesReportRow _item;
            final String _tmpCatName;
            _tmpCatName = _cursor.getString(_cursorIndexOfCatName);
            final int _tmpTotalQty;
            _tmpTotalQty = _cursor.getInt(_cursorIndexOfTotalQty);
            final double _tmpTotalCost;
            _tmpTotalCost = _cursor.getDouble(_cursorIndexOfTotalCost);
            final double _tmpTotalSales;
            _tmpTotalSales = _cursor.getDouble(_cursorIndexOfTotalSales);
            final double _tmpTotalProfit;
            _tmpTotalProfit = _cursor.getDouble(_cursorIndexOfTotalProfit);
            final int _tmpTotalTrx;
            _tmpTotalTrx = _cursor.getInt(_cursorIndexOfTotalTrx);
            _item = new CategorySalesReportRow(_tmpCatName,_tmpTotalQty,_tmpTotalCost,_tmpTotalSales,_tmpTotalProfit,_tmpTotalTrx);
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
  public Flow<List<SubCategorySalesReportRow>> getSalesBySubCategoryReport(final String startDate,
      final String endDate) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "          c.cat_name,\n"
            + "          c.cat_subname,\n"
            + "          SUM(si.itm_quantity) as total_qty,\n"
            + "          SUM(si.itm_quantity * si.itm_cost_price) as total_cost,\n"
            + "          SUM(si.itm_subtotal) as total_sales,\n"
            + "          SUM(si.itm_subtotal - (si.itm_quantity * si.itm_cost_price)) as total_profit,\n"
            + "          COUNT(DISTINCT si.itm_sale_id) as total_trx\n"
            + "        FROM sale_items si\n"
            + "        JOIN products p ON si.itm_sku = p.prd_sku\n"
            + "        JOIN categories c ON p.prd_category_id = c.cat_id\n"
            + "        JOIN sales s ON si.itm_sale_id = s.sls_invoice_number\n"
            + "        WHERE DATE(s.sls_transaction_date) BETWEEN DATE(?) AND DATE(?)\n"
            + "        GROUP BY c.cat_name, c.cat_subname\n"
            + "        ORDER BY total_sales DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sale_items", "products",
        "categories", "sales"}, new Callable<List<SubCategorySalesReportRow>>() {
      @Override
      @NonNull
      public List<SubCategorySalesReportRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCatName = 0;
          final int _cursorIndexOfCatSubname = 1;
          final int _cursorIndexOfTotalQty = 2;
          final int _cursorIndexOfTotalCost = 3;
          final int _cursorIndexOfTotalSales = 4;
          final int _cursorIndexOfTotalProfit = 5;
          final int _cursorIndexOfTotalTrx = 6;
          final List<SubCategorySalesReportRow> _result = new ArrayList<SubCategorySalesReportRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubCategorySalesReportRow _item;
            final String _tmpCatName;
            _tmpCatName = _cursor.getString(_cursorIndexOfCatName);
            final String _tmpCatSubname;
            _tmpCatSubname = _cursor.getString(_cursorIndexOfCatSubname);
            final int _tmpTotalQty;
            _tmpTotalQty = _cursor.getInt(_cursorIndexOfTotalQty);
            final double _tmpTotalCost;
            _tmpTotalCost = _cursor.getDouble(_cursorIndexOfTotalCost);
            final double _tmpTotalSales;
            _tmpTotalSales = _cursor.getDouble(_cursorIndexOfTotalSales);
            final double _tmpTotalProfit;
            _tmpTotalProfit = _cursor.getDouble(_cursorIndexOfTotalProfit);
            final int _tmpTotalTrx;
            _tmpTotalTrx = _cursor.getInt(_cursorIndexOfTotalTrx);
            _item = new SubCategorySalesReportRow(_tmpCatName,_tmpCatSubname,_tmpTotalQty,_tmpTotalCost,_tmpTotalSales,_tmpTotalProfit,_tmpTotalTrx);
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
  public Object getTodayCount(final String today, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM sales WHERE DATE(sls_transaction_date) = DATE(?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, today);
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

  @Override
  public Object getTodayOmzet(final String today, final Continuation<? super Double> $completion) {
    final String _sql = "SELECT COALESCE(SUM(sls_grand_total), 0.0) FROM sales WHERE DATE(sls_transaction_date) = DATE(?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, today);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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

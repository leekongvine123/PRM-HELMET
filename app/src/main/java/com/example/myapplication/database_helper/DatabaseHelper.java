package com.example.myapplication.database_helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.model.Helmet;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderDetail;
import com.example.myapplication.model.Payment;
import com.example.myapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu
    private static final String DATABASE_NAME = "helmet_shop.db";

    // Phiên bản cơ sở dữ liệu
    private static final int DATABASE_VERSION = 4;

    // Tên các bảng
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_HELMETS = "Helmets";
    private static final String TABLE_ORDERS = "Orders";
    private static final String TABLE_ORDER_DETAILS = "OrderDetails";
    private static final String TABLE_PAYMENTS = "Payments";

    // Tên các cột
    private static final String COLUMN_USER_ID = "Id";
    private static final String COLUMN_USER_NAME = "Name";
    private static final String COLUMN_USER_EMAIL = "Email";
    private static final String COLUMN_USER_PHONE = "Phone";
    private static final String COLUMN_USER_ADDRESS = "Address";
    private static final String COLUMN_USER_CREATED_AT = "CreatedAt";

    private static final String COLUMN_HELMET_ID = "HelmetID";
    private static final String COLUMN_HELMET_NAME = "Name";
    private static final String COLUMN_HELMET_PRODUCT_CODE="ProductCode";
    private static final String COLUMN_HELMET_BRAND = "Brand";
    private static final String COLUMN_HELMET_SIZE = "Size";
    private static final String COLUMN_HELMET_COLOR = "Color";
    private static final String COLUMN_HELMET_DESCRIPTION = "Description";
    private static final String COLUMN_HELMET_PRICE = "Price";
    private static final String COLUMN_HELMET_STOCK = "Stock";
    private static final String COLUMN_HELMET_IMAGE_URL = "ImageURL";
    private static final String COLUMN_HELMET_CREATED_AT = "CreatedAt";

    private static final String COLUMN_ORDER_ID = "OrderID";
    private static final String COLUMN_ORDER_CUSTOMER_ID = "CustomerID";
    private static final String COLUMN_ORDER_DATE = "OrderDate";
    private static final String COLUMN_ORDER_TOTAL_AMOUNT = "TotalAmount";
    private static final String COLUMN_ORDER_PAYMENT_STATUS = "PaymentStatus";

    private static final String COLUMN_ORDER_DETAIL_ID = "OrderDetailID";
    private static final String COLUMN_ORDER_DETAIL_ORDER_ID = "OrderID";
    private static final String COLUMN_ORDER_DETAIL_HELMET_ID = "HelmetID";
    private static final String COLUMN_ORDER_DETAIL_QUANTITY = "Quantity";
    private static final String COLUMN_ORDER_DETAIL_PRICE = "Price";

    private static final String COLUMN_PAYMENT_ID = "PaymentID";
    private static final String COLUMN_PAYMENT_ORDER_ID = "OrderID";
    private static final String COLUMN_PAYMENT_DATE = "PaymentDate";
    private static final String COLUMN_PAYMENT_AMOUNT = "Amount";
    private static final String COLUMN_PAYMENT_METHOD = "PaymentMethod";

    // Câu lệnh SQL để tạo bảng
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USER_NAME + " TEXT," +
            COLUMN_USER_EMAIL + " TEXT UNIQUE," +
            COLUMN_USER_PHONE + " TEXT," +
            COLUMN_USER_ADDRESS + " TEXT," +
            COLUMN_USER_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ")";

    private static final String CREATE_TABLE_HELMETS = "CREATE TABLE " + TABLE_HELMETS + "(" +
            COLUMN_HELMET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_HELMET_NAME + " TEXT," +
            COLUMN_HELMET_PRODUCT_CODE + " TEXT," +
            COLUMN_HELMET_BRAND + " TEXT," +
            COLUMN_HELMET_SIZE + " TEXT," +
            COLUMN_HELMET_COLOR + " TEXT," +
            COLUMN_HELMET_DESCRIPTION + " TEXT," +
            COLUMN_HELMET_PRICE + " DECIMAL(10, 2)," +
            COLUMN_HELMET_STOCK + " INTEGER," +
            COLUMN_HELMET_IMAGE_URL + " TEXT," +
            COLUMN_HELMET_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ")";

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "(" +
            COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ORDER_CUSTOMER_ID + " INTEGER," +
            COLUMN_ORDER_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
            COLUMN_ORDER_TOTAL_AMOUNT + " DECIMAL(10, 2)," +
            COLUMN_ORDER_PAYMENT_STATUS + " TEXT," +
            "FOREIGN KEY (" + COLUMN_ORDER_CUSTOMER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
            ")";

    private static final String CREATE_TABLE_ORDER_DETAILS = "CREATE TABLE " + TABLE_ORDER_DETAILS + "(" +
            COLUMN_ORDER_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ORDER_DETAIL_ORDER_ID + " INTEGER," +
            COLUMN_ORDER_DETAIL_HELMET_ID + " INTEGER," +
            COLUMN_ORDER_DETAIL_QUANTITY + " INTEGER," +
            COLUMN_ORDER_DETAIL_PRICE + " DECIMAL(10, 2)," +
            "FOREIGN KEY (" + COLUMN_ORDER_DETAIL_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ORDER_ID + ")," +
            "FOREIGN KEY (" + COLUMN_ORDER_DETAIL_HELMET_ID + ") REFERENCES " + TABLE_HELMETS + "(" + COLUMN_HELMET_ID + ")" +
            ")";

    private static final String CREATE_TABLE_PAYMENTS = "CREATE TABLE " + TABLE_PAYMENTS + "(" +
            COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_PAYMENT_ORDER_ID + " INTEGER," +
            COLUMN_PAYMENT_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
            COLUMN_PAYMENT_AMOUNT + " DECIMAL(10, 2)," +
            COLUMN_PAYMENT_METHOD + " TEXT," +
            "FOREIGN KEY (" + COLUMN_PAYMENT_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ORDER_ID + ")" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo các bảng
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_HELMETS);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_ORDER_DETAILS);
        db.execSQL(CREATE_TABLE_PAYMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ và taạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELMETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        onCreate(db);
    }

    // --- Users ---

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        long newRowId = db.insert(TABLE_USERS, null, values);
        return newRowId;
    }

    @SuppressLint("Range")
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
            user.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
            user.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_AT)));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    @SuppressLint("Range")
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
                user.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
                user.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_AT)));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }

    public int deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    // --- Helmets ---

    public long addHelmet(Helmet helmet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HELMET_NAME, helmet.getName());
        values.put(COLUMN_HELMET_BRAND, helmet.getBrand());
        values.put(COLUMN_HELMET_PRODUCT_CODE, helmet.getProductCode());
        values.put(COLUMN_HELMET_SIZE, helmet.getSize());
        values.put(COLUMN_HELMET_COLOR, helmet.getColor());
        values.put(COLUMN_HELMET_DESCRIPTION, helmet.getDescription());
        values.put(COLUMN_HELMET_PRICE, helmet.getPrice());
        values.put(COLUMN_HELMET_STOCK, helmet.getStock());
        values.put(COLUMN_HELMET_IMAGE_URL, helmet.getImageUrl());
        long newRowId = db.insert(TABLE_HELMETS, null, values);
        return newRowId;
    }

    @SuppressLint("Range")
    public Helmet getHelmetById(int helmetId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HELMETS, null, COLUMN_HELMET_ID + " = ?", new String[]{String.valueOf(helmetId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Helmet helmet = new Helmet();
            helmet.setHelmetID(cursor.getInt(cursor.getColumnIndex(COLUMN_HELMET_ID)));
            helmet.setName(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_NAME)));
            helmet.setProductCode(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_PRODUCT_CODE)));
            helmet.setBrand(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_BRAND)));
            helmet.setSize(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_SIZE)));
            helmet.setColor(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_COLOR)));
            helmet.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_DESCRIPTION)));
            helmet.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_HELMET_PRICE)));
            helmet.setStock(cursor.getInt(cursor.getColumnIndex(COLUMN_HELMET_STOCK)));
            helmet.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_IMAGE_URL)));
            helmet.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_CREATED_AT)));
            cursor.close();
            return helmet;
        }
        cursor.close();
        return null;
    }

    @SuppressLint("Range")
    public List<Helmet> getHelmets() {
        List<Helmet> helmets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to get distinct helmets by product code
        String query = "SELECT * FROM " + TABLE_HELMETS +
                " WHERE " + COLUMN_HELMET_ID + " IN ( " +
                "SELECT MIN(" + COLUMN_HELMET_ID + ") FROM " + TABLE_HELMETS +
                " GROUP BY " + COLUMN_HELMET_PRODUCT_CODE + ")";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Helmet helmet = new Helmet();
                helmet.setProductCode(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_PRODUCT_CODE)));
                helmet.setHelmetID(cursor.getInt(cursor.getColumnIndex(COLUMN_HELMET_ID)));
                helmet.setName(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_NAME)));
                helmet.setBrand(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_BRAND)));
                helmet.setSize(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_SIZE)));
                helmet.setColor(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_COLOR)));
                helmet.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_DESCRIPTION)));
                helmet.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_HELMET_PRICE)));
                helmet.setStock(cursor.getInt(cursor.getColumnIndex(COLUMN_HELMET_STOCK)));
                helmet.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_IMAGE_URL)));
                helmet.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_CREATED_AT)));
                helmets.add(helmet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return helmets;
    }


    public int updateHelmet(Helmet helmet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HELMET_NAME, helmet.getName());
        values.put(COLUMN_HELMET_BRAND, helmet.getBrand());
        values.put(COLUMN_HELMET_SIZE, helmet.getSize());
        values.put(COLUMN_HELMET_COLOR, helmet.getColor());
        values.put(COLUMN_HELMET_DESCRIPTION, helmet.getDescription());
        values.put(COLUMN_HELMET_PRICE, helmet.getPrice());
        values.put(COLUMN_HELMET_STOCK, helmet.getStock());
        values.put(COLUMN_HELMET_IMAGE_URL, helmet.getImageUrl());
        return db.update(TABLE_HELMETS, values, COLUMN_HELMET_ID + " = ?", new String[]{String.valueOf(helmet.getHelmetID())});
    }

    public int deleteHelmet(int helmetId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_HELMETS, COLUMN_HELMET_ID + " = ?", new String[]{String.valueOf(helmetId)});
    }

    // Modify your DatabaseHelper class
    @SuppressLint("Range")
    public List<Helmet> getHelmetsByProductCode(String productCode) {
        List<Helmet> helmetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Helmets WHERE productCode = ?", new String[]{productCode});
        if (cursor.moveToFirst()) {
            do {
                Helmet helmet = new Helmet();
                helmet.setProductCode(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_PRODUCT_CODE)));
                helmet.setHelmetID(cursor.getInt(cursor.getColumnIndex(COLUMN_HELMET_ID)));
                helmet.setName(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_NAME)));
                helmet.setBrand(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_BRAND)));
                helmet.setSize(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_SIZE)));
                helmet.setColor(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_COLOR)));
                helmet.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_DESCRIPTION)));
                helmet.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_HELMET_PRICE)));
                helmet.setStock(cursor.getInt(cursor.getColumnIndex(COLUMN_HELMET_STOCK)));
                helmet.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_IMAGE_URL)));
                helmet.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_HELMET_CREATED_AT)));
                helmetList.add(helmet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return helmetList;
    }

    // --- Orders ---

    public long addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_CUSTOMER_ID, order.getCustomerID());
        values.put(COLUMN_ORDER_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COLUMN_ORDER_PAYMENT_STATUS, order.getPaymentStatus());
        long newRowId = db.insert(TABLE_ORDERS, null, values);
        return newRowId;
    }

    @SuppressLint("Range")
    public Order getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Order order = new Order();
            order.setOrderID(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_ID)));
            order.setCustomerID(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_CUSTOMER_ID)));
            order.setOrderDate(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DATE)));
            order.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_ORDER_TOTAL_AMOUNT)));
            order.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_ORDER_TOTAL_AMOUNT)));
            order.setPaymentStatus(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PAYMENT_STATUS)));
            cursor.close();
            return order;
        }
        cursor.close();
        return null;
    }

    @SuppressLint("Range")
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderID(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_ID)));
                order.setCustomerID(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_CUSTOMER_ID)));
                order.setOrderDate(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DATE)));
                order.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_ORDER_TOTAL_AMOUNT)));
                order.setPaymentStatus(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PAYMENT_STATUS)));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    public int updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_CUSTOMER_ID, order.getCustomerID());
        values.put(COLUMN_ORDER_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COLUMN_ORDER_PAYMENT_STATUS, order.getPaymentStatus());
        return db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(order.getOrderID())});
    }

    public int deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ORDERS, COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});
    }

    // --- Order Details ---

    public long addOrderDetail(OrderDetail orderDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_DETAIL_ORDER_ID, orderDetail.getOrderID());
        values.put(COLUMN_ORDER_DETAIL_HELMET_ID, orderDetail.getHelmetID());
        values.put(COLUMN_ORDER_DETAIL_QUANTITY, orderDetail.getQuantity());
        values.put(COLUMN_ORDER_DETAIL_PRICE, orderDetail.getPrice());
        long newRowId = db.insert(TABLE_ORDER_DETAILS, null, values);
        return newRowId;
    }

    @SuppressLint("Range")
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDER_DETAILS, null, COLUMN_ORDER_DETAIL_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderDetailID(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_DETAIL_ID)));
                orderDetail.setOrderID(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_DETAIL_ORDER_ID)));
                orderDetail.setHelmetID(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_DETAIL_HELMET_ID)));
                orderDetail.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_DETAIL_QUANTITY)));
                orderDetail.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_ORDER_DETAIL_PRICE)));
                orderDetails.add(orderDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderDetails;
    }

    public int updateOrderDetail(OrderDetail orderDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_DETAIL_ORDER_ID, orderDetail.getOrderID());
        values.put(COLUMN_ORDER_DETAIL_HELMET_ID, orderDetail.getHelmetID());
        values.put(COLUMN_ORDER_DETAIL_QUANTITY, orderDetail.getQuantity());
        values.put(COLUMN_ORDER_DETAIL_PRICE, orderDetail.getPrice());
        return db.update(TABLE_ORDER_DETAILS, values, COLUMN_ORDER_DETAIL_ID + " = ?", new String[]{String.valueOf(orderDetail.getOrderDetailID())});
    }

    public int deleteOrderDetail(int orderDetailId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ORDER_DETAILS, COLUMN_ORDER_DETAIL_ID + " = ?", new String[]{String.valueOf(orderDetailId)});
    }

    // --- Payments ---

    public long addPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_ORDER_ID, payment.getOrderID());
        values.put(COLUMN_PAYMENT_AMOUNT, payment.getAmount());
        values.put(COLUMN_PAYMENT_METHOD, payment.getPaymentMethod());
        long newRowId = db.insert(TABLE_PAYMENTS, null, values);
        return newRowId;
    }

    @SuppressLint("Range")
    public Payment getPaymentById(int paymentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PAYMENTS, null, COLUMN_PAYMENT_ID + " = ?", new String[]{String.valueOf(paymentId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Payment payment = new Payment();
            payment.setPaymentID(cursor.getInt(cursor.getColumnIndex(COLUMN_PAYMENT_ID)));
            payment.setOrderID(cursor.getInt(cursor.getColumnIndex(COLUMN_PAYMENT_ORDER_ID)));
            payment.setPaymentDate(cursor.getString(cursor.getColumnIndex(COLUMN_PAYMENT_DATE)));
            payment.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_PAYMENT_AMOUNT)));
            payment.setPaymentMethod(cursor.getString(cursor.getColumnIndex(COLUMN_PAYMENT_METHOD)));
            cursor.close();
            return payment;
        }
        cursor.close();
        return null;
    }

    @SuppressLint("Range")
    public List<Payment> getPayments() {
        List<Payment> payments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PAYMENTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setPaymentID(cursor.getInt(cursor.getColumnIndex(COLUMN_PAYMENT_ID)));
                payment.setOrderID(cursor.getInt(cursor.getColumnIndex(COLUMN_PAYMENT_ORDER_ID)));
                payment.setPaymentDate(cursor.getString(cursor.getColumnIndex(COLUMN_PAYMENT_DATE)));
                payment.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_PAYMENT_AMOUNT)));
                payment.setPaymentMethod(cursor.getString(cursor.getColumnIndex(COLUMN_PAYMENT_METHOD)));
                payments.add(payment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return payments;
    }

    public int updatePayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_ORDER_ID, payment.getOrderID());
        values.put(COLUMN_PAYMENT_AMOUNT, payment.getAmount());
        values.put(COLUMN_PAYMENT_METHOD, payment.getPaymentMethod());
        return db.update(TABLE_PAYMENTS, values, COLUMN_PAYMENT_ID + " = ?", new String[]{String.valueOf(payment.getPaymentID())});
    }

    public int deletePayment(int paymentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PAYMENTS, COLUMN_PAYMENT_ID + " = ?", new String[]{String.valueOf(paymentId)});
    }
    public void deleteAllHelmets() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HELMETS);
    }

    public void deleteAllOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ORDERS);
    }

    public void deleteAllPayments() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PAYMENTS);
    }

    public void deleteAllOrderDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ORDER_DETAILS);
    }

    //insertdata
    public void insertSampleHelmets() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Define product codes
        String productCode1 = "P001"; // Product code for Red and Blue helmets
        String productCode2 = "P002"; // Product code for Green helmet
        String productCode3 = "P003"; // Product code for Yellow helmet
        String productCode4 = "P004"; // Product code for Black helmet

        // Sample data 1
        values.put(COLUMN_HELMET_NAME, "Helmet BLACK");
        values.put(COLUMN_HELMET_BRAND, "Brand A");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Red");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet A");
        values.put(COLUMN_HELMET_PRICE, 49.99);
        values.put(COLUMN_HELMET_STOCK, 10);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/gbU8ykimg0024.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Set product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 2
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet B");
        values.put(COLUMN_HELMET_BRAND, "Brand B");
        values.put(COLUMN_HELMET_SIZE, "L");
        values.put(COLUMN_HELMET_COLOR, "Blue");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet B");
        values.put(COLUMN_HELMET_PRICE, 59.99);
        values.put(COLUMN_HELMET_STOCK, 15);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/gbU8ykimg0024.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Set same product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 3
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet C");
        values.put(COLUMN_HELMET_BRAND, "Brand C");
        values.put(COLUMN_HELMET_SIZE, "S");
        values.put(COLUMN_HELMET_COLOR, "Red");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet C");
        values.put(COLUMN_HELMET_PRICE, 39.99);
        values.put(COLUMN_HELMET_STOCK, 5);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/gbU8ykimg0024.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 4
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet D");
        values.put(COLUMN_HELMET_BRAND, "Brand D");
        values.put(COLUMN_HELMET_SIZE, "XL");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet D");
        values.put(COLUMN_HELMET_PRICE, 69.99);
        values.put(COLUMN_HELMET_STOCK, 8);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/gbU8ykimg0024.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 5
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet E");
        values.put(COLUMN_HELMET_BRAND, "Brand E");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet E");
        values.put(COLUMN_HELMET_PRICE, 79.99);
        values.put(COLUMN_HELMET_STOCK, 12);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/gbU8ykimg0024.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Different product code
        db.insert(TABLE_HELMETS, null, values);
    }

}

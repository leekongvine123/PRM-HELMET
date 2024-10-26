package com.example.myapplication.database_helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.model.CartItem;
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
    private static final int DATABASE_VERSION = 13;

    // Tên các bảng
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_HELMETS = "Helmets";
    private static final String TABLE_ORDERS = "Orders";
    private static final String TABLE_ORDER_DETAILS = "OrderDetails";
    private static final String TABLE_PAYMENTS = "Payments";
    private static final String TABLE_CART = "Cart";

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

    //Cột của Cart
    private static final String COLUMN_CART_ID = "CartID";
    private static final String COLUMN_CART_HELMET_ID = "HelmetID";
    private static final String COLUMN_CART_HELMET_NAME = "HelmetName";
    private static final String COLUMN_CART_SIZE = "Size";
    private static final String COLUMN_CART_COLOR = "Color";
    private static final String COLUMN_CART_QUANTITY = "Quantity";
    private static final String COLUMN_CART_PRICE = "Price";
    private static final String COLUMN_CART_USER_ID = "UserID";

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

    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "("
            + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CART_USER_ID + " INTEGER,"
            + COLUMN_CART_HELMET_ID + " INTEGER,"
            + COLUMN_CART_HELMET_NAME + " TEXT,"
            + COLUMN_CART_SIZE + " TEXT,"
            + COLUMN_CART_COLOR + " TEXT,"
            + COLUMN_CART_QUANTITY + " INTEGER,"
            + COLUMN_CART_PRICE + " DECIMAL(10,2),"
            + "FOREIGN KEY (" + COLUMN_CART_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";


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
        db.execSQL(CREATE_TABLE_CART);

        insertSampleUsers(db);
        insertSampleHelmets(db);
        insertSampleOrders(db);  // Insert sample orders and order details
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ và taạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELMETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);  // You missed this
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
    public User getUserByEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_EMAIL+ " = ?", new String[]{String.valueOf(userEmail)}, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Email = ?", new String[]{String.valueOf(userEmail)});

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

    public int updateUserByGmail(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        return db.update(TABLE_USERS, values, COLUMN_USER_EMAIL + " = ?", new String[]{String.valueOf(user.getEmail())});
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
    public List<Helmet> searchHelmetsByName(String name) {
        List<Helmet> helmets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to search helmets by name and get distinct helmets by product code
        String query = "SELECT * FROM " + TABLE_HELMETS +
                " WHERE " + COLUMN_HELMET_NAME + " LIKE ?" +
                " AND " + COLUMN_HELMET_ID + " IN ( " +
                "SELECT MIN(" + COLUMN_HELMET_ID + ") FROM " + TABLE_HELMETS +
                " GROUP BY " + COLUMN_HELMET_PRODUCT_CODE + ")";

        // Use wildcard in the search query
        String[] selectionArgs = new String[]{"%" + name + "%"};

        Cursor cursor = db.rawQuery(query, selectionArgs);
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

    public int addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_CUSTOMER_ID, order.getCustomerID());
        values.put(COLUMN_ORDER_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COLUMN_ORDER_PAYMENT_STATUS, order.getPaymentStatus());

        // Insert the new order into the database
        long newRowId = db.insert(TABLE_ORDERS, null, values);

        // If the insertion was successful, return the row ID; otherwise, return -1
        if (newRowId != -1) {
            return (int) newRowId;  // Cast long to int safely
        } else {
            return -1;  // Indicate failure
        }
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
            order.setPaymentStatus(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PAYMENT_STATUS)));
            cursor.close();
            return order;
        }
        cursor.close();

        return null;
    }

    @SuppressLint("Range")
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_CUSTOMER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

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

    public int addPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_ORDER_ID, payment.getOrderID());
        values.put(COLUMN_PAYMENT_AMOUNT, payment.getAmount());
        values.put(COLUMN_PAYMENT_METHOD, payment.getPaymentMethod());
        int newRowId = (int) db.insert(TABLE_PAYMENTS, null, values);
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

    //-------------Cart

    public long addItemToCart(int helmetID, String helmetName, String size, String color, int quantity, double price, int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_HELMET_ID, helmetID);
        values.put(COLUMN_CART_HELMET_NAME, helmetName);
        values.put(COLUMN_CART_SIZE, size);
        values.put(COLUMN_CART_COLOR, color);
        values.put(COLUMN_CART_QUANTITY, quantity);
        values.put(COLUMN_CART_PRICE, price);
        values.put(COLUMN_CART_USER_ID, userID);

        return db.insert(TABLE_CART, null, values);
    }

    @SuppressLint("Range")
    public List<CartItem> getCartItemsByUser(int userID) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        // Query to get all cart items for a specific user
        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem();
                cartItem.setHelmetID(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_HELMET_ID)));
                cartItem.setCartID(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_ID)));
                cartItem.setHelmetName(cursor.getString(cursor.getColumnIndex(COLUMN_CART_HELMET_NAME)));
                cartItem.setColor(cursor.getString(cursor.getColumnIndex(COLUMN_CART_COLOR)));
                cartItem.setSize(cursor.getString(cursor.getColumnIndex(COLUMN_CART_SIZE)));
                cartItem.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_CART_PRICE)));
                cartItem.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_QUANTITY)));
                cartItem.setHelmet(getHelmetById(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_HELMET_ID))));

                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return cartItems;
    }


    @SuppressLint("Range")
    public List<CartItem> getAllCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem();
                cartItem.setCartID(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_ID)));
                cartItem.setHelmetID(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_HELMET_ID)));
                cartItem.setHelmetName(cursor.getString(cursor.getColumnIndex(COLUMN_CART_HELMET_NAME)));
                cartItem.setSize(cursor.getString(cursor.getColumnIndex(COLUMN_CART_SIZE)));
                cartItem.setColor(cursor.getString(cursor.getColumnIndex(COLUMN_CART_COLOR)));
                cartItem.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_CART_QUANTITY)));
                cartItem.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_CART_PRICE)));

                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    public boolean isItemInCart(int helmetId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to check if the item is in the cart for the given user
        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_HELMET_ID + " = ? AND " + COLUMN_CART_USER_ID + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(helmetId), String.valueOf(userId)});

        // Check if the cursor returns any rows
        boolean isInCart = cursor.moveToFirst();

        // Close the cursor to avoid memory leaks
        cursor.close();

        return isInCart;
    }

    public int incrementCartItemQuantityByHelmetId(int helmetID, int userID) {
        SQLiteDatabase db = this.getWritableDatabase();

        // First, get the current quantity of the item in the cart
        String query = "SELECT " + COLUMN_CART_QUANTITY + " FROM " + TABLE_CART + " WHERE " + COLUMN_CART_HELMET_ID + " = ? AND " + COLUMN_CART_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(helmetID), String.valueOf(userID)});

        if (cursor != null && cursor.moveToFirst()) {
            // Get the current quantity, increment by 1
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
            cursor.close();

            // Increment the quantity by 1
            int newQuantity = currentQuantity + 1;

            // Update the cart with the new quantity
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QUANTITY, newQuantity);

            return db.update(TABLE_CART, values, COLUMN_CART_HELMET_ID + " = ? AND " + COLUMN_CART_USER_ID + " = ?",
                    new String[]{String.valueOf(helmetID), String.valueOf(userID)});
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return 0; // Return 0 if the item is not found in the cart
        }
    }



    // Update the quantity of an item in the cart
    public int updateCartItemQuantityByHelmetId(int helmetID, int userID, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, newQuantity);

        // Update the quantity where helmetID and userID match
        return db.update(TABLE_CART, values, COLUMN_CART_HELMET_ID + " = ? AND " + COLUMN_CART_USER_ID + " = ?",
                new String[]{String.valueOf(helmetID), String.valueOf(userID)});
    }


    // Remove an item from the cart
    public void removeItemFromCart(int cartID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartID)});
    }

    @SuppressLint("Range")
    public List<CartItem> getCartItemsForUser(int userID) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Cart WHERE UserID = ?", new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem();
                cartItem.setHelmetID(cursor.getInt(cursor.getColumnIndex("HelmetID")));
                cartItem.setHelmetName(cursor.getString(cursor.getColumnIndex("HelmetName")));
                cartItem.setSize(cursor.getString(cursor.getColumnIndex("Size")));
                cartItem.setColor(cursor.getString(cursor.getColumnIndex("Color")));
                cartItem.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                cartItem.setPrice(cursor.getDouble(cursor.getColumnIndex("Price")));

                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return cartItems;
    }

    public int updateCartItemQuantity(int helmetID, int userID, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, newQuantity);

        return db.update(TABLE_CART, values, COLUMN_CART_HELMET_ID + " = ? AND " + COLUMN_CART_USER_ID + " = ?",
                new String[]{String.valueOf(helmetID), String.valueOf(userID)});
    }

    //--------------------------------------------------------------------------------------------------------------------------------

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

    public void deleteAllCartItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CART);
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS);
    }

    public void deleteCartItem(int cartItemID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartItemID)});
    }
    //insertdata
    public void insertSampleHelmets(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Define product codes
        String productCode1 = "P001"; // Product code for Red and Blue helmets
        String productCode2 = "P002"; // Product code for Green helmet
        String productCode3 = "P003"; // Product code for Yellow helmet
        String productCode4 = "P004"; // Product code for Black helmet
        String productCode5 = "P005"; // Product code for Black helmet


        // Sample data 1
        values.put(COLUMN_HELMET_NAME, "Helmet Royal");
        values.put(COLUMN_HELMET_BRAND, "Brand Royal");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Red");
        values.put(COLUMN_HELMET_DESCRIPTION, "Royal Helmet for knight");
        values.put(COLUMN_HELMET_PRICE, 49.99);
        values.put(COLUMN_HELMET_STOCK, 10);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/8ocZp6CmSw3f_MG_9437.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Set product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 2
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Royal");
        values.put(COLUMN_HELMET_BRAND, "Brand B");
        values.put(COLUMN_HELMET_SIZE, "L");
        values.put(COLUMN_HELMET_COLOR, "Blue");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet B");
        values.put(COLUMN_HELMET_PRICE, 59.99);
        values.put(COLUMN_HELMET_STOCK, 15);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/76EQzx_MG_9681.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Set same product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 3
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Royal");
        values.put(COLUMN_HELMET_BRAND, "Brand C");
        values.put(COLUMN_HELMET_SIZE, "S");
        values.put(COLUMN_HELMET_COLOR, "Red");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet C");
        values.put(COLUMN_HELMET_PRICE, 39.99);
        values.put(COLUMN_HELMET_STOCK, 5);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/8ocZp6CmSw3f_MG_9437.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 4
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Royal");
        values.put(COLUMN_HELMET_BRAND, "Brand D");
        values.put(COLUMN_HELMET_SIZE, "XL");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet D");
        values.put(COLUMN_HELMET_PRICE, 69.99);
        values.put(COLUMN_HELMET_STOCK, 8);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/m8EBwaM139-TRON%20(1).jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 5
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Royal");
        values.put(COLUMN_HELMET_BRAND, "Brand E");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet E");
        values.put(COLUMN_HELMET_PRICE, 79.99);
        values.put(COLUMN_HELMET_STOCK, 12);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/m8EBwaM139-TRON%20(1).jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode1); // Different product code
        db.insert(TABLE_HELMETS, null, values);


//product code 2
        // Sample data 1
        values.put(COLUMN_HELMET_NAME, "Helmet Dragon");
        values.put(COLUMN_HELMET_BRAND, "Brand Dragon");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Blue");
        values.put(COLUMN_HELMET_DESCRIPTION, "Dragon Helmet for dragon slayer");
        values.put(COLUMN_HELMET_PRICE, 49.99);
        values.put(COLUMN_HELMET_STOCK, 10);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/76EQzx_MG_9681.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode2); // Set product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 2
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Dragon");
        values.put(COLUMN_HELMET_BRAND, "Brand B");
        values.put(COLUMN_HELMET_SIZE, "L");
        values.put(COLUMN_HELMET_COLOR, "Blue");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet B");
        values.put(COLUMN_HELMET_PRICE, 59.99);
        values.put(COLUMN_HELMET_STOCK, 15);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/76EQzx_MG_9681.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode2); // Set same product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 3
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Dragon");
        values.put(COLUMN_HELMET_BRAND, "Brand C");
        values.put(COLUMN_HELMET_SIZE, "S");
        values.put(COLUMN_HELMET_COLOR, "Red");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet C");
        values.put(COLUMN_HELMET_PRICE, 39.99);
        values.put(COLUMN_HELMET_STOCK, 5);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/8ocZp6CmSw3f_MG_9437.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode2); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 4
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Dragon");
        values.put(COLUMN_HELMET_BRAND, "Brand D");
        values.put(COLUMN_HELMET_SIZE, "XL");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet D");
        values.put(COLUMN_HELMET_PRICE, 69.99);
        values.put(COLUMN_HELMET_STOCK, 8);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/m8EBwaM139-TRON%20(1).jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode2); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 5
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Dragon");
        values.put(COLUMN_HELMET_BRAND, "Brand E");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet E");
        values.put(COLUMN_HELMET_PRICE, 79.99);
        values.put(COLUMN_HELMET_STOCK, 12);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/m8EBwaM139-TRON%20(1).jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode2); // Different product code
        db.insert(TABLE_HELMETS, null, values);

    //product code 3
        // Sample data 1
        values.put(COLUMN_HELMET_NAME, "Helmet Cristal");
        values.put(COLUMN_HELMET_BRAND, "Brand A");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Cristal Helmet for Cristalant");
        values.put(COLUMN_HELMET_PRICE, 49.99);
        values.put(COLUMN_HELMET_STOCK, 10);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/m8EBwaM139-TRON%20(1).jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode3); // Set product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 2
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Cristal");
        values.put(COLUMN_HELMET_BRAND, "Brand B");
        values.put(COLUMN_HELMET_SIZE, "L");
        values.put(COLUMN_HELMET_COLOR, "Blue");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet B");
        values.put(COLUMN_HELMET_PRICE, 59.99);
        values.put(COLUMN_HELMET_STOCK, 15);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/76EQzx_MG_9681.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode3); // Set same product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 3
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Cristal");
        values.put(COLUMN_HELMET_BRAND, "Brand C");
        values.put(COLUMN_HELMET_SIZE, "S");
        values.put(COLUMN_HELMET_COLOR, "Red");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet C");
        values.put(COLUMN_HELMET_PRICE, 39.99);
        values.put(COLUMN_HELMET_STOCK, 5);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/8ocZp6CmSw3f_MG_9437.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode3); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 4
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Cristal");
        values.put(COLUMN_HELMET_BRAND, "Brand D");
        values.put(COLUMN_HELMET_SIZE, "XL");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Helmet for someone from galaxy");
        values.put(COLUMN_HELMET_PRICE, 69.99);
        values.put(COLUMN_HELMET_STOCK, 8);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/m8EBwaM139-TRON%20(1).jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode3); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 5
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Cristal");
        values.put(COLUMN_HELMET_BRAND, "Brand E");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet E");
        values.put(COLUMN_HELMET_PRICE, 79.99);
        values.put(COLUMN_HELMET_STOCK, 12);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/m8EBwaM139-TRON%20(1).jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode3); // Different product code
        db.insert(TABLE_HELMETS, null, values);


        //product code 4
        // Sample data 1
        values.put(COLUMN_HELMET_NAME, "Helmet Sun");
        values.put(COLUMN_HELMET_BRAND, "Brand Sun");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Yellow");
        values.put(COLUMN_HELMET_DESCRIPTION, "Helmet for who come from sun");
        values.put(COLUMN_HELMET_PRICE, 49.99);
        values.put(COLUMN_HELMET_STOCK, 10);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/ePkvgN_MG_6211b.png");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode4); // Set product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 2
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Sun");
        values.put(COLUMN_HELMET_BRAND, "Brand B");
        values.put(COLUMN_HELMET_SIZE, "L");
        values.put(COLUMN_HELMET_COLOR, "Blue");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet B");
        values.put(COLUMN_HELMET_PRICE, 59.99);
        values.put(COLUMN_HELMET_STOCK, 15);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/76EQzx_MG_9681.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode4); // Set same product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 3
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Sun");
        values.put(COLUMN_HELMET_BRAND, "Brand C");
        values.put(COLUMN_HELMET_SIZE, "S");
        values.put(COLUMN_HELMET_COLOR, "Red");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet C");
        values.put(COLUMN_HELMET_PRICE, 39.99);
        values.put(COLUMN_HELMET_STOCK, 5);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/8ocZp6CmSw3f_MG_9437.jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode4); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 4
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Sun");
        values.put(COLUMN_HELMET_BRAND, "Brand D");
        values.put(COLUMN_HELMET_SIZE, "XL");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet D");
        values.put(COLUMN_HELMET_PRICE, 69.99);
        values.put(COLUMN_HELMET_STOCK, 8);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/m8EBwaM139-TRON%20(1).jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode4); // Different product code
        db.insert(TABLE_HELMETS, null, values);

        // Sample data 5
        values.clear();
        values.put(COLUMN_HELMET_NAME, "Helmet Sun");
        values.put(COLUMN_HELMET_BRAND, "Brand E");
        values.put(COLUMN_HELMET_SIZE, "M");
        values.put(COLUMN_HELMET_COLOR, "Green");
        values.put(COLUMN_HELMET_DESCRIPTION, "Description for Helmet E");
        values.put(COLUMN_HELMET_PRICE, 79.99);
        values.put(COLUMN_HELMET_STOCK, 12);
        values.put(COLUMN_HELMET_IMAGE_URL, "https://thegioimubaohiem.vn/ckfinder/userfiles/images/products/m8EBwaM139-TRON%20(1).jpg");
        values.put(COLUMN_HELMET_CREATED_AT, "2024-09-26");
        values.put(COLUMN_HELMET_PRODUCT_CODE, productCode4); // Different product code
        db.insert(TABLE_HELMETS, null, values);
    }




    private void insertSampleUsers(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        try {
            // Sample User 1
            values.put(COLUMN_USER_NAME, "John Doe");
            values.put(COLUMN_USER_EMAIL, "john@example.com");
            values.put(COLUMN_USER_PHONE, "123456789");
            values.put(COLUMN_USER_ADDRESS, "123 Street, City, Country");
            db.insertOrThrow(TABLE_USERS, null, values); // use insertOrThrow for better error handling

            // Sample User 2
            values.clear();
            values.put(COLUMN_USER_NAME, "Jane Smith");
            values.put(COLUMN_USER_EMAIL, "jane@example.com");
            values.put(COLUMN_USER_PHONE, "987654321");
            values.put(COLUMN_USER_ADDRESS, "456 Avenue, City, Country");
            db.insertOrThrow(TABLE_USERS, null, values);

            // Sample User 3
            values.clear();
            values.put(COLUMN_USER_NAME, "Alice Johnson");
            values.put(COLUMN_USER_EMAIL, "alice@example.com");
            values.put(COLUMN_USER_PHONE, "543216789");
            values.put(COLUMN_USER_ADDRESS, "789 Boulevard, City, Country");
            db.insertOrThrow(TABLE_USERS, null, values);

            values.clear();
            values.put(COLUMN_USER_NAME, "Le Huu Cuong");
            values.put(COLUMN_USER_EMAIL, "cuong@gmail.com");
            values.put(COLUMN_USER_PHONE, "09977095127");
            values.put(COLUMN_USER_ADDRESS, "342 Nguyen Thi Dang, Ho Chi Minh city");
            db.insertOrThrow(TABLE_USERS, null, values);

            values.clear();
            values.put(COLUMN_USER_NAME, "Le Huu Cuong");
            values.put(COLUMN_USER_EMAIL, "lehuucuong270603@gmail.com");
            values.put(COLUMN_USER_PHONE, "09977095127");
            values.put(COLUMN_USER_ADDRESS, "342 Nguyen Thi Dang, Ho Chi Minh city");
            db.insertOrThrow(TABLE_USERS, null, values);

            values.clear();
            values.put(COLUMN_USER_NAME, "Le Cong Vinh");
            values.put(COLUMN_USER_EMAIL, "congvinhdt1223@gmail.com");
            values.put(COLUMN_USER_PHONE, "09303030333");
            values.put(COLUMN_USER_ADDRESS, "342 Nguyen Thi Dang, Ho Chi Minh city");
            db.insertOrThrow(TABLE_USERS, null, values);





        } catch (Exception e) {
            // Handle exceptions (like UNIQUE constraint failures)
            e.printStackTrace();
        }
    }

    private void insertSampleOrders(SQLiteDatabase db) {
        ContentValues orderValues = new ContentValues();
        ContentValues orderDetailValues = new ContentValues();

        try {
            // Insert Order 1
            orderValues.put(COLUMN_ORDER_CUSTOMER_ID, 4); // Assuming customerID 1 exists
            orderValues.put(COLUMN_ORDER_TOTAL_AMOUNT, 150.50);
            orderValues.put(COLUMN_ORDER_PAYMENT_STATUS, "Paid");
            long orderId1 = db.insertOrThrow(TABLE_ORDERS, null, orderValues); // Insert order 1 and get order ID

            // Insert Order Details for Order 1
            orderDetailValues.put(COLUMN_ORDER_DETAIL_ORDER_ID, orderId1);
            orderDetailValues.put(COLUMN_ORDER_DETAIL_HELMET_ID, 1); // Assuming helmetID 1 exists
            orderDetailValues.put(COLUMN_ORDER_DETAIL_QUANTITY, 2);
            orderDetailValues.put(COLUMN_ORDER_DETAIL_PRICE, 50.25);
            db.insertOrThrow(TABLE_ORDER_DETAILS, null, orderDetailValues); // Order detail 1

            orderDetailValues.clear();
            orderDetailValues.put(COLUMN_ORDER_DETAIL_ORDER_ID, orderId1);
            orderDetailValues.put(COLUMN_ORDER_DETAIL_HELMET_ID, 2); // Assuming helmetID 2 exists
            orderDetailValues.put(COLUMN_ORDER_DETAIL_QUANTITY, 1);
            orderDetailValues.put(COLUMN_ORDER_DETAIL_PRICE, 50.00);
            db.insertOrThrow(TABLE_ORDER_DETAILS, null, orderDetailValues); // Order detail 2

            // Insert Order 2
            orderValues.clear();
            orderValues.put(COLUMN_ORDER_CUSTOMER_ID, 4); // Assuming customerID 2 exists
            orderValues.put(COLUMN_ORDER_TOTAL_AMOUNT, 200.00);
            orderValues.put(COLUMN_ORDER_PAYMENT_STATUS, "Pending");
            long orderId2 = db.insertOrThrow(TABLE_ORDERS, null, orderValues); // Insert order 2 and get order ID

            // Insert Order Details for Order 2
            orderDetailValues.clear();
            orderDetailValues.put(COLUMN_ORDER_DETAIL_ORDER_ID, orderId2);
            orderDetailValues.put(COLUMN_ORDER_DETAIL_HELMET_ID, 3); // Assuming helmetID 3 exists
            orderDetailValues.put(COLUMN_ORDER_DETAIL_QUANTITY, 3);
            orderDetailValues.put(COLUMN_ORDER_DETAIL_PRICE, 60.00);
            db.insertOrThrow(TABLE_ORDER_DETAILS, null, orderDetailValues); // Order detail 1

            orderDetailValues.clear();
            orderDetailValues.put(COLUMN_ORDER_DETAIL_ORDER_ID, orderId2);
            orderDetailValues.put(COLUMN_ORDER_DETAIL_HELMET_ID, 4); // Assuming helmetID 4 exists
            orderDetailValues.put(COLUMN_ORDER_DETAIL_QUANTITY, 2);
            orderDetailValues.put(COLUMN_ORDER_DETAIL_PRICE, 40.00);
            db.insertOrThrow(TABLE_ORDER_DETAILS, null, orderDetailValues); // Order detail 2

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    ///============================================================================///============================================================================
    ///============================================================================///============================================================================
    ///============================================================================///============================================================================
    ///============================================================================///============================================================================
    ///============================================================================///============================================================================

    // Method to get all users
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);  // Replace 'user' with your actual user table name
    }

    // Method to get all cart items
    public Cursor getAllCartItemsView() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CART, null);  // Replace 'cart' with your actual cart table name
    }

    // Method to get all cart items
    public Cursor getAllCartItemsByUserId(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_USER_ID + " = ?", new String[]{String.valueOf(userID)});  // Replace 'cart' with your actual cart table name
    }

    // Method to get all cart items
    public Cursor getAllHelmets() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_HELMETS, null);  // Replace 'cart' with your actual cart table name
    }

    public String getDatabasePath() {
        return this.getWritableDatabase().getPath();
    }
}

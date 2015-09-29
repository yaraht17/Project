package com.fourlines.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fourlines.model.NotificationItem;

import java.util.ArrayList;

public class DatabaseNotif extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_doctor";
    public static final String TABLE_NAME = "tbl_notification";
    public static final int verson = 6;
    public static final String TAG = "TienDH";

    public static final String CL_ID = "ID";
    public static final String CL_TITLE = "title";
    public static final String CL_TOPIC = "topic";
    public static final String CL_CONTENT = "content";

    public SQLiteDatabase mSQLitedb;

    public Context mContext;

    public DatabaseNotif(Context context) {
        super(context, DATABASE_NAME, null, verson);
        mSQLitedb = getWritableDatabase();
        mContext = context;
    }

    /*
     * - Kiem tra da co bang ton tai trong csdl chua. - neu co tra ve la true
     */
    public boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                        + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + CL_ID
                + " TEXT, " + CL_TITLE + " TEXT, "
                + CL_TOPIC + " TEXT, " + CL_CONTENT + " TEXT);";

        if (!isTableExists(db, TABLE_NAME)) {
            Log.d(null, "Oncreate " + createTable);
            db.execSQL(createTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqldrop = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sqldrop);
        onCreate(db);
    }

    /*
     * - them mot doi tuong Friend vao trong csdl
     */
    public void insertNotifItem(NotificationItem notificationItem) {

        ContentValues values = new ContentValues();
        values.put(CL_ID, notificationItem.getId());
        values.put(CL_TITLE, notificationItem.getTitle());
        values.put(CL_TOPIC, notificationItem.getTopic());
        values.put(CL_CONTENT, notificationItem.getBody());

        if (mSQLitedb.insert(TABLE_NAME, null, values) == -1) {
            // Log.d("INSERT", friend.getName() + "- " + friend.getId());
            Log.d("TienDH", "insert error");
        } else {
//            Log.d("TienDH", "Insert " + notificationItem.getTitle() + " " + notificationItem.getTopic()
//                    + " " + notificationItem.getBody());
        }

    }

    /*
     * - lay toan bo ban ghi trong bang co trong csdl
     */
    public ArrayList<NotificationItem> getNotifList() {

        ArrayList<NotificationItem> list = new ArrayList<NotificationItem>();
        Cursor cursor = mSQLitedb.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int numberId = cursor.getColumnIndex(CL_ID);
            int numberTitle = cursor.getColumnIndex(CL_TITLE);
            int numberTopic = cursor.getColumnIndex(CL_TOPIC);
            int numberContent = cursor.getColumnIndex(CL_CONTENT);

            String id = cursor.getString(numberId);
            String title = cursor.getString(numberTitle);
            String topic = cursor.getString(numberTopic);
            String content = cursor.getString(numberContent);

            NotificationItem item = new NotificationItem(id, 0, title, content, topic);
            list.add(item);
//            Log.d(TAG, "getList :" + item.getTitle() + " " + item.getTopic() + " " + item.getBody());
        }
        cursor.close();
        return list;
    }

    public void createTable() {
        onCreate(mSQLitedb);
        Log.d(TAG, "CREATE TABLE");
    }

    public void dropTable() {
        Log.d(TAG, "DROP TABLE");
        String sqldrop = "DROP TABLE IF EXISTS " + TABLE_NAME;
        mSQLitedb.execSQL(sqldrop);
        onCreate(mSQLitedb);
    }

    /*
     * - dong database
     */
    public void closeBD() {
        if (mSQLitedb != null && mSQLitedb.isOpen())
            mSQLitedb.close();
    }
}

package com.fourlines.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.fourlines.model.ChatMessage;

import java.util.ArrayList;

/**
 * Created by LiLo on 2015-09-24.
 */
public class DatabaseChat extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_doctor";
    public static final String TABLE_NAME = "tbl_chat_history";
    public static final int verson = 5;
    public static final String TAG = "TienDH";

    public static final String CL_ID = "ID";
    public static final String CL_SENDER = "sender";
    public static final String CL_MESSAGE = "message";

    public SQLiteDatabase mSQLitedb;

    public Context mContext;

    public DatabaseChat(Context context) {
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
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CL_SENDER + " TEXT, "
                + CL_MESSAGE + " TEXT);";

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
    public void insertChatHistoryItem(ChatMessage chatMessage) {

        ContentValues values = new ContentValues();
        values.put(CL_SENDER, String.valueOf(chatMessage.left));
        values.put(CL_MESSAGE, chatMessage.message);

        if (mSQLitedb.insert(TABLE_NAME, null, values) == -1) {
            // Log.d("INSERT", friend.getName() + "- " + friend.getId());
            Toast.makeText(mContext, "Add New Error", Toast.LENGTH_SHORT)
                    .show();
        }
        else{
            Log.d("INSERT", chatMessage.left + " " + chatMessage.message);
        }

    }

    public int getLastId(){
        Cursor cursor = mSQLitedb.rawQuery("SELECT COUNT(*) AS last_id FROM "+ TABLE_NAME, null);

        int lastId = 0;
        if(cursor.moveToNext()){
            int numberLastId = cursor.getColumnIndex("last_id");
            lastId = cursor.getInt(numberLastId);
        }

        cursor.close();
        Log.d("Last Id", String.valueOf(lastId));
        return lastId;
    }

    /*
     * - lay toan bo ban ghi trong bang co trong csdl
     */
    public ArrayList<ChatMessage> getChatHistoryList(long startId, long endId) {

        ArrayList<ChatMessage> list = new ArrayList<ChatMessage>();
        Cursor cursor = mSQLitedb.query(TABLE_NAME, null, CL_ID + ">= ? AND " + CL_ID + " <= ? "
                , new String[]{String.valueOf(startId), String.valueOf(endId)}, null,
                null, null);

        while (cursor.moveToNext()) {
            int numberId = cursor.getColumnIndex(CL_ID);
            int numberSender = cursor.getColumnIndex(CL_SENDER);
            int numberMessage = cursor.getColumnIndex(CL_MESSAGE);

            int id = cursor.getInt(numberId);
            String sender = cursor.getString(numberSender);
            String message = cursor.getString(numberMessage);

            ChatMessage item = new ChatMessage(id, Boolean.valueOf(sender), message);
            list.add(item);
            Log.d(TAG, "getList :" + item.left + " " + item.message);
        }
        cursor.close();
        return list;
    }

    public void dropTable() {
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

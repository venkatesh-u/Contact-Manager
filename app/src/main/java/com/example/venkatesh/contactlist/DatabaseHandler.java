package com.example.venkatesh.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    SQLiteDatabase sdb;
    public static String DATABASE = "contacts.db";
    public static String TABLE_CONTACTS = "contactsTable";
    public static String COL_ID = "ID";
    public static String COL_NAME = "CONTACT_NAME";
    public static String COL_MOBILE = "MOBILE";

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, CONTACT_NAME VARCHAR, MOBILE INTEGER )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+  TABLE_CONTACTS );

    }

    public void openDb() {
        sdb = this.getWritableDatabase();
    }

    public void closeDb() {
        sdb.close();
    }


    public boolean insertDataForContact(String name, String mobile) {
        openDb();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_MOBILE, mobile);
        long result= sdb.insert(TABLE_CONTACTS, null, contentValues);
        closeDb();
        if (result == -1)
        {
            return false;
        }
        else
        {
            return  true;
        }
    }

    public ArrayList<DataContacts> getContactsData() {

        ArrayList<DataContacts> contactsList = new ArrayList<>();
        openDb();
        Cursor cursor = sdb.query(true, TABLE_CONTACTS, new String[]{COL_NAME, COL_MOBILE}, null, null, null, null, null, null);
        if(cursor != null)
        {
            for (; cursor.moveToNext();)
            {
                DataContacts contacts = new DataContacts();
                contacts.dc_name =cursor.getString(cursor.getColumnIndex("CONTACT_NAME"));
                contacts.dc_mobile = cursor.getString(cursor.getColumnIndex("MOBILE"));
                contactsList.add(contacts);
            }
        }
        cursor.close();
        closeDb();
        return contactsList;
    }

    public void deleteContact(String selectedName) {

        openDb();
        sdb.delete(TABLE_CONTACTS, "CONTACT_NAME=?", new String[]{selectedName});
        closeDb();
    }

    public DataContacts editContact(String dc_mobile) {
        openDb();

//        ArrayList<DataContacts> arraylist = new ArrayList<>();
        DataContacts data = null;
        Cursor cursor = sdb.query(TABLE_CONTACTS, null, "MOBILE=?", new String[]{dc_mobile},null,null,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            Log.d("cursor count:", cursor.getCount() +"");
            data = new DataContacts();

            data.dc_name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            Log.d("name:", data.dc_name);
            data.dc_mobile = cursor.getString(cursor.getColumnIndex(COL_MOBILE));
           data.dc_id = cursor.getString(cursor.getColumnIndex(COL_ID));

        }
        cursor.close();
        closeDb();
        return data;
    }

    public boolean updateDataForContact(String name, String number, String id) {
        boolean isOk = false;
        openDb();
        Cursor cursor = sdb.query(TABLE_CONTACTS, null, "ID=?", new String[]{id},null,null,null);
        if(cursor.getCount()>0) {
            ContentValues cont = new ContentValues();
            cont.put(COL_NAME, name);
            cont.put(COL_MOBILE, number);
            int isUpdated = sdb.update(TABLE_CONTACTS, cont, "ID=?", new String[]{id});
            if (isUpdated>0)
            {
                isOk = true;
            }
        }
        cursor.close();
        closeDb();
        return isOk;
    }
}

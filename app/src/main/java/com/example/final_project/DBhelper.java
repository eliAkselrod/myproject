package com.example.final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    public static final String DB_FILE = "users.db";
    public static final String USERS_TABLE = "Users";
    public static final String USERS_NAME = "Name";
    public static final String USERS_EMAIL = "Email";
    public static final String USERS_PASSWORD = "Password";

    SQLiteDatabase db;

     static final String CREATE_TABLE="create table if not exists " + USERS_TABLE + "  (  " + USERS_EMAIL + "  TEXT , " + USERS_NAME + "  TEXT , " + USERS_PASSWORD + "  TEXT );";

    public DBhelper(Context context) {
        super(context, DB_FILE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long insertUser(User user)
    {
        db=getWritableDatabase();
        ContentValues cv =new ContentValues();
        cv.put(USERS_EMAIL,user.getEmail());
        cv.put(USERS_NAME,user.getName());
        cv.put(USERS_PASSWORD,user.getPassword());
        long id=db.insert(USERS_TABLE,null,cv);
        return id;
    }
    public boolean checkLogin (String email, String password){
        SQLiteDatabase myDB=this.getWritableDatabase();
        Cursor cursor =myDB.rawQuery("Select * from users where email = ? and password = ?", new String[ ] {email,password});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public User getUser(String email, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String name = cursor.getString(1);
            return new User(name, email, password);
        } else {
            return null;
        }


    }
}

package com.example.mylocation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.mylocation.model.User;
import com.example.mylocation.utils.JsonTranslation;

public class UserDBHelper extends SQLiteOpenHelper {

    // declaring name of the database
    static final String DATABASE_NAME = "UserSelectedLocationData";

    // declaring table name of the database
    static final String TABLE_NAME = "Users";

    // declaring version of the database
    static final int DATABASE_VERSION = 1;

    private final JsonTranslation jsonTranslation;
    private static final String TAG = "UserDBHelper";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        jsonTranslation = new JsonTranslation(new Gson());
    }

    /**
     * Used to create Users table in database when the database is opened. Throws Runtime Exception if the table failed to create.
     * @param db - database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table when the database is created for the first time
        try {
            String sql = ("create table if not exists " + TABLE_NAME + " (user_id  TEXT PRIMARY KEY," +
                    "user_name TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL," +
                    "user_locations TEXT," +
                    "created_timestamp TEXT," +
                    "updated_timestamp TEXT)");
            db.execSQL(sql);
        } catch (Exception e) {
            Log.e(TAG,"Exception occurred while create table "+e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Used to update the table schema. For development purpose we are dropping the table and recreating new one
     * @param db - database instance
     * @param i - old version
     * @param i1 - new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL("drop Table if exists " + TABLE_NAME);
            onCreate(db);
        } catch (Exception e) {
            Log.e(TAG,"Exception occurred while dropping table "+e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * This method is used to store the user details in database for new user.
     * @param user - User object which contains details like userId, userName, password, userPreferences
     * @param userSelectedLocations - the user selected locations
     * @return - true if the operation was success otherwise false
     */
    public Boolean insertUserData(User user,String userSelectedLocations) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user.getUserId());
        contentValues.put("user_name", user.getUserName());
        contentValues.put("password", user.getPassword());
        contentValues.put("user_locations", userSelectedLocations);
        contentValues.put("created_timestamp", getDateTime());
        contentValues.put("updated_timestamp", getDateTime());
        long result = DB.insert(TABLE_NAME, null, contentValues);
        return result == -1 ? false : true;
    }

    /**
     * This method is used to verify user from database given the username and password
     * @param userName - the username of user
     * @param password - the password of user
     * @return - true if the user is a valid user otherwise false
     */
    public boolean verifyUserData(String userName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where user_name = ? AND password = ?", new String[] {userName, password});
        while (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * This method is used to verify if the username has been used before or not
     * @param userName - username entered
     * @return - true if username is already used otherwise false
     */
    public boolean verifyRegisterData(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where user_name = ?", new String[] {userName});
        while (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * This method is used to perform update of user selected location in case the username exists otherwise returns false
     * @param userName - username of user updating user selected location
     * @param userSelectedLocations - updated list of locations
     * @return - true if the operation is successful otherwise return false
     */
    public Boolean updateUserLocation(String userName, String userSelectedLocations) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name", userName);
        contentValues.put("user_locations", userSelectedLocations);
        contentValues.put("updated_timestamp", getDateTime());
        Cursor cursor = DB.rawQuery("Select * from "+TABLE_NAME+" where user_name = ?", new String[]{userName});
        if (cursor.getCount()>0) {
            long result = DB.update(TABLE_NAME, contentValues,"user_name=?", new String[]{userName});
            return result == -1 ? false : true;
        } else return false;
    }

    /**
     * This method is used to get the location list for a given user
     * @param userName - username of the user
     * @return - the list of user locations for the user
     */
    public Cursor getLocations (String userName) {
        //only create the getLocations method to get location data. You can create other methods
        //for authenticating users or setting UI preference.
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select user_locations from "+TABLE_NAME+" where user_name = ?", new String[]{userName});
        return cursor;
    }


    /**
     * Used to get current timestamp for storing created, updated timestamps
     * @return - string data time value
     */
    private String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);

    }

    /**
     * remove the user provided.
     * @param user - the user to be deleted from the table
     */
    public Boolean removeUserData(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "user_name = ?", new String[]{user.getUserName()});
        return result == -1 ? false : true;
    }
}
package com.example.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pets.data.PetsContract.PetsEntry;

import androidx.annotation.Nullable;

public class PetsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PetsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "shelter.db";

    private static final int DATABASE_VERSION = 1;

    public PetsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PetsEntry.TABLE_NAME + " ("
                + PetsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetsEntry.COLUMN_PETS_NAME + " TEXT NOT NULL, "
                + PetsEntry.COLUMN_PETS_BREED + " TEXT, "
                + PetsEntry.COLUMN_PETS_GENDER + " INTEGER NOT NULL, "
                + PetsEntry.COLUMN_PETS_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}

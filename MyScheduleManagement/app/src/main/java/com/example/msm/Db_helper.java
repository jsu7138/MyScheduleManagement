package com.example.msm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Locale;

public class Db_helper extends SQLiteOpenHelper {
    public static final int dbVersion = 1;
    public String table_Pass = "passDB";
    public String table_Info = "infoDB";

    public Db_helper(Context context){
        super(context, "admin_Info.db", null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;

        sql ="create table "+table_Pass+" (password text primary key);";
        db.execSQL(sql);
        sql ="create table "+table_Info+" (number integer PRIMARY KEY AUTOINCREMENT, week text, day_of_month integer, month integer, year integer, hour integer, minute integer, context text, password text, checking int);";
        db.execSQL(sql);
        Log.d("Log: ","create new Table!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_Pass);
        db.execSQL("DROP TABLE IF EXISTS " + table_Info);
        onCreate(db);
    }

    public Cursor infoTableSearch(SQLiteDatabase db){
        Cursor cursor =  db.rawQuery("SELECT * FROM "+ table_Info+" where password='"+ Admin_cooki.getPassword()
                +"' order by year asc, month asc, day_of_month asc, hour asc, minute asc, checking asc;" , null);
        return cursor;
    }

    public void infoTableAdd(SQLiteDatabase db, String week, int day_of_month, int month, int year, int hour, int minutes, String context, int checking){
        db.execSQL(
                String.format(Locale.US, "INSERT INTO %s VALUES (null, '%s', %d, %d, %d, %d, %d, '%s', '%s', %d);",
                table_Info, week, day_of_month, month, year, hour, minutes, context, Admin_cooki.getPassword(),checking)
        );
    }
    public void infoTableRemove(SQLiteDatabase db, int day_of_month, int month, int year, int hour, int minute, String context, int checking){
        db.execSQL(
                String.format(Locale.US,"DELETE FROM %s WHERE day_of_month=%d and month=%d and year=%d and hour=%d and minute=%d and context='%s' and checking=%d and password='%s';",
                        table_Info, day_of_month,month,year,hour,minute,context,checking,Admin_cooki.getPassword())
        );
    }

    public void infoTableUpdate(SQLiteDatabase db, int day_of_month, int month, int year, int hour, int minute, String context, int checking, int afterChecking){
        db.execSQL(
                String.format(Locale.US,"UPDATE %s SET checking=%d WHERE day_of_month=%d and month=%d and year=%d and hour=%d and minute=%d and context='%s' and checking=%d and password='%s';",
                        table_Info, afterChecking, day_of_month, month, year, hour, minute, context, checking,Admin_cooki.getPassword())
        );
    }

    public void infoTableContextUpdate(SQLiteDatabase db, int day_of_month, int month, int year, int hour, int minute, String context, int checking, String afterContext){
        db.execSQL(
                String.format(Locale.US,"UPDATE %s SET context='%s' WHERE day_of_month=%d and month=%d and year=%d and hour=%d and minute=%d and context='%s' and checking=%d and password='%s';",
                        table_Info, afterContext, day_of_month, month, year, hour, minute, context, checking,Admin_cooki.getPassword())
        );
    }
}

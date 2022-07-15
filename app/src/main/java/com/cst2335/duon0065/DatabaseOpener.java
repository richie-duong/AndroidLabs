package com.cst2335.duon0065;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "Messages";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "Chatlog";
    public final static String COL_ID = "_id";
    public final static String COL_CONTENT = "Message";
    public final static String COL_SENT = "IsSend";


    public DatabaseOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CONTENT + " text, " + COL_SENT + " integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}


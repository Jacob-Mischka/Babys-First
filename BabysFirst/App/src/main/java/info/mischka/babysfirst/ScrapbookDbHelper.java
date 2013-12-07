package info.mischka.babysfirst;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mischka on 11/25/13.
 */
public class ScrapbookDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BabysFirstScrapbook.db";

    public ScrapbookDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE scrapbook (id INTEGER PRIMARY KEY, username TEXT, date TEXT, time TEXT, title TEXT, comments TEXT);");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS scrapbook");
        onCreate(db);

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
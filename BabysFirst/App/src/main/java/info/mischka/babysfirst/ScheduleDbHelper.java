package info.mischka.babysfirst;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mischka on 11/25/13.
 */
    public class ScheduleDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "BabysFirstSchedule.db";

        public ScheduleDbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db){
            db.execSQL("CREATE TABLE schedule (id INTEGER PRIMARY KEY, username TEXT, date TEXT, time TEXT, description TEXT, recurring TEXT);");

        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS schedule");
            onCreate(db);

        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }
    }
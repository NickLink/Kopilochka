package ua.kiev.foxtrot.kopilochka.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by NickNb on 30.09.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DBHelper.class.getName();

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(LOG_TAG, "SSS onCreate database ---");
        // создаем таблицу с полями
        db.execSQL(Tables.table_bbs_news);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
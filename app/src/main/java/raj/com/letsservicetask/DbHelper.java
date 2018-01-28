package raj.com.letsservicetask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prakash on 1/27/2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "log.db";

    public DbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void createTable(SQLiteDatabase database) {
        String log_table_sql = "create table " + Constant.LOG_TABLE + "("
                + Constant.LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constant.LOG_LAT + " TEXT,"
                + Constant.LOG_LNG + " TEXT,"
                + Constant.LOG_TIME + " TEXT)";

        try {
            database.execSQL(log_table_sql);
            Log.d(Constant.TAG, "table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDetail(LogObject object) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.LOG_LAT, object.getLat());
        values.put(Constant.LOG_LNG, object.getLng());
        values.put(Constant.LOG_TIME, object.getDateAndTime());

        db.insert(Constant.LOG_TABLE, null, values);
        db.close();
    }

    public List<LogObject> getAllDetails() {
        String[] columns = {
                Constant.LOG_ID, Constant.LOG_LNG, Constant.LOG_LAT, Constant.LOG_TIME
        };
        // sorting orders
        String sortOrder =
                Constant.LOG_ID + " ASC";
        List<LogObject> objectList = new ArrayList<LogObject>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(Constant.LOG_TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LogObject object = new LogObject();

                object.setLat(cursor.getString(cursor.getColumnIndex(Constant.LOG_LAT)));
                object.setLng(cursor.getString(cursor.getColumnIndex(Constant.LOG_LNG)));
                object.setDateAndTime(cursor.getString(cursor.getColumnIndex(Constant.LOG_TIME)));
                // Adding user record to list
                objectList.add(object);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return objectList;
    }
}

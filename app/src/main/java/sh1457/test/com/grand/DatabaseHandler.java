package sh1457.test.com.grand;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gesturedb";
    private static final String TABLE_GESTURES = "gestures";

    private static final String KEY_ID = "id";
    private static final String KEY_GSTRING = "gstring";

    private final ArrayList<Gesture> gesture_list = new ArrayList<>();

    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GESTURES_TABLE = "CREATE TABLE " + TABLE_GESTURES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GSTRING + " TEXT" + ")";
        db.execSQL(CREATE_GESTURES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GESTURES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    void Add_Gesture(Gesture gesture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, gesture.getID()); // Gesture ID
        values.put(KEY_GSTRING, gesture.getString()); // Gesture String

        db.insert(TABLE_GESTURES, null, values);
        db.close(); // Closing database connection
    }

    Gesture Get_Gesture(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String gstring;
        int gid=-1;

        Cursor cursor = db.query(TABLE_GESTURES, new String[] { KEY_ID, KEY_GSTRING }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        try {
            assert cursor != null;
            gid = cursor.getInt(0);
        }catch (NullPointerException e) {
            Log.e("get_gesture", "cursor is null" + e);
        }
        gstring=cursor.getString(1);

        Gesture gesture = new Gesture(gid, gstring);

        cursor.close();
        db.close();

        return gesture;
    }

    ArrayList<Gesture> Get_Gestures() {
        String gstring;
        int gid;

        try {
            gesture_list.clear();

            String selectQuery = "SELECT * FROM " + TABLE_GESTURES;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    gid = cursor.getInt(0);
                    gstring=cursor.getString(1);

                    Gesture gesture = new Gesture(gid, gstring);
                    // Adding gesture to list
                    gesture_list.add(gesture);
                }while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return gesture_list;
        }catch(NullPointerException e) {
            Log.e("get_all_gesture", "cursor is null" + e);
        }catch (Exception e) {
            Log.e("get_all_gesture", "" + e);
        }

        return gesture_list;
    }

    void Delete_Gesture(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GESTURES, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public int Get_Total_Gestures() {
        String countQuery = "SELECT  * FROM " + TABLE_GESTURES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
}
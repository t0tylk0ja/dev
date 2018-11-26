package dominikmalek.com.dev_test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class TaskDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 2 ;
    public static final String TABLE_NAME = "Tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STATUS = "status";

    private static TaskDBHelper mInstance = null;

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    public static TaskDBHelper getInstanse(Context ctx){
        if (mInstance == null) {
            mInstance = new TaskDBHelper(ctx.getApplicationContext());

            File f = ctx.getDatabasePath(DATABASE_NAME);
            long dbSize = f.length();
            if(dbSize==0){
                mInstance.addSomeTasks();}
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_STATUS + " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void saveNewTask(String name, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_STATUS, status);

        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    /**Query records, give options to filter results**/
    public List<Task> taskList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<Task> taskLinkedList = new LinkedList<Task>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Task task;

        if (cursor.moveToFirst()) {
            do {
                task = new Task();

                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                task.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));

                taskLinkedList.add(task);
            } while (cursor.moveToNext());
        }

        return taskLinkedList;
    }

    public void updateStatus(int id, String value){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cv.put("status",value);
        db.update("Tasks",cv,"_id="+id,null);
    }

    public void addSomeTasks(){
        String startingStatus="OPEN";
        saveNewTask("TASK 1",startingStatus);
        saveNewTask("TASK 2",startingStatus);
        saveNewTask("TASK 3",startingStatus);
        saveNewTask("TASK 4",startingStatus);
        saveNewTask("TASK 5",startingStatus);
        saveNewTask("TASK 6",startingStatus);
        saveNewTask("TASK 7",startingStatus);
        saveNewTask("TASK 8",startingStatus);
        saveNewTask("TASK 9",startingStatus);
        saveNewTask("TASK 10",startingStatus);
        saveNewTask("TASK 11",startingStatus);
        saveNewTask("TASK 12",startingStatus);
        saveNewTask("TASK 13",startingStatus);
        saveNewTask("TASK 14",startingStatus);
        saveNewTask("TASK 15",startingStatus);
        saveNewTask("TASK 16",startingStatus);
        saveNewTask("TASK 17",startingStatus);
        saveNewTask("TASK 18",startingStatus);
        saveNewTask("TASK 19",startingStatus);
        saveNewTask("TASK 20",startingStatus);
    }
}

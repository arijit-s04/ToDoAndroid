package com.android.arijit.todoapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataAccess extends SQLiteOpenHelper {
    private String TAG="DataAccess";
    private Context context;

    public DataAccess(@Nullable Context context, int version) {
        super(context, "TODO", null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db);
        createTable(db);
    }

    private void createTable(SQLiteDatabase db){
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE todo(tid INTEGER PRIMARY KEY, TODOTASK TEXT, CDATE TEXT, COMPLETED INTEGER)");
        db.execSQL("CREATE TABLE comment(cid INTEGER PRIMARY KEY, tid INTEGER, CMTEXT TEXT, CMDATE TEXT)");

        Log.i(TAG, "createTable: successful");
    }

    private void dropTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE todo");
        db.execSQL("DROP table comment");
    }

    public ToDo addNewTodo(ToDo obj){
        SQLiteDatabase db = getWritableDatabase();
        //cv to container fo rputting all the values
        ContentValues cv = new ContentValues();
        cv.put("todotask", obj.getTask());
        cv.put("completed", (obj.isCompleted()?1:0));
        cv.put("cdate", obj.getCdate());
        long tid = db.insert("todo", null, cv);
        db.close();
        obj.setId((int)tid);
//        addNewComments((int)tid);
        //close database after writing
        return obj;
    }

    public int addNewComments(int tid, Comment cmoobj){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tid", tid);
        cv.put("cmtext", cmoobj.getCmtext());
        cv.put("cmdate", cmoobj.getCmdate());
        long cid = db.insert("comment", null, cv);
        db.close();
        return (int)cid;
    }
    public ArrayList<ToDo> getAllTodo(){

        //opening the Database
        SQLiteDatabase db = getReadableDatabase();
        Cursor cr = db.rawQuery("SELECT tid, todotask, cdate, completed from todo", null);

        ArrayList<ToDo> todolist = new ArrayList<>();
        if(cr.moveToFirst()){
            do{
                int tid = cr.getInt(0);
                String todotask = cr.getString(1);
                String cdate = cr.getString(2);
                boolean completed = (cr.getInt(3)==1?true:false);
                ToDo obj =  new ToDo(tid, todotask, cdate, completed);
                todolist.add(obj);
            }while (cr.moveToNext());
        }
        db.close();
        return todolist;
    }

    public ArrayList<Comment> getAllComment(int tid){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT cid, cmtext, cmdate from comment where tid="+tid,null);
        ArrayList<Comment> cmlist = new ArrayList<>();
        if(cr.moveToFirst()){
            do{
                int cid = cr.getInt(0);
                String cmtext = cr.getString(1);
                String cmdate = cr.getString(2);
                cmlist.add(new Comment(cid, cmtext, cmdate));
            }while(cr.moveToNext());
        }
        db.close();
        return cmlist;
    }

    public String fetchParticularTask(int tid){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT todotask from todo where tid="+tid,null);
        if(cr.moveToFirst()){
            db.close();
            return cr.getString(0);
        }
        db.close();
        return null;
    }

    public void mDaCompleteUpdate(ToDo obj, boolean flag){
        Log.i(TAG, "mDaCompleteUpdate: im at start");
        int val = (flag?1:0);
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE todo set completed=" + val +
                " where tid=" + obj.getId();
        Log.i(TAG, "mDaCompleteUpdate: im at end");
        db.execSQL(query);
        db.close();
    }

    public void deleteTask(int todoId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM todo WHERE tid="+todoId);
        db.execSQL("DELETE FROM comment WHERE tid="+todoId);
        Log.i(TAG, "deleteTask: todo deleted");
        db.close();
    }
    public void deleteComment(int cid){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM comment WHERE cid="+cid);
        Log.i(TAG, "deleteTask: todo deleted");
        db.close();
    }
}

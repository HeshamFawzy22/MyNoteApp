package com.example.mynoteapp.database;

import android.content.Context;

import com.example.mynoteapp.database.daos.NotesDao;
import com.example.mynoteapp.database.model.Note;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class},version = 1,exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {

    private static MyDataBase myDataBase;
    private final static String DATABASE_NAME = "notesDatabaseRoute";
    public abstract NotesDao notesDao();
    public static MyDataBase getInstance(Context context){
        if (myDataBase == null){
            //create object
            myDataBase = Room.databaseBuilder(context,MyDataBase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return myDataBase;
    }
}

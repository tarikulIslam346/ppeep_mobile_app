package com.example.ppeepfinal.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {UserModel.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static final String LOG_TAG = UserDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "userInfo";
    private static UserDatabase sInstance;

    public static UserDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        UserDatabase.class, UserDatabase.DATABASE_NAME)
                        // COMPLETED (2) call allowMainThreadQueries before building the instance
                        // Queries should be done in a separate thread to avoid locking the UI
                        // We will allow this ONLY TEMPORALLY to see that our DB is working
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract UserDAO userDAO();
}

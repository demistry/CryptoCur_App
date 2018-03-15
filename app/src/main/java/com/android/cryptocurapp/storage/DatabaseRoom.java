package com.android.cryptocurapp.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by ILENWABOR DAVID on 12/03/2018.
 */

@Database(entities = RoomEntity.class, version = 1, exportSchema = false)
public abstract class DatabaseRoom extends RoomDatabase {
    public abstract DAOInterface getDAOInterface();
}

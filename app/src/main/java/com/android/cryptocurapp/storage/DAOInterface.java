package com.android.cryptocurapp.storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by ILENWABOR DAVID on 12/03/2018.
 */
@Dao
public interface DAOInterface {
    @Query("SELECT * FROM RoomEntity")
    public List<RoomEntity> queryAllData();

    @Insert
    public void insertAllData(List<RoomEntity> roomEntities);

    @Delete
    public void deleteAllData(List<RoomEntity> roomEntities);

    @Delete()
    public void deleteACard(RoomEntity entity);
}

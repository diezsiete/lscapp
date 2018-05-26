package com.diezsiete.lscapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;


import com.diezsiete.lscapp.db.entity.WordEntity;

import java.util.List;

@Dao
public interface WordDao extends BaseDao<WordEntity> {
    @Query("DELETE FROM word")
    void deleteAll();

    @Query("SELECT * FROM word")
    LiveData<List<WordEntity>> loadAll();

    @Query("SELECT * FROM word WHERE word = :word")
    LiveData<WordEntity> load(String word);
}

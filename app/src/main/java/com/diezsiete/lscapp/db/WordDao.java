package com.diezsiete.lscapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;


import com.diezsiete.lscapp.vo.Word;

import java.util.List;

@Dao
public interface WordDao extends BaseDao<Word> {
    @Query("DELETE FROM word")
    void deleteAll();

    @Query("SELECT * FROM word")
    LiveData<List<Word>> loadAll();

    @Query("SELECT * FROM word WHERE word = :word")
    LiveData<Word> load(String word);
}

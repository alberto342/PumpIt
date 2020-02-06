package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TagsTAO {

    @Insert
    void insert(Tags item);

    @Update
    void update(Tags item);

    @Delete
    void delete(Tags item);

    @Query("SELECT * FROM tags_table")
    LiveData<List<Tags>> getAllTags();

    @Query("SELECT * FROM tags_table WHERE food_id==:id")
    LiveData<Tags> getTagsByFoodId(int id);


    @Query("SELECT * FROM tags_table WHERE tag_id==:id")
    LiveData<List<Tags>> getTagss(int id);

}

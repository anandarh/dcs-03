package com.anandarh.storyapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anandarh.storyapp.models.StoryModel

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<StoryModel>)

    @Query("SELECT * FROM stories")
    fun getStories(): PagingSource<Int, StoryModel>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}
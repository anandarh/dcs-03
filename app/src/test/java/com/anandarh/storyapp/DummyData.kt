package com.anandarh.storyapp

import com.anandarh.storyapp.models.StoryModel

object DummyData {
    fun generateDummyStories(): List<StoryModel> {
        val storyList = ArrayList<StoryModel>()
        for (i in 0..10) {
            val story = StoryModel(
                "1",
                "Jhon",
                "Lorem ipsum dolor sit amet",
                "https://img-19.commentcamarche.net/cI8qqj-finfDcmx6jMK6Vr-krEw=/1500x/smart/b829396acc244fd484c5ddcdcb2b08f3/ccmcms-commentcamarche/20494859.jpg",
                "2022-01-08T06:34:18.598Z",
                null,
                null
            )
            storyList.add(story)
        }
        return storyList
    }
}
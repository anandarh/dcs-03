package com.anandarh.storyapp.utils

import com.anandarh.storyapp.models.LoginModel
import com.anandarh.storyapp.models.ResponseModel
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

    fun generateDummyLoginResponse(): ResponseModel {
        return ResponseModel(
            error = false,
            message = "success",
            loginResult = LoginModel(
                userId = "user-yj5pc_LARC_AgK61",
                name = "Arif Faizin",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
            ),
            listStory = null
        )
    }

    fun generateDummyRegisterResponse(): ResponseModel {
        return ResponseModel(
            error = false,
            message = "success",
            listStory = null,
            loginResult = null
        )
    }
}
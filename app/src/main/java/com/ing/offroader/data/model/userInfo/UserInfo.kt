package com.ing.offroader.data.model.userInfo

data class UserInfo(
        var attendance : ArrayList<String>
)

data class UserData(
        var user_name : Any ?= null,
        var user_email : Any ?= null,
        var user_age : Any ?= null,
        var photo_Url : Any ?= null,
        var achievements : Achievements ?= null,
        var community : Post ?= null,
)

data class Achievements(
        var san_id: SanID ?= null,
        var attendance: Attendance ?= null,
)

data class SanID(
        var badge_total : Any ?= null,
        var climb : Any ?= null,
        var distance : Any ?= null,
)

data class Attendance(
        var attendance_record : Any ?= null,
)

data class Post(
        var title : Any ?= null,
        var post_id : Any ?= null,
        var san : Any ?= null,
        var upload_date : Any ?= null,
        var contents : Any ?= null,
        var images : Any ?= null,
        var like : Any ?= null
)
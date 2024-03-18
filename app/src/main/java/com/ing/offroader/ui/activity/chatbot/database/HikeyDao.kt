package com.ing.offroader.ui.activity.chatbot.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HikeyDao {

    // 하이키
    @Insert(onConflict = OnConflictStrategy.IGNORE) // INSERT, key 충돌이 나도 무시하고 뒤에 추가해주도록 함.
    suspend fun insertHikeyChat(chat: HikeyData)
    @Query("SELECT * FROM hikey_data")
    fun getHikeyConversation(): LiveData<List<HikeyData>>

    @Query("DELETE FROM hikey_data")
    suspend fun deleteHikeyConversation()

}

@Dao
interface BongbongDao {
    // 봉봉이
    @Insert(onConflict = OnConflictStrategy.IGNORE) // INSERT, key 충돌이 나도 무시하고 뒤에 추가해주도록 함.
    suspend fun insertBongbongChat(chat: BongbongData)
    @Query("SELECT * FROM BONGBONG_DATA")
    fun getBongbongConversation(): LiveData<List<BongbongData>>

    @Query("DELETE FROM BONGBONG_DATA")
    suspend fun deleteBongbongConversation()
}
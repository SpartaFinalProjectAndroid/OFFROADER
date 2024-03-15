package com.ing.offroader.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.ing.offroader.ui.activity.chatbot.database.BongbongDao
import com.ing.offroader.ui.activity.chatbot.database.BongbongData
import com.ing.offroader.ui.activity.chatbot.database.HikeyDao
import com.ing.offroader.ui.activity.chatbot.database.HikeyData


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class HikeyRepository(private val hikeyDao: HikeyDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val hikeyConversation: LiveData<List<HikeyData>> = hikeyDao.getHikeyConversation()
    @Suppress("RedundantSuspendModifier") // suspend 키워드가 중복되어 사용되었을 때 나타나는 경고를 무시하도록 지정
    @WorkerThread  // 해당 메서드가 백그라운드 스레드에서 실행되어야 함
    suspend fun deleteHikeyConversation() {
        hikeyDao.deleteHikeyConversation()

    }
    @Suppress("RedundantSuspendModifier") // suspend 키워드가 중복되어 사용되었을 때 나타나는 경고를 무시하도록 지정
    @WorkerThread  // 해당 메서드가 백그라운드 스레드에서 실행되어야 함
    suspend fun insertHikeyChat(chat: HikeyData) {
        hikeyDao.insertHikeyChat(chat)
    }

}
class BongbongRepository(private val bongbongDao: BongbongDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.

    val bongbongConversation: LiveData<List<BongbongData>> = bongbongDao.getBongbongConversation()
    @Suppress("RedundantSuspendModifier") // suspend 키워드가 중복되어 사용되었을 때 나타나는 경고를 무시하도록 지정
    @WorkerThread  // 해당 메서드가 백그라운드 스레드에서 실행되어야 함
    suspend fun deleteBongbongConversation() {
        bongbongDao.deleteBongbongConversation()

    }
    @Suppress("RedundantSuspendModifier") // suspend 키워드가 중복되어 사용되었을 때 나타나는 경고를 무시하도록 지정
    @WorkerThread  // 해당 메서드가 백그라운드 스레드에서 실행되어야 함
     suspend fun insertBongbongChat(chat: BongbongData) {
        bongbongDao.insertBongbongChat(chat)
    }


}
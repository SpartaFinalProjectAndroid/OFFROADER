package com.mit.offroader.ui.fragment.chatbot.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ConversationRecord::class], exportSchema = false, version = 1)
abstract class ChatBotDatabase : RoomDatabase() {
    abstract fun getChatBotDao(): ChatBotDao

    private class ChatBotDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var chatBotDao = database.getChatBotDao()

                    // Delete all content here.

                }

            }
        }
    }


    companion object {
        private var INSTANCE: ChatBotDatabase? = null

        fun getDatabase(
            context: Context
            ,
            scope: CoroutineScope
        ): ChatBotDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context, ChatBotDatabase::class.java, "record_database"
                )
                    .addCallback(ChatBotDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as ChatBotDatabase
        }
    }

}
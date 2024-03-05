package com.mit.offroader.ui.fragment.chatbot.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [BongbongData::class], exportSchema = false, version = 1)
abstract class BongbongDatabase : RoomDatabase() {
    abstract fun getChatBotDao(): BongbongDao

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
        private var INSTANCE: BongbongDatabase? = null

        fun getDatabase(
            context: Context
            ,
            scope: CoroutineScope
        ): BongbongDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context, BongbongDatabase::class.java, "bongbong"
                )
                    .addCallback(ChatBotDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as BongbongDatabase
        }
    }

}
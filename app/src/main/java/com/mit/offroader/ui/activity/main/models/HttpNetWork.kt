package com.mit.offroader.ui.activity.main.models

import android.app.Application
import android.content.Context
import android.util.Log
import com.mit.offroader.data.RadioChannelURL
import com.mit.offroader.ui.activity.main.adapters.HttpItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import kotlin.concurrent.thread

class HttpNetWork {

    var radioUrl : String ?= null
    //private lateinit var listener: HttpTestInterface

    suspend fun httpNetWork(item: HttpItem, listener: HttpTestInterface) : String {

        when {
            RadioChannelURL.KBS_LIST.keys.contains(item.key) -> {
                val client = OkHttpClient()
                val request: Request = Request.Builder().url(item.url).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("Minyong", "fail!")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val jsonObject =
                                JSONTokener(response.body?.string()).nextValue() as JSONObject
                            val url = jsonObject.getJSONArray("channel_item")
                                    .getJSONObject(0)
                                    .getString("service_url")

                            listener.onReceive(url)
                            //Log.i("Minyong Http", (rx - tx).toString())
                            //radioUrl = url
                        }
                    }
                })
                return radioUrl.toString()
            }

            else -> {
                val client = OkHttpClient()
                val request = Request.Builder().url(item.url).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) { Log.i("Minyong", "fail!") }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            radioUrl = response.body?.string().toString()
                        }
                    }
                })
                return radioUrl.toString()
            }
        }
    }
}
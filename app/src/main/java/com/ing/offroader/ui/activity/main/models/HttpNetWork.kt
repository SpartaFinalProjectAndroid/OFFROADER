package com.ing.offroader.ui.activity.main.models

import com.ing.offroader.data.RadioChannelURL
import com.ing.offroader.ui.activity.main.adapters.HttpItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONTokener

class HttpNetWork {

    fun httpNetWork(item: HttpItem) : String {
        when {
            RadioChannelURL.KBS_LIST.keys.contains(item.key) -> {
                val client = OkHttpClient()
                val request: Request = Request.Builder().url(item.url).build()

                client.newCall(request).execute().use {
                    return if (it.body != null) {
                        val jsonObject = JSONTokener(it.body?.string()).nextValue() as JSONObject
                        val url = jsonObject.getJSONArray("channel_item")
                            .getJSONObject(0)
                            .getString("service_url")
                        url
                    } else {
                        "body null"
                    }
                }
            }

            else -> {
                val client = OkHttpClient()
                val request = Request.Builder().url(item.url).build()
                client.newCall(request).execute().use{
                    return if (it.body != null) { it.body!!.string() }
                    else { "body null" }
                }
            }
        }
    }
}
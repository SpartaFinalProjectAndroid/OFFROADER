package com.ing.offroader.ui.activity.sandetail

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ing.offroader.R
import com.ing.offroader.data.model.weather.WeekendWeatherData
import com.ing.offroader.databinding.ItemDetailWeatherBinding


private const val TAG = "WeatherRecyclerAdapter"

class WeatherRecyclerAdapter(private val context: Context, private var data: MutableList<WeekendWeatherData>) :
    RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemDetailWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        val suffix = binding.tvWeatherAmpm
        val time = binding.tvWeatherTime
        val thermo = binding.tvWeatherTemperature
        val img = binding.ivWeatherImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherInfo = data[position]

        val splitParts = weatherInfo.dt_txt.split(" ")
//        val formattedTemp = convertAndFormatTemperature(weatherInfo.main.temp).toDouble().toInt()
        val formattedTemp = convertAndFormatTemperature(weatherInfo.main.temp)
        Log.d(TAG, "onBindViewHolder: $formattedTemp")
        val icon = weatherInfo.weather[0].icon
        Log.d(TAG, "onBindViewHolder: $icon")
        val iconUrl = "http://openweathermap.org/img/w/$icon.png"
        Log.d(TAG, "onBindViewHolder: $iconUrl")

        holder.apply {
            suffix.text = if (splitParts[1].substring(0, 2).toInt() < 12) "AM" else "PM"
            time.text = "${splitParts[1].substring(0, 2).toInt()}시"
            thermo.text = "${formattedTemp}°"
            Glide.with(context)
                .load(iconUrl)
                .error(R.drawable.ic_launcher_foreground)
//                .timeout(30)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e(TAG, "Glide load failed", e)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(img)

        }
    }

    fun updateData(newData: List<WeekendWeatherData>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size
}

private fun convertAndFormatTemperature(kelvinTemp: Double): String {
    //켈빈온도에서 273.15를 빼면 섭씨온도임
    val celsiusTemp = kelvinTemp - 273.15
//    return String.format("%.1f", celsiusTemp)
    return Math.round(celsiusTemp).toString()
}
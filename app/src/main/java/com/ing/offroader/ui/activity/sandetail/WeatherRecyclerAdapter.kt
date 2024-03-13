package com.ing.offroader.ui.activity.sandetail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ing.offroader.data.model.weather.WeekendWeatherData
import com.ing.offroader.databinding.ItemDetailWeatherBinding

class WeatherRecyclerAdapter(private val context: Context, private var data: MutableList<WeekendWeatherData>) :
    RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemDetailWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemDetailWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherItem: WeekendWeatherData) {
            with(binding) {
                val splitParts = weatherItem.dt_txt.split(" ")
                val formattedTemp = convertAndFormatTemperature(weatherItem.main.temp)
                val icon = weatherItem.weather[0].icon
                val iconUrl = "https://openweathermap.org/img/w/$icon.png"

                tvWeatherAmpm.text = if (splitParts[1].substring(0, 2).toInt() < 12) "AM" else "PM"
                tvWeatherTime.text = splitParts[1].substring(0, 5)
                tvWeatherTemperature.text = "${formattedTemp}°"
                Glide.with(context)
                    .load(iconUrl)
                    .into(ivWeatherImg)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherItem = data[position]
        holder.bind(weatherItem)
    }

    fun updateData(newData: List<WeekendWeatherData>) {
        data.clear() // 기존 데이터를 지웁니다.
        data.addAll(newData) // 새로운 데이터로 업데이트합니다.
        notifyDataSetChanged() // 데이터 변경을 알려 리사이클러뷰를 새로고침합니다.
    }
}

private fun convertAndFormatTemperature(kelvinTemp: Double): String {
    val celsiusTemp = kelvinTemp - 273.15
    return String.format("%.1f", celsiusTemp)
}

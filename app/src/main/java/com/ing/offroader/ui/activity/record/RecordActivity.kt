package com.ing.offroader.ui.activity.record

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ing.offroader.BuildConfig
import com.ing.offroader.R
import com.ing.offroader.databinding.ActivityRecordBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MapConstants
import com.naver.maps.map.widget.CompassView
import com.naver.maps.map.widget.ZoomControlView


class RecordActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private lateinit var binding: ActivityRecordBinding

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var uiSettings: UiSettings

    private var auth: FirebaseAuth? = null
    private val user = FirebaseAuth.getInstance().currentUser
    private val firestore = FirebaseFirestore.getInstance()

    private var coordinateList: MutableList<Map<String, Double>> = mutableListOf()
    private lateinit var name: String
    private lateinit var category: String
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVERMAPS_API_KEY)
        setContentView(binding.root)

        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        name = intent.getStringExtra("name").toString()
        category = intent.getStringExtra("category").toString()
        date = intent.getStringExtra("date").toString()

        mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // 이동거리를 단순하게 보기 위해 km로 변환
    fun kmFormat(distance: String): String {
        val meter = distance.toInt()
        val km: Double = meter.toDouble() / 1000
        val formattedResult = java.lang.String.format("%.1f", km) + "km"
        return formattedResult
    }

    private fun setUpMap() {
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.maxZoom = 15.0
        naverMap.minZoom = 7.0
        naverMap.extent = MapConstants.EXTENT_KOREA
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        this.naverMap.mapType = NaverMap.MapType.Basic
        this.naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true)
        this.uiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = false
        uiSettings.isCompassEnabled = false

        val zoomControlView: ZoomControlView = binding.zoom
        zoomControlView.map = naverMap

        val compassView: CompassView = binding.compass
        compassView.map = naverMap

        val path = PathOverlay()
        val coords = mutableListOf<LatLng>()

        firestore.collection("polyLine").document(user!!.uid).collection(category).document(date)
            .get()
            .addOnSuccessListener { documents ->
                binding.tvMountainName.text = name
                binding.tvDate.text = documents.data!!["date"].toString()
                binding.tvDistance.text = kmFormat(documents.data!!["distance"].toString())
                binding.tvDuration.text = documents.data!!["duration"].toString()
                coordinateList = documents.data!!["coordinate"] as MutableList<Map<String, Double>>
                for (i in 0 until coordinateList.size) {
                    val lat = coordinateList.get(i).getValue("lat")
                    val lng = coordinateList.get(i).getValue("lng")
                    coords.add(LatLng(lat, lng))
                }
                // 경로선은 최소 2개의 좌표가 있어야 생기므로 오류 방지
                if (coords.size > 2) {
                    path.coords = coords
                    path.map = naverMap
                }
            }
        setUpMap()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }
}
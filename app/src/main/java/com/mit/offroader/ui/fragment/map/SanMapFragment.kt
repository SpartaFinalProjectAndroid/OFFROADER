package com.mit.offroader.ui.fragment.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mit.offroader.R
import com.mit.offroader.databinding.FragmentSanMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.CompassView
import com.naver.maps.map.widget.LocationButtonView
import com.naver.maps.map.widget.LogoView
import com.naver.maps.map.widget.ScaleBarView
import com.naver.maps.map.widget.ZoomControlView


class SanMapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = SanMapFragment()
        private const val PERMISSION_REQUEST_CODE = 100
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private var _binding: FragmentSanMapBinding? = null
    private val binding get() = _binding!!

    private val sanMapViewModel by viewModels<SanMapViewModel>()

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var uiSettings: UiSettings

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                initMapView()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSanMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        if (!hasPermission()) {
            requestMultiplePermissions.launch(PERMISSIONS)
        } else {
            initMapView()
        }
    }

    // 프래그먼트에 지도 추가
    private fun initMapView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_view) as MapFragment?
                ?: MapFragment.newInstance().also {
                    childFragmentManager.beginTransaction().add(R.id.map_view, it)
                        .commit()
                }

        // fragment의 getMapAsync() 메서드로 OnMapReadyCallBack 콜백을 등록하면, 비동기로 NaverMap 객체를 얻을 수 있음
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)
    }

    // hasPermission()에서는 위치 권한 있을 경우 true, 없을 경우 false
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) }
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    // 지도 그리는 부분
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 지도 타입 설정
        this.naverMap.mapType = NaverMap.MapType.Satellite
        // 지도 레이어 설정
        this.naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true)
        // 지도 컨트롤 요소 수동 설정
        this.uiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = false
        uiSettings.isCompassEnabled = true
        uiSettings.isScaleBarEnabled = false

        val zoomControlView: ZoomControlView = binding.zoom
        zoomControlView.map = naverMap

        val compassView: CompassView = binding.compass
        compassView.map = naverMap

        val scaleBarView: ScaleBarView = binding.scalebar
        scaleBarView.map = naverMap

        // 최대 확대 및 축소 비율 설정
        this.naverMap.maxZoom = 21.0
        this.naverMap.minZoom = 15.0

        setUpMap()

        //권한 확인하여 위치 추가
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext()) // 초기화
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // 산 마커 목록
        Marker().apply {
            position = LatLng(37.658100, 126.977712)
//            icon = MarkerIcons.YELLOW
//            iconTintColor = Color.WHITE
//            alpha = 0.5f
            icon = OverlayImage.fromResource(R.drawable.ic_transparent)
            width = resources.getDimensionPixelSize(R.dimen.marker_size)
            height = resources.getDimensionPixelSize(R.dimen.marker_size)
            captionText = "북한산"
            captionColor = Color.WHITE
            setCaptionAligns(Align.Top)
            captionHaloColor = Color.rgb(0, 0, 0)
//            captionMinZoom = 12.0
            captionTextSize = 16f
            map = naverMap
        }

        Marker().apply {
            position = LatLng(35.3664, 127.7156)
            icon = MarkerIcons.BLACK
            iconTintColor = Color.RED
            alpha = 0.5f
            captionText = "지리산"
            map = naverMap
        }

        Marker().apply {
            position = LatLng(33.362096, 126.533608)
            icon = OverlayImage.fromResource(R.drawable.ic_marker2)
            width = resources.getDimensionPixelSize(R.dimen.marker_size)
            height = resources.getDimensionPixelSize(R.dimen.marker_size)
            captionText = "한라산"
            captionColor = Color.WHITE
            captionHaloColor = Color.rgb(0, 0, 0)
//            captionMinZoom = 12.0
            captionTextSize = 16f
            map = naverMap
        }

        Marker().apply {
            position = LatLng(38.1208537, 128.4603735)
            icon = MarkerIcons.BLACK
            iconTintColor = Color.RED
            alpha = 0.5f
            captionText = "설악산"
            map = naverMap
        }

        Marker().apply {
            position = LatLng(35.4879127, 126.9074771)
            icon = MarkerIcons.BLACK
            iconTintColor = Color.RED
            alpha = 0.5f
            captionText = "내장산"
            map = naverMap
        }

        Marker().apply {
            position = LatLng(36.9584900, 128.4804082)
            icon = MarkerIcons.BLACK
            iconTintColor = Color.RED
            alpha = 0.5f
            captionText = "소백산"
            map = naverMap
        }

        Marker().apply {
            position = LatLng(36.543146, 127.870689)
            icon = MarkerIcons.BLACK
            iconTintColor = Color.RED
            alpha = 0.5f
            captionText = "속리산"
            map = naverMap
        }

        Marker().apply {
            position = LatLng(36.350315, 127.201472)
            icon = MarkerIcons.BLACK
            iconTintColor = Color.RED
            alpha = 0.5f
            captionText = "계룡산"
            map = naverMap
        }

        Marker().apply {
            position = LatLng(37.794433, 128.543595)
            icon = MarkerIcons.BLACK
            iconTintColor = Color.RED
            alpha = 0.5f
            captionText = "오대산"
            map = naverMap
        }
    }

    // 현재 위치 기능 지도에 추가
    private fun setUpMap() {
        naverMap.locationSource = locationSource //현위치
        naverMap.uiSettings.isLocationButtonEnabled = true // 현 위치 버튼 기능
        naverMap.locationTrackingMode = LocationTrackingMode.Follow // 위치를 추적하면서 카메라도 같이 움직임
        // 줌
        naverMap.maxZoom = 15.0  // (최대 21)
        naverMap.minZoom = 9.0
    }

    // MapView 라이프 사이클 메서드를 호출
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

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
package com.mit.offroader.ui.fragment.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.CompassView
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

    // 마커 이동을 위한 개별 마커마다 구분
    private var marker1 = Marker()
    private var marker2 = Marker()
    private var marker3 = Marker()
    private var marker4 = Marker()
    private var marker5 = Marker()
    private var marker6 = Marker()
    private var marker7 = Marker()
    private var marker8 = Marker()
    private var marker9 = Marker()

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
            marker1.position = LatLng(37.658100, 126.977712)
            marker1.captionText = getString(R.string.san_name_bukhansan)
            marker1.icon = OverlayImage.fromResource(R.drawable.ic_transparent)
            marker1.width = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker1.height = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker1.captionColor = Color.WHITE
            marker1.setCaptionAligns(Align.Top)
            marker1.captionHaloColor = Color.rgb(0, 0, 0)
            marker1.captionTextSize = 16f
            marker1.map = naverMap
        }

        Marker().apply {
            marker2.position = LatLng(35.3664, 127.7156)
            marker2.captionText = getString(R.string.san_name_jirisan)
            marker2.icon = OverlayImage.fromResource(R.drawable.ic_transparent)
            marker2.width = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker2.height = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker2.captionColor = Color.WHITE
            marker2.setCaptionAligns(Align.Top)
            marker2.captionHaloColor = Color.rgb(0, 0, 0)
            marker2.captionTextSize = 16f
            marker2.map = naverMap
        }

        Marker().apply {
            marker3.position = LatLng(33.362096, 126.533608)
            marker3.captionText = getString(R.string.san_name_hallasan)
            marker3.icon = OverlayImage.fromResource(R.drawable.ic_transparent)
            marker3.width = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker3.height = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker3.captionColor = Color.WHITE
            marker3.setCaptionAligns(Align.Top)
            marker3.captionHaloColor = Color.rgb(0, 0, 0)
            marker3.captionTextSize = 16f
            marker3.map = naverMap
        }

        Marker().apply {
            marker4.position = LatLng(38.1208537, 128.4603735)
            marker4.captionText = getString(R.string.san_name_seoraksan)
            marker4.icon = OverlayImage.fromResource(R.drawable.transparent)
            marker4.width = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker4.height = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker4.captionColor = Color.WHITE
            marker4.setCaptionAligns(Align.Top)
            marker4.captionHaloColor = Color.rgb(0, 0, 0)
            marker4.captionTextSize = 16f
            marker4.map = naverMap
        }

        Marker().apply {
            marker5.position = LatLng(35.4879127, 126.9074771)
            marker5.captionText = getString(R.string.san_name_naejangsan)
            marker5.icon = OverlayImage.fromResource(R.drawable.ic_transparent)
            marker5.width = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker5.height = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker5.captionColor = Color.WHITE
            marker5.setCaptionAligns(Align.Top)
            marker5.captionHaloColor = Color.rgb(0, 0, 0)
            marker5.captionTextSize = 16f
            marker5.map = naverMap
        }

        Marker().apply {
            marker6.position = LatLng(36.9584900, 128.4804082)
            marker6.captionText = getString(R.string.san_name_sobaeksan)
            marker6.icon = OverlayImage.fromResource(R.drawable.ic_transparent)
            marker6.width = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker6.height = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker6.captionColor = Color.WHITE
            marker6.setCaptionAligns(Align.Top)
            marker6.captionHaloColor = Color.rgb(0, 0, 0)
            marker6.captionTextSize = 16f
            marker6.map = naverMap
        }

        Marker().apply {
            marker7.position = LatLng(36.543146, 127.870689)
            marker7.captionText = getString(R.string.san_name_songnisan)
            marker7.icon = OverlayImage.fromResource(R.drawable.ic_transparent)
            marker7.width = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker7.height = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker7.captionColor = Color.WHITE
            marker7.setCaptionAligns(Align.Top)
            marker7.captionHaloColor = Color.rgb(0, 0, 0)
            marker7.captionTextSize = 16f
            marker7.map = naverMap
        }

        Marker().apply {
            marker8.position = LatLng(36.350315, 127.201472)
            marker8.captionText = getString(R.string.san_name_gyeryongsan)
            marker8.icon = OverlayImage.fromResource(R.drawable.ic_transparent)
            marker8.width = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker8.height = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker8.captionColor = Color.WHITE
            marker8.setCaptionAligns(Align.Top)
            marker8.captionHaloColor = Color.rgb(0, 0, 0)
            marker8.captionTextSize = 16f
            marker8.map = naverMap
        }

        Marker().apply {
            marker9.position = LatLng(37.794433, 128.543595)
            marker9.captionText = getString(R.string.san_name_odaesan)
            marker9.icon = OverlayImage.fromResource(R.drawable.ic_transparent)
            marker9.width = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker9.height = resources.getDimensionPixelSize(R.dimen.marker_size)
            marker9.captionColor = Color.WHITE
            marker9.setCaptionAligns(Align.Top)
            marker9.captionHaloColor = Color.rgb(0, 0, 0)
            marker9.captionTextSize = 16f
            marker9.map = naverMap
        }

        // 검색창에서 키보드로 엔터 구현 / 키보드 비활성화 및 마커로 이동
        binding.etInputLocation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.ivSearchLocation.performClick()
                binding.ivSearchLocation.setOnClickListener {
                    when(binding.etInputLocation.text.toString()) {
                        getString(R.string.san_name_bukhansan) -> {
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                marker1.position, 15.0
                            )
                            naverMap.moveCamera(cameraUpdate)
                        }
                        getString(R.string.san_name_jirisan) -> {
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                marker2.position, 15.0
                            )
                            naverMap.moveCamera(cameraUpdate)
                        }
                        getString(R.string.san_name_hallasan) -> {
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                marker3.position, 15.0
                            )
                            naverMap.moveCamera(cameraUpdate)
                        }
                        getString(R.string.san_name_seoraksan) -> {
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                marker4.position, 15.0
                            )
                            naverMap.moveCamera(cameraUpdate)
                        }
                        getString(R.string.san_name_naejangsan) -> {
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                marker5.position, 15.0
                            )
                            naverMap.moveCamera(cameraUpdate)
                        }
                        getString(R.string.san_name_sobaeksan) -> {
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                marker6.position, 15.0
                            )
                            naverMap.moveCamera(cameraUpdate)
                        }
                        getString(R.string.san_name_songnisan) -> {
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                marker7.position, 15.0
                            )
                            naverMap.moveCamera(cameraUpdate)
                        }
                        getString(R.string.san_name_gyeryongsan) -> {
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                marker8.position, 15.0
                            )
                            naverMap.moveCamera(cameraUpdate)
                        }
                        getString(R.string.san_name_odaesan) -> {
                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                marker9.position, 15.0
                            )
                            naverMap.moveCamera(cameraUpdate)
                        }
                    }
                }
            }
            false
        }

        // 검색 버튼 클릭 시 마커 이동
        binding.ivSearchLocation.setOnClickListener {
            when(binding.etInputLocation.text.toString()) {
                getString(R.string.san_name_bukhansan) -> {
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        marker1.position, 15.0
                    )
                    naverMap.moveCamera(cameraUpdate)
                }
                getString(R.string.san_name_jirisan) -> {
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        marker2.position, 15.0
                    )
                    naverMap.moveCamera(cameraUpdate)
                }
                getString(R.string.san_name_hallasan) -> {
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        marker3.position, 15.0
                    )
                    naverMap.moveCamera(cameraUpdate)
                }
                getString(R.string.san_name_seoraksan) -> {
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        marker4.position, 15.0
                    )
                    naverMap.moveCamera(cameraUpdate)
                }
                getString(R.string.san_name_naejangsan) -> {
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        marker5.position, 15.0
                    )
                    naverMap.moveCamera(cameraUpdate)
                }
                getString(R.string.san_name_sobaeksan) -> {
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        marker6.position, 15.0
                    )
                    naverMap.moveCamera(cameraUpdate)
                }
                getString(R.string.san_name_songnisan) -> {
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        marker7.position, 15.0
                    )
                    naverMap.moveCamera(cameraUpdate)
                }
                getString(R.string.san_name_gyeryongsan) -> {
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        marker8.position, 15.0
                    )
                    naverMap.moveCamera(cameraUpdate)
                }
                getString(R.string.san_name_odaesan) -> {
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        marker9.position, 15.0
                    )
                    naverMap.moveCamera(cameraUpdate)
                }
            }
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
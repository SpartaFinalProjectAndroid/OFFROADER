package com.ing.offroader.ui.fragment.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.ing.offroader.BuildConfig
import com.ing.offroader.R
import com.ing.offroader.databinding.FragmentSanMapBinding
import com.ing.offroader.ui.activity.sandetail.SanDetailActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.widget.CompassView
import com.naver.maps.map.widget.ScaleBarView
import com.naver.maps.map.widget.ZoomControlView
import java.text.NumberFormat
import java.util.Locale


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

    private val firestore = FirebaseFirestore.getInstance()

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
        NaverMapSdk.getInstance(requireContext()).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVERMAPS_API_KEY)
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
        this.naverMap.mapType = NaverMap.MapType.Basic
        // 지도 레이어 설정
        this.naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true)
        // 지도 컨트롤 요소 수동 설정
        this.uiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = false
        uiSettings.isCompassEnabled = false
        uiSettings.isScaleBarEnabled = false

        val zoomControlView: ZoomControlView = binding.zoom
        zoomControlView.map = naverMap

        val compassView: CompassView = binding.compass
        compassView.map = naverMap

        val scaleBarView: ScaleBarView = binding.scalebar
        scaleBarView.map = naverMap

        // 최대 확대 및 축소 비율 설정
        this.naverMap.maxZoom = 19.0
        this.naverMap.minZoom = 10.0

        setUpMap()

        with(binding) {
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
            // 마커 정보를 담을 배열 설정
            var markerDTOs: ArrayList<MarkerDTO> = arrayListOf()
            // Firestore에서 markers collection 접근하여 쿼리를 가져옴
            firestore.collection("markers")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (querySnapshot == null) {
                        return@addSnapshotListener
                    }
                    if (markerDTOs.size == 0) {
                        for (snapshot in querySnapshot.documents) {
                            markerDTOs.add(snapshot.toObject(MarkerDTO::class.java)!!)
                            for (idx in 0 until markerDTOs.size) {
                                // 마커 여러개 찍기
                                val markers = arrayOfNulls<Marker>(markerDTOs.size)
                                markers[idx] = Marker()
                                val lat = markerDTOs[idx].lat
                                val lnt = markerDTOs[idx].lng
                                markers[idx]!!.position = LatLng(lat!!, lnt!!)
                                markers[idx]!!.captionText = markerDTOs[idx].name!!
                                markers[idx]!!.icon =
                                    OverlayImage.fromResource(R.drawable.ic_marker)
                                markers[idx]!!.width =
                                    resources.getDimensionPixelSize(R.dimen.marker_size_3)
                                markers[idx]!!.height =
                                    resources.getDimensionPixelSize(R.dimen.marker_size_3)
                                //카메라 변화 감지하여 줌 레벨에 따라 마커의 크기 변경
                                naverMap.addOnCameraChangeListener { _, _ ->
                                    if (naverMap.cameraPosition.zoom >= 10.0 && naverMap.cameraPosition.zoom < 11.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_1)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_1)
                                    } else if (naverMap.cameraPosition.zoom >= 11.0 && naverMap.cameraPosition.zoom < 12.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_2)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_2)
                                    } else if (naverMap.cameraPosition.zoom >= 12.0 && naverMap.cameraPosition.zoom < 13.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_3)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_3)
                                    } else if (naverMap.cameraPosition.zoom >= 13.0 && naverMap.cameraPosition.zoom < 14.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_4)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_4)
                                    } else if (naverMap.cameraPosition.zoom >= 14.0 && naverMap.cameraPosition.zoom < 15.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_5)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_5)
                                    } else if (naverMap.cameraPosition.zoom >= 15.0 && naverMap.cameraPosition.zoom < 16.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_6)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_6)
                                    } else if (naverMap.cameraPosition.zoom >= 16.0 && naverMap.cameraPosition.zoom < 17.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_7)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_7)
                                    } else if (naverMap.cameraPosition.zoom >= 17.0 && naverMap.cameraPosition.zoom < 18.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_8)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_8)
                                    } else if (naverMap.cameraPosition.zoom >= 18.0 && naverMap.cameraPosition.zoom < 19.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_9)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_9)
                                    } else if (naverMap.cameraPosition.zoom in 19.0..20.0) {
                                        markers[idx]!!.width =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_10)
                                        markers[idx]!!.height =
                                            resources.getDimensionPixelSize(R.dimen.marker_size_10)
                                    }
                                }
                                markers[idx]!!.isIconPerspectiveEnabled = true
                                markers[idx]!!.captionColor = Color.WHITE
                                markers[idx]!!.captionHaloColor = Color.rgb(0, 0, 0)
                                markers[idx]!!.captionTextSize = 16f
                                //마커 클릭 시 정보창 visibility 유무
                                markers[idx]!!.setOnClickListener {
                                    if (markerInfo.visibility == View.GONE) {
                                        tvMarkerName.text = markerDTOs[idx].name
                                        tvMarkerHeight.text =
                                            NumberFormat.getInstance(Locale.getDefault())
                                                .format(markerDTOs[idx].height) + "m"
                                        tvMarkerDescription.text = markerDTOs[idx].description
                                        Glide.with(requireContext())
                                            .asDrawable()
                                            .load(markerDTOs[idx].image)
                                            .into(ivMarkerInfoImage)
                                        roundLeft(ivMarkerInfoImage, 15f)
                                        markerInfo.visibility = View.VISIBLE
                                        ivMarkerInfoImage.visibility = View.VISIBLE
                                    } else if (markerInfo.visibility == View.VISIBLE) {
                                        markerInfo.visibility = View.GONE
                                        ivMarkerInfoImage.visibility = View.GONE
                                    }
                                    // 마커 정보창 클릭 시 상세 정보로 이동
                                    markerInfo.setOnClickListener {
                                        val intent = Intent(activity, SanDetailActivity::class.java)
                                        intent.putExtra("name", markerDTOs[idx].name)
                                        startActivity(intent)
                                    }
                                    false
                                }
                                ivInfoClose.setOnClickListener {
                                    if (markerInfo.visibility == View.VISIBLE) {
                                        markerInfo.visibility = View.GONE
                                        ivMarkerInfoImage.visibility = View.GONE
                                    }
                                }
                                markers[idx]!!.map = naverMap
                            }
                        }
                    }
                    etInputLocation.setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            ivSearchLocation.performClick()
                            ivSearchLocation.setOnClickListener {
                                for (idx in 0 until markerDTOs.size) {
                                    if (etInputLocation.text.toString() == markerDTOs[idx].name) {
                                        val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                            LatLng(markerDTOs[idx].lat!!, markerDTOs[idx].lng!!),
                                            15.0
                                        ).animate(CameraAnimation.Fly, 1500)
                                        naverMap.moveCamera(cameraUpdate)
                                    }
                                }
                            }
                        }
                        false
                    }
                    ivSearchLocation.setOnClickListener {
                        hideKeyboard()
                        for (idx in 0 until markerDTOs.size) {
                            if (etInputLocation.text.toString() == markerDTOs[idx].name) {
                                val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                    LatLng(markerDTOs[idx].lat!!, markerDTOs[idx].lng!!), 17.0
                                ).animate(CameraAnimation.Fly, 1500)
                                naverMap.moveCamera(cameraUpdate)
                            }
                        }
                    }

                }
        }
    }

    // 키보드 내림처리
    private fun hideKeyboard() {
        if (activity != null && activity?.currentFocus != null) {
            // 프래그먼트기 때문에 getActivity() 사용
            val inputManager =
                requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    // 마커 정보창 이미지 왼쪽만 라운드 처리
    fun roundLeft(iv: ImageView, curveRadius: Float): ImageView {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            iv.outlineProvider = object : ViewOutlineProvider() {

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(
                        0,
                        0,
                        (view!!.width + curveRadius).toInt(),
                        (view.height).toInt(),
                        curveRadius
                    )
                }
            }

            iv.clipToOutline = true
        }
        return iv
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

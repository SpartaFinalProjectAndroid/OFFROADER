package com.ing.offroader.ui.fragment.map

data class latLng(
    var lat: Double? = null,
    var lng: Double? = null
)

data class LatLngTest(
    val latLng: MutableList<Coordinate> ?= null,
    val name: Any ?= null,
)




data class Coordinate(
    val lat: Any ?= null,
    val lng: Any ?= null
)

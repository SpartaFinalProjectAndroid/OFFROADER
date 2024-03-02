package com.mit.offroader.data

object RadioChannelURL {

    val MBC_LIST : Map<String, String> = mapOf(
        "표준FM" to "https://sminiplay.imbc.com/aacplay.ashx?agent=webapp&channel=sfm",
        "FM4U" to "https://sminiplay.imbc.com/aacplay.ashx?agent=webapp&channel=mfm"
    )

    val SBS_LIST : Map<String, String> = mapOf(
        "러브FM" to "https://apis.sbs.co.kr/play-api/1.0/livestream/lovepc/lovefm?protocol=hls&ssl=Y",
        "파워FM" to "https://apis.sbs.co.kr/play-api/1.0/livestream/powerpc/powerfm?protocol=hls&ssl=Y"
    )

}
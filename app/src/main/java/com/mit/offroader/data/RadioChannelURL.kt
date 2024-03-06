package com.mit.offroader.data

object RadioChannelURL {

    const val PREFERENCE_KEY = "minyong"
    const val DATA_KEY = "radioLikeListdatakey"

    val RADIO_API_URL : Map<String, String> = mapOf(

        // MBC_LIST
        "표준FM" to "https://sminiplay.imbc.com/aacplay.ashx?agent=webapp&channel=sfm",
        "FM4U" to "https://sminiplay.imbc.com/aacplay.ashx?agent=webapp&channel=mfm",
        "올댓뮤직" to "https://sminiplay.imbc.com/aacplay.ashx?agent=webapp&channel=chm",

        // SBS_LIST
        "러브FM" to "https://apis.sbs.co.kr/play-api/1.0/livestream/lovepc/lovefm?protocol=hls&ssl=Y",
        "파워FM" to "https://apis.sbs.co.kr/play-api/1.0/livestream/powerpc/powerfm?protocol=hls&ssl=Y",

        // KBS_LIST
        "1Radio" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/21",
        "HappyFM" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/22",
        "3Radio" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/23",
        "ClassicFM" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/24",
        //"CoolFM" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/25",
        "한민족방송" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/26",
    )

    val MBC_LIST : Map<String, String> = mapOf(
        "표준FM" to "https://sminiplay.imbc.com/aacplay.ashx?agent=webapp&channel=sfm",
        "FM4U" to "https://sminiplay.imbc.com/aacplay.ashx?agent=webapp&channel=mfm",
        "올댓뮤직" to "https://sminiplay.imbc.com/aacplay.ashx?agent=webapp&channel=chm"
    )

    val SBS_LIST : Map<String, String> = mapOf(
        "러브FM" to "https://apis.sbs.co.kr/play-api/1.0/livestream/lovepc/lovefm?protocol=hls&ssl=Y",
        "파워FM" to "https://apis.sbs.co.kr/play-api/1.0/livestream/powerpc/powerfm?protocol=hls&ssl=Y"
    )

    val KBS_LIST : Map<String, String> = mapOf(
        "1Radio" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/21",
        "HappyFM" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/22",
        "3Radio" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/23",
        "ClassicFM" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/24",
        //"CoolFM" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/25",
        "한민족방송" to "https://cfpwwwapi.kbs.co.kr/api/v1/landing/live/channel_code/26",
    )

}
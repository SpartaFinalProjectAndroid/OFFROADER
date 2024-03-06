package com.mit.offroader.ui.fragment.home

sealed class HomeUiData {

    data object First : HomeUiData()
    data object Second : HomeUiData()
    data object Third : HomeUiData()
    data class Fourth(
        var image: String?,
        var title: String?,
        var des: String?,
        var date: Long?
    ) : HomeUiData()

    data object Attribute : HomeUiData()

    /** Fourth의 생성자 라고 말 하는게 맞는지 정확히 모르겠으나
     * 데이터 클래스를 만들어 할당하는 것과 직접 할당하는 것이 같음
     * 검색결과 사람마다 구현하는 방법이 다름.
     * 추가학습 요

    data class Fourth(val contentModel: ContentModel) : HomeUiData()

    data class ContentModel(
        var image: String = "",
        var title: String = "",
        var des: String = "",
        var date: String = ""
    )
    */

}

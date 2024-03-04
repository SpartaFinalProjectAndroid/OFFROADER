package com.mit.offroader.ui.activity.sandetail

import android.media.Image
import com.mit.offroader.R

data class SanDetailImageData(val mountain: String, val img: Int) {
    companion object {
        val imageList = mutableListOf(
            SanDetailImageData("계룡산", R.drawable.img_kyeryongsan1),
            SanDetailImageData("계룡산", R.drawable.img_kyeryongsan2),
            SanDetailImageData("계룡산", R.drawable.img_kyeryongsan3),
            SanDetailImageData("계룡산", R.drawable.img_kyeryongsan4),
            SanDetailImageData("계룡산", R.drawable.img_kyeryongsan5),
            SanDetailImageData("내장산", R.drawable.img_naejangsan1),
            SanDetailImageData("내장산", R.drawable.img_naejangsan2),
            SanDetailImageData("내장산", R.drawable.img_naejangsan3),
            SanDetailImageData("내장산", R.drawable.img_naejangsan4),
            SanDetailImageData("내장산", R.drawable.img_naejangsan5),
            SanDetailImageData("북한산", R.drawable.img_northhansan1),
            SanDetailImageData("북한산", R.drawable.img_northhansan2),
            SanDetailImageData("북한산", R.drawable.img_northhansan3),
            SanDetailImageData("북한산", R.drawable.img_northhansan4),
            SanDetailImageData("북한산", R.drawable.img_northhansan5),
            SanDetailImageData("설악산", R.drawable.img_seullaksan1),
            SanDetailImageData("설악산", R.drawable.img_seullaksan2),
            SanDetailImageData("설악산", R.drawable.img_seullaksan3),
            SanDetailImageData("설악산", R.drawable.img_seullaksan4),
            SanDetailImageData("설악산", R.drawable.img_seullaksan5),
            SanDetailImageData("소백산", R.drawable.img_sobaeksan1),
            SanDetailImageData("소백산", R.drawable.img_sobaeksan2),
            SanDetailImageData("소백산", R.drawable.img_sobaeksan3),
            SanDetailImageData("소백산", R.drawable.img_sobaeksan4),
            SanDetailImageData("소백산", R.drawable.img_sobaeksan5),
            SanDetailImageData("속리산", R.drawable.img_sokrisan1),
            SanDetailImageData("속리산", R.drawable.img_sokrisan2),
            SanDetailImageData("속리산", R.drawable.img_sokrisan3),
            SanDetailImageData("속리산", R.drawable.img_sokrisan4),
            SanDetailImageData("속리산", R.drawable.img_sokrisan5),
            SanDetailImageData("오대산", R.drawable.img_5daesan1),
            SanDetailImageData("오대산", R.drawable.img_5daesan2),
            SanDetailImageData("오대산", R.drawable.img_5daesan3),
            SanDetailImageData("오대산", R.drawable.img_5daesan4),
            SanDetailImageData("오대산", R.drawable.img_5daesan5),
            SanDetailImageData("지리산", R.drawable.img_jirisan1),
            SanDetailImageData("지리산", R.drawable.img_jirisan2),
            SanDetailImageData("지리산", R.drawable.img_jirisan3),
            SanDetailImageData("지리산", R.drawable.img_jirisan4),
            SanDetailImageData("지리산", R.drawable.img_jirisan5),
            SanDetailImageData("한라산", R.drawable.img_hanrasan1),
            SanDetailImageData("한라산", R.drawable.img_hanrasan2),
            SanDetailImageData("한라산", R.drawable.img_hanrasan3),
            SanDetailImageData("한라산", R.drawable.img_hanrasan4),
            SanDetailImageData("한라산", R.drawable.img_hanrasan5)
        )
    }
}

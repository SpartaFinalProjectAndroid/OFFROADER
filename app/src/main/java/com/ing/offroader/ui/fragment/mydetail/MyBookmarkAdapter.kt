package com.ing.offroader.ui.fragment.mydetail

interface OnBookmarkClickedInMyLikedListener{
    fun onBookmarkClicked()
}
//class MyBookmarkAdapter(val mItems: MutableList<MyDetailUiState>) : RecyclerView.Adapter<MyBookmarkAdapter.Holder>(), OnBookmarkClickedInMyLikedListener {
//
//    var onBookmarkClickedInMyLikedListener: List<OnBookmarkClickedInMyLikedListener>? = null
//    private var bookmarkedItem = LikedUtil.getLiked()
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
//        val binding = ItemSanListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return Holder(binding)
//    }
//
////    override fun onBindViewHolder(holder: Holder, position: Int) {
////        holder.bind(bookmarkedItem[position])
////    }
////
////    override fun getItemCount(): Int {
////        return bookmarkedItem.size
////    }
//
//    fun refreshRecyclerView() {
//        bookmarkedItem = LikedUtil.getLiked()
//        notifyDataSetChanged()
//    }
//
//    override fun onBookmarkClicked() {
//        Log.d("MyBookmarkAdapter", "onBookmarkClickedInMyLiked")
//        refreshRecyclerView()
//    }
//
//    inner class Holder(val binding: ItemSanListBinding) : RecyclerView.ViewHolder(binding.root) {
//        var mountainImage = binding.ivSanImage
//        var mountainName = binding.tvSanName
//
//        fun bind(item: SanDetailDTO) {
//            mountainName.text = item.mountain
//
//            Glide.with(mountainImage.context)
//                .load(mItems)
//                .into(mountainImage)
//        }
//    }
//}
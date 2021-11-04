package com.example.movieapp.presentation.movies_screen

import android.widget.AbsListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.util.Constants.DEFAULT_ITEM_INDEX

class OnScrollListener(val call: () -> Unit, val isLastPage: Boolean,val pageSize:Int) :
    RecyclerView.OnScrollListener() {

    var isScrolling = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isScrolling = true
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
        val visibleItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount

        val isAtLastItem = firstItemPosition + visibleItems >= totalItems
        val isNotAtStart = firstItemPosition >= DEFAULT_ITEM_INDEX
        val isTotalThanVisible = totalItems >= pageSize
        val paginate =
            isNotAtStart && isAtLastItem && isNotAtStart && isTotalThanVisible && isScrolling && !isLastPage
        if (paginate) {
            call()
            isScrolling = false
        }
    }
}
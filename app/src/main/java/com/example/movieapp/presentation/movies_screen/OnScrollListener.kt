package com.example.movieapp.presentation.movies_screen

import android.widget.AbsListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OnScrollListener(val call: () -> Unit, val isLastPage: Boolean) :
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
        val isNotAtStart = firstItemPosition >= 0
        val isTotalThanVisible = totalItems >= 20
        val paginate =
            isNotAtStart && isAtLastItem && isNotAtStart && isTotalThanVisible && isScrolling && !isLastPage
        if (paginate) {
            call()
            isScrolling = false
        }
    }
}
package com.vp.list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridPagingScrollListener(
    private val layoutManager: GridLayoutManager
) : RecyclerView.OnScrollListener() {
    var loadMoreItemsListener: LoadMoreItemsListener = EMPTY_LISTENER
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (shouldLoadNextPage()) {
            loadMoreItemsListener.loadMoreItems(getNextPageNumber())
        }
    }

    fun markLoading(isLoading: Boolean) {
        this.isLoading = isLoading;
    }

    fun markLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage;
    }

    private fun shouldLoadNextPage(): Boolean = isNotLoadingInProgress() &&
            userScrollsToNextPage() &&
            isNotFirstPage() &&
            hasNextPage()

    private fun userScrollsToNextPage(): Boolean =
        (layoutManager.childCount + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.itemCount

    private fun isNotFirstPage(): Boolean = layoutManager.findFirstVisibleItemPosition() >= 0 &&
            layoutManager.itemCount >= PAGE_SIZE

    private fun hasNextPage(): Boolean = !isLastPage

    private fun isNotLoadingInProgress(): Boolean = !isLoading

    private fun getNextPageNumber(): Int = layoutManager.itemCount / PAGE_SIZE + 1

    interface LoadMoreItemsListener {
        fun loadMoreItems(page: Int);
    }

    companion object {
        private const val PAGE_SIZE = 10
        private val EMPTY_LISTENER: LoadMoreItemsListener = object : LoadMoreItemsListener {
            override fun loadMoreItems(page: Int) {
                // empty listener
            }
        }
    }
}

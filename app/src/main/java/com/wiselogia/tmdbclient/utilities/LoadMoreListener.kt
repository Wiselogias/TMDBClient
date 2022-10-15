package com.wiselogia.tmdbclient.utilities

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoadMoreListener(private val onLoadMore: () -> Unit) : View.OnScrollChangeListener {
    var isLoading = false
    override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
        if (isLoading) return
        val recyclerView = p0 as RecyclerView
        val adapter = recyclerView.adapter ?: return
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        if (layoutManager.findLastVisibleItemPosition() > adapter.itemCount - 10) {
            Log.println(Log.INFO, "r", "scrolled")
            onLoadMore()
            isLoading = true
        }
    }

}
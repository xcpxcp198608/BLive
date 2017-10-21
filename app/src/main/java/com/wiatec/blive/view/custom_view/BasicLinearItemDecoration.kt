package com.wiatec.blive.view.custom_view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.px.common.utils.ConvertUnit

/**
 * Created by patrick on 21/10/2017.
 * create time : 9:34 AM
 */

class BasicLinearItemDecoration(dividerHeight: Int) : RecyclerView.ItemDecoration() {

    private var dividerHeight = 0

    init {
        this.dividerHeight = ConvertUnit.px2dp(dividerHeight)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.bottom = dividerHeight
        val layoutManager = parent.layoutManager as LinearLayoutManager
        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.top = dividerHeight
            }
            outRect.bottom = dividerHeight
        } else {
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.right = dividerHeight
            }
            outRect.top = dividerHeight
            outRect.left = dividerHeight
            outRect.bottom = dividerHeight
        }
    }

}

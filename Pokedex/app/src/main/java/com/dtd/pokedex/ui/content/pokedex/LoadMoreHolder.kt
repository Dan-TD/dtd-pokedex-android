package com.dtd.pokedex.ui.content.pokedex

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dtd.pokedex.R
import kotlinx.android.synthetic.main.viewholder_loadmore.view.*

class LoadMoreHolder(itemView: View, loadMoreListener: LoadMoreListener): RecyclerView.ViewHolder(itemView) {

    init {
        itemView.load_more_button.setOnClickListener { loadMoreListener.onLoadMorePressed() }
    }

    fun bind(loadingMoreState: LoadMoreState) {
        when (loadingMoreState) {
            LoadMoreState.LoadingMore -> {
                itemView.load_more_textview.text = itemView.context.getString(R.string.screens_content_pokedex_button_loading_more)
                itemView.load_more_spinner.visibility = View.VISIBLE
            }
            else -> {
                itemView.load_more_textview.text = itemView.context.getString(R.string.screens_content_pokedex_button_load_more)
                itemView.load_more_spinner.visibility = View.GONE
            }
        }
    }
}
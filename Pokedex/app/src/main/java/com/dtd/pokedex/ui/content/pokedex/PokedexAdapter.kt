package com.dtd.pokedex.ui.content.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dtd.pokedex.R

interface LoadMoreListener {
    fun onLoadMorePressed()
}

interface OnBindListener {
    fun onBind(pokemon: UIPokemon)
}

enum class LoadMoreState {
    CanLoadMore, LoadingMore, CannotLoadMore
}

class PokedexAdapter(private val loadMoreListener: LoadMoreListener,
                     private val onBindListener: OnBindListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class CardType(val value: Int) {
        PokemonCard(0),
        LoadMoreCard(1)
    }

    var pokemon: List<UIPokemon> = emptyList()
    var loadMoreState = LoadMoreState.CannotLoadMore

    override fun getItemCount(): Int {
        if (pokemon.isEmpty()) {
            return 0
        }

        return when (loadMoreState) {
            LoadMoreState.CannotLoadMore -> pokemon.size
            else -> pokemon.size + 1
        }
    }

    override fun getItemId(position: Int): Long {
        return when (getItemViewType(position)) {
            CardType.PokemonCard.value -> pokemon[position].pokemonResponse.name.hashCode().toLong()
            else -> LoadMoreHolder::class.java.name.hashCode().toLong()
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < pokemon.size) {
            return CardType.PokemonCard.value
        }
        return CardType.LoadMoreCard.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CardType.PokemonCard.value -> PokemonHolder(inflater.inflate(R.layout.viewholder_pokemon, parent, false))
            CardType.LoadMoreCard.value -> LoadMoreHolder(inflater.inflate(R.layout.viewholder_loadmore, parent, false), loadMoreListener)
            else -> throw IllegalStateException("Unrecognized view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PokemonHolder -> {
                holder.bind(pokemon[position])
                onBindListener.onBind(pokemon[position])
            }
            is LoadMoreHolder -> holder.bind(loadMoreState)
        }
    }
}
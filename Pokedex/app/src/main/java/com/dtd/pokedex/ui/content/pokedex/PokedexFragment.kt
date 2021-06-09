package com.dtd.pokedex.ui.content.pokedex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dtd.pokedex.R
import kotlinx.android.synthetic.main.fragment_pokedex.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class PokedexFragment : Fragment(), LoadMoreListener, OnBindListener {

    private object Flipper {
        const val Loading = 0
        const val Content = 1
        const val Error = 2
    }

    private val pokedexViewModel by viewModel<PokedexViewModel>()

    private val pokedexStateObserver = Observer<PokedexState> { state ->
        when(state) {
            is PokedexState.Content -> {
                pokedexAdapter.pokemon = state.pokemon
                if (state.canLoadMore) {
                    pokedexAdapter.loadMoreState = LoadMoreState.CanLoadMore
                } else {
                    pokedexAdapter.loadMoreState = LoadMoreState.CannotLoadMore
                }

            }
            is PokedexState.LoadingMore -> {
                pokedexAdapter.pokemon = state.currentPokemon
                pokedexAdapter.loadMoreState = LoadMoreState.LoadingMore
            }
            else -> {
                pokedexAdapter.pokemon = emptyList()
                pokedexAdapter.loadMoreState = LoadMoreState.CannotLoadMore
            }
        }
        pokedexAdapter.notifyDataSetChanged()

        when (state) {
            PokedexState.Loading -> {
                pokedex_view_flipper.displayedChild = Flipper.Loading
            }
            PokedexState.Error -> {
                pokedex_view_flipper.displayedChild = Flipper.Error
            }
            is PokedexState.Content -> {
                pokedex_view_flipper.displayedChild = Flipper.Content
            }
            is PokedexState.LoadingMore -> {
                pokedex_view_flipper.displayedChild = Flipper.Content
            }
        }
    }

    private val pokedexAdapter = PokedexAdapter(this, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokedex, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokedex_scrollview.setOnScrollChangeListener({ view, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY < oldScrollY) {
                if (Abs(scrollY) > Threshold) {
                    toolBar.visbility = View.VISIBLE
                }
            } else {
                if (Abs(scrollY) < Threshold) {
                    toolBar.visibility = View.GONE
                }
            }
        })

        pokedex_recyclerview.adapter = pokedexAdapter
        pokedex_recyclerview.layoutManager = LinearLayoutManager(context)

        pokedexViewModel.pokedexState.observe(viewLifecycleOwner, pokedexStateObserver)
        pokedexViewModel.viewCreated()
    }

    override fun onLoadMorePressed() {
        pokedexViewModel.loadMorePressed()
    }

    override fun onBind(pokemon: UIPokemon) {
        pokedexViewModel.onBind(pokemon)
        Log.i("PokedexFragment()", "onBind: " + pokemon.pokemonResponse.name)
    }
}

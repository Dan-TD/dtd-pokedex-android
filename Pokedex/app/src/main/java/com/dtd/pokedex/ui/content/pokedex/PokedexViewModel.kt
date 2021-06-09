package com.dtd.pokedex.ui.content.pokedex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dtd.pokedex.engine.RequestState
import com.dtd.pokedex.engine.models.PokemonDetailsResponse
import com.dtd.pokedex.engine.models.PokemonResponse
import com.dtd.pokedex.engine.pokedex.PokedexRepository
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

data class UIPokemon(val pokemonResponse: PokemonResponse, var detailsState: DetailsState) {

    sealed class DetailsState {
        object NotAvailable: DetailsState()
        object Loading: DetailsState()
        object Error: DetailsState()
        data class Content(val pokemonDetailsResponse: PokemonDetailsResponse): DetailsState()
    }
}

sealed class PokedexState {
    object Loading: PokedexState()
    object Error: PokedexState()
    data class Content(val page: Int, val pokemon: List<UIPokemon>, val canLoadMore: Boolean): PokedexState()
    data class LoadingMore(val page: Int, val currentPokemon: List<UIPokemon>): PokedexState()
}

class PokedexViewModel(private val pokedexRepository: PokedexRepository): ViewModel() {

    private val _pokedexState = MediatorLiveData<PokedexState>()
    val pokedexState: LiveData<PokedexState> get() = _pokedexState

    fun viewCreated() {
        viewModelScope.launch {
            pokedexRepository.getAllPokemon(1).collect { state ->
                _pokedexState.postValue(when(state) {
                    is RequestState.Loading -> PokedexState.Loading
                    is RequestState.Success -> {
                        val uiPokemon = state.data.results.map { UIPokemon(it, UIPokemon.DetailsState.NotAvailable) }
                        PokedexState.Content(1, uiPokemon, state.data.next != null)
                    }
                    is RequestState.Failure -> PokedexState.Error
                })

                if (state !is RequestState.Loading) {
                    cancel()
                }
            }
        }
    }

    fun loadMorePressed() {
        val currentState = pokedexState.value
        val currentPokemon: List<UIPokemon>
        val nextPage: Int

        when (currentState) {
            is PokedexState.Content -> {
                currentPokemon = currentState.pokemon
                nextPage = currentState.page + 1
            }
            else -> {
                currentPokemon = emptyList()
                nextPage = 1
            }
        }

        viewModelScope.launch {
            pokedexRepository.getAllPokemon(nextPage).collect { state ->
                _pokedexState.postValue(when(state) {
                    is RequestState.Loading -> PokedexState.LoadingMore(nextPage, currentPokemon)
                    is RequestState.Success -> {
                        val newPokemon = state.data.results.map { UIPokemon(it, UIPokemon.DetailsState.NotAvailable) }
                        PokedexState.Content(nextPage, currentPokemon + newPokemon, state.data.next != null)
                    }
                    is RequestState.Failure -> PokedexState.Error
                })

                if (state !is RequestState.Loading) {
                    cancel()
                }
            }
        }
    }

    fun onBind(pokemon: UIPokemon) {
        if (pokemon.detailsState !is UIPokemon.DetailsState.NotAvailable) {
            return
        }

        viewModelScope.launch {
            pokedexRepository.getPokemonDetails(pokemon.pokemonResponse.name).collect { state ->
                when (val pokedexState = pokedexState.value) {
                    is PokedexState.Content -> {
                        val pokemonList = pokedexState.pokemon
                        pokemonList.find { it.pokemonResponse.name == pokemon.pokemonResponse.name }?.detailsState = when (state) {
                            is RequestState.Loading -> UIPokemon.DetailsState.Loading
                            is RequestState.Success -> UIPokemon.DetailsState.Content(state.data)
                            is RequestState.Failure -> UIPokemon.DetailsState.Error
                        }
                        _pokedexState.postValue(PokedexState.Content(pokedexState.page, pokemonList, pokedexState.canLoadMore))
                    }
                }

                if (state !is RequestState.Loading) {
                    cancel()
                }
            }
        }
    }
}
package com.dtd.pokedex.engine.pokedex

import com.dtd.pokedex.engine.RequestState
import com.dtd.pokedex.engine.models.AllPokemonResponse
import com.dtd.pokedex.engine.models.PokemonDetailsResponse
import com.dtd.pokedex.engine.pokedexapi.PokedexApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PokedexRepository(private val pokedexApi: PokedexApi) {

    fun getAllPokemon(page: Int = 1) = flow<RequestState<AllPokemonResponse>> {
        emit(RequestState.Loading())

        val response = pokedexApi.getAllPokemon(50, (page - 1) * 50)

        val body = response.body()
        if (response.isSuccessful && body != null) {
            emit(RequestState.Success(body))
        } else {
            emit(RequestState.Failure(response.message()))
        }
    }.flowOn(Dispatchers.IO)

    fun getPokemonDetails(name: String) = flow<RequestState<PokemonDetailsResponse>> {
        emit(RequestState.Loading())

        val response = pokedexApi.getPokemonDetails(name)

        val body = response.body()
        if (response.isSuccessful && body != null) {
            emit(RequestState.Success(body))
        } else {
            emit(RequestState.Failure(response.message()))
        }
    }.flowOn(Dispatchers.IO)
}
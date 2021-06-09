package com.dtd.pokedex.engine.pokedexapi

import com.dtd.pokedex.engine.models.AllPokemonResponse
import com.dtd.pokedex.engine.models.PokemonDetailsResponse
import retrofit2.Response

interface PokedexApi {

    suspend fun getAllPokemon(limit: Int, offset: Int): Response<AllPokemonResponse>

    suspend fun getPokemonDetails(name: String): Response<PokemonDetailsResponse>
}
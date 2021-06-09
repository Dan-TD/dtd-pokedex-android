package com.dtd.pokedex.engine.pokedexapi

import com.dtd.pokedex.engine.models.AllPokemonResponse
import com.dtd.pokedex.engine.models.PokemonDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getAllPokemon(@Query("limit") limit: Int, @Query("offset") offset: Int): Response<AllPokemonResponse>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): Response<PokemonDetailsResponse>
}
package com.dtd.pokedex.engine.pokedexapi

import android.content.Context
import com.dtd.pokedex.engine.models.AllPokemonResponse
import com.dtd.pokedex.engine.models.PokemonDetailsResponse
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PokeApi(context: Context): PokedexApi {

    private val cache = Cache(context.cacheDir, (5 * 1024 * 1024).toLong())

    private val httpClient = OkHttpClient.Builder().cache(cache).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val client = retrofit.create(PokeApiService::class.java)

    override suspend fun getAllPokemon(limit: Int, offset: Int): Response<AllPokemonResponse> = client.getAllPokemon(limit, offset)

    override suspend fun getPokemonDetails(name: String): Response<PokemonDetailsResponse> = client.getPokemonDetails(name)
}
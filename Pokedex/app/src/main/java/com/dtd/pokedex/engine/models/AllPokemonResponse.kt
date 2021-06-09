package com.dtd.pokedex.engine.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllPokemonResponse (
    val count: Long,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResponse>
)

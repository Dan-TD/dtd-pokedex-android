package com.dtd.pokedex.engine.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse (
    val name: String,
    val url: String
)
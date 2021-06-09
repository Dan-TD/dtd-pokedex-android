package com.dtd.pokedex.engine.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailsResponse (
    val id: Long,
    val name: String,
    val baseExperience: Long?,
    val height: Long,
    val isDefault: Boolean?,
    val order: Long,
    val weight: Long,
    val forms: List<Species>,
    val species: Species,
    val sprites: Sprites,
    val types: List<Type>
)

@JsonClass(generateAdapter = true)
data class Species (
    val name: String,
    val url: String
)

@JsonClass(generateAdapter = true)
data class Sprites (
    @Json(name = "back_default")
    val backDefault: String,
    @Json(name = "back_shiny")
    val backShiny: String?,
    @Json(name = "front_default")
    val frontDefault: String,
    @Json(name = "front_shiny")
    val frontShiny: String?
)

@JsonClass(generateAdapter = true)
data class Type (
    val slot: Long,
    val type: Species
)
package com.dtd.pokedex.ui.content.pokedex

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dtd.pokedex.R
import kotlinx.android.synthetic.main.viewholder_pokemon.view.*

class PokemonHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(pokemon: UIPokemon) {
        itemView.pokemon_name_textview.text = pokemon.pokemonResponse.name.capitalize()

        when (val detailsState = pokemon.detailsState) {
            UIPokemon.DetailsState.Loading -> {
                itemView.pokemon_type_textview.text = ""
            }
            UIPokemon.DetailsState.Error -> {
                itemView.pokemon_type_textview.text = "Error"
            }
            is UIPokemon.DetailsState.Content -> {
                itemView.pokemon_type_textview.text = detailsState.pokemonDetailsResponse.types.first().type.name.capitalize()
                Glide.with(itemView.context)
                    .load(detailsState.pokemonDetailsResponse.sprites.frontDefault)
                    .fitCenter()
                    .placeholder(R.drawable.ic_pokeballs)
                    .into(itemView.pokemon_imageview)
            }
        }
    }
}
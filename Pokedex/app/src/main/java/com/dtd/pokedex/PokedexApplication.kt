package com.dtd.pokedex

import android.app.Application
import com.dtd.pokedex.engine.pokedex.PokedexRepository
import com.dtd.pokedex.engine.pokedexapi.PokeApi
import com.dtd.pokedex.engine.pokedexapi.PokedexApi
import com.dtd.pokedex.ui.content.pokedex.PokedexViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Suppress("unused") // Android Studio doesn't recognise application file in manifest
class PokedexApplication: Application() {

    private val pokedexModule = module {
        single<PokedexApi> { PokeApi(get()) }
        single { PokedexRepository(get()) }

        viewModel { PokedexViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PokedexApplication)
            modules(pokedexModule)
        }
    }
}
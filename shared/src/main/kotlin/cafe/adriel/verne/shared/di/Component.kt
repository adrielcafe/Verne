package cafe.adriel.verne.shared.di

import org.koin.core.module.Module

interface Component {

    fun getModules(): List<Module>
}

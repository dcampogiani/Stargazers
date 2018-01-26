package com.danielecampogiani.demo.inject

import com.danielecampogiani.demo.ui.StargazerFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelBuilder::class,
    StargazersModule::class,
    NetworkModule::class])
interface AppComponent {

    fun inject(stargazerFragment: StargazerFragment)
}
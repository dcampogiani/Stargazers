package com.danielecampogiani.demo.inject

import androidx.lifecycle.ViewModel
import com.danielecampogiani.demo.ui.StargazersViewModel
import com.danielecampogiani.demo.usecase.LoadFirstPageUseCase
import com.danielecampogiani.demo.usecase.LoadFirstPageUseCaseImpl
import com.danielecampogiani.demo.usecase.LoadPageUseCase
import com.danielecampogiani.demo.usecase.LoadPageUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class StargazersModule {

    @Binds
    @IntoMap
    @ViewModelKey(StargazersViewModel::class)
    internal abstract fun bindStargazersViewModel(viewModel: StargazersViewModel): ViewModel

    @Binds
    internal abstract fun bindLoadFirstPageUseCase(loadFirstPageUseCaseImpl: LoadFirstPageUseCaseImpl): LoadFirstPageUseCase

    @Binds
    internal abstract fun bindLoadPageUseCase(loadPageUseCaseImpl: LoadPageUseCaseImpl): LoadPageUseCase
}
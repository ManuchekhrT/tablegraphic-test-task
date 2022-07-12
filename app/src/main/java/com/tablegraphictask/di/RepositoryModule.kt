package com.tablegraphictask.di

import com.tablegraphictask.ui.points.model.PointsApi
import com.tablegraphictask.ui.points.model.PointsInteractor
import com.tablegraphictask.ui.points.model.PointsInteractorImpl
import com.tablegraphictask.ui.points.presenter.PointsPresenter
import com.tablegraphictask.ui.points.presenter.PointsPresenterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import retrofit2.Retrofit

@Module
@InstallIn(FragmentComponent::class)
interface RepositoryModule {

    companion object {
        @Provides
        @FragmentScoped
        fun providePointsApi(
            retrofit: Retrofit
        ): PointsApi = retrofit.create(PointsApi::class.java)
    }

    @Binds
    @FragmentScoped
    fun bindPointsInteractor(pointsInteractor: PointsInteractorImpl): PointsInteractor

    @Binds
    @FragmentScoped
    fun bindPointsPresenter(pointsPresenter: PointsPresenterImpl): PointsPresenter

}
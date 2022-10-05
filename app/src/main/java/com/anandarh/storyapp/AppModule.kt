package com.anandarh.storyapp
import android.content.Context
import com.anandarh.storyapp.repositories.AuthRepository
import com.anandarh.storyapp.services.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return ApiService()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(apiService: ApiService) = AuthRepository(apiService)

}
package com.tx.edusphere.di

import com.tx.edusphere.data.repository.AuthRepositoryImpl
import com.tx.edusphere.data.repository.StudentRepositoryImpl
import com.tx.edusphere.domain.repository.AuthRepository
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindStudentRepository(
        studentRepositoryImpl: StudentRepositoryImpl
    ): StudentRepository
}

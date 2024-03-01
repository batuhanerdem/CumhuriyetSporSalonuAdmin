package com.example.cumhuriyetsporsalonuadmin.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class Module {

    @Singleton
    @Provides
    fun provideDB() = Firebase.firestore
}
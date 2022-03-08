package com.example.apollographqlplayground.apollo

import android.os.Looper
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class SeriesApi {

    fun getApolloClient() : ApolloClient {
        check(Looper.myLooper() == Looper.getMainLooper()){
            "Only the main thread can access apollo client !"
        }
        val okHttpClient = OkHttpClient.Builder().build()
        return ApolloClient.builder()
            .serverUrl("https://rickandmortyapi.com/graphql")
            .okHttpClient(okHttpClient)
            .build()
    }
}
package com.example.apollographqlplayground.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.example.apollographqlplayground.CharacterDetailsQuery
import com.example.apollographqlplayground.Response
import com.example.apollographqlplayground.SeriesCharactersQuery
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val client: ApolloClient
) : AppRepository {

    /**
     * A function that return all the characters available on our graphql
     * @return a Response which can be of the type Success(with data) or Error(with message)
     */
    override suspend fun getAllCharacters(): Response<SeriesCharactersQuery.Data> {
        return try{
            /** Send our request & wait for the response.
             * It's possible because it has a built-in support for Coroutines */
            val result = client.query(SeriesCharactersQuery()).await()
            result.data?.let {
                Response.Success(data = it)
            } ?: Response.Error(message = "Data is null for some reason !")
        } catch(t: Throwable) {
            /** await() throws ApolloException when it fails - e.g: No connection.
             * We should catch it so that our app doesn't crash
             */
            Response.Error(message = "Network connection error !")
        }
    }

    /** A function that return the character's details */
    override suspend fun getCharacterDetails(characterId: String): Response<CharacterDetailsQuery.Data> {
        return try{
            /** Send our request & wait for the response.
             * It's possible because it has a built-in support for Coroutines */
            val result = client.query(CharacterDetailsQuery(characterId = characterId)).await()
            result.data?.let {
                Response.Success(data = it)
            } ?: Response.Error(message = "Data is null for some reason !")
        } catch(t: Throwable) {
            /** await() throws ApolloException when it fails - e.g: No connection.
             * We should catch it so that our app doesn't crash
             */
            Response.Error(message = "Network connection error !")
        }
    }

}
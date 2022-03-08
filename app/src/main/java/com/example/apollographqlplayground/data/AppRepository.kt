package com.example.apollographqlplayground.data

import com.example.apollographqlplayground.CharacterDetailsQuery
import com.example.apollographqlplayground.Response
import com.example.apollographqlplayground.SeriesCharactersQuery

interface AppRepository {

    /** A function that return all the characters available on our graphql */
    suspend fun getAllCharacters() : Response<SeriesCharactersQuery.Data>

    /** A function that return the character's details */
    suspend fun getCharacterDetails(characterId: String) : Response<CharacterDetailsQuery.Data>
}
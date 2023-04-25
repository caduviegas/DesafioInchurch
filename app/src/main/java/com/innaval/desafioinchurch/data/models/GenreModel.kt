package com.innaval.desafioinchurch.data.models

import com.innaval.desafioinchurch.domain.entities.Genre

data class GenreModel(
    val id: Int,
    val name: String?
) {
    fun toEntity(): Genre {
        return Genre(
            this.id,
            this.name ?: ""
        )
    }
}
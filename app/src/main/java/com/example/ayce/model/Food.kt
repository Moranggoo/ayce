package com.example.ayce.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: String,
    val name: String,
    val color: Long,
    val icon: String,
    val count: Int = 0
)
package com.example.ayce.model

import kotlinx.serialization.Serializable

@Serializable
data class Participant(
    val id: String,
    val name: String,
    val count: Int = 0
)
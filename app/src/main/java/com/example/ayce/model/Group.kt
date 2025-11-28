package com.example.ayce.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Group(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val participants: List<Participant> = emptyList()
)
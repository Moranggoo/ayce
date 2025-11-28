package com.example.ayce.data

import com.example.ayce.model.Participant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GroupRepository(
    private val store: GroupDataStore
) {

    val participants: Flow<List<Participant>> =
        store.flow.map { it.participants }

    suspend fun addParticipant(name: String) {
        val current = store.flow.first().participants
        val new = current + Participant(
            id = System.currentTimeMillis().toString(),
            name = name
        )
        store.save(new)
    }

    suspend fun increment(id: String) {
        val current = store.flow.first().participants
        val new = current.map {
            if (it.id == id) it.copy(count = it.count + 1) else it
        }
        store.save(new)
    }

    suspend fun resetParticipant(id: String) {
        val current = store.flow.first().participants
        val new = current.map {
            if (it.id == id) it.copy(count = 0) else it
        }
        store.save(new)
    }

    // <-- CORREÇÃO AQUI: remove por participantId (sem groupId)
    suspend fun removeParticipant(participantId: String) {
        val current = store.flow.first().participants
        val new = current.filterNot { it.id == participantId }
        store.save(new)
    }
}
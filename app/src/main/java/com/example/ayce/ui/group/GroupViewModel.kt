package com.example.ayce.ui.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayce.data.GroupRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupViewModel(
    private val repo: GroupRepository,
    val foodId: String   // comida escolhida
) : ViewModel() {

    val participants = repo.participants
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addParticipant(name: String) {
        viewModelScope.launch { repo.addParticipant(name) }
    }

    fun increment(id: String) {
        viewModelScope.launch { repo.increment(id) }
    }

    fun reset(id: String) {
        viewModelScope.launch { repo.resetParticipant(id) }
    }

    fun removeParticipant(participantId: String) {
        viewModelScope.launch {
            repo.removeParticipant(participantId)
        }
    }

}
package com.example.ayce.ui.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ayce.data.GroupRepository

class GroupViewModelFactory(
    private val repo: GroupRepository,
    private val foodId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupViewModel(repo, foodId) as T
    }
}
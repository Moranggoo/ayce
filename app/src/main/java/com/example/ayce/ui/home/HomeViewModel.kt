package com.example.ayce.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayce.data.FoodRepository
import com.example.ayce.model.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: FoodRepository
) : ViewModel() {

    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods.asStateFlow()

    init {
        observeFoods()
    }

    private fun observeFoods() {
        viewModelScope.launch {
            repository.foods.collectLatest { list ->
                _foods.value = list
            }
        }
    }

    fun addFood(food: Food) {
        viewModelScope.launch {
            repository.addFood(food)
        }
    }

    fun incrementFood(id: String) {
        viewModelScope.launch {
            repository.incrementCounter(id)
        }
    }

    fun removeFood(id: String) {
        viewModelScope.launch {
            repository.removeFood(id)
        }
    }

    fun resetFood(id: String) {
        viewModelScope.launch {
            repository.resetCounter(id)
        }
    }
}
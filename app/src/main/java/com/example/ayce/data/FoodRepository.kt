package com.example.ayce.data

import com.example.ayce.model.Food
import kotlinx.coroutines.flow.map

class FoodRepository(
    private val foodDataStore: FoodDataStore
) {

    val foods = foodDataStore.foodsFlow.map { it.foods }

    suspend fun addFood(food: Food) {
        foodDataStore.update { current ->
            current + food
        }
    }

    suspend fun removeFood(id: String) {
        foodDataStore.update { current ->
            current.filterNot { it.id == id }
        }
    }

    suspend fun resetCounter(id: String) {
        foodDataStore.update { current ->
            current.map { if (it.id == id) it.copy(count = 0) else it }
        }
    }

    suspend fun incrementCounter(id: String) {
        foodDataStore.update { current ->
            current.map { if (it.id == id) it.copy(count = it.count + 1) else it }
        }
    }
}
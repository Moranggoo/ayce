package com.example.ayce.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.ayce.model.Food
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

// 1. Wrapper do DataStore
@Serializable
data class FoodListWrapper(
    val foods: List<Food> = emptyList()
)

// 2. Serializer obrigatório
object FoodListSerializer : Serializer<FoodListWrapper> {
    override val defaultValue: FoodListWrapper = FoodListWrapper(
        foods = listOf(
            Food(id = "Pizza" ,name = "Pizza", color = 0xFFFF0000, icon = "🍕", count = 0),
            Food(id = "Sushi" ,name = "Sushi", color = 0xFF00AAFF, icon = "🍣", count = 0),
            Food(id = "Hambúrguer" ,name = "Hambúrguer", color = 0xFFFFAA00, icon = "🍔", count = 0)
        )
    )


    override suspend fun readFrom(input: InputStream): FoodListWrapper {
        return try {
            Json.decodeFromString(
                input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: FoodListWrapper, output: OutputStream) {
        output.write(
            Json.encodeToString(t).encodeToByteArray()
        )
    }
}

// 3. DataStore real
private val Context.foodDataStore: DataStore<FoodListWrapper> by dataStore(
    fileName = "foods.json",
    serializer = FoodListSerializer
)

// 4. CLASSE CERTA — e somente ela
class FoodDataStore(private val context: Context) {

    val foodsFlow = context.foodDataStore.data

    // sobrescreve a lista inteira
    suspend fun saveFoods(list: List<Food>) {
        context.foodDataStore.updateData { current ->
            current.copy(foods = list)
        }
    }

    // FUNÇÃO IMPORTANTE: atualiza só parte da lista
    suspend fun update(transform: (List<Food>) -> List<Food>) {
        context.foodDataStore.updateData { current ->
            current.copy(
                foods = transform(current.foods)
            )
        }
    }
}
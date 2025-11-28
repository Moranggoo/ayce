package com.example.ayce.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.ayce.model.Participant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class GroupListWrapper(
    val participants: List<Participant> = emptyList()
)

object GroupListSerializer : Serializer<GroupListWrapper> {

    override val defaultValue: GroupListWrapper = GroupListWrapper()

    override suspend fun readFrom(input: InputStream): GroupListWrapper {
        return try {
            val text = input.readBytes().decodeToString()
            Json.decodeFromString(GroupListWrapper.serializer(), text)
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: GroupListWrapper, output: OutputStream) {
        val text = Json.encodeToString(GroupListWrapper.serializer(), t)
        output.write(text.encodeToByteArray())
    }
}

private val Context.groupDataStore: DataStore<GroupListWrapper> by dataStore(
    fileName = "groups.json",
    serializer = GroupListSerializer
)

class GroupDataStore(private val context: Context) {

    val flow = context.groupDataStore.data

    suspend fun save(list: List<Participant>) {
        context.groupDataStore.updateData { current ->
            current.copy(participants = list)
        }
    }
}
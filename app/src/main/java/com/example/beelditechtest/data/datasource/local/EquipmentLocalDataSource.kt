package com.example.beelditechtest.data.datasource.local
import android.content.Context
import com.example.beelditechtest.data.model.EquipmentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException

class EquipmentLocalDataSource(
    private val context: Context,
) {
    fun getEquipments(): Flow<List<EquipmentEntity>> =
        flow {
            val entities =
                withContext(Dispatchers.IO) {
                    try {
                        val jsonString =
                            context.assets
                                .open("equipments.json")
                                .bufferedReader()
                                .use { it.readText() }

                        val jsonArray = JSONArray(jsonString)
                        buildList {
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val equipmentEntity =
                                    EquipmentEntity(
                                        id = jsonObject.getString("id"),
                                        name = jsonObject.getString("name"),
                                        brand = jsonObject.getString("brand"),
                                        model = jsonObject.getString("model"),
                                        serialNumber = jsonObject.getString("serialNumber"),
                                        location = jsonObject.getString("location"),
                                        type = jsonObject.getInt("type"),
                                    )
                                add(equipmentEntity)
                            }
                        }
                    } catch (e: IOException) {
                        emptyList()
                    } catch (e: Exception) {
                        emptyList()
                    }
                }
            emit(entities)
        }
}

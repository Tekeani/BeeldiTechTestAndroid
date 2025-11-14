package com.example.beelditechtest

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException

class EquipmentDataSource(private val context: Context) {

    suspend fun getEquipments(): List<EquipmentEntity> = withContext(Dispatchers.Main) {
        try {
            val jsonString = context.assets.open("equipments.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val equipmentEntities = mutableListOf<EquipmentEntity>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val equipmentEntity = EquipmentEntity(
                    id = jsonObject.getString("id"),
                    name = jsonObject.getString("name"),
                    brand = jsonObject.getString("brand"),
                    model = jsonObject.getString("model"),
                    serialNumber = jsonObject.getString("serialNumber"),
                    location = jsonObject.getString("location")
                )
                equipmentEntities.add(equipmentEntity)
            }

            equipmentEntities
        } catch (e: IOException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

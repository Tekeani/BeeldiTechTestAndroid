package com.example.beelditechtest.domain.model

data class Equipment(
    val id: String,
    val name: String,
    val brand: String,
    val model: String,
    val serialNumber: String,
    val location: String,
    val type: Int,
)

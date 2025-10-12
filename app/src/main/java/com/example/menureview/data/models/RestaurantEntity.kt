package com.example.menureview.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "restaurants",
    foreignKeys = [
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    //indices = [Index(value = ["tagId"])]
    indices = [Index("tagId")]
)
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val ubication: String,
    val description: String,
    val phone: String,
    val imageUrl: String?,
    val score: Float?,
    val tagId: Int // Clave foránea
)
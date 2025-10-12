package com.example.menureview.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RestaurantEntity::class,
            parentColumns = ["id"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    //indices = [Index(value = ["userId"]), Index(value = ["restaurantId"])]
    indices = [Index("userId"), Index("restaurantId")]
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val score: Float,
    val date: String,
    val userId: Int, // Clave foránea
    val restaurantId: Int // Clave foránea
)
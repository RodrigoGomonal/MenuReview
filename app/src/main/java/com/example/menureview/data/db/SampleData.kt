package com.example.menureview.data.db

import com.example.menureview.data.models.*

val sampleTags = listOf(
    TagEntity(id = 1, name = "Italiana"),
    TagEntity(id = 2, name = "Japonesa"),
    TagEntity(id = 3, name = "Rápida"),
    TagEntity(id = 4, name = "Cafetería"),
    TagEntity(id = 5, name = "Vegetariana")
)

val sampleRestaurants = listOf(
    RestaurantEntity(
        id = 1,
        name = "La Trattoria",
        ubication = "Av. Italia 123",
        description = "Comida italiana tradicional",
        phone = "+56 9 1234 5678",
        imageUrl = "https://picsum.photos/200/100",
        score = 4.6f,
        tagId = 1
    ),
    RestaurantEntity(
        id = 2,
        name = "SushiZen",
        ubication = "Mall Costanera",
        description = "Sushi fresco y ramen artesanal",
        phone = "+56 9 9876 5432",
        imageUrl = "https://picsum.photos/200/101",
        score = 4.8f,
        tagId = 2
    ),
    RestaurantEntity(
        id = 3,
        name = "BurgerHouse",
        ubication = "Providencia 456",
        description = "Hamburguesas gourmet con papas caseras",
        phone = "+56 2 3333 2222",
        imageUrl = null,
        score = 4.3f,
        tagId = 3
    ),
    RestaurantEntity(
        id = 4,
        name = "GreenBowl",
        ubication = "Ñuñoa Plaza",
        description = "Restaurante vegano con productos locales",
        phone = "+56 2 1111 4444",
        imageUrl = "https://picsum.photos/200/103",
        score = 4.5f,
        tagId = 5
    ),
    RestaurantEntity(
        id = 5,
        name = "Café Aroma",
        ubication = "Lastarria 99",
        description = "Cafetería artesanal y pastelería casera",
        phone = "+56 9 4444 8888",
        imageUrl = "https://picsum.photos/200/104",
        score = 4.7f,
        tagId = 4
    )
)

val sampleUsers = listOf(
    UserEntity(1, "Rodrigo", "rodrigo@gmail.com", "1234", null),
    UserEntity(2, "María", "maria@gmail.com", "abcd", null),
    UserEntity(3, "Luis", "luis@gmail.com", "pass", null),
    UserEntity(4, "Camila", "camila@gmail.com", "admin", null),
    UserEntity(5, "Pedro", "pedro@gmail.com", "qwerty", null)
)

val sampleComments = listOf(
    CommentEntity(1, "Excelente comida!", 5f, "2025-11-10", 1, 1),
    CommentEntity(2, "Muy buena atención", 4.5f, "2025-11-09", 2, 2),
    CommentEntity(3, "Porciones pequeñas", 3.8f, "2025-11-08", 3, 3),
    CommentEntity(4, "Ambiente tranquilo", 4.2f, "2025-11-07", 4, 4),
    CommentEntity(5, "El café estaba increíble", 5f, "2025-11-06", 5, 5)
)
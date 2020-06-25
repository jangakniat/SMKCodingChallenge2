package com.pedolu.smkcodingchallenge2.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserModel(
    var name: String,
    var gender: String,
    var age: String,
    var telp: String,
    var address: String,
    @PrimaryKey var key: String
) {
    constructor() : this("", "", "", "", "", "")
}

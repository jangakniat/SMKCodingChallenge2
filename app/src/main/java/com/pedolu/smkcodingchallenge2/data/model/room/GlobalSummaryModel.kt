package com.pedolu.smkcodingchallenge2.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "global_summary")
data class GlobalSummaryModel(
    var confirmed: String,
    var recovered: String,
    var deaths: String,
    var last_update: String,
    @PrimaryKey var key: String
) {
    constructor() : this("", "", "", "", "")
}
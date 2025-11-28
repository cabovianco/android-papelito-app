package com.cabovianco.papelito.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cabovianco.papelito.domain.model.Tag

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String
)

fun TagEntity.toModel() = Tag(id, name)

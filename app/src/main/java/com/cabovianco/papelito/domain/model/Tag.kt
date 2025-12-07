package com.cabovianco.papelito.domain.model

import com.cabovianco.papelito.data.local.entity.TagEntity

data class Tag(
    val id: Long,
    val name: String
)

fun Tag.toEntity() = TagEntity(id, name)

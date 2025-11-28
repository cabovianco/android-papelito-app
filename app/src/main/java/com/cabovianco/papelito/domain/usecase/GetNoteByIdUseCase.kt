package com.cabovianco.papelito.domain.usecase

import com.cabovianco.papelito.domain.repository.NoteRepository
import jakarta.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(id: Long) = noteRepository.getById(id)
}

package com.cabovianco.papelito.domain.usecase

import com.cabovianco.papelito.domain.model.Result
import com.cabovianco.papelito.domain.model.note.Note
import com.cabovianco.papelito.domain.repository.NoteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(id: Long): Flow<Result<Note>> = noteRepository.observeById(id)
}

package com.cabovianco.papelito.domain.usecase

import com.cabovianco.papelito.domain.model.Note
import com.cabovianco.papelito.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = noteRepository.delete(note)
}

package com.cabovianco.papelito.domain.usecase

import com.cabovianco.papelito.domain.repository.NoteRepository
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke() = noteRepository.getAll()
}

package com.cabovianco.papelito.domain.usecase

import com.cabovianco.papelito.domain.model.Result
import com.cabovianco.papelito.domain.model.note.Note
import com.cabovianco.papelito.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAllNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<Result<List<Note>>> = noteRepository.observeAll()
}

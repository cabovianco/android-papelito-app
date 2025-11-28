package com.cabovianco.papelito.presentation.navigation

sealed class Screen(
    val route: String
) {
    data object MainScreen : Screen("main")
    data object AddNoteScreen : Screen("add_note")
    data object EditNoteScreen : Screen("edit_note/{id}") {
        fun navTo(id: Long) = "edit_note/$id"
    }
}

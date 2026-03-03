package com.cabovianco.papelito.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cabovianco.papelito.presentation.ui.screen.FormScreen
import com.cabovianco.papelito.presentation.ui.screen.MainScreen
import com.cabovianco.papelito.presentation.viewmodel.AddNoteViewModel
import com.cabovianco.papelito.presentation.viewmodel.EditNoteViewModel
import com.cabovianco.papelito.presentation.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(
            route = Screen.MainScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            val viewModel = hiltViewModel<MainViewModel>()

            MainScreen(
                viewModel,
                onAddButtonClick = { navController.navigate(Screen.AddNoteScreen.route) },
                onEditNoteClick = { navController.navigate(Screen.EditNoteScreen.navTo(it)) },
                onDeleteNoteClick = { viewModel.deleteNote(it) }
            )
        }

        composable(
            route = Screen.AddNoteScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            val viewModel = hiltViewModel<AddNoteViewModel>()

            FormScreen(
                viewModel,
                onSaveButtonClick = { viewModel.addNote() },
                onCancelButtonClick = { navController.navigateUp() },
                onSaveEvent = { navController.navigateUp() }
            )
        }

        composable(
            Screen.EditNoteScreen.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: -1

            val viewModel = hiltViewModel<EditNoteViewModel>()

            LaunchedEffect(Unit) { viewModel.loadNoteById(id) }

            FormScreen(
                viewModel,
                onSaveButtonClick = { viewModel.editNote() },
                onCancelButtonClick = { navController.navigateUp() },
                onSaveEvent = { navController.navigateUp() }
            )
        }
    }
}

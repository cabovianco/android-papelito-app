package com.cabovianco.papelito.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cabovianco.papelito.presentation.ui.screen.AddNoteScreen
import com.cabovianco.papelito.presentation.ui.screen.EditNoteScreen
import com.cabovianco.papelito.presentation.ui.screen.MainScreen
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme
import com.cabovianco.papelito.presentation.viewmodel.AddNoteViewModel
import com.cabovianco.papelito.presentation.viewmodel.EditNoteViewModel
import com.cabovianco.papelito.presentation.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val colors = LocalColorScheme.current
    val screenModifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.navigationBars.add(WindowInsets.statusBars))

    NavHost(
        modifier = modifier.background(color = colors.background),
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(route = Screen.MainScreen.route) {
            val viewmodel = hiltViewModel<MainViewModel>()

            MainScreen(
                viewmodel,
                navController,
                modifier = screenModifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
        }

        composable(route = Screen.AddNoteScreen.route) {
            val viewmodel = hiltViewModel<AddNoteViewModel>()

            AddNoteScreen(
                viewmodel,
                navController,
                modifier = screenModifier.padding(16.dp)
            )
        }

        composable(
            Screen.EditNoteScreen.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: -1

            val viewmodel: EditNoteViewModel = hiltViewModel()

            LaunchedEffect(Unit) { viewmodel.editNoteById(id) }

            EditNoteScreen(
                viewmodel,
                navController,
                modifier = screenModifier.padding(16.dp)
            )
        }
    }
}

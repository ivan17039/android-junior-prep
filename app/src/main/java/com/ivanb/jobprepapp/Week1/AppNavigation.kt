package com.ivanb.jobprepapp.Week1

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay

@Composable
fun AppNavigation(onCloseApp: () -> Unit = {}) {
    // stvara se upravitelj navigacije, pamti na kojem smo ekranu
    val navController = rememberNavController()

    // Scaffold uveden kako dio ekrana ne bi isao preko sata i datuma
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "book_list",
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- EKRAN 1: Popis knjiga (Glavna ruta) ---
            composable("book_list") {
                BookListScreen(
                    onBookClick = { book ->
                        navController.navigate("book_detail/${book.id}/list")
                    },
                    onAboutClick = {
                        navController.navigate("about")
                    },
                    onBack = {
                        onCloseApp()
                    }
                )
            }
            // --- EKRAN 2: Detalji pojedine knjige ---
            composable(
                route = "book_detail/{bookId}/{fromScreen}",
                arguments = listOf(
                    navArgument("bookId") { type = NavType.StringType },
                    navArgument("fromScreen") { type = NavType.StringType }
                )
            ) {
                BookDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // --- EKRAN 3: O aplikaciji ---
            composable(route="about"){
                AboutScreen(
                    onBack = {navController.popBackStack()}
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}
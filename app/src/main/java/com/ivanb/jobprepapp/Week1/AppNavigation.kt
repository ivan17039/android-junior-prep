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
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
@Composable
fun AppNavigation(onCloseApp: () -> Unit = {}) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "book_list",
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- EKRAN 1: Popis knjiga ---
            composable("book_list") {
                BookListScreen(
                    onBookClick = { book ->
                        // 1. Enkodiramo book.id da sve kose crte premetne u %2F
                        val encodedId = URLEncoder.encode(book.id, StandardCharsets.UTF_8.name())
                        navController.navigate("book_detail/$encodedId/list")
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
            ) { backStackEntry ->
                // 2. Izvlačimo enkodirani ID i dekodiramo ga natrag u originalni oblik (/works/...)
                val rawId = backStackEntry.arguments?.getString("bookId") ?: ""
                val decodedId = URLDecoder.decode(rawId, StandardCharsets.UTF_8.name())

                BookDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // --- EKRAN 3: O aplikaciji ---
            composable(route = "about") {
                AboutScreen(
                    onBack = { navController.popBackStack() }
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
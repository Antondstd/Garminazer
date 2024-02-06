package net.garminazer.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeRoute(
) {
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory())

    HomeScreen(
    )
}
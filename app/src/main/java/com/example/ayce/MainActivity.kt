package com.example.ayce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ayce.data.FoodDataStore
import com.example.ayce.data.FoodRepository
import com.example.ayce.ui.counter.FoodCounterScreen
import com.example.ayce.ui.home.HomeRoute
import com.example.ayce.ui.home.HomeViewModel
import com.example.ayce.ui.home.HomeViewModelFactory
import com.example.ayce.data.GroupDataStore
import com.example.ayce.data.GroupRepository
import com.example.ayce.ui.group.GroupViewModel
import com.example.ayce.ui.group.GroupViewModelFactory
import com.example.ayce.ui.group.GroupScreen
import com.example.ayce.ui.theme.AyceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AyceTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // DI manual
    val context = LocalContext.current
    val repository = FoodRepository(FoodDataStore(context))
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeRoute(
                viewModel = viewModel,
                onNavigateToCounter = { foodId ->
                    navController.navigate("counter/$foodId")
                },
                onNavigateToGroup = { foodId ->
                    navController.navigate("group/$foodId")
                }
            )
        }

        composable("group/{foodId}") { backStackEntry ->
            val foodId = backStackEntry.arguments?.getString("foodId") ?: ""

            // DI para o GroupViewModel
            val groupContext = LocalContext.current
            val groupRepo = GroupRepository(GroupDataStore(groupContext))
            val groupVm: GroupViewModel = viewModel(
                factory = GroupViewModelFactory(groupRepo, foodId)
            )

            // --- LÓGICA ADICIONADA ---
            // Buscar o nome do alimento para a TopAppBar do novo design
            val foods = viewModel.foods.collectAsState()
            val food = foods.value.find { it.id == foodId }

            GroupScreen(
                viewModel = groupVm,
                foodName = food?.name ?: "Grupo", // Passa o nome do alimento
                onNavigateBack = { navController.popBackStack() } // <-- RESPOSTA
            )
        }

        composable("counter/{foodId}") { backStackEntry ->
            val foodId = backStackEntry.arguments?.getString("foodId") ?: ""

            val foods = viewModel.foods.collectAsState()
            val food = foods.value.find { it.id == foodId }

            if (food != null) {
                FoodCounterScreen(
                    foodName = food.name,
                    count = food.count,
                    icon = food.icon,
                    color = food.color,
                    onIncrement = { viewModel.incrementFood(food.id) },
                    onReset = { viewModel.resetFood(food.id) },
                    onNavigateBack = { navController.popBackStack() } // <-- RESPOSTA
                )
            }
        }
    }
}
package com.example.ayce.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ayce.model.Food
import com.example.ayce.ui.home.components.AddFoodDialog
import java.util.UUID

// ===============================
//  HOME ROUTE
// ===============================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    onNavigateToCounter: (String) -> Unit,
    onNavigateToGroup: (String) -> Unit
) {
    val foods by viewModel.foods.collectAsState()

    HomeScreen(
        foods = foods,
        onAddFood = { name, color, icon ->
            viewModel.addFood(
                Food(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    color = color,
                    icon = icon,
                    count = 0
                )
            )
        },
        onNavigateToCounter = onNavigateToCounter,
        onNavigateToGroup = onNavigateToGroup
    )
}
// ===============================
//  HOME SCREEN — NOVO DESIGN
// ===============================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    foods: List<Food>,
    onAddFood: (String, Long, String) -> Unit,
    onNavigateToCounter: (String) -> Unit,
    onNavigateToGroup: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val showSheet = selectedFood != null

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // ===============================
            //  HEADER BONITO
            // ===============================
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 32.dp)
            ) {
                Text(
                    "AYCE",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 42.sp
                    )
                )
                Text(
                    "Seu contador de comida 🍽️",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // ===============================
            //  LISTA DE ALIMENTOS
            // ===============================
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(foods) { food ->
                    FoodCardModern(
                        food = food,
                        onClick = { selectedFood = food }
                    )
                }
            }
        }
    }

    // ===============================
    //  BOTTOM SHEET (MODERNO)
    // ===============================
    if (showSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { selectedFood = null },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(
                    text = "Escolha o modo",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))
                Divider()

                ListItem(
                    headlineContent = { Text("Contador Individual") },
                    supportingContent = { Text("Perfeito para uso rápido") },
                    leadingContent = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.clickable {
                        selectedFood?.id?.let { onNavigateToCounter(it) }
                        selectedFood = null
                    }
                )

                ListItem(
                    headlineContent = { Text("Contador em Grupo") },
                    supportingContent = { Text("Para dividir com amigos") },
                    leadingContent = { Icon(Icons.Default.Face, null) },
                    modifier = Modifier.clickable {
                        selectedFood?.id?.let { onNavigateToGroup(it) }
                        selectedFood = null
                    }
                )
            }
        }
    }

    // ===============================
    //  DIALOG — ADICIONAR ALIMENTO
    // ===============================
    if (showAddDialog) {
        AddFoodDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, emoji ->
                val color = (0xFFB0E0E6..0xFFFFFACD).random()
                onAddFood(name, color, emoji)
                showAddDialog = false
            },
        )
    }
}

// ===============================
//  CARD MODERNIZADO
// ===============================
@Composable
fun FoodCardModern(
    food: Food,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            Modifier
                .padding(18.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ICON BOX
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(food.color).copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(food.icon, fontSize = 32.sp)
            }

            Spacer(Modifier.width(16.dp))

            Text(
                text = food.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}
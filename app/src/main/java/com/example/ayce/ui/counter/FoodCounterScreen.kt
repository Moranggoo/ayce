package com.example.ayce.ui.counter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Nota de Design:
// Esta é a tela de ação principal. Ela precisa ser 'engajante'.
// 1. A cor do alimento (food.color) domina o fundo para criar imersão.
// 2. O contador é o elemento central e gigante.
// 3. A principal ação (+1) é um botão enorme, quase a tela inteira,
//    facilitando o clique rápido e repetido (Lei de Fitts).
// 4. A ação secundária ("Resetar") foi movida para a TopAppBar,
//    evitando cliques acidentais e limpando a UI.
// 5. Adicionada uma leve animação de 'scale' ao pressionar o botão.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCounterScreen(
    foodName: String,
    count: Int,
    icon: String,
    color: Long,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    onNavigateBack: () -> Unit // Adicionado para voltar
) {
    val containerColor = Color(color)
    // Decide se o conteúdo (texto/ícones) deve ser claro ou escuro
    val contentColor = if (containerColor.luminance() > 0.5) Color.Black else Color.White

    // Animação para o contador
    val countScale by animateFloatAsState(
        targetValue = if (count > 0) 1.05f else 1f,
        animationSpec = tween(100),
        label = "CountScale"
    )

    // Animação para o botão
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1f,
        animationSpec = tween(100),
        label = "ButtonScale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(foodName, color = contentColor) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = contentColor)
                    }
                },
                actions = {
                    IconButton(onClick = onReset) {
                        Icon(Icons.Default.Refresh, "Resetar", tint = contentColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent // Fundo transparente
                )
            )
        },
        containerColor = containerColor // Cor de fundo da tela
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null // Sem ripple, a animação de scale já é o feedback
                ) { onIncrement() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(0.5f))

            Text(
                text = icon,
                fontSize = 80.sp
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Você comeu",
                style = MaterialTheme.typography.headlineSmall,
                color = contentColor.copy(alpha = 0.8f)
            )

            Text(
                text = "$count",
                fontSize = 120.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                modifier = Modifier
                    .scale(countScale)
                    .padding(vertical = 16.dp)
            )

            Text(
                text = if (count == 1) "unidade" else "unidades",
                style = MaterialTheme.typography.headlineSmall,
                color = contentColor.copy(alpha = 0.8f)
            )

            Spacer(Modifier.weight(1f))

            // Feedback visual de clique
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.2f))
                    .scale(buttonScale),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+1",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }
            Spacer(Modifier.height(64.dp))
        }
    }
}



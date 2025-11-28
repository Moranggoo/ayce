package com.example.ayce.ui.group

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ayce.model.Participant

// Nota de Design:
// A tela de grupo pode ficar muito poluída (muitos botões).
// 1. A ação primária ("Adicionar Participante") foi movida para um FAB,
//    que abre um 'ModalBottomSheet' ou 'Dialog' (aqui usamos um Dialog).
//    Isso limpa a tela, focando na lista (Divulgação Progressiva).
// 2. O Card do participante (`ParticipantCard`) foi simplificado.
//    - Ação primária: O card todo é clicável para adicionar +1.
//    - Ações secundárias (Resetar, Excluir) são acionadas com 'long press' (clique longo),
//      abrindo um menu de confirmação. Isso evita cliques acidentais e
//      remove 2/3 dos botões da tela.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    viewModel: GroupViewModel,
    foodName: String,
    onNavigateBack: () -> Unit
) {
    val list by viewModel.participants.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var participantToManage by remember { mutableStateOf<Participant?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grupo: $foodName") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Create, "Adicionar Participante")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(list) { participant ->
                ParticipantCard(
                    participant = participant,
                    onIncrement = { viewModel.increment(participant.id) },
                    onManage = { participantToManage = participant }
                )
            }
        }
    }

    // --- Dialog para adicionar participante ---
    if (showAddDialog) {
        AddParticipantDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name ->
                if (name.isNotBlank()) {
                    viewModel.addParticipant(name)
                    showAddDialog = false
                }
            }
        )
    }

    // --- Dialog para gerenciar (Resetar/Excluir) ---
    participantToManage?.let { participant ->
        ManageParticipantDialog(
            participantName = participant.name,
            onDismiss = { participantToManage = null },
            onReset = {
                viewModel.reset(participant.id)
                participantToManage = null
            },
            onDelete = {
                viewModel.removeParticipant(participant.id)
                participantToManage = null
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParticipantCard(
    participant: Participant,
    onIncrement: () -> Unit,
    onManage: () -> Unit // Acionado por Long Press
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onIncrement, // Clique simples para a ação mais comum (+1)
                onLongClick = onManage   // Clique longo para ações secundárias
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = participant.name,
                style = MaterialTheme.typography.titleLarge
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${participant.count}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                // O Ícone de + é apenas visual, o clique é no card todo
                Icon(Icons.Default.Add, "Adicionar", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun AddParticipantDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Participante") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(name) }) { Text("Adicionar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun ManageParticipantDialog(
    participantName: String,
    onDismiss: () -> Unit,
    onReset: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Warning, null) },
        title = { Text("Gerenciar: $participantName") },
        text = { Text("O que você gostaria de fazer?") },
        confirmButton = {
            // Botão de Excluir (ação destrutiva)
            TextButton(
                onClick = onDelete,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Excluir")
            }
        },
        dismissButton = {
            // Botão de Resetar (ação neutra)
            TextButton(onClick = onReset) {
                Text("Resetar Contagem")
            }
            // Botão de Cancelar
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
package cdu278.mangotest.ui.main.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cdu278.mangotest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel,
    goToChat: (chatId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
             TopAppBar(
                 title = { Text(stringResource(R.string.main_chats)) },
             )
        },
        modifier = modifier
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .consumeWindowInsets(paddings)
                .fillMaxSize()
        ) {
            val items by viewModel.itemsFlow.collectAsState()
            if (items.isEmpty()) {
                Text(
                    text = stringResource(R.string.chats_noChats),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 50.dp)
                )
            } else {
                LazyColumn {
                    items(items, key = ChatsItemUi::id) {
                        ChatsItem(
                            item = it,
                            modifier = Modifier
                                .clickable { goToChat(it.id) }
                        )
                    }
                }
            }
        }
    }
}
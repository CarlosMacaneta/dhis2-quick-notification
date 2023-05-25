package org.saudigitus.quicknotification.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import org.saudigitus.quicknotification.R
import org.saudigitus.quicknotification.data.model.MessageType
import org.saudigitus.quicknotification.ui.components.TEIItem
import org.saudigitus.quicknotification.ui.util.Constants.MAX_COL_COUNT
import org.saudigitus.quicknotification.ui.util.Constants.MIN_COL_COUNT
import org.saudigitus.quicknotification.ui.util.DateTimeHelper
import org.saudigitus.quicknotification.ui.util.FunctionalityNotAvailablePopup
import org.saudigitus.quicknotification.ui.util.Utils.capitalizeText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    drawerState: DrawerState,
    messageType: MessageType,
    sync: () -> Unit,
    navigateToChat: (conversationId: String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current

    val columnCount =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            MAX_COL_COUNT
        else MIN_COL_COUNT

    val messagesData by viewModel.messagesData.collectAsState()


    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Inbox")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { sync() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_sync),
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { functionalityNotAvailablePopupShown = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(colorResource(id = R.color.colorPrimary).toArgb()),
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            //columns = GridCells.Fixed(columnCount)
        ) {
            val messages = messagesData.messageConversations

            if (!messages.isNullOrEmpty()) {
                items(messages) { conversation ->
                    TEIItem(
                        themeColor = Color(colorResource(R.color.colorPrimaryLight).toArgb()),
                        icoLetter = "${
                            if (conversation?.sender != null)
                                conversation.sender.displayName?.get(0)
                            else
                                conversation?.messageType?.getOrNull(0)
                        }",
                        onClick = {
                            navigateToChat(conversation?.id!!)
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "${
                                        if (conversation?.sender != null)
                                            conversation.sender.displayName
                                        else if (
                                            conversation?.messageType == MessageType
                                                .VALIDATION_RESULT.name.uppercase())
                                            "Validation"
                                        else
                                            capitalizeText(conversation?.messageType ?: "")
                                    }",
                                    maxLines = 1,
                                    softWrap = true,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 18.sp,
                                    fontWeight = if (conversation?.read == false)
                                        FontWeight.Bold
                                    else FontWeight.Normal
                                )

                                Text(
                                    text = "${conversation?.subject}",
                                    color = Color.Black.copy(.65f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    softWrap = true,
                                    fontSize = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = DateTimeHelper.ddMMDate(
                                        date = conversation?.lastMessage!!
                                    )
                                )

                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (conversation.messageCount != null
                                        && conversation.messageCount >= 0 &&
                                        conversation.read == false
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(20.dp)
                                                .background(
                                                    color = Color.Red.copy(.65f),
                                                    shape = RoundedCornerShape(100.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${conversation.messageCount}",
                                                color = Color.White
                                            )
                                        }
                                    }
                                    if (conversation.followUp == true) {
                                        Icon(
                                            imageVector = Icons.Default.Flag,
                                            contentDescription = null,
                                            tint = Color(0xFFE8711F).copy(.65f)
                                        )
                                    }
                                }

                                if (conversation.priority != null &&
                                    !conversation.priority.equals("none", true)
                                ) {
                                    Text(
                                        text = conversation.priority,
                                        maxLines = 1,
                                        softWrap = true,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.size(144.dp),
                            painter = painterResource(R.drawable.ic_no_message),
                            contentDescription = stringResource(R.string.no_messages)
                        )
                        Text(
                            text = stringResource(R.string.no_messages),
                            color = Color.Black.copy(.5f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

}
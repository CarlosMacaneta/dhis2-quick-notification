package org.saudigitus.quicknotification.ui.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.saudigitus.quicknotification.R
import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.MessageType
import org.saudigitus.quicknotification.ui.components.QuickNotificationAppBar
import org.saudigitus.quicknotification.ui.components.TEIItem
import org.saudigitus.quicknotification.ui.home.HomeRoutes
import org.saudigitus.quicknotification.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationDetailsScreen(
    viewModel: HomeViewModel,
    messageType: MessageType = MessageType.PRIVATE,
    navBack: () -> Unit
) {

    val messageDetail by viewModel.messageDetails.collectAsState()

    Scaffold(
        topBar = {
            QuickNotificationAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.Start) {
                        // Channel name
                        Text(
                            text = "${messageDetail.subject}",
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            softWrap = true,
                            overflow = TextOverflow.Ellipsis
                        )
                        // Number of members
                        Text(
                            text = "${messageDetail.userMessages?.size ?: 0} participants",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navAction = {
                    IconButton(onClick = { navBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.GroupAdd, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            if (messageType != MessageType.PRIVATE) {
                item {
                    ChatStatus(messageDetail)
                }
            }
            item {
                ChatMembers(messageDetail)
            }
        }
    }
}

@Composable
private fun ChatStatus(
    messageDetail: MessageDetail
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(bottom = 32.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Status", fontWeight = FontWeight.Bold)
                Text(text = "${messageDetail.status}", fontWeight = FontWeight.Normal)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Priority", fontWeight = FontWeight.Bold)
                Text(text = "${messageDetail.priority}", fontWeight = FontWeight.Normal)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Assignee", fontWeight = FontWeight.Bold)
                Text(text = "${messageDetail.assignee?.displayName}", fontWeight = FontWeight.Normal)
            }
        }
    }
}

@Composable
private fun ChatMembers(
    messageDetail: MessageDetail
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(0.dp))
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${messageDetail.userMessages?.size ?: 0}", fontWeight = FontWeight.Normal)
            Text(text = "participants", fontWeight = FontWeight.Normal)
        }

        TEIItem(
            painter = painterResource(R.drawable.ic_group_add),
            themeColor = MaterialTheme.colorScheme.primary.copy(.65f),
            enableDivider = false,
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Add participants")
        }

        messageDetail.userMessages?.forEach { participant ->
            TEIItem(
                painter = listOf(
                    painterResource(R.drawable.ic_person_female),
                    painterResource(R.drawable.ic_person_male)
                ).random(),
                themeColor = listOf(
                    Color.Magenta.copy(.65f),
                    Color.Red.copy(.65f),
                    Color.Blue.copy(.65f),
                ).random(),
                enableDivider = false,
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "${participant?.user?.displayName}"
                )
            }
        }
    }
}
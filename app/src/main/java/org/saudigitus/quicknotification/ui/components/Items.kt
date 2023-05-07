package org.saudigitus.quicknotification.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MetadataIcon(
    modifier: Modifier = Modifier,
    cornerShape: Dp = 4.dp,
    backgroundColor: Color,
    size: Dp = 40.dp,
    paddingAll: Dp = 0.dp,
    painter: Painter? = null,
    contentDescription: String? = null,
    colorFilter: ColorFilter? = null
) {
    if (painter != null) {
        Image(
            modifier = modifier
                .clip(RoundedCornerShape(cornerShape))
                .background(color = backgroundColor)
                .size(size)
                .padding(paddingAll),
            painter = painter,
            contentDescription = contentDescription,
            colorFilter = colorFilter
        )
    }
}

@Composable
private fun ItemContainer(
    content: @Composable (RowScope.() -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
private fun CardItemContainer(
    onClick: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier = Modifier
            .clickable { onClick.invoke() },
        shape = RoundedCornerShape(0.dp),
        content = content,
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(0.dp, Color.White)
    )
}

@Composable
fun TEIItem(
    painter: Painter? = null,
    icoLetter: String? = null,
    themeColor: Color,
    enableDivider: Boolean = true,
    dividerWidth: Float = .83f,
    onClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp)
            .clickable { onClick.invoke() },
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(0.dp, Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(color = themeColor)
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                MetadataIcon(
                    cornerShape = 100.dp,
                    backgroundColor = themeColor,
                    size = 48.dp,
                    paddingAll = 5.dp,
                    painter = painter,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                if (painter == null) {
                    Text(text = "$icoLetter", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            content()
        }
        if (enableDivider) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth(dividerWidth)
                    .align(Alignment.End)
                    .wrapContentWidth(Alignment.End, false)
            )
        }
    }
}

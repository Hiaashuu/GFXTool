package com.hiaashuu.gfxtool.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hiaashuu.gfxtool.data.model.GameProfile
import com.hiaashuu.gfxtool.ui.theme.DarkSurface
import com.hiaashuu.gfxtool.ui.theme.SuccessGreen
import com.hiaashuu.gfxtool.ui.theme.WarningOrange

@Composable
fun GameCard(
    game: GameProfile,
    version: String?,
    configFound: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlowCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        glowColor = game.primaryColor,
        backgroundColor = DarkSurface,
        cornerRadius = 16.dp,
        borderAlpha = if (configFound) 0.4f else 0.15f
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                game.primaryColor.copy(alpha = 0.18f),
                                game.accentColor.copy(alpha = 0.06f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(52.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(game.primaryColor)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = game.tagLine,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.55f)
                        )
                        if (version != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = game.primaryColor.copy(alpha = 0.22f)
                            ) {
                                Text(
                                    text = "v$version",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = game.primaryColor,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    if (configFound) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Config found",
                            tint = SuccessGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Config not found",
                            tint = WarningOrange,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Open",
                        tint = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
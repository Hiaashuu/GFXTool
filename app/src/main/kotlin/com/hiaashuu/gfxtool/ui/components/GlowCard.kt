package com.hiaashuu.gfxtool.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hiaashuu.gfxtool.ui.theme.DarkSurface
import com.hiaashuu.gfxtool.ui.theme.NeonPurple

@Composable
fun GlowCard(
    modifier: Modifier = Modifier,
    glowColor: Color = NeonPurple,
    backgroundColor: Color = DarkSurface,
    cornerRadius: Dp = 16.dp,
    borderAlpha: Float = 0.35f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = glowColor.copy(alpha = 0.4f),
                spotColor = glowColor.copy(alpha = 0.6f)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = glowColor.copy(alpha = borderAlpha),
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}
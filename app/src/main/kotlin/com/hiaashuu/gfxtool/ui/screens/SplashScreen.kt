package com.hiaashuu.gfxtool.ui.screens

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiaashuu.gfxtool.ui.theme.DarkBackground
import com.hiaashuu.gfxtool.ui.theme.NeonCyan
import com.hiaashuu.gfxtool.ui.theme.NeonPurple
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    var animTarget by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (animTarget) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = EaseOutCubic),
        label = "splashAlpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (animTarget) 1f else 0.82f,
        animationSpec = tween(durationMillis = 700, easing = EaseOutBack),
        label = "splashScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "splashGlow")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    LaunchedEffect(Unit) {
        delay(80)
        animTarget = true
        delay(2400)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(340.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = glowPulse * 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alpha)
                .scale(scale)
        ) {

            Text(
                text = "GFX",
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(NeonPurple, NeonCyan)
                    ),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 6.sp
                )
            )
            Text(
                text = "TOOL",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.65f),
                letterSpacing = 10.sp
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "GRAPHICS  OPTIMIZER",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = NeonCyan.copy(alpha = 0.8f),
                letterSpacing = 5.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "PUBG  ·  BGMI  ·  FREE FIRE  ·  COD",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.35f),
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
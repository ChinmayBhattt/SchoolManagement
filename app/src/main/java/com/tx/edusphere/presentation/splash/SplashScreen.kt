package com.tx.edusphere.presentation.splash

import android.app.Activity
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    var startAnimation by remember { mutableStateOf(false) }

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.6f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "alpha"
    )

    val textAlphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300),
        label = "textAlpha"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2200)
        onSplashFinished()
    }

    // Rich Deep Royal Blue Gradient Background
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF031348),
            Color(0xFF020D38),
            Color(0xFF010826)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        // Subtle Background Geometric Curves/Glows matching image
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top Left Corner Curve
            drawCircle(
                color = Color(0xFF0052D4).copy(alpha = 0.22f),
                radius = size.width * 0.75f,
                center = Offset(x = 0f, y = 0f)
            )

            // Bottom Right Corner Curves
            drawCircle(
                color = Color(0xFF0038A8).copy(alpha = 0.28f),
                radius = size.width * 0.85f,
                center = Offset(x = size.width, y = size.height)
            )
            drawCircle(
                color = Color(0xFF0072FF).copy(alpha = 0.18f),
                radius = size.width * 0.55f,
                center = Offset(x = size.width, y = size.height)
            )
        }

        // Center Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scaleAnim.value)
                .alpha(alphaAnim.value)
        ) {
            // Shiny Glossy App Icon Container
            Surface(
                modifier = Modifier
                    .size(136.dp)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.8f), Color(0xFF0052D4).copy(alpha = 0.3f))
                        ),
                        shape = RoundedCornerShape(36.dp)
                    ),
                shape = RoundedCornerShape(36.dp),
                color = Color.Transparent,
                shadowElevation = 24.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF0066FF),
                                    Color(0xFF0040C1),
                                    Color(0xFF021B79)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Custom TX Logo Drawing (Cap + TX + Open Book)
                    TxLogoCanvas(modifier = Modifier.size(96.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Title Text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(textAlphaAnim.value)
            ) {
                Text(
                    text = "TX ",
                    color = Color(0xFF00A2FF), // Bright Cyan / Light Blue
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "EduSphere",
                    color = Color.White, // Crisp White
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Smarter School Management",
                color = Color(0xFFA0C6FF), // Soft Blue
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.8.sp,
                modifier = Modifier.alpha(textAlphaAnim.value)
            )
        }
    }
}

@Composable
fun TxLogoCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Graduation Cap (Mortarboard Top)
        val capPath = Path().apply {
            moveTo(w * 0.5f, h * 0.12f)
            lineTo(w * 0.88f, h * 0.28f)
            lineTo(w * 0.5f, h * 0.44f)
            lineTo(w * 0.12f, h * 0.28f)
            close()
        }
        drawPath(path = capPath, color = Color.White)

        // Cap Base / Skull cap
        val capBase = Path().apply {
            moveTo(w * 0.32f, h * 0.36f)
            lineTo(w * 0.32f, h * 0.46f)
            quadraticTo(w * 0.5f, h * 0.56f, w * 0.68f, h * 0.46f)
            lineTo(w * 0.68f, h * 0.36f)
            close()
        }
        drawPath(path = capBase, color = Color(0xFFE2E8F0))

        // Cap Tassel
        val tasselPath = Path().apply {
            moveTo(w * 0.88f, h * 0.28f)
            lineTo(w * 0.88f, h * 0.48f)
        }
        drawPath(path = tasselPath, color = Color.White, style = Stroke(width = 3f))
        drawCircle(color = Color.White, radius = 4f, center = Offset(w * 0.88f, h * 0.50f))

        // 2. Open Book at Base
        val leftPage = Path().apply {
            moveTo(w * 0.10f, h * 0.84f)
            quadraticTo(w * 0.30f, h * 0.72f, w * 0.50f, h * 0.80f)
            lineTo(w * 0.50f, h * 0.90f)
            quadraticTo(w * 0.30f, h * 0.82f, w * 0.10f, h * 0.94f)
            close()
        }
        drawPath(path = leftPage, color = Color.White)

        val rightPage = Path().apply {
            moveTo(w * 0.90f, h * 0.84f)
            quadraticTo(w * 0.70f, h * 0.72f, w * 0.50f, h * 0.80f)
            lineTo(w * 0.50f, h * 0.90f)
            quadraticTo(w * 0.70f, h * 0.82f, w * 0.90f, h * 0.94f)
            close()
        }
        drawPath(path = rightPage, color = Color.White)

        // Book 3D Blue Trim
        val bookTrim = Path().apply {
            moveTo(w * 0.10f, h * 0.94f)
            quadraticTo(w * 0.30f, h * 0.82f, w * 0.50f, h * 0.90f)
            quadraticTo(w * 0.70f, h * 0.82f, w * 0.90f, h * 0.94f)
            lineTo(w * 0.90f, h * 0.97f)
            quadraticTo(w * 0.70f, h * 0.85f, w * 0.50f, h * 0.93f)
            quadraticTo(w * 0.30f, h * 0.85f, w * 0.10f, h * 0.97f)
            close()
        }
        drawPath(path = bookTrim, color = Color(0xFF00A2FF))

        // 3. Center "TX" Monogram
        // Letter "T" (White)
        val tTop = Path().apply {
            moveTo(w * 0.20f, h * 0.50f)
            lineTo(w * 0.60f, h * 0.50f)
            lineTo(w * 0.60f, h * 0.57f)
            lineTo(w * 0.20f, h * 0.57f)
            close()
        }
        drawPath(path = tTop, color = Color.White)

        val tStem = Path().apply {
            moveTo(w * 0.36f, h * 0.57f)
            lineTo(w * 0.44f, h * 0.57f)
            lineTo(w * 0.44f, h * 0.78f)
            lineTo(w * 0.36f, h * 0.78f)
            close()
        }
        drawPath(path = tStem, color = Color.White)

        // Letter "X" (Cyan Highlight)
        val xDiagonal1 = Path().apply {
            moveTo(w * 0.42f, h * 0.50f)
            lineTo(w * 0.80f, h * 0.78f)
            lineTo(w * 0.70f, h * 0.78f)
            lineTo(w * 0.32f, h * 0.50f)
            close()
        }
        drawPath(path = xDiagonal1, color = Color(0xFF00A2FF))

        val xDiagonal2 = Path().apply {
            moveTo(w * 0.70f, h * 0.50f)
            lineTo(w * 0.32f, h * 0.78f)
            lineTo(w * 0.42f, h * 0.78f)
            lineTo(w * 0.80f, h * 0.50f)
            close()
        }
        drawPath(path = xDiagonal2, color = Color(0xFF00D2FF))
    }
}

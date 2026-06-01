package com.example.projectmap.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DoughnutChart(
    data: Map<String, Long>,
    modifier: Modifier = Modifier
) {
    val total = data.values.sum().toFloat()
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            data.values.forEachIndexed { index, value ->
                val sweepAngle = (value / total) * 360f
                drawArc(
                    color = colors.getOrElse(index) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 40.dp.toPx(), cap = StrokeCap.Round)
                )
                startAngle += sweepAngle
            }
        }
    }
}

@Composable
fun HorizontalBarChart(
    data: Map<String, Long>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.values.maxOrNull()?.toFloat() ?: 1f
    
    Column(modifier = modifier) {
        data.forEach { (label, value) ->
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(text = label, style = MaterialTheme.typography.labelMedium)
                val barColor = MaterialTheme.colorScheme.primary
                Box(
                    modifier = Modifier
                        .fillMaxWidth(value / maxValue)
                        .height(12.dp)
                        .padding(top = 4.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRoundRect(
                            color = barColor,
                            size = size,
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleLineChart(
    data: List<Long>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOrNull()?.toFloat() ?: 1f
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val spacing = size.width / (data.size - 1).coerceAtLeast(1)
        val path = Path().apply {
            data.forEachIndexed { index, value ->
                val x = index * spacing
                val y = size.height - (value / maxValue) * size.height
                if (index == 0) moveTo(x, y) else lineTo(x, y)
            }
        }
        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

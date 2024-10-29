package com.hamidrezabashiri.calendar.presentation.common.components
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class InwardCurveShape(
    private val curveWidth: Dp,
    private val curveDepth: Dp,
    private val centerXOffset: Float // Added parameter for dynamic center X
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = with(density) {
        val curveWidthPx = curveWidth.toPx()
        val curveDepthPx = curveDepth.toPx()
        val centerX = (size.width / 2) + centerXOffset // Apply center offset
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(centerX - curveWidthPx, 0f)
            cubicTo(
                centerX - curveWidthPx / 2, 0f,
                centerX - curveWidthPx / 2, curveDepthPx / 2,
                centerX, curveDepthPx / 2
            )
            cubicTo(
                centerX + curveWidthPx / 2, curveDepthPx / 2,
                centerX + curveWidthPx / 2, 0f,
                centerX + curveWidthPx, 0f
            )
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        Outline.Generic(path)
    }
}


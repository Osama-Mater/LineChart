package com.mattar.osama.plotchart.line

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mattar.osama.plotchart.detectDragZoomGesture
import com.mattar.osama.plotchart.line.render.xaxis.IXAxisDrawer
import com.mattar.osama.plotchart.line.render.xaxis.SimpleXAxisDrawer
import com.mattar.osama.plotchart.line.render.yaxis.IYAxisDrawer
import com.mattar.osama.plotchart.line.render.yaxis.SimpleYAxisDrawer

/**
 * A composable that draws a Line graph with the configurations provided by the [LinePlot]. The graph
 * can be scrolled, zoomed and touch dragged for selection. Every part of the line graph can be customized,
 * by changing the configuration in the [LinePlot].
 *
 * @param plot the configuration to render the full graph
 * @param modifier Modifier
 * @param onSelectionStart invoked when the selection has started
 * @param onSelectionEnd invoked when the selection has ended
 * @param onSelection invoked when selection changes from one point to the next. You are provided
 * with the xOffset where the selection occurred in the graph and the [Point]s that are selected. If there
 * are multiple lines, you will get multiple data points.
 */
@Composable
fun LineGraph(
    plot: LinePlot, modifier: Modifier = Modifier,
    xAxisDrawer: IXAxisDrawer = SimpleXAxisDrawer(),
    yAxisDrawer: IYAxisDrawer = SimpleYAxisDrawer(),
    horizontalOffset: Float = 5F,
    onSelectionStart: () -> Unit = {},
    onSelectionEnd: () -> Unit = {},
    onSelection: ((Float, List<Point>) -> Unit)? = null
) {
    val paddingRight = plot.paddingRight

    val globalXScale = 1f

    val dragOffset = remember { mutableStateOf(0f) }
    val isDragging = remember { mutableStateOf(false) }
    val xZoom = remember { mutableStateOf(globalXScale) }
    val rowHeight = remember { mutableStateOf(0f) }
    val columnWidth = remember { mutableStateOf(0f) }

    val lines = plot.lines
    val points = lines.flatMap { it.points }

    check(horizontalOffset in 0F..25F) {
        "Horizontal Offset is the percentage offset from side, and must be between 0 and 25, included."
    }
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr,
    ) {
        Box(
            modifier = modifier.clipToBounds(),
        ) {
            Canvas(modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit, Unit) {
                    detectDragZoomGesture(
                        isDragAllowed = plot.selection.enabled,
                        detectDragTimeOut = plot.selection.detectionTime,
                        onDragStart = {
                            dragOffset.value = it.x
                            onSelectionStart()
                            isDragging.value = true
                        }, onDragEnd = {
                            isDragging.value = false
                            onSelectionEnd()
                        }, onZoom = { zoom ->
                            xZoom.value *= zoom
                        }
                    ) { change, _ ->
                        dragOffset.value = change.position.x
                    }
                })
            {
                drawIntoCanvas { canvas ->
                    val yBottom = size.height - rowHeight.value
                    val xOffset = 20.dp.toPx() * xZoom.value

                    val dragLocks = mutableMapOf<LinePlot.Line, Pair<Point, Offset>>()

                    val yAxisDrawableArea = computeYAxisDrawableArea(
                        xAxisLabelSize = xAxisDrawer.requireHeight(this),
                        size = size
                    )

                    val xAxisDrawableArea = computeXAxisDrawableArea(
                        yAxisWidth = yAxisDrawableArea.width,
                        labelHeight = xAxisDrawer.requireHeight(this),
                        size = size
                    )
                    val xAxisLabelsDrawableArea = computeXAxisLabelsDrawableArea(
                        xAxisDrawableArea = xAxisDrawableArea,
                        offset = horizontalOffset
                    )


                    val chartDrawableArea = computeDrawableArea(
                        xAxisDrawableArea = xAxisDrawableArea,
                        yAxisDrawableArea = yAxisDrawableArea,
                        size = size,
                        offset = horizontalOffset
                    )

                    // Draw Lines and Points
                    lines.forEach { line ->
                        val intersection = line.intersection
                        val connection = line.connection

                        connection?.drawPath?.invoke(
                            this,
                            canvas,
                            computeLinePath(
                                drawableArea = chartDrawableArea,
                                lineChartData = line,
                                transitionProgress = 1.0f
                            )
                        )

                        // Draw Lines and Points
                        line.points.forEachIndexed { index, point ->

                            val curOffset = computePointLocation(
                                drawableArea = chartDrawableArea,
                                lineChartData = line,
                                point = point,
                                index = index
                            )

                            curOffset.let {
                                if (isDragging.value && isDragLocked(
                                        dragOffset.value,
                                        it,
                                        xOffset
                                    )
                                ) {
                                    dragLocks[line] = line.points[index] to it
                                } else {
                                    intersection?.draw?.invoke(this, it, line.points[index])
                                }
                            }
                        }
                    }

                    // Draw drag selection Highlight
                    if (isDragging.value) {
                        // Draw Drag Line highlight
                        dragLocks.values.firstOrNull()?.let { (_, location) ->
                            val (x, _) = location
                            if (x >= columnWidth.value && x <= size.width - paddingRight.toPx()) {
                                plot.selection.highlight?.draw?.invoke(
                                    this,
                                    Offset(x, yBottom),
                                    Offset(x, 0f)
                                )
                            }
                        }
                        // Draw Point Highlight
                        dragLocks.entries.forEach { (line, lock) ->
                            val highlight = line.highlight
                            val location = lock.second
                            val x = location.x
                            if (x >= columnWidth.value && x <= size.width - paddingRight.toPx()) {
                                highlight?.draw?.invoke(this, location)
                            }
                        }
                    }

                    // OnSelection
                    if (isDragging.value) {
                        val x = dragLocks.values.firstOrNull()?.second?.x
                        if (x != null) {
                            onSelection?.invoke(x, dragLocks.values.map { it.first })
                        }
                    }

                    xAxisDrawer.drawXAxisLine(
                        drawScope = this,
                        drawableArea = xAxisDrawableArea,
                        canvas = canvas
                    )

                    xAxisDrawer.drawXAxisLabels(
                        drawScope = this,
                        canvas = canvas,
                        drawableArea = xAxisLabelsDrawableArea,
                        labels = points.map { it.label })

                    yAxisDrawer.drawAxisLine(
                        drawScope = this,
                        drawableArea = yAxisDrawableArea,
                        canvas = canvas
                    )

                    lines.forEach { line ->
                        yAxisDrawer.drawAxisLabels(
                            drawScope = this,
                            canvas = canvas,
                            drawableArea = yAxisDrawableArea,
                            minValue = line.minY,
                            maxValue = line.maxY
                        )
                    }
                }
            }
        }
    }
}
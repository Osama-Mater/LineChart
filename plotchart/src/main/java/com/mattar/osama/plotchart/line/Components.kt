package com.mattar.osama.plotchart.line

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mattar.osama.plotchart.line.LinePlot.*

/**
 * Denotes a point in the graph.
 *
 * @param label the x coordinate or the label in the x axis
 * @param value the y coordinate or the number in the y axis
 */
data class Point(val value: Float, val label: String)

/**
 * The configuration for the [LineGraph]
 *
 * @param lines list of lines to be represented
 * @param selection controls the touch and drag selection behaviour using [Selection]
 * @param paddingRight adjust the right padding of the graph.
 */
data class LinePlot(
    val lines: List<Line>,
    val selection: Selection = Selection(),
    val paddingRight: Dp = 0.dp,
) {
    /**
     * Represent a Line in the [LineGraph]
     *
     * @param points list of points in the line. Note that this list should be sorted by x coordinate
     * from decreasing to increasing value, so that the graph can be drawn properly.
     * @param connection drawing logic for the line between two adjacent points. If null, no line is drawn.
     * @param intersection drawing logic to draw the point itself. If null, the point is not drawn.
     * @param highlight drawing logic to draw the highlight at the point when it is selected. If null, the point
     * won't be highlighted on selection
     */
    data class Line(
        val points: List<Point>,
        val connection: Connection?,
        val intersection: Intersection?,
        val highlight: Highlight? = null,
    ) {
        val padBy: Float = 20F//percentage we pad yValue by

        init {
            require(padBy in 0F..100F) {
                "padBy must be between 0F and 100F, included"
            }
        }

        private val yMinMaxValues: Pair<Float, Float>
            get() {
                val minValue = points.minOf { it.value }
                val maxValue = points.maxOf { it.value }
                return minValue to maxValue
            }
        val startAtZero: Boolean = false

        internal val maxY: Float
            get() = yMinMaxValues.second + (yMinMaxValues.second - yMinMaxValues.first) * padBy / 100F
        internal val minY: Float
            get() = if (startAtZero) 0F else yMinMaxValues.first - (yMinMaxValues.second - yMinMaxValues.first) * padBy / 100F

        internal val yRange: Float
            get() = maxY - minY

    }

    /**
     * Represents a line between two data points
     *
     * @param connectionColor the color to be applied to the line
     * @param lineThickness The stroke width to apply to the line
     * @param cap treatment applied to the ends of the line segment
     * @param pathEffect optional effect or pattern to apply to the line
     * @param alpha opacity to be applied to the [color] from 0.0f to 1.0f representing
     * fully transparent to fully opaque respectively
     * @param colorFilter ColorFilter to apply to the [color] when drawn into the destination
     * @param blendMode the blending algorithm to apply to the [color]
     * @param draw override this to change the default drawLine implementation. You are provided with
     * the 'start' [Offset] and 'end' [Offset]
     */
    data class Connection(
        val connectionColor: Color = Color.Blue,
        val lineThickness: Dp = 3.dp,
        val cap: StrokeCap = Stroke.DefaultCap,
        val pathEffect: PathEffect? = null,
        val alpha: Float = 1.0f,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Offset, Offset) -> Unit = { start, end ->
            drawLine(
                connectionColor,
                start,
                end,
                lineThickness.toPx(),
                cap,
                pathEffect,
                alpha,
                colorFilter,
                blendMode
            )
        },

        val drawPath: (drawScope: DrawScope, canvas: Canvas, linePath: Path) -> Unit = { drawScope, canvas, linePath ->

            val mPaint by lazy {
                Paint().apply {
                    color = Color.Blue
                    style = PaintingStyle.Stroke
                    isAntiAlias = true
                }
            }

            val lineThicknessPath = with(drawScope) {
                3.dp.toPx()
            }


            canvas.drawPath(
                path = linePath,
                paint = mPaint.apply { strokeWidth = lineThicknessPath })
        }
    )

    /**
     * Represents a data point on the graph
     *
     * @param color The color or fill to be applied to the circle
     * @param radius The radius of the circle
     * @param alpha Opacity to be applied to the circle from 0.0f to 1.0f representing
     * fully transparent to fully opaque respectively
     * @param style Whether or not the circle is stroked or filled in
     * @param colorFilter ColorFilter to apply to the [color] when drawn into the destination
     * @param blendMode Blending algorithm to be applied to the brush
     * @param draw override this to change the default drawCircle implementation. You are provided
     * with the 'center' [Offset] and the actual [Point] that represents the intersection.
     */
    data class Intersection(
        val color: Color = Color.Blue,
        val radius: Dp = 6.dp,
        val alpha: Float = 1.0f,
        val style: DrawStyle = Fill,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Offset, Point) -> Unit = { center, _ ->
            drawCircle(
                color,
                radius.toPx(),
                center,
                alpha,
                style,
                colorFilter,
                blendMode
            )
        }
    )

    /**
     * Represents the data point when it is selected
     *
     * @param color The color or fill to be applied to the circle
     * @param radius The radius of the circle
     * @param alpha Opacity to be applied to the circle from 0.0f to 1.0f representing
     * fully transparent to fully opaque respectively
     * @param style Whether or not the circle is stroked or filled in
     * @param colorFilter ColorFilter to apply to the [color] when drawn into the destination
     * @param blendMode Blending algorithm to be applied to the brush
     * @param draw override this to change the default drawCircle implementation. You are provided
     * with the 'center' [Offset].
     */
    data class Highlight(
        val color: Color = Color.Black,
        val radius: Dp = 6.dp,
        val alpha: Float = 1.0f,
        val style: DrawStyle = Fill,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Offset) -> Unit = { center ->
            drawCircle(
                color,
                radius.toPx(),
                center,
                alpha,
                style,
                colorFilter,
                blendMode
            )
        }
    )

    /**
     * Configuration for the selection operation
     *
     * @param enabled if true, you can touch and drag to select the points. The point currently selected
     * is exposed via the [onSelection] param in the [LineGraph]. If false, the drag gesture is disabled.
     * @param highlight controls how the selection is represented in the graph. The default implementation
     * is a vertical dashed line. You can override this by supplying your own [Connection]
     * @param detectionTime the time taken for the touch to be recognised as a drag gesture
     */
    data class Selection(
        val enabled: Boolean = true,
        val highlight: Connection? = Connection(
            Color.Red,
            lineThickness = 2.dp,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
        ),
        val detectionTime: Long = 100L,
    )
}
package com.magneticplace.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.defaultWeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val TextWhite = Color(0xFFFFFFFF)
private val TextMuted = Color(0xFFD5DEE8)

private data class ForecastItem(val minC: Int, val maxC: Int)

/**
 * Weather-style widget: a sun/moon path card (location, sunrise/sunset,
 * moonrise/moonset) followed by the current time, temperature, wind and a
 * short forecast row.
 */
class SimpleGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val density = context.resources.displayMetrics.density
        val skyBitmap = createSkyBitmap(
            widthPx = (340 * density).toInt(),
            heightPx = (190 * density).toInt(),
            sunProgress = 0.36f,
            moonProgress = 0.62f,
        )

        provideContent {
            WidgetContent(skyBitmap = skyBitmap)
        }
    }

    @Composable
    private fun WidgetContent(skyBitmap: Bitmap) {
        val timeText = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val forecast = listOf(
            ForecastItem(14, 25),
            ForecastItem(14, 25),
            ForecastItem(14, 25),
        )

        Column(modifier = GlanceModifier.fillMaxSize().padding(16.dp)) {
            Box(modifier = GlanceModifier.fillMaxWidth().height(190.dp)) {
                Image(
                    provider = ImageProvider(skyBitmap),
                    contentDescription = "Trajeto do sol e da lua",
                    contentScale = ContentScale.FillBounds,
                    modifier = GlanceModifier.fillMaxSize(),
                )
                Column(modifier = GlanceModifier.fillMaxSize().padding(16.dp)) {
                    Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                        Text(text = "📍", style = TextStyle(fontSize = 14.sp))
                        Spacer(modifier = GlanceModifier.width(6.dp))
                        Text(
                            text = "Urnieta",
                            style = TextStyle(
                                color = ColorProvider(TextWhite),
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                            ),
                        )
                    }
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Row(modifier = GlanceModifier.fillMaxWidth()) {
                        Column(modifier = GlanceModifier.defaultWeight()) {
                            SunMoonRow(icon = "☀ ↑", time = "07:42")
                            SunMoonRow(icon = "☀ ↓", time = "20:27")
                        }
                        Column(modifier = GlanceModifier.defaultWeight()) {
                            SunMoonRow(icon = "☾ ↑", time = "06:45")
                            SunMoonRow(icon = "☾ ↓", time = "20:04")
                        }
                    }
                }
            }

            Spacer(modifier = GlanceModifier.height(10.dp))

            Text(
                text = "Viajam sol e lua",
                style = TextStyle(
                    color = ColorProvider(TextWhite),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                ),
                modifier = GlanceModifier.fillMaxWidth(),
            )

            Spacer(modifier = GlanceModifier.height(18.dp))

            Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.Vertical.Bottom) {
                Text(
                    text = timeText,
                    style = TextStyle(
                        color = ColorProvider(TextWhite),
                        fontWeight = FontWeight.Bold,
                        fontSize = 52.sp,
                    ),
                )
                Spacer(modifier = GlanceModifier.defaultWeight())
                Column(horizontalAlignment = Alignment.Horizontal.End) {
                    Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                        Text(
                            text = "↘",
                            style = TextStyle(color = ColorProvider(TextWhite), fontSize = 16.sp),
                        )
                        Spacer(modifier = GlanceModifier.width(4.dp))
                        Column(horizontalAlignment = Alignment.Horizontal.End) {
                            Text(
                                text = "0 km/h",
                                style = TextStyle(color = ColorProvider(TextWhite), fontSize = 13.sp),
                            )
                            Text(
                                text = "Oeste",
                                style = TextStyle(color = ColorProvider(TextMuted), fontSize = 11.sp),
                            )
                        }
                    }
                    Text(
                        text = "18°C",
                        style = TextStyle(
                            color = ColorProvider(TextWhite),
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                        ),
                    )
                }
            }

            Spacer(modifier = GlanceModifier.height(4.dp))

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Spacer(modifier = GlanceModifier.defaultWeight())
                Text(text = "☁", style = TextStyle(color = ColorProvider(TextWhite), fontSize = 40.sp))
            }

            Spacer(modifier = GlanceModifier.height(14.dp))

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                forecast.forEach { item ->
                    Column(
                        modifier = GlanceModifier.defaultWeight(),
                        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                    ) {
                        Text(text = "☁", style = TextStyle(color = ColorProvider(TextWhite), fontSize = 18.sp))
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Text(
                            text = "${item.minC}°C/${item.maxC}°C",
                            style = TextStyle(
                                color = ColorProvider(TextWhite),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SunMoonRow(icon: String, time: String) {
    Row(
        verticalAlignment = Alignment.Vertical.CenterVertically,
        modifier = GlanceModifier.padding(vertical = 2.dp),
    ) {
        Text(text = icon, style = TextStyle(color = ColorProvider(TextWhite), fontSize = 13.sp))
        Spacer(modifier = GlanceModifier.width(6.dp))
        Text(text = time, style = TextStyle(color = ColorProvider(TextWhite), fontSize = 15.sp))
    }
}

class SimpleGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SimpleGlanceWidget()
}

/** Draws the rounded sky/sea card with the sun and moon arcs baked in. */
private fun createSkyBitmap(
    widthPx: Int,
    heightPx: Int,
    sunProgress: Float,
    moonProgress: Float,
): Bitmap {
    val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val cornerRadius = heightPx * 0.18f

    val outerPath = Path().apply {
        addRoundRect(
            RectF(0f, 0f, widthPx.toFloat(), heightPx.toFloat()),
            cornerRadius,
            cornerRadius,
            Path.Direction.CW,
        )
    }
    canvas.clipPath(outerPath)

    val skyHeight = heightPx * 0.6f
    val skyPaint = Paint().apply {
        isAntiAlias = true
        shader = LinearGradient(
            0f, 0f, 0f, skyHeight,
            AndroidColor.rgb(0x7F, 0xC7, 0xF5),
            AndroidColor.rgb(0x3E, 0x86, 0xC9),
            Shader.TileMode.CLAMP,
        )
    }
    canvas.drawRect(0f, 0f, widthPx.toFloat(), skyHeight, skyPaint)

    val bandPaint = Paint().apply {
        isAntiAlias = true
        shader = LinearGradient(
            0f, skyHeight, 0f, heightPx.toFloat(),
            AndroidColor.rgb(0x3B, 0x7E, 0xC4),
            AndroidColor.rgb(0x2A, 0x5E, 0x9C),
            Shader.TileMode.CLAMP,
        )
    }
    canvas.drawRect(0f, skyHeight, widthPx.toFloat(), heightPx.toFloat(), bandPaint)

    val horizonPaint = Paint().apply {
        isAntiAlias = true
        color = AndroidColor.argb(90, 255, 255, 255)
        strokeWidth = 2f
    }
    canvas.drawLine(0f, skyHeight, widthPx.toFloat(), skyHeight, horizonPaint)

    val marginX = widthPx * 0.06f
    val startPoint = marginX to skyHeight
    val endPoint = (widthPx - marginX) to skyHeight
    val sunArcPeak = (widthPx * 0.5f) to (skyHeight - heightPx * 0.62f)
    val moonArcPeak = (widthPx * 0.42f) to (skyHeight - heightPx * 0.42f)

    val arcPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = heightPx * 0.012f
        strokeCap = Paint.Cap.ROUND
        color = AndroidColor.argb(130, 255, 255, 255)
    }

    val sunArcPath = Path().apply {
        moveTo(startPoint.first, startPoint.second)
        quadTo(sunArcPeak.first, sunArcPeak.second, endPoint.first, endPoint.second)
    }
    canvas.drawPath(sunArcPath, arcPaint)

    val moonArcPath = Path().apply {
        moveTo(startPoint.first, startPoint.second)
        quadTo(moonArcPeak.first, moonArcPeak.second, endPoint.first, endPoint.second)
    }
    canvas.drawPath(moonArcPath, arcPaint)

    val sunPos = pointOnQuadraticCurve(sunProgress, startPoint, sunArcPeak, endPoint)
    val sunRadius = heightPx * 0.09f
    val sunPaint = Paint().apply {
        isAntiAlias = true
        shader = RadialGradient(
            sunPos.first, sunPos.second, sunRadius,
            AndroidColor.rgb(0xFF, 0xE8, 0x82),
            AndroidColor.rgb(0xFF, 0xB6, 0x2E),
            Shader.TileMode.CLAMP,
        )
    }
    canvas.drawCircle(sunPos.first, sunPos.second, sunRadius, sunPaint)

    val moonPos = pointOnQuadraticCurve(moonProgress, startPoint, moonArcPeak, endPoint)
    val moonRadius = heightPx * 0.055f
    val moonPaint = Paint().apply {
        isAntiAlias = true
        shader = RadialGradient(
            moonPos.first, moonPos.second, moonRadius,
            AndroidColor.rgb(0xFF, 0xFF, 0xFF),
            AndroidColor.rgb(0xD8, 0xE4, 0xEE),
            Shader.TileMode.CLAMP,
        )
    }
    canvas.drawCircle(moonPos.first, moonPos.second, moonRadius, moonPaint)

    return bitmap
}

/** Point at parameter [t] on the quadratic Bezier curve p0 -> p1 -> p2. */
private fun pointOnQuadraticCurve(
    t: Float,
    p0: Pair<Float, Float>,
    p1: Pair<Float, Float>,
    p2: Pair<Float, Float>,
): Pair<Float, Float> {
    val oneMinusT = 1f - t
    val x = oneMinusT * oneMinusT * p0.first + 2 * oneMinusT * t * p1.first + t * t * p2.first
    val y = oneMinusT * oneMinusT * p0.second + 2 * oneMinusT * t * p1.second + t * t * p2.second
    return x to y
}

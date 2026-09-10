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
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
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
private val TextMuted = Color(0xFFB9C0C6)
private val TodayCircle = Color(0x24FFFFFF)

private const val CARD_WIDTH_DP = 280f
private const val CARD_HEIGHT_DP = 210f
private const val TOP_HEIGHT_DP = 76f
private const val ARC_HEIGHT_DP = 34f
private const val ARC_WIDTH_DP = 260f
private const val DAY_ROW_HEIGHT_DP = 76f
private const val ADDRESS_HEIGHT_DP = 24f
private const val DAY_WIDTH_DP = 52f

private sealed class DayEntry {
    data class Regular(val name: String, val icon: String, val maxC: Int, val minC: Int) : DayEntry()
    data class Today(val name: String, val dayNumber: String, val month: String, val maxC: Int, val minC: Int) :
        DayEntry()
}

private data class ArcMarker(val icon: String, val xDp: Float, val yDp: Float)

/**
 * Weather-style widget combining every reviewed piece: clock/wind/temperature
 * with a large cloud icon, a visible sun/moon path, a 5-day forecast with
 * "today" highlighted in the middle, and an address strip at the bottom.
 */
class SimpleGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val density = context.resources.displayMetrics.density
        val cardBitmap = createCardBitmap(
            widthPx = (CARD_WIDTH_DP * density).toInt(),
            heightPx = (CARD_HEIGHT_DP * density).toInt(),
            addressFraction = ADDRESS_HEIGHT_DP / CARD_HEIGHT_DP,
        )
        val arcBitmap = createArcBitmap(
            widthPx = (ARC_WIDTH_DP * density).toInt(),
            heightPx = (ARC_HEIGHT_DP * density).toInt(),
        )

        provideContent {
            WidgetContent(cardBitmap = cardBitmap, arcBitmap = arcBitmap)
        }
    }

    @Composable
    private fun WidgetContent(cardBitmap: Bitmap, arcBitmap: Bitmap) {
        val timeText = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val days = listOf(
            DayEntry.Regular("Qua", "☀️", 23, 14),
            DayEntry.Regular("Qui", "🌤️", 24, 15),
            DayEntry.Today("Sex", "10", "septiembre", 22, 13),
            DayEntry.Regular("Sáb", "⛈️", 21, 12),
            DayEntry.Regular("Dom", "❄️", 20, 11),
        )
        val markers = listOf(
            ArcMarker("🌙", 31f, 4f),
            ArcMarker("☀️", 91f, 12f),
            ArcMarker("☀️", 152f, 12f),
            ArcMarker("🌙", 213f, 5f),
        )

        Box(modifier = GlanceModifier.fillMaxWidth().height(CARD_HEIGHT_DP.dp)) {
            Image(
                provider = ImageProvider(cardBitmap),
                contentDescription = "Cartão do tempo",
                contentScale = ContentScale.FillBounds,
                modifier = GlanceModifier.fillMaxSize(),
            )
            Column(modifier = GlanceModifier.fillMaxSize()) {
                Box(modifier = GlanceModifier.fillMaxWidth().height(TOP_HEIGHT_DP.dp)) {
                    Box(
                        modifier = GlanceModifier.fillMaxSize().padding(start = 11.dp, top = 6.dp),
                        contentAlignment = Alignment.TopStart,
                    ) {
                        Text(
                            text = timeText,
                            style = TextStyle(
                                color = ColorProvider(TextWhite),
                                fontWeight = FontWeight.Normal,
                                fontSize = 24.sp,
                            ),
                        )
                    }
                    Box(
                        modifier = GlanceModifier.fillMaxSize().padding(end = 11.dp, top = 6.dp),
                        contentAlignment = Alignment.TopEnd,
                    ) {
                        Column(horizontalAlignment = Alignment.Horizontal.End) {
                            Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                                Text(
                                    text = "↖",
                                    style = TextStyle(color = ColorProvider(TextWhite), fontSize = 10.sp),
                                )
                                Spacer(modifier = GlanceModifier.width(3.dp))
                                Column(horizontalAlignment = Alignment.Horizontal.End) {
                                    Text(
                                        text = "4 km/h",
                                        style = TextStyle(
                                            color = ColorProvider(TextWhite),
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 9.sp,
                                        ),
                                    )
                                    Text(text = "Leste", style = TextStyle(color = ColorProvider(TextMuted), fontSize = 8.sp))
                                }
                            }
                            Spacer(modifier = GlanceModifier.height(2.dp))
                            Text(
                                text = "22°C",
                                style = TextStyle(
                                    color = ColorProvider(TextWhite),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                ),
                            )
                        }
                    }
                    Box(
                        modifier = GlanceModifier.fillMaxSize().padding(end = 2.dp, bottom = 1.dp),
                        contentAlignment = Alignment.BottomEnd,
                    ) {
                        Text(text = "☁️", style = TextStyle(fontSize = 42.sp))
                    }
                }

                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(ARC_HEIGHT_DP.dp)
                        .padding(horizontal = 10.dp),
                ) {
                    Image(
                        provider = ImageProvider(arcBitmap),
                        contentDescription = "Percurso do sol e da lua",
                        contentScale = ContentScale.FillBounds,
                        modifier = GlanceModifier.fillMaxSize(),
                    )
                    markers.forEach { marker ->
                        Box(
                            modifier = GlanceModifier
                                .fillMaxSize()
                                .padding(start = marker.xDp.dp, top = marker.yDp.dp),
                            contentAlignment = Alignment.TopStart,
                        ) {
                            Text(text = marker.icon, style = TextStyle(fontSize = 13.sp))
                        }
                    }
                }

                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(DAY_ROW_HEIGHT_DP.dp)
                        .padding(horizontal = 10.dp),
                ) {
                    Row(modifier = GlanceModifier.fillMaxSize()) {
                        days.forEach { entry ->
                            when (entry) {
                                is DayEntry.Regular -> DayColumn(entry)
                                is DayEntry.Today -> TodayColumn(entry)
                            }
                        }
                    }
                }

                Box(
                    modifier = GlanceModifier.fillMaxWidth().height(ADDRESS_HEIGHT_DP.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "PRAÇA DO EXEMPLO, 1, 2710-000 LOCALIDADE, PORTUGAL",
                        style = TextStyle(
                            color = ColorProvider(TextWhite),
                            fontWeight = FontWeight.Bold,
                            fontSize = 6.5.sp,
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun DayColumn(day: DayEntry.Regular) {
    Column(
        modifier = GlanceModifier.width(DAY_WIDTH_DP.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        Text(
            text = day.name,
            style = TextStyle(color = ColorProvider(TextWhite), fontWeight = FontWeight.Medium, fontSize = 9.sp),
        )
        Spacer(modifier = GlanceModifier.height(3.dp))
        Text(text = day.icon, style = TextStyle(fontSize = 17.sp))
        Spacer(modifier = GlanceModifier.height(3.dp))
        Text(
            text = "${day.maxC}°/${day.minC}°",
            style = TextStyle(color = ColorProvider(TextMuted), fontWeight = FontWeight.Medium, fontSize = 8.sp),
        )
    }
}

@Composable
private fun TodayColumn(day: DayEntry.Today) {
    Column(
        modifier = GlanceModifier.width(DAY_WIDTH_DP.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        Text(
            text = day.name,
            style = TextStyle(color = ColorProvider(TextWhite), fontWeight = FontWeight.Medium, fontSize = 9.sp),
        )
        Spacer(modifier = GlanceModifier.height(2.dp))
        Box(
            modifier = GlanceModifier
                .width(20.dp)
                .height(20.dp)
                .cornerRadius(10.dp)
                .background(ColorProvider(TodayCircle)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = day.dayNumber,
                style = TextStyle(color = ColorProvider(TextWhite), fontWeight = FontWeight.Medium, fontSize = 9.sp),
            )
        }
        Spacer(modifier = GlanceModifier.height(2.dp))
        Text(
            text = "${day.maxC}°/${day.minC}°",
            style = TextStyle(color = ColorProvider(TextMuted), fontWeight = FontWeight.Medium, fontSize = 8.sp),
        )
    }
}

class SimpleGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SimpleGlanceWidget()
}

/** Draws the rounded dark card: gradient, top-left highlight, and the address-bar band. */
private fun createCardBitmap(widthPx: Int, heightPx: Int, addressFraction: Float): Bitmap {
    val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val cornerRadius = widthPx * 0.045f

    val outerPath = Path().apply {
        addRoundRect(RectF(0f, 0f, widthPx.toFloat(), heightPx.toFloat()), cornerRadius, cornerRadius, Path.Direction.CW)
    }
    canvas.clipPath(outerPath)

    val basePaint = Paint().apply {
        isAntiAlias = true
        shader = LinearGradient(
            0f, 0f, widthPx.toFloat(), heightPx.toFloat(),
            intArrayOf(
                AndroidColor.rgb(0x4A, 0x4A, 0x4A),
                AndroidColor.rgb(0x33, 0x33, 0x33),
                AndroidColor.rgb(0x1C, 0x1C, 0x1C),
            ),
            floatArrayOf(0f, 0.45f, 1f),
            Shader.TileMode.CLAMP,
        )
    }
    canvas.drawRect(0f, 0f, widthPx.toFloat(), heightPx.toFloat(), basePaint)

    val highlightPaint = Paint().apply {
        isAntiAlias = true
        shader = RadialGradient(
            widthPx * 0.18f, -heightPx * 0.1f, widthPx * 0.9f,
            AndroidColor.argb(70, 255, 255, 255),
            AndroidColor.argb(0, 255, 255, 255),
            Shader.TileMode.CLAMP,
        )
    }
    canvas.drawRect(0f, 0f, widthPx.toFloat(), heightPx.toFloat(), highlightPaint)

    val addressTop = heightPx * (1f - addressFraction)
    val addressPaint = Paint().apply {
        isAntiAlias = true
        color = AndroidColor.argb(71, 0, 0, 0)
    }
    canvas.drawRect(0f, addressTop, widthPx.toFloat(), heightPx.toFloat(), addressPaint)
    val dividerPaint = Paint().apply {
        isAntiAlias = true
        color = AndroidColor.argb(31, 255, 255, 255)
        strokeWidth = 1f
    }
    canvas.drawLine(0f, addressTop, widthPx.toFloat(), addressTop, dividerPaint)

    return bitmap
}

/** A shallow "valley" arc spanning the width, for the sun/moon markers to sit on. */
private fun createArcBitmap(widthPx: Int, heightPx: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val arcPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = heightPx * 0.05f
        strokeCap = Paint.Cap.ROUND
        color = AndroidColor.argb(89, 255, 255, 255)
    }
    val arcPath = Path().apply {
        moveTo(widthPx * 0.032f, heightPx * 0.157f)
        quadTo(widthPx * 0.5f, heightPx * 0.902f, widthPx * 0.968f, heightPx * 0.157f)
    }
    canvas.drawPath(arcPath, arcPaint)

    return bitmap
}

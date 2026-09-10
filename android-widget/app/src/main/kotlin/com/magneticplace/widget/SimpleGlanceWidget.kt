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
private const val TOP_HEIGHT_DP = 99f
private const val DAY_ROW_HEIGHT_DP = 86f
private const val ADDRESS_HEIGHT_DP = 25f
private const val DAY_WIDTH_DP = 39f
private const val TODAY_WIDTH_DP = 46f

private data class DayForecast(val name: String, val icon: String, val maxC: Int, val minC: Int)

/**
 * Weather-style widget: a dark 4:3 card with the clock, wind, temperature and
 * a large cloud icon on top, a 7-day row with today highlighted in the
 * middle, and an address strip at the bottom. A faint sun path is baked into
 * the background, tucked into the empty corner below the clock.
 */
class SimpleGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val density = context.resources.displayMetrics.density
        val cardBitmap = createCardBitmap(
            widthPx = (CARD_WIDTH_DP * density).toInt(),
            heightPx = (CARD_HEIGHT_DP * density).toInt(),
            topFraction = TOP_HEIGHT_DP / CARD_HEIGHT_DP,
            addressFraction = ADDRESS_HEIGHT_DP / CARD_HEIGHT_DP,
        )

        provideContent {
            WidgetContent(cardBitmap = cardBitmap)
        }
    }

    @Composable
    private fun WidgetContent(cardBitmap: Bitmap) {
        val timeText = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        val days = listOf(
            DayForecast("lun", "☀️", 25, 14),
            DayForecast("mar", "🌦️", 21, 13),
            DayForecast("mié", "🌦️", 20, 13),
            DayForecast("vie", "☁️", 24, 15),
            DayForecast("sáb", "☁️", 24, 15),
            DayForecast("dom", "☁️", 24, 15),
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
                        modifier = GlanceModifier.fillMaxSize().padding(start = 11.dp, top = 7.dp),
                        contentAlignment = Alignment.TopStart,
                    ) {
                        Text(
                            text = timeText,
                            style = TextStyle(
                                color = ColorProvider(TextWhite),
                                fontWeight = FontWeight.Normal,
                                fontSize = 28.sp,
                            ),
                        )
                    }
                    Box(
                        modifier = GlanceModifier.fillMaxSize().padding(end = 11.dp, top = 8.dp),
                        contentAlignment = Alignment.TopEnd,
                    ) {
                        Column(horizontalAlignment = Alignment.Horizontal.End) {
                            Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                                Text(text = "↖", style = TextStyle(color = ColorProvider(TextWhite), fontSize = 10.sp))
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
                            Text(
                                text = "22°C",
                                style = TextStyle(
                                    color = ColorProvider(TextWhite),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                ),
                            )
                        }
                    }
                    Box(
                        modifier = GlanceModifier.fillMaxSize().padding(end = 2.dp, bottom = 2.dp),
                        contentAlignment = Alignment.BottomEnd,
                    ) {
                        Text(text = "☁️", style = TextStyle(fontSize = 58.sp))
                    }
                }

                Box(modifier = GlanceModifier.fillMaxWidth().height(DAY_ROW_HEIGHT_DP.dp)) {
                    Row(modifier = GlanceModifier.fillMaxSize()) {
                        DayColumn(days[0])
                        DayColumn(days[1])
                        DayColumn(days[2])
                        TodayColumn()
                        DayColumn(days[3])
                        DayColumn(days[4])
                        DayColumn(days[5])
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
                            fontSize = 7.sp,
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun DayColumn(day: DayForecast) {
    Column(
        modifier = GlanceModifier.width(DAY_WIDTH_DP.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        Text(
            text = day.name,
            style = TextStyle(color = ColorProvider(TextWhite), fontWeight = FontWeight.Medium, fontSize = 8.sp),
        )
        Spacer(modifier = GlanceModifier.height(3.dp))
        Text(text = day.icon, style = TextStyle(fontSize = 13.sp))
        Spacer(modifier = GlanceModifier.height(3.dp))
        Text(
            text = "${day.maxC}°/${day.minC}°",
            style = TextStyle(color = ColorProvider(TextMuted), fontWeight = FontWeight.Medium, fontSize = 7.sp),
        )
    }
}

@Composable
private fun TodayColumn() {
    Column(
        modifier = GlanceModifier.width(TODAY_WIDTH_DP.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        Text(
            text = "Jueves",
            style = TextStyle(color = ColorProvider(TextWhite), fontWeight = FontWeight.Medium, fontSize = 9.sp),
        )
        Spacer(modifier = GlanceModifier.height(2.dp))
        Box(
            modifier = GlanceModifier
                .width(23.dp)
                .height(23.dp)
                .cornerRadius(11.5.dp)
                .background(ColorProvider(TodayCircle)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "10",
                style = TextStyle(color = ColorProvider(TextWhite), fontWeight = FontWeight.Medium, fontSize = 11.sp),
            )
        }
        Spacer(modifier = GlanceModifier.height(2.dp))
        Text(text = "septiembre", style = TextStyle(color = ColorProvider(TextWhite), fontSize = 8.sp))
        Spacer(modifier = GlanceModifier.height(2.dp))
        Text(
            text = "25°/14°",
            style = TextStyle(color = ColorProvider(TextMuted), fontWeight = FontWeight.Medium, fontSize = 7.sp),
        )
    }
}

class SimpleGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SimpleGlanceWidget()
}

/**
 * Draws the whole card in one bitmap: rounded dark gradient, a faint sun path
 * tucked below where the clock sits, and the darker address-bar band at the
 * bottom. Everything else (text) is overlaid by Glance composables.
 */
private fun createCardBitmap(
    widthPx: Int,
    heightPx: Int,
    topFraction: Float,
    addressFraction: Float,
): Bitmap {
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

    // Faint sun path, positioned within the top band, below-left of the clock.
    val topHeight = heightPx * topFraction
    val arcPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = topHeight * 0.0125f
        strokeCap = Paint.Cap.ROUND
        color = AndroidColor.argb(46, 255, 255, 255)
    }
    val arcPath = Path().apply {
        moveTo(widthPx * 0.0412f, topHeight * 0.9333f)
        quadTo(widthPx * 0.2647f, topHeight * 0.65f, widthPx * 0.5147f, topHeight * 0.9f)
    }
    canvas.drawPath(arcPath, arcPaint)

    val sunCx = widthPx * 0.1618f
    val sunCy = topHeight * 0.7833f
    val glowRadius = widthPx * 0.0353f
    val glowPaint = Paint().apply {
        isAntiAlias = true
        shader = RadialGradient(
            sunCx, sunCy, glowRadius,
            AndroidColor.argb(128, 255, 216, 115),
            AndroidColor.argb(0, 255, 216, 115),
            Shader.TileMode.CLAMP,
        )
    }
    canvas.drawCircle(sunCx, sunCy, glowRadius, glowPaint)
    val corePaint = Paint().apply {
        isAntiAlias = true
        color = AndroidColor.rgb(0xFF, 0xD8, 0x73)
    }
    canvas.drawCircle(sunCx, sunCy, widthPx * 0.0103f, corePaint)

    // Address-bar band at the bottom, with a thin top divider.
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

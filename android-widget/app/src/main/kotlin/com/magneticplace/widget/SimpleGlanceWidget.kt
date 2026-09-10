package com.magneticplace.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

/** Preference keys backing the widget's persisted state. */
private object WidgetStateKeys {
    val TITLE = stringPreferencesKey("widget_title")
    val COUNTER = intPreferencesKey("widget_counter")
}

private val WidgetBackgroundColor = Color(0xFF121212)
private val WidgetTextColor = Color(0xFFFFFFFF)
private val WidgetAccentColor = Color(0xFF64FFDA)

/** This widget always renders dark, so day and night use the same color. */
private fun solidColor(color: Color): ColorProvider = ColorProvider(day = color, night = color)

/**
 * A minimal Glance app widget: dark background, a custom title, a counter that
 * demonstrates persisted/updatable state, and a button that mutates that state.
 */
class SimpleGlanceWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<Preferences> =
        PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }

    @Composable
    private fun WidgetContent() {
        val context = LocalContext.current
        val prefs = currentState<Preferences>()
        val title = prefs[WidgetStateKeys.TITLE] ?: context.getString(R.string.widget_default_title)
        val counter = prefs[WidgetStateKeys.COUNTER] ?: 0

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(WidgetBackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically,
        ) {
            Text(
                text = title,
                style = TextStyle(
                    color = solidColor(WidgetTextColor),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                ),
            )

            Spacer(modifier = GlanceModifier.height(8.dp))

            Text(
                text = "Cliques: $counter",
                style = TextStyle(
                    color = solidColor(WidgetAccentColor),
                    fontSize = 14.sp,
                ),
            )

            Spacer(modifier = GlanceModifier.height(12.dp))

            Text(
                text = context.getString(R.string.widget_refresh_action),
                style = TextStyle(
                    color = solidColor(WidgetBackgroundColor),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                ),
                modifier = GlanceModifier
                    .background(WidgetAccentColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable(actionRunCallback<IncrementCounterAction>()),
            )
        }
    }
}

class SimpleGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SimpleGlanceWidget()
}

/** Increments the persisted counter and triggers a widget recomposition. */
class IncrementCounterAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val current = prefs[WidgetStateKeys.COUNTER] ?: 0
            prefs[WidgetStateKeys.COUNTER] = current + 1
        }
        SimpleGlanceWidget().update(context, glanceId)
    }
}

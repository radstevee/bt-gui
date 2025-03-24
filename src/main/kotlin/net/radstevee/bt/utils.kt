package net.radstevee.bt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
public inline fun <T> state(crossinline calculation: @DisallowComposableCalls () -> T): MutableState<T> {
  return remember { mutableStateOf<T>(calculation()) }
}

@Composable
public fun <T> state(value: T): MutableState<T> {
  return state { value }
}

public fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
  factory = {
    val density = LocalDensity.current
    val strokeWidthPx = density.run { strokeWidth.toPx() }

    Modifier.drawBehind {
      val width = size.width
      val height = size.height - strokeWidthPx/2

      drawLine(
        color = color,
        start = Offset(x = 0f, y = height),
        end = Offset(x = width , y = height),
        strokeWidth = strokeWidthPx
      )
    }
  }
)

public fun Modifier.endBorder(strokeWidth: Dp, color: Color) = composed(
  factory = {
    val density = LocalDensity.current
    val strokeWidthPx = density.run { strokeWidth.toPx() }

    Modifier.drawBehind {
      val width = size.width
      val height = size.height - strokeWidthPx/2

      drawLine(
        color = color,
        start = Offset(x = width, y = 0f),
        end = Offset(x = width, y = height),
        strokeWidth = strokeWidthPx
      )
    }
  }
)

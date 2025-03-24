package net.radstevee.bt.screen.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.radstevee.bt.BARS_BACKGROUND
import net.radstevee.bt.endBorder

@Composable
public fun SideBar() {
  Box {
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .width(55.dp)
        .padding(top = 65.dp)
        .align(Alignment.TopStart)
        .background(BARS_BACKGROUND)
        .endBorder(2.dp, Color(0xFF2A2A2A))
    ) {
    }
  }
}

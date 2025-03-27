package net.radstevee.bt.screen.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import net.radstevee.bt.BARS_BACKGROUND
import net.radstevee.bt.endBorder
import net.radstevee.bt.screen.BuildToolsRoute
import org.jetbrains.jewel.ui.component.painterResource

@Composable
public fun SideBar(
  onClickHome: () -> Unit,
) {
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
      Box(
        modifier = Modifier
          .width(35.dp)
          .height(43.dp)
          .align(Alignment.CenterHorizontally)
          .padding(top = 10.dp)
          .clip(RoundedCornerShape(5.dp))
          .background(Color(0x2535A854))
          .clickable { onClickHome() }
      ) {
        Image(
          painter = painterResource("home.svg"),
          contentDescription = "Home",
          modifier = Modifier
            .align(Alignment.Center)
            .size(21.dp)
        )
      }
    }
  }
}

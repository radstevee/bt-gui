package net.radstevee.bt.screen.bar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.radstevee.bt.BARS_BACKGROUND
import net.radstevee.bt.bottomBorder
import net.radstevee.bt.screen.BuilderScreen
import net.radstevee.bt.state
import net.radstevee.bt.state.instance.Instance
import net.radstevee.bt.view.model.InstancesViewModel
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.painterResource
import kotlin.system.exitProcess

@OptIn(ExperimentalFoundationApi::class, DelicateCoroutinesApi::class)
@Composable
public fun TopBar(
  screen: BuilderScreen,
  window: ComposeWindow,
  instancesViewModel: InstancesViewModel = viewModel { InstancesViewModel() },
) {
  val instancesState by instancesViewModel.instancesState.collectAsState()

  Box {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(68.dp)
        .align(Alignment.TopStart)
        .background(BARS_BACKGROUND)
        .bottomBorder(2.dp, Color(0xFF2A2A2A)),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Image(
          painter = painterResource("spigot.png"),
          contentDescription = "SpigotMC Logo",
          contentScale = ContentScale.Fit,
          modifier = Modifier
            .scale(0.625f)
            .padding(start = 10.dp, top = 2.dp)
        )

        Text(
          screen.displayName,
          modifier = Modifier
            .padding(start = 10.dp)
        )
      }
      var supportMenuExpanded by state(false)
      var instanceMenuExpanded by state(false)

      Row {
        Box(
          modifier = Modifier
            .padding(end = 20.dp)
            .width(140.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFF373737))
            .clickable { supportMenuExpanded = true; println("hi") }
        ) {
          Row(
            modifier = Modifier
              .align(Alignment.Center)
          ) {
            Image(
              painter = painterResource("chat.svg"),
              contentDescription = "Chat",
              modifier = Modifier
                .size(22.dp)
                .padding(end = 7.dp)
            )

            Text(
              "Get Support",
              modifier = Modifier
                .padding(top = 2.dp)
            )

            Image(
              painter = painterResource("chevron-down.svg"),
              contentDescription = "Dropdown",
              modifier = Modifier
                .size(22.dp)
                .padding(start = 3.dp, bottom = 2.dp)
            )
          }
        }

        Box(
          modifier = Modifier
            .padding(end = 0.dp)
            .width(300.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(5.dp))
            .border(2.dp, Color(0xFF4A4A4A))
        ) {
          Row(
            modifier = Modifier
              .align(Alignment.Center)
          ) {
            Image(
              painter = painterResource("instances.svg"),
              contentDescription = "Instances",
              modifier = Modifier
                .size(28.dp)
                .padding(top = 4.dp, end = 5.dp)
            )

            Text(
              instancesState.displayedName(),
              modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 2.dp)
                .clickable { instanceMenuExpanded = true; println("oh hi") }
            )

            Image(
              painter = painterResource("chevron-down.svg"),
              contentDescription = "Dropdown",
              modifier = Modifier
                .size(24.dp)
                .padding(start = 2.dp, top = 10.dp)
                .clickable { instanceMenuExpanded = true; println("oh hi") }
            )

            Image(
              painter = painterResource("run.svg"),
              contentDescription = "Run",
              modifier = Modifier
                .size(32.dp)
                .padding(start = 10.dp, end = 8.dp, bottom = 4.dp)
                .clickable {
                  instancesViewModel.add(
                    Instance(
                      "hello!1!11!",
                      "",
                      false
                    )
                  )
                  println("gonna run away")
                }
            )

            Image(
              painter = painterResource("stop.svg"),
              contentDescription = "Stop",
              modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp, top = 5.dp)
                .clickable {
                  instancesViewModel.removeLast()
                  println("me when I stop")
                }
            )

            Image(
              painter = painterResource("terminal.svg"),
              contentDescription = "View Logs",
              modifier = Modifier
                .size(25.dp)
                .padding(end = 4.dp, top = 5.dp)
                .clickable { println("read the fucking logs") }
            )
          }
        }

        Box(
          modifier = Modifier
            .padding(start = 20.dp, top = 7.dp)
            .width(24.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFF36383B))
            .clickable { window.isMinimized = true }
        ) {
          Image(
            painter = painterResource("minimize.svg"),
            contentDescription = "Minimize",
            modifier = Modifier
              .align(Alignment.Center)
          )
        }

        Box(
          modifier = Modifier
            .padding(start = 10.dp, top = 7.dp)
            .width(24.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFF36383B))
            .clickable { window.placement = WindowPlacement.Maximized }
        ) {
          Image(
            painter = painterResource("restore.svg"),
            contentDescription = "Restore",
            modifier = Modifier
              .align(Alignment.Center)
          )
        }

        Box(
          modifier = Modifier
            .padding(start = 10.dp, top = 7.dp, end = 20.dp)
            .width(24.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFF36383B))
            .clickable {
              GlobalScope.launch { exitProcess(0) }
            }
        ) {
          Image(
            painter = painterResource("close.svg"),
            contentDescription = "Close",
            modifier = Modifier
              .align(Alignment.Center)
          )
        }
      }
    }
  }
}

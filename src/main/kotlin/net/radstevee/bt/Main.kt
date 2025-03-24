package net.radstevee.bt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.application
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.radstevee.bt.screen.BuilderScreen
import net.radstevee.bt.screen.bar.SideBar
import net.radstevee.bt.screen.bar.TopBar
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.styling.TitleBarStyle

@Composable
public fun App(
  window: ComposeWindow,
  navController: NavHostController = rememberNavController(),
) {
  IntUiTheme(isDark = true) {
    Box(
      modifier = Modifier.background(BACKGROUND)
    ) {
      NavHost(
        navController = navController,
        startDestination = BuilderScreen.MainMenu.name,
        modifier = Modifier
          .fillMaxSize()
          .background(BACKGROUND)
      ) {
        BuilderScreen.entries.forEach { screen ->
          composable(screen.name) { back ->
            SideBar()
            TopBar(screen, window)

            screen.view(navController, back, window)
          }
        }
      }
    }
  }
}

public fun main() {
  application {
    IntUiTheme(
      theme = JewelTheme.darkThemeDefinition(),
      styling = ComponentStyling.default()
        .decoratedWindow(titleBarStyle = TitleBarStyle.dark())
    ) {
      DecoratedWindow(
        onCloseRequest = ::exitApplication
      ) {
        DevelopmentEntryPoint {
          App(window)
        }
      }
    }
  }
}

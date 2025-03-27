package net.radstevee.bt

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.application
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.radstevee.bt.screen.BuildToolsRoute
import net.radstevee.bt.screen.MainMenu
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
      val backStackEntry by navController.currentBackStackEntryAsState()
      val currentScreen = BuildToolsRoute.valueOf(
        backStackEntry?.destination?.route ?: BuildToolsRoute.MainMenu.name
      )

      Scaffold(
        topBar = {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(BACKGROUND)
          ) {
            SideBar {
              navController.navigate(BuildToolsRoute.MainMenu.name)
            }
            TopBar(
              currentScreen,
              window
            )
          }
        }
      ) {
        NavHost(
          navController = navController,
          startDestination = BuildToolsRoute.MainMenu.name,
        ) {
          composable(BuildToolsRoute.MainMenu.name) {
            MainMenu()
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

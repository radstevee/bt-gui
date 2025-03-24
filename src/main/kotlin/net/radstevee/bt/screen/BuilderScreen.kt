package net.radstevee.bt.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

public enum class BuilderScreen(
  public val displayName: String,
  public val view: @Composable (NavHostController, NavBackStackEntry, ComposeWindow) -> Unit,
) {
  MainMenu("Home", { nav, back, window -> MainMenu(nav, back, window) }),
}

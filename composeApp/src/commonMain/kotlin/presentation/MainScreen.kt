package presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import presentation.favorites.FavoritesTab
import presentation.home.HomeTab

class MainScreen: Screen {

    @Composable
    override fun Content() {
        Navigator(HomeTab) {
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        BottomNavigationItem(
                            selected = true,
                            onClick = {},
                            icon = {
                                Icon(
                                    painter = rememberVectorPainter(Icons.Default.Home),
                                    contentDescription = "Home"
                                )
                            }
                        )
                        BottomNavigationItem(
                            selected = false,
                            onClick = {},
                            icon = {
                                Icon(
                                    painter = rememberVectorPainter(Icons.Default.Favorite),
                                    contentDescription = "Favorites"
                                )
                            }

                        )
//                        TabNavigationItem(HomeTab)
//                        TabNavigationItem(FavoritesTab)
                    }
                }
            ) { innerPadding ->
                Box(Modifier.padding(innerPadding)) {
                    CurrentScreen()
                }
            }
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        BottomNavigationItem(
            selected = tabNavigator.current == tab,
            onClick = { tabNavigator.current = tab },
            icon = {
                Icon(
                    painter = tab.options.icon ?: rememberVectorPainter(Icons.Default.Home),
                    contentDescription = tab.options.title
                )
            }
        )
    }
}
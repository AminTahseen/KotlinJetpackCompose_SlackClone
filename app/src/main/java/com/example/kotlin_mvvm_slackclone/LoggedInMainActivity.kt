package com.example.kotlin_mvvm_slackclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kotlin_mvvm_slackclone.presentation.signIn.screens.*
import com.example.kotlin_mvvm_slackclone.ui.theme.KotlinMVVMSlackCloneTheme
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackPurpleDark
import com.example.kotlin_mvvm_slackclone.utils.Routes
import com.example.kotlin_mvvm_slackclone.utils.navigation.BottomNavItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoggedInMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinMVVMSlackCloneTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(name = "Home", route = Routes.HOME_ROUTE, icon = R.drawable.home, iconSelected =  R.drawable.home),
                                BottomNavItem(name = "DMs", route = Routes.DM_ROUTE, icon = R.drawable.dms, iconSelected =  R.drawable.dms),
                                BottomNavItem(name = "Mentions", route = Routes.MENTIONS_ROUTE, icon = R.drawable.mentions, iconSelected =  R.drawable.mentions),
                                BottomNavItem(name = "You", route = Routes.PROFILE_ROUTE, icon = R.drawable.you_unselected, iconSelected =  R.drawable.you_selected),
                            ),
                            navController =navController,
                            onItemClick ={
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME_ROUTE,
                        modifier = Modifier.padding(it)
                    ) {
                        composable(Routes.HOME_ROUTE) {
                            MainScreen()
                        }
                        composable(Routes.DM_ROUTE) {
                            DmScreen()
                        }
                        composable(Routes.MENTIONS_ROUTE) {
                           MentionsScreen()
                        }
                        composable(Routes.PROFILE_ROUTE) {
                            ProfileScreen()
                        }
                    }
                }

            }
        }
    }
}
@Composable
fun BottomNavigationBar(
    items:List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick:(BottomNavItem)->Unit
){
    val backStackEntry=navController.currentBackStackEntryAsState()
    BottomNavigation(
        backgroundColor = Color.White,
        elevation = 5.dp
    ) {
        items.forEach {item ->
            val selected=item.route==backStackEntry.value?.destination?.route
            BottomNavigationItem(
                modifier = modifier.padding(5.dp),
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = SlackPurpleDark,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if(selected){
                            Icon(painterResource(item.iconSelected), contentDescription = item.name)
                        }else{
                            Icon(painterResource(item.icon), contentDescription = item.name)
                        }
                        if(selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }else{
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            )
        }

    }
}

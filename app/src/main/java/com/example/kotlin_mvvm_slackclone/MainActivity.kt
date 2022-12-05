package com.example.kotlin_mvvm_slackclone

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlin_mvvm_slackclone.presentation.auth.LoginScreen
import com.example.kotlin_mvvm_slackclone.presentation.auth.LoginUserScreen
import com.example.kotlin_mvvm_slackclone.ui.theme.KotlinMVVMSlackCloneTheme
import com.example.kotlin_mvvm_slackclone.utils.PrefManager
import com.example.kotlin_mvvm_slackclone.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinMVVMSlackCloneTheme {
                val prefManager=PrefManager(MainActivity@this)
                if(prefManager.loggedInUserValue!="null"){
                    finish()
                    val loggedInIntent= Intent(MainActivity@this, LoggedInMainActivity::class.java)
                    startActivity(loggedInIntent)
                }else {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.LOGIN_SCREEN
                    ) {
                        composable(Routes.LOGIN_SCREEN) {
                            LoginScreen(onNavigate = {
                                navController.navigate(it.route)
                            })
                        }
                        composable(
                            route = Routes.LOGIN_VERIFY_USER_SCREEN + "?channelName={channelName}",
                            arguments = listOf(
                                navArgument(name = "channelName") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )) {
                            LoginUserScreen()
                        }
                    }
                }
            }
        }
    }
}


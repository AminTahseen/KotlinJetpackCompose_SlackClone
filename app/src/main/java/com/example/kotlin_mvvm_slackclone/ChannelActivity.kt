package com.example.kotlin_mvvm_slackclone

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlin_mvvm_slackclone.presentation.signIn.screens.AddChannelScreen
import com.example.kotlin_mvvm_slackclone.presentation.signIn.screens.AddThreadPostScreen
import com.example.kotlin_mvvm_slackclone.presentation.signIn.screens.ChannelDetailsScreen
import com.example.kotlin_mvvm_slackclone.presentation.signIn.screens.ThreadDetailsScreen
import com.example.kotlin_mvvm_slackclone.ui.theme.KotlinMVVMSlackCloneTheme
import com.example.kotlin_mvvm_slackclone.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChannelActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KotlinMVVMSlackCloneTheme {
                val intent=intent
                val args=intent.extras?.getInt("channelId")
                val routeArg=intent.extras?.getString("route")
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = if(args===null) {
                        Routes.ADD_CHANNEL_ROUTE
                    }
                    else{
                        routeArg.toString()
                    }
                ) {
                    composable(Routes.ADD_CHANNEL_ROUTE){
                        AddChannelScreen()
                    }
                    composable(Routes.CHANNEL_DETAILS_ROUTE){
                        ChannelDetailsScreen(args!!, onNavigate = {
                            navController.navigate(it.route)
                        })
                    }
                    composable(Routes.ADD_CHANNEL_POST_ROUTE + "?channelId={channelId}",
                        arguments = listOf(
                            navArgument(name = "channelId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )){
                        AddThreadPostScreen(onNavigateBack = {
                            navController.popBackStack()
                        })
                    }
                    composable(Routes.THREAD_POST_DETAILS_ROUTE + "?threadId={threadId}&channelName={channelName}",
                        arguments = listOf(
                            navArgument(name = "threadId") {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(name = "channelName") {
                                type = NavType.StringType
                                defaultValue = ""
                            }
                        )){
                        ThreadDetailsScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

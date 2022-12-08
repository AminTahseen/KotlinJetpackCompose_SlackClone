package com.example.kotlin_mvvm_slackclone.presentation.signIn.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.kotlin_mvvm_slackclone.R
import com.example.kotlin_mvvm_slackclone.common.AppbarChannelInside
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.ChannelDetailsViewModel
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackRed
import com.example.kotlin_mvvm_slackclone.utils.PrefManager
import com.example.kotlin_mvvm_slackclone.presentation.events.UIEvent

@Composable
fun AddThreadPostScreen(
    onNavigateBack: () -> Unit,
    viewModel: ChannelDetailsViewModel = hiltViewModel(),
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val activity = (context as? Activity)
    viewModel.onEvent(ChannelEvents.ShowChannelDetails(viewModel.foundChannelId))
    val subChannel = viewModel.subChannel?.collectAsState(initial = null)
    var imageURI by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageURI = uri
        }
    Log.d("foundChannelId", viewModel.foundChannelId.toString())
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            galleryLauncher.launch("image/*")
        } else {
            // Permission Denied: Do something
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { value: UIEvent ->
            when (value) {
                is UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = value.message,
                    )
                }
                else -> {

                }
            }
        }
    }

    Scaffold(modifier = Modifier, scaffoldState = scaffoldState, topBar = {
        when {
            subChannel?.value != null -> AppbarChannelInside(
                showBackBtn = true,
                screenName = "# ${subChannel.value?.channelName.toString()}",
                subTitle = "${subChannel.value?.channelMembersId?.size} Members",
                functionPerform = onNavigateBack,
                viewModel = viewModel,
                functionPerform2 = {
                    viewModel.onEvent(ChannelEvents.ShowSearchBar(true))
                },
                painterResource(R.drawable.help),
            )
            else -> AppbarChannelInside(
                showBackBtn = true,
                screenName = "Loading...",
                subTitle = "Loading...",
                functionPerform = onNavigateBack,
                viewModel = viewModel,
                functionPerform2 = {
                    viewModel.onEvent(ChannelEvents.ShowSearchBar(true))
                },
                painterResource(R.drawable.help),
            )
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight)
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            TextField(value = viewModel.channelThreadField, modifier = Modifier
                .padding(
                    start = 5.dp, end = 5.dp
                )
                .fillMaxWidth()
                .height(screenHeight * 0.81f), onValueChange = { value ->
                viewModel.onEvent(ChannelEvents.OnChannelThreadDetailsChange(value))
            }, colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ), textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
            ), placeholder = {
                Text(
                    "What's on your mind ?",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
            })
            PostOptions(
                screenHeight,
                screenWidth,
                viewModel,
                viewModel.foundChannelId,
                permissionLauncher,
                galleryLauncher,
                imageURI,
                context
            )
        }
    }
}

@Composable
fun PostOptions(
    screenHeight: Dp,
    screenWidth: Dp,
    viewModel: ChannelDetailsViewModel,
    channelId: Int,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri>,
    imageURI: Uri?,
    context: Context
) {
    val prefManager = PrefManager(LocalContext.current)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .height(screenHeight * 0.1f)
            .background(com.example.kotlin_mvvm_slackclone.ui.theme.SearchBar)
    ) {
        Row(
            modifier = Modifier.width(screenWidth * 0.8f)
        ) {
            when {
                imageURI != null -> {
                    OptionItem(rememberAsyncImagePainter(imageURI)) {

                    }
                }
                else -> {
                    OptionItem(painterResource(R.drawable.add_image), Color.DarkGray) {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context, Manifest.permission.READ_EXTERNAL_STORAGE
                            ) -> {
                                // Some works that require permission
                                Log.d("ExampleScreen", "Code requires permission")
                                galleryLauncher.launch("image/*")
                            }
                            else -> {
                                // Asking for permission
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                    }
                }
            }

            OptionItem(painterResource(R.drawable.emoji), Color.DarkGray) {}
            OptionItem(painterResource(R.drawable.mentions_grey), Color.DarkGray) {}
        }
        Button(
            onClick = {
                viewModel.onEvent(
                    ChannelEvents.OnCreateChannelThread(
                        viewModel.channelThreadField,
                        imageURI,
                        prefManager.userGsonToObj(prefManager.loggedInUserValue).id,
                        channelId
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = SlackRed),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Text(text = "post".uppercase(), color = Color.White)
        }
    }
}

@Composable
fun OptionItem(
    painterResource: Painter, color: Color? = null, functionPerform: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 10.dp)
    ) {
        Image(painterResource,
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(50.dp)
                .padding(10.dp)
                .clickable {
                    functionPerform()
                },
            colorFilter = color?.let { ColorFilter.tint(it) }

        )
    }
}
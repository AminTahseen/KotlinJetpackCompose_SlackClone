package com.example.kotlin_mvvm_slackclone.utils

object Routes {
    const val LOGIN_SCREEN="/login"
    const val LOGIN_VERIFY_USER_SCREEN="/login/verify"
    // Intents routes
    const val LOGGED_IN_INTENT="/loggedIn"
    const val LOGGED_OUT_INTENT="/logout"

    // Bottom nav routes
    const val HOME_ROUTE="/home"
    const val DM_ROUTE="/dm"
    const val MENTIONS_ROUTE="/mentions"
    const val PROFILE_ROUTE="/profile"

    // Channels routes
    const val ADD_CHANNEL_ROUTE="/add-channel"
    const val CHANNEL_DETAILS_ROUTE="/channel-details" // /channel-details/:id
    const val ADD_CHANNEL_POST_ROUTE="/add-channel-post"
    const val THREAD_POST_DETAILS_ROUTE="/thread-details"

}
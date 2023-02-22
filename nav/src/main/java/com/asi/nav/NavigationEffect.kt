package com.asi.nav

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

/**
 * @ClassName NavigationEffect.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:48:00
 */

@Composable
fun NavigationEffect(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val activity = (LocalContext.current as? Activity)
    val flow = NavChannel.navChannel
    LaunchedEffect(activity, navController, flow) {
        flow.collect {
            if (activity?.isFinishing == true) {
                return@collect
            }
            navController.handleComposeNavigationIntent(it)
            navController.backQueue.forEachIndexed { index, navBackStackEntry ->
                Log.e(
                    "NavigationEffects",
                    "index:$index=NavigationEffects: ${navBackStackEntry.destination.route}",
                )
            }
        }
    }
    content()
}
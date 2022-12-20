package com.asi.nav

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

/**
 * @ClassName NavigationEffect.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:48:00
 */

@Composable
fun NavigationEffect(
    navController: NavHostController = rememberNavController(),
    startDestination: String, builder: NavGraphBuilder.() -> Unit,
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
                Log.d(
                    "NavigationEffects",
                    "index:$index=NavigationEffects: ${navBackStackEntry.destination.route}",
                )
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = builder
    )
}


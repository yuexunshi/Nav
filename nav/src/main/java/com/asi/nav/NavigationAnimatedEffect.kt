package com.asi.nav

import android.app.Activity
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


/**
 * @ClassName NavigationEffect.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:48:00
 */

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationAnimatedEffect(
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: String,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    route: String? = null,
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition) =
        {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition) =
        {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec =tween(durationMillis = 300))
        },
    popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition) =
        {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
    popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition) =
        {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        },
    builder: NavGraphBuilder.() -> Unit,
) {
    val activity = (LocalContext.current as? Activity)
    val navChannel = NavChannel.navChannel
    LaunchedEffect(activity, navController, navChannel) {
        navChannel.collect {
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
    AnimatedNavHost(
        navController,
        startDestination = startDestination,
        modifier = modifier,
        contentAlignment = contentAlignment,
        route = route,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        builder = builder
    )
}





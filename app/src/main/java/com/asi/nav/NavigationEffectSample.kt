package com.asi.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import com.asi.nav.model.User
import com.asi.nav.page.TwoScreen
import com.asi.navsample.nav.*
import com.asi.navsample.page.FiveScreen
import com.asi.navsample.page.FourScreen
import com.asi.navsample.page.OneScreen
import com.asi.navsample.page.ThreeScreen

/**
 * @ClassName NavigationEffectSample.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2023年02月22日 16:12:00
 */


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationEffectSample() {
    NavigationEffect(startDestination = OneDestination.route) {
        composable(OneDestination.route) { OneScreen() }
        composable(TwoDestination.route) { TwoScreen() }
        composable(FourDestination.route, arguments = FourDestination.arguments) {
            val user = it.arguments?.getParcelable<User>(FourDestination.ARG)
                ?: return@composable
            FourScreen(user)
        }
        composable(ThreeDestination.route, arguments = ThreeDestination.arguments) {
            val channelId =
                it.arguments?.getString(ThreeDestination.ARG) ?: return@composable
            ThreeScreen(channelId)
        }

        composable(FiveDestination.route, arguments = FiveDestination.arguments) {
            val age =
                it.arguments?.getInt(FiveDestination.ARG_AGE) ?: return@composable
            val name =
                it.arguments?.getString(FiveDestination.ARG_NAME)
                    ?: return@composable
            FiveScreen(age, name)
        }
    }

}

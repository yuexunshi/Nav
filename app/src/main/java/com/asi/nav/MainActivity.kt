package com.asi.nav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.asi.nav.ui.theme.NavTheme
import com.asi.nav.model.User
import com.asi.navsample.nav.*
import com.asi.navsample.page.*
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    NavigationEffect(OneDestination.route) {
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
            }
        }
    }
}


package com.asi.navsample.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.asi.nav.Nav
import com.asi.nav.model.User
import com.asi.navsample.nav.FiveDestination
import com.asi.navsample.nav.FourDestination
import com.asi.navsample.nav.ThreeDestination
import com.asi.navsample.nav.TwoDestination

/**
 * @ClassName OneScreen.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月21日 11:00:00
 */

@Composable
fun OneScreen() {
    BackHandler(onBack = {
        Log.e("==", "OneScreen:BackHandler ")
    })

    Column {
        Text(text = "OneScreen")

        Button(onClick = {
            Nav.to(TwoDestination.route)
        }) {
            Text(text = "去TwoScreen")
        }
        Button(onClick = {
            Nav.to(ThreeDestination("来自首页"))
        }) {
            Text(text = "去ThreeScreen")
        }
        Button(onClick = {
            Nav.to(FourDestination(User("来着首页", "110")))
        }) {
            Text(text = "去FourScreen")
        }
        Button(onClick = {
            Nav.to(FiveDestination(20, "来自首页"))
        }) {
            Text(text = "去FiveScreen")
        }
    }
}


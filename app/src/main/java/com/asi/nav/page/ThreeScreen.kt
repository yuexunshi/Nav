package com.asi.navsample.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.asi.nav.Nav
import com.asi.nav.model.User
import com.asi.navsample.nav.FourDestination
import com.asi.navsample.nav.OneDestination
import com.asi.navsample.nav.ThreeDestination


/**
 * @ClassName OneScreen.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月21日 11:00:00
 */

@Composable
fun ThreeScreen(id: String) {

    Column() {
        Text(text = "ThreeScreen")
        Text(text = "id=$id")
        Button(onClick = {
            Nav.offAllTo(OneDestination.route)
        }) {
            Text(text = "回到OneScreen")
        }
        Button(onClick = {
            Nav.to(FourDestination(User("来自Three", "110")))
        }) {
            Text(text = "去FourScreen")
        }
        Button(onClick = {
            Nav.to(route=FourDestination(User("replace来自Three", "110")),popUpToRoute=ThreeDestination.route,inclusive = true)
        }) {
            Text(text = "replaceFourScreen")
        }


    }
}
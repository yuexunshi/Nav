package com.asi.navsample.page


import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.asi.nav.Nav
import com.asi.nav.model.User
import com.asi.navsample.nav.OneDestination
import com.asi.navsample.nav.TwoDestination

/**
 * @ClassName OneScreen.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月21日 11:00:00
 */

@Composable
fun FourScreen(user: User) {

    Column() {
        Text(text = "FourScreen")
        Text(text = "name=${user.name}")
        Button(onClick = {
            Nav.offAllTo(TwoDestination.route)
        }) {
            Text(text = "offUntilTwoScreen")
        }
        Button(onClick = {
            Nav.offAllTo(OneDestination.route)
        }) {
            Text(text = "offUntilOneScreen")
        }
    }

}
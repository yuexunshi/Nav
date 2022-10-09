package com.asi.navsample.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.asi.nav.Nav
import com.asi.navsample.nav.OneDestination


/**
 * @ClassName OneScreen.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月21日 11:00:00
 */

@Composable
fun FiveScreen(age: Int, name: String) {

    Column() {
        Text(text = "FiveScreen")
        Text(text = "age=$age||name=$name")
        Button(onClick = {
            Nav.offAllTo(OneDestination.route)
        }) {
            Text(text = "回到OneScreen")
        }
        Button(onClick = {
            Nav.back()
        }) {
            Text(text = "back")
        }
    }
}
package com.asi.navsample.model

import android.os.Bundle
import androidx.navigation.NavType
import com.asi.nav.model.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * @ClassName NavUserType.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月23日 15:01:00
 */

class NavUserType : NavType<User>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): User? =
        bundle.getParcelable(key)

    override fun put(bundle: Bundle, key: String, value: User) =
        bundle.putParcelable(key, value)

    override fun parseValue(value: String): User {
        return Json.decodeFromString(value)
    }

}
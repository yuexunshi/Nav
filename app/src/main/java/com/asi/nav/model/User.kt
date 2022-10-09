package com.asi.nav.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * @ClassName User.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月23日 14:56:00
 */

@Parcelize
@kotlinx.serialization.Serializable
data class User(
    val name: String,
    val phone: String,
) : Parcelable{
    override fun toString(): String {
        return Uri.encode(Json.encodeToString(this))
    }
}
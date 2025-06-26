package io.mohammedalaamorsi.nyt.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import io.mohammedalaamorsi.nyt.data.models.Result
import kotlinx.serialization.json.Json

object ResultNavType : NavType<Result>(isNullableAllowed = false) {
    
    override fun get(bundle: Bundle, key: String): Result? {
        return bundle.getParcelable(key)
    }
    
    override fun parseValue(value: String): Result {
        return Json.decodeFromString<Result>(Uri.decode(value))
    }
    
    override fun put(bundle: Bundle, key: String, value: Result) {
        bundle.putParcelable(key, value)
    }
    
    override fun serializeAsValue(value: Result): String {
        return Uri.encode(Json.encodeToString(value))
    }
}

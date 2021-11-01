package com.example.movieapp.util

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import androidx.annotation.NonNull


@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @NonNull
    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return context.getString(resId, *formatArgs)
    }
}
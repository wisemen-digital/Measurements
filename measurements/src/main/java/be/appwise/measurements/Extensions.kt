package be.appwise.measurements

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast


@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
val isAtLeastO: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
val isAtLeastR: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
val isAtLeastP: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
val isAtLeastN: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

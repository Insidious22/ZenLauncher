package com.insidious22.zenlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.insidious22.zenlauncher.domain.AppModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppsRepository(private val context: Context) {

    private val iconCache = LruCache<String, ImageBitmap>(220)

    suspend fun getInstalledApps(): List<AppModel> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)

        val apps = if (Build.VERSION.SDK_INT >= 33) {
            pm.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            pm.queryIntentActivities(intent, 0)
        }

        val sizePx = (48f * context.resources.displayMetrics.density).toInt().coerceAtLeast(48)

        apps.mapNotNull { resolveInfo ->
            val packageName = resolveInfo.activityInfo.packageName
            val label = resolveInfo.loadLabel(pm)?.toString()?.trim().orEmpty()
            if (label.isBlank()) return@mapNotNull null

            val icon = iconCache.get(packageName) ?: run {
                val drawable = resolveInfo.loadIcon(pm)
                val bmp = drawableToBitmap(drawable, sizePx, sizePx).asImageBitmap()
                iconCache.put(packageName, bmp)
                bmp
            }

            AppModel(
                packageName = packageName,
                label = label,
                icon = icon
            )
        }.sortedBy { it.label.lowercase() }
    }

    private fun drawableToBitmap(drawable: Drawable, w: Int, h: Int): Bitmap {
        if (drawable is BitmapDrawable && drawable.bitmap != null) {
            return Bitmap.createScaledBitmap(drawable.bitmap, w, h, true)
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }
}
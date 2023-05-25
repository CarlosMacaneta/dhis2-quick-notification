package org.saudigitus.quicknotification.ui.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.TypedValue
import android.view.WindowManager
import org.saudigitus.quicknotification.R
import timber.log.Timber

object Utils {

    val mimeTypes = arrayOf(
        "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
        "application/pdf", "image/png", "image/jpeg"
    )

    fun intentFileChooser(): Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

        return intent
    }
    fun capitalizeText(text: String): String {
        return text.lowercase()
            .replaceFirstChar {
                it.uppercase()
            }
    }

    fun isOnline(context: Context): Boolean {
        var isOnline = false
        try {
            val manager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val netInfo = manager.activeNetworkInfo
            isOnline = netInfo != null && netInfo.isConnectedOrConnecting
        } catch (ex: Exception) {
            Timber.e(ex)
        }
        return isOnline
    }

    fun setProgramTheme(activity: Activity, theme: Int) {
        activity.theme.applyStyle(theme, true)

        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val typedValue = TypedValue()
        val a = activity.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimaryDark))
        val colorToReturn = a.getColor(0, 0)
        a.recycle()
        window.statusBarColor = colorToReturn
    }
}
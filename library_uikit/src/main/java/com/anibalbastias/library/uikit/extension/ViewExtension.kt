package com.anibalbastias.library.uikit.extension

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.anibalbastias.library.uikit.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.ByteArrayOutputStream


fun dpToPx(dp: Float): Int =
    (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        Resources.getSystem().displayMetrics
    )).toInt()

fun dpToPx(dp: Int) = dpToPx(dp.toFloat())

fun Toolbar.applyFontForToolbarTitle(context: Activity, color: Int = R.color.primaryColor) {
    for (i in 0 until this.childCount) {
        val view = this.getChildAt(i)
        if (view is TextView) {
            try {
                val titleFont = ResourcesCompat.getFont(context, R.font.opensans_regular)
                val subtitleFont = ResourcesCompat.getFont(context, R.font.opensans_regular)
                if (view.text == this.title) {
                    view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f)
                    view.setTextColor(resources.getColor(color))
                    view.typeface = titleFont
                } else if (view.text == this.subtitle) {
                    view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f)
                    view.setTextColor(resources.getColor(color))
                    view.typeface = subtitleFont
                    break
                }
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }
        }
    }
    (context as AppCompatActivity).setSupportActionBar(this)
}

fun Toolbar.setArrowUpToolbar(context: Activity) {
    (context as AppCompatActivity).setSupportActionBar(this)
    context.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
}

fun Toolbar.setNoArrowUpToolbar(context: Activity) {
    (context as AppCompatActivity).setSupportActionBar(this)
    context.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
}

fun shareImageFromURI(url: String?, bitmapBlock: (bitmap: Bitmap?) -> Unit) {
    Picasso.get().load(url).into(object : Target {
        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            bitmapBlock.invoke(bitmap)
        }
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {}
    })
}

fun Context.getImageUri(inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(
        contentResolver,
        inImage,
        "Title",
        null
    )
    return Uri.parse(path)
}
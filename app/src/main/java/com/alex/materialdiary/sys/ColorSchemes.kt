package com.alex.materialdiary.sys

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import com.alex.materialdiary.R

class ColorSchemes(val context: Context) {
    var currentScheme = 1
    init {
        instance = this
        currentScheme = DiaryPreferences.getInstance().getInt("color_scheme")
    }
    @ColorInt
    fun getColorArr(@ColorRes ids: List<Int>): List<Int> {
        return ids.map { ContextCompat.getColor(context, it) }
    }
    companion object {
        var instance: ColorSchemes? = null
       fun getInstance(context: Context): ColorSchemes{
            if (instance == null) return ColorSchemes(context)
            else return instance as ColorSchemes
        }
    }
    @ColorInt
    fun getCurrentScheme(sch: Int? = currentScheme): List<Int>{
        return when(sch){
            1 -> Excelent
            2 -> Classic
            3 -> Material
            null -> getCurrentScheme(currentScheme)
            else -> Excelent
        }
    }
    val Excelent = getColorArr(listOf(R.color.one_tint, R.color.two_tint, R.color.three_tint, R.color.four_tint, R.color.five_tint))
    val Classic = getColorArr(listOf(R.color.one2_tint, R.color.two2_tint, R.color.three2_tint, R.color.four2_tint, R.color.five2_tint))
    val Material = getColorArr(listOf(R.color.one3_tint, R.color.two3_tint, R.color.three3_tint, R.color.four3_tint, R.color.five3_tint))
}
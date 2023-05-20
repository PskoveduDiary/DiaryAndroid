package com.alex.materialdiary.sys.adapters

import android.view.View
import android.widget.TextView
import com.alex.materialdiary.R
import com.alex.materialdiary.sys.net.models.diary_day.Absence

class ProgramViewHolderDiary internal constructor(v: View) {
    @JvmField
    var Num: TextView
    @JvmField
    var Time: TextView
    @JvmField
    var Name: TextView
    @JvmField
    var Teacher: TextView
    @JvmField
    var Mark: TextView
    @JvmField
    var HomeWork: TextView
    @JvmField
    var Absence: TextView

    // Get the handles by calling findViewById() on View object inside the constructor
    init {
        Num = v.findViewById(R.id.lesson_Num)
        Time = v.findViewById(R.id.lesson_Time)
        Name = v.findViewById(R.id.lesson_Name)
        Mark = v.findViewById(R.id.lesson_Mark)
        Teacher = v.findViewById(R.id.lesson_Teacher)
        HomeWork = v.findViewById(R.id.lesson_Homework)
        Absence = v.findViewById(R.id.lesson_Absence)
    }
}
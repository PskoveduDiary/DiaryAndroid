package com.alex.materialdiary.sys.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.materialdiary.R;

public class ProgramViewHolderDiary {

    TextView Num;
    TextView Time;
    TextView Name;
    TextView Teacher;
    TextView Mark;
    TextView HomeWork;
    // Get the handles by calling findViewById() on View object inside the constructor
    ProgramViewHolderDiary(View v)
    {
        Num = v.findViewById(R.id.lesson_Num);
        Time = v.findViewById(R.id.lesson_Time);
        Name = v.findViewById(R.id.lesson_Name);
        Mark = v.findViewById(R.id.lesson_Mark);
        Teacher = v.findViewById(R.id.lesson_Teacher);
        HomeWork = v.findViewById(R.id.lesson_Homework);
    }
}

package com.alex.materialdiary.sys.adapters;

import android.view.View;
import android.widget.TextView;

import com.alex.materialdiary.R;

public class ProgramViewHolderUsers {

    TextView Grade;
    TextView Name;
    TextView SchoolName;
    // Get the handles by calling findViewById() on View object inside the constructor
    ProgramViewHolderUsers(View v)
    {
        Grade = v.findViewById(R.id.user_Grade);
        Name = v.findViewById(R.id.user_Name);
        SchoolName = v.findViewById(R.id.user_SchoolName);
    }
}

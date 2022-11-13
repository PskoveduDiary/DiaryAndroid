package com.alex.materialdiary.sys.adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.alex.materialdiary.R;

public class ProgramViewHolderUsers {

    TextView Grade;
    TextView Name;
    TextView SchoolName;
    AppCompatImageView scan;
    // Get the handles by calling findViewById() on View object inside the constructor
    ProgramViewHolderUsers(View v)
    {
        Grade = v.findViewById(R.id.user_Grade);
        Name = v.findViewById(R.id.user_Name);
        SchoolName = v.findViewById(R.id.user_SchoolName);
        scan = v.findViewById(R.id.scanQr);
    }
}

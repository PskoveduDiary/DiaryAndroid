package com.alex.materialdiary.sys.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alex.materialdiary.R;
import com.treeview.TreeNode;
import com.treeview.TreeViewHolder;


public class HolderAvailableContacts extends TreeViewHolder {

    private TextView fileName;
    private ImageView fileStateIcon;
    private ImageView fileTypeIcon;

    public HolderAvailableContacts(@NonNull View itemView) {
        super(itemView);
        initViews();
    }

    private void initViews() {
        fileName = itemView.findViewById(R.id.NameofContact);
        fileStateIcon = itemView.findViewById(R.id.collapseIcon);
        fileTypeIcon = itemView.findViewById(R.id.iconAvContact);
    }

    @Override
    public void bindTreeNode(TreeNode node) {
        super.bindTreeNode(node);

        String fileNameStr = node.getValue().toString();
        fileName.setText(fileNameStr);
        fileTypeIcon.setImageResource(R.drawable.folder);

        if (node.getChildren().isEmpty()) {
            fileStateIcon.setVisibility(View.INVISIBLE);
            fileTypeIcon.setImageResource(R.drawable.user);
        } else {
            fileStateIcon.setVisibility(View.VISIBLE);
            int stateIcon = node.isExpanded() ? R.drawable.ic_baseline_keyboard_arrow_down_24 : R.drawable.ic_baseline_keyboard_arrow_right_24;
            fileStateIcon.setImageResource(stateIcon);
        }
    }
}


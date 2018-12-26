package com.javierhelguera.mytaskslist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class addListDialog extends AlertDialog.Builder {

    public interface addListDialogListener {
        public abstract void onOK(String number);

        public abstract void onCancel(String number);
    }

    private EditText mtextEdit;

    public addListDialog(Activity activity, final addListDialogListener listener) {
        super(new ContextThemeWrapper(activity, R.style.AppTheme));

        @SuppressLint("InflateParams")
                View dialogLayout = LayoutInflater.from(activity).inflate(R.layout.dialog_add_list, null);
        setView(dialogLayout);

        mtextEdit = dialogLayout.findViewById(R.id.textEdit);

        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (listener != null)
                    listener.onOK(String.valueOf(mtextEdit.getText()));

            }
        });

        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (listener != null)
                    listener.onCancel(String.valueOf(mtextEdit.getText()));
            }
        });
    }


    @Override
    public AlertDialog show() {
        AlertDialog dialog = super.show();
        Window window = dialog.getWindow();
        if (window != null)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }
}

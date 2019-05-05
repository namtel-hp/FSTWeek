package com.qerat.fstweek;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class YesNoAlertDialog {
    private Activity context;
    private String title, msg;
    YesNoAlertDialog(Activity context, String title, String msg){
        this.context=context;
        this.title=title;
        this.msg=msg;
    }

    public void Show(){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        YesNoAlertDialog.this.context.finish();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }
}

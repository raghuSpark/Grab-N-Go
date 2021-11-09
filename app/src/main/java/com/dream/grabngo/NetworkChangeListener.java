package com.dream.grabngo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layoutView =
                LayoutInflater.from(context).inflate(R.layout.no_internet_popup_dialog, null);
        builder.setView(layoutView);

        Button retryButton = layoutView.findViewById(R.id.no_internet_refresh_button);

        AlertDialog dialog = builder.create();
        if (CheckInternet.isConnectedToInternet(context)) {
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setWindowAnimations(R.style.PopupDialogAnimation);
            dialog.setCancelable(false);
            dialog.show();

            retryButton.setOnClickListener(v -> {
                dialog.dismiss();
                onReceive(context, intent);
            });
        } else {
            if (dialog.isShowing()) dialog.dismiss();
        }
    }
}

package grodriguez.com.deliveriutest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.listeners.OnConfirmationDialogClickListener;
import grodriguez.com.deliveriutest.utils.FieldValidator;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class ConfirmationDialog extends Dialog implements View.OnClickListener {

    private final String LOG_TAG = getClass().getSimpleName();

    private OnConfirmationDialogClickListener onConfirmationDialogClickListener;

    public ConfirmationDialog(Context context, String message,
                              OnConfirmationDialogClickListener onConfirmationDialogClickListener) {
        super(context);
        this.onConfirmationDialogClickListener = onConfirmationDialogClickListener;
        showDialog(null, message, null, null, false);
    }

    public ConfirmationDialog(Context context, String title, String message,
                              OnConfirmationDialogClickListener onConfirmationDialogClickListener) {
        super(context);
        this.onConfirmationDialogClickListener = onConfirmationDialogClickListener;
        showDialog(title, message, null, null, false);
    }

    public ConfirmationDialog(Context context, String buttonYes, String buttonNo,
                              String message, OnConfirmationDialogClickListener onConfirmationDialogClickListener) {
        super(context);
        this.onConfirmationDialogClickListener = onConfirmationDialogClickListener;
        showDialog(null, message, buttonYes, buttonNo, false);
    }

    public ConfirmationDialog(Context context, String title, String buttonYes, String buttonNo,
                              String message, OnConfirmationDialogClickListener onConfirmationDialogClickListener) {
        super(context);
        this.onConfirmationDialogClickListener = onConfirmationDialogClickListener;
        showDialog(title, message, buttonYes, buttonNo, false);
    }

    public ConfirmationDialog(Context context, String message, boolean buttonDisappear,
                              OnConfirmationDialogClickListener onConfirmationDialogClickListener) {
        super(context);
        this.onConfirmationDialogClickListener = onConfirmationDialogClickListener;
        showDialog(null, message, null, null, buttonDisappear);
    }


    private void showDialog(String title, String message, String buttonYes, String buttonNo,
                            boolean buttonDisappear) {
        if (FieldValidator.isValid(message)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_confirmation);
            setCancelable(false);

            if (FieldValidator.isValid(title)) {
                TextView confirmationTitle = (TextView) findViewById(R.id.title);
                confirmationTitle.setText(title);
            }

            TextView confirmationMessage = (TextView) findViewById(R.id.message);
            confirmationMessage.setText(message);

            Button yes = (Button) findViewById(R.id.yes);
            Button no = (Button) findViewById(R.id.no);

            if (FieldValidator.isValid(buttonYes))
                yes.setText(buttonYes);
            if (FieldValidator.isValid(buttonNo))
                no.setText(buttonNo);
            if (buttonDisappear)
                no.setVisibility(View.GONE);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

            show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                if (onConfirmationDialogClickListener != null) {
                    onConfirmationDialogClickListener.onConfirm();
                    dismiss();
                }
                break;
            case R.id.no:
                if (onConfirmationDialogClickListener != null) {
                    onConfirmationDialogClickListener.onRelease();
                    dismiss();
                }
                dismiss();
                break;
            default:
                Log.wtf(LOG_TAG, "No button");
        }
    }
}

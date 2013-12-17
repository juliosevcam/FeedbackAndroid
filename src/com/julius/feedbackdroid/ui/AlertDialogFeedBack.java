
package com.julius.feedbackdroid.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.julius.feedbackdroid.R;
import com.julius.feedbackdroid.mail.SendEmailTask;
import com.julius.feedbackdroid.utils.Configuration;

/**
 * Dialog to send feedback
 * 
 * @author julio
 */
public class AlertDialogFeedBack extends DialogFragment {

    public static final String CONFIGURATION = "configuration";
    private View detailView;

    public static AlertDialogFeedBack newInstance(Configuration configuration) {
        AlertDialogFeedBack frag = new AlertDialogFeedBack();
        Bundle args = new Bundle();
        args.putParcelable(CONFIGURATION, configuration);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        this.detailView = inflater.inflate(R.layout.dialog_feedback, null);

        builder.setView(this.detailView)
                .setTitle(R.string.feed_back_form_title)

                .setPositiveButton(R.string.button_send_feeback,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // La implementaciónd de la funcionalidad del
                                // PositiveButton la hago en el onResume
                            }
                        })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        final AlertDialog dialog = (AlertDialog) getDialog();

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View onClick) {
                    final EditText txtFeedback = (EditText) detailView
                            .findViewById(R.id.txtFeedback);
                    final RadioGroup radioGroup = (RadioGroup) detailView
                            .findViewById(R.id.radioTypeRequest);
                    final EditText txtUserEmail = (EditText) detailView
                            .findViewById(R.id.txtUserEmail);

                    if (txtFeedback != null && txtFeedback.getText() != null) {
                        final String userMessage = txtFeedback.getText().toString();
                        // Mostramos mensaje de que el texto es obligatorio
                        if (userMessage == null || userMessage.length() == 0) {
                            Toast.makeText(getActivity(),
                                    getString(R.string.feed_back_form_no_text), Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else if (txtUserEmail.getText() == null
                                || txtUserEmail.getText().toString().equals("")) {
                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            AlertDialog.Builder builderConfirmEmail = new AlertDialog.Builder(
                                    getActivity());

                            builderConfirmEmail
                                    .setTitle(R.string.feed_back_form_email_empty_title)
                                    .setView(
                                            inflater.inflate(R.layout.dialog_feedback_no_email,
                                                    null))
                                    .setPositiveButton(R.string.button_send_feeback,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogEmail,
                                                        int which) {
                                                    sendEmail(radioGroup, userMessage, txtUserEmail
                                                            .getText().toString());
                                                    dialogEmail.dismiss();
                                                    dialog.dismiss();
                                                }
                                            })
                                    .setNegativeButton(R.string.button_send_feeback_review,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                        int which) {
                                                    dialog.cancel();
                                                }
                                            });
                            builderConfirmEmail.create().show();
                        }
                        else if (txtUserEmail.getText() != null
                                && !txtUserEmail.getText().toString().equals("")) {
                            boolean validMail = isEmailValid(txtUserEmail.getText().toString());
                            // Correct format send email
                            if (validMail) {
                                sendEmail(radioGroup, userMessage, txtUserEmail.getText()
                                        .toString());
                                dialog.dismiss();
                            }
                            else {
                                // Wrong email format
                                Toast.makeText(getActivity(),
                                        getString(R.string.feed_back_form_wrong_mail_format),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * Call asyncTask to send email
     * @param radioGroup
     * @param userMessage
     * @param userEmail
     */
    public void sendEmail(RadioGroup radioGroup, String userMessage, String userEmail) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String typeRequest = "";

        //Type of report
        if (selectedId == R.id.radioBug) {
            typeRequest = getActivity().getString(R.string.feed_back_form_radio_bug);
        } else if (selectedId == R.id.radioIdea) {
            typeRequest = getActivity().getString(R.string.feed_back_form_radio_idea);
        }
        else if (selectedId == R.id.radioQuestion) {
            typeRequest = getActivity().getString(R.string.feed_back_form_radio_question);
        }

        ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        if (userEmail != null && !userEmail.equals("")) {
            userMessage = userMessage + "\n " + getString(R.string.user_email_set) + " " + userEmail;
        }
        else
            userMessage = userMessage + "\n " + getString(R.string.user_email_not_set);
        
        Configuration configuration = getArguments().getParcelable(CONFIGURATION);
        new SendEmailTask(getActivity(), pDialog, userMessage, typeRequest, userEmail, configuration).execute();
    }

    /**
     * Check email format
     * @param email
     * @return True if email is valid
     */
    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

package com.julius.feedbackdroid.mail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.julius.feedbackdroid.R;
import com.julius.feedbackdroid.utils.Configuration;

/**
 * Task to send an email in background
 * @author julio
 *
 */
public class SendEmailTask extends AsyncTask<Void, Void, Void>{
    private ProgressDialog pDialog;
    private Activity activity;
    //Text send by the user
    private String userMessage;
    //Email type: But, comment or suggestion
    private String userTypeRequest; 
    //Email of the user
    private String userEmail;
    private Configuration configuration;
    
    public SendEmailTask(Activity activity, ProgressDialog progress, String userMessage, String userType, String email, Configuration configuration){
        this.pDialog = progress;
        this.activity = activity;
        this.userMessage = userMessage;
        this.userTypeRequest = userType;
        this.userEmail = email;
        this.configuration = configuration;
    }
    
    @Override
    protected void onPreExecute(){
        if (this.pDialog!=null && activity!=null && !this.pDialog.isShowing()){
            this.pDialog.setMessage(activity.getString(R.string.feed_back_sending));
            this.pDialog.show();
        }
    }
    
    @Override
    protected Void doInBackground(Void... params) {
        try {   
            GMailSender sender = new GMailSender(this.configuration.getFeedbackUsername(), this.configuration.getFeedbackPassword());
            String senderUser="";
            if (this.userEmail==null || this.userEmail.equals("")){
                senderUser = this.configuration.getFeedbackFrom();
            }
            else{
                senderUser = this.userEmail;
            }
                
            sender.sendMail(activity.getString(R.string.feed_email_subject) + ": " + this.userTypeRequest,   
                    this.userMessage,   
                    senderUser,   
                    this.configuration.getFeedbackFrom());   
        } catch (Exception e) {   
            Log.e("SendMail", e.getMessage(), e);   
        }

        return null;
    }
    
    @Override
    protected void onPostExecute(Void result){
        if (this.pDialog!=null && this.pDialog.isShowing()){
            this.pDialog.dismiss();
            Toast.makeText(activity, activity.getString(R.string.feed_back_send_thanks), Toast.LENGTH_SHORT).show();
        }
    }
}


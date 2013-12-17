
package com.julius.feedbackdroid.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Configuration implements Parcelable {
    private String feedbackFrom;
    private String feedbackUsername;
    private String feedbackPassword;
    private String title;

    public Configuration(String feedbackFrom, String feedbackUsername, String feedbackPassword, String title) {
        super();
        this.feedbackFrom = feedbackFrom;
        this.feedbackUsername = feedbackUsername;
        this.feedbackPassword = feedbackPassword;
        this.title = title;
    }

    public String getFeedbackFrom() {
        return feedbackFrom;
    }

    public void setFeedbackFrom(String feedbackFrom) {
        this.feedbackFrom = feedbackFrom;
    }

    public String getFeedbackUsername() {
        return feedbackUsername;
    }

    public void setFeedbackUsername(String feedbackUsername) {
        this.feedbackUsername = feedbackUsername;
    }

    public String getFeedbackPassword() {
        return feedbackPassword;
    }

    public void setFeedbackPassword(String feedbackPassword) {
        this.feedbackPassword = feedbackPassword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(feedbackFrom);
        dest.writeString(feedbackUsername);
        dest.writeString(feedbackPassword);
        dest.writeString(title);
    }

    public Configuration(Parcel in) {
        feedbackFrom = in.readString();
        feedbackUsername = in.readString();
        feedbackPassword = in.readString();
        title = in.readString();
    }

    public static final Configuration.Creator<Configuration> CREATOR = new Parcelable.Creator<Configuration>() {
        public Configuration createFromParcel(Parcel in) {
            return new Configuration(in);
        }

        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };

}

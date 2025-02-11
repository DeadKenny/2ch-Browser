package com.vortexwolf.dvach.models.domain;

import java.io.File;

public class PostEntity {
    private String mCaptchaKey;
    private String mCaptchaAnswer;
    private String mComment;
    private boolean mIsSage;
    private File mAttachment;
    private String mSubject;
    private String mPolitics;
    private String mName;
    private String mVideo;

    public PostEntity(String captchaKey, String captchaAnswer, String comment, boolean isSage, File attachment, String subject, String politics, String name, String video) {
        this.mCaptchaKey = captchaKey;
        this.mCaptchaAnswer = captchaAnswer;
        this.mComment = comment;
        this.mIsSage = isSage;
        this.mAttachment = attachment;
        this.mSubject = subject;
        this.mPolitics = politics;
        this.mName = name;
        this.mVideo = video;
    }

    public void setCaptchaKey(String captchaKey) {
        this.mCaptchaKey = captchaKey;
    }

    public String getCaptchaKey() {
        return mCaptchaKey;
    }

    public void setCaptchaAnswer(String captchaAnswer) {
        this.mCaptchaAnswer = captchaAnswer;
    }

    public String getCaptchaAnswer() {
        return mCaptchaAnswer;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public String getComment() {
        return mComment;
    }

    public void setSage(boolean isSage) {
        this.mIsSage = isSage;
    }

    public boolean isSage() {
        return mIsSage;
    }

    public void setAttachment(File attachment) {
        this.mAttachment = attachment;
    }

    public File getAttachment() {
        return mAttachment;
    }

    public void setSubject(String subject) {
        this.mSubject = subject;
    }

    public String getSubject() {
        return mSubject;
    }

    public String getPolitics() {
        return mPolitics;
    }

    public void setPolitics(String politics) {
        this.mPolitics = politics;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getVideo() {
        return mVideo;
    }

    public void setVideo(String video) {
        this.mVideo = video;
    }
}

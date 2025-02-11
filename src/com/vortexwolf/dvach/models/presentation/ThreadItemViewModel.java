package com.vortexwolf.dvach.models.presentation;

import android.content.res.Resources.Theme;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.vortexwolf.dvach.common.utils.HtmlUtils;
import com.vortexwolf.dvach.common.utils.StringUtils;
import com.vortexwolf.dvach.common.utils.ThreadPostUtils;
import com.vortexwolf.dvach.models.domain.PostInfo;
import com.vortexwolf.dvach.models.domain.ThreadInfo;
import com.vortexwolf.dvach.services.presentation.DvachUriBuilder;

public class ThreadItemViewModel {

    private final Theme mTheme;
    private final PostInfo mOpPost;
    private final int mReplyCount;
    private final int mImageCount;
    private final DvachUriBuilder mDvachUriBuilder;

    private SpannableStringBuilder mSpannedComment = null;
    private AttachmentInfo mAttachment = null;
    private boolean mEllipsized = false;
    private boolean mHidden = false;

    public ThreadItemViewModel(ThreadInfo model, Theme theme, DvachUriBuilder dvachUriBuilder) {
        this.mTheme = theme;
        this.mDvachUriBuilder = dvachUriBuilder;

        this.mOpPost = model.getPosts()[0];
        this.mReplyCount = model.getReplyCount();
        this.mImageCount = model.getImageCount();
    }

    public SpannableStringBuilder getSpannedComment() {
        if (this.mSpannedComment == null) {
            String fixedComment = HtmlUtils.fixHtmlTags(this.mOpPost.getComment());
            Spanned spanned = HtmlUtils.createSpannedFromHtml(fixedComment, this.mTheme);
            this.mSpannedComment = (SpannableStringBuilder) spanned;
        }

        return mSpannedComment;
    }

    public String getSubject() {
        return this.mOpPost.getSubject();
    }

    public boolean hasAttachment() {
        return ThreadPostUtils.hasAttachment(this.mOpPost);
    }

    public AttachmentInfo getAttachment(String boardCode) {
        if (this.mAttachment == null && this.hasAttachment()) {
            this.mAttachment = new AttachmentInfo(this.mOpPost, boardCode, this.mDvachUriBuilder);
        }

        return mAttachment;
    }

    public PostInfo getOpPost() {
        return this.mOpPost;
    }

    public String getNumber() {
        return this.mOpPost.getNum();
    }

    public int getReplyCount() {
        return mReplyCount;
    }

    public int getImageCount() {
        return mImageCount;
    }

    public void setEllipsized(boolean ellipsized) {
        this.mEllipsized = ellipsized;
    }

    public boolean isEllipsized() {
        return mEllipsized;
    }

    public boolean isHidden() {
        return mHidden;
    }

    public void setHidden(boolean hidden) {
        this.mHidden = hidden;
    }
}

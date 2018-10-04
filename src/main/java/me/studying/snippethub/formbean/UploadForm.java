package me.studying.snippethub.formbean;

import java.util.Date;

public class UploadForm {

    private Long snippet_id;
    private Long PL_id;
    private String snippet_name;
    private Long user_id;
    private Date upload_date;
    private int like_count;
    private boolean approved;
    private String code_text;
    private String tags;

    public UploadForm() {

    }

    public UploadForm(Long snippet_id, Long PL_id,
                      String snippet_name, Long user_id, Date upload_date,
                      int like_count, boolean approved, String code_text, String tags) {
        this.snippet_id = snippet_id;
        this.PL_id = PL_id;
        this.snippet_name = snippet_name;
        this.user_id = user_id;
        this.upload_date = upload_date;
        this.like_count = like_count;
        this.approved = approved;
        this.code_text = code_text;
        this.tags = tags;
    }

    public Long getSnippetId() {
        return snippet_id;
    }

    public void setSnippetId(Long snippet_id) {
        this.snippet_id = snippet_id;
    }

    public Long getPLID() {
        return PL_id;
    }

    public void setPLID(Long PL_name) {
        this.PL_id = PL_name;
    }

    public String getSnippet_name() {
        return snippet_name;
    }

    public void setSnippet_name(String snippet_name) {
        this.snippet_name = snippet_name;
    }

    public void setUser_id(Long user_id) {this.user_id = user_id; }

    public Long getUser_id() {return user_id;}

    public Date getUploadDate() {
        return upload_date;
    }

    public void setUploadDate(Date upload_date) {
        this.upload_date = upload_date;
    }

    public int getLikeCount() {
        return like_count;
    }

    public void setLikeCount(int like_count) { this.like_count = like_count; }

    public boolean isApproved() {return approved; }

    public void setApproved(boolean approved) {this.approved = approved; }

    public String getCode_text() {return code_text; }

    public void setCode_text(String code_text) {
        this.code_text = code_text;
    }

    public String getTags() {return tags;}

    public void setTags(String tags) {this.tags = tags;}
    }

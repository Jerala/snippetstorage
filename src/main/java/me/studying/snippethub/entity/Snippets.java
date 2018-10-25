package me.studying.snippethub.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Snippets", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "Snippets_UK", columnNames = "Snippet_id") })
public class Snippets {

    public Snippets() {}
    public Snippets(Long snippet_id, Long PL_ID, String snippet_name,
                    Long user_id, Date upload_date, Long like_count,
                    boolean approved, String tags) {
        this.snippet_id = snippet_id;
        this.PL_ID = PL_ID;
        this.snippet_name = snippet_name;
        this.user_id = user_id;
        this.upload_date = upload_date;
        this.like_count = like_count;
        this.approved = approved;
        this.tags = tags;
    }

    @Id
    @GeneratedValue
    @Column(name = "Snippet_Id", nullable = false)
    private Long snippet_id;

    @Column(name = "PL_ID", length = 5, nullable = false)
    private Long PL_ID;

    @Column(name = "Snippet_name", length = 100, nullable = false)
    private String snippet_name;

    @Column(name = "User_id", length = 15, nullable = false)
    private Long user_id;

    @Column(name = "UPLOAD_DATE", nullable = false)
    private Date upload_date;

    @Column(name = "LIKE_COUNT", length = 10, nullable = false)
    private Long like_count;

    @Column(name = "APPROVED", length = 1, nullable = false)
    private boolean approved;

    @Column(name = "TAGS", nullable = true)
    private String tags;

    private String code_text;

    public Long getSnippetId() {
        return snippet_id;
    }

    public void setSnippetId(Long snippet_id) {
        this.snippet_id = snippet_id;
    }

    public Long getPLID() {
        return PL_ID;
    }

    public void setPLID(Long PL_name) {
        this.PL_ID = PL_name;
    }

    public String getSnippet_name() {
        return snippet_name;
    }

    public void setSnippet_name(String snippet_name) {
        this.snippet_name = snippet_name;
    }

    public void setUser_id(Long user_id) {this.user_id = user_id;}

    public Long getUser_id() {return user_id;}

    public Date getUploadDate() {
        return upload_date;
    }

    public void setUploadDate(Date upload_date) {
        this.upload_date = upload_date;
    }

    public Long getLikesCount() {
        return like_count;
    }

    public void setLikesCount(Long like_count) { this.like_count = like_count; }

    public boolean isApproved() {return approved; }

    public void setApproved(boolean approved) {this.approved = approved; }

    public String getCode_text() {return code_text; }

    public void setCode_text(String code_text) {
        this.code_text = code_text;
    }

    public String getTags() {return tags;}

    public void setTags(String tags) {this.tags = tags;}

}

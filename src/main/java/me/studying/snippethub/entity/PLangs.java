package me.studying.snippethub.entity;

public class PLangs {
    private Long PL_id;
    private String PL_name;

    public PLangs() {

    }

    public PLangs(Long PL_id, String PL_name) {
        this.PL_id = PL_id;
        this.PL_name = PL_name;
    }

    public Long getPLID() {
        return PL_id;
    }

    public void setPLID(Long PL_id) {
        this.PL_id = PL_id;
    }

    public String getPLName() {
        return PL_name;
    }

    public void setPLName(String PL_name) {
        this.PL_name = PL_name;
    }
}

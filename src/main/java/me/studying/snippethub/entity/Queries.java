package me.studying.snippethub.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "queries")
@Entity
public class Queries {

    public Queries() {}

    public Queries(Long query_id, String query, Long counter, Date searched) {
        this.query_id = query_id;
        this.query = query;
        this.counter = counter;
        this.searched = searched;
    }

    @Id
    @Column(name = "query_id")
    private Long query_id;

    @Column(name = "query")
    private String query;

    @Column(name = "counter")
    private Long counter;

    @Column(name = "searched")
    private Date searched;

    public Long getQuery_id() {
        return query_id;
    }

    public void setQuery_id(Long query_id) {
        this.query_id = query_id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

    public Date getSearched() {
        return searched;
    }

    public void setSearched(Date searched) {
        this.searched = searched;
    }
}

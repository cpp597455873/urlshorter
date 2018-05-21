package com.cpp.urlshorter.shortsite;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "test_short_url")
public class ShortUrlModel {
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    @Column
    private String longUrl;

    @Column(unique = true)
    private String longUrlMd5;

    @Column
    private int visitCount;

    @Column
    private Date latestVisitTime;

    @Column
    private Date createTime;


    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLatestVisitTime() {
        return latestVisitTime;
    }

    public void setLatestVisitTime(Date latestVisitTime) {
        this.latestVisitTime = latestVisitTime;
    }

    public String getLongUrlMd5() {
        return longUrlMd5;
    }

    public void setLongUrlMd5(String longUrlMd5) {
        this.longUrlMd5 = longUrlMd5;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

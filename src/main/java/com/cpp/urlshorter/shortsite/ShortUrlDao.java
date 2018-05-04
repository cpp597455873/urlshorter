package com.cpp.urlshorter.shortsite;

public interface ShortUrlDao {
    void insertShortUrl(String shortUrl, String originalUrl);
}

package com.cpp.urlshorter.shortsite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShortUrlRepository extends JpaRepository<ShortUrlModel, String> {
    ShortUrlModel findByShortUrl(String shortUrl);

    List<ShortUrlModel> findByLongUrl(String longUrl);

    List<ShortUrlModel> findByLongUrlMd5(String longUrlMd5);
}
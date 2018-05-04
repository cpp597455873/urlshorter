package com.cpp.urlshorter.shortsite;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrlModel, String> {
    ShortUrlModel findByShortUrl(String shortUrl);
}
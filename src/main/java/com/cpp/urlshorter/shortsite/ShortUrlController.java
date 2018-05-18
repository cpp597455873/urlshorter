package com.cpp.urlshorter.shortsite;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@RestController
public class ShortUrlController {
    private final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String BASE_VISIT_URL = "127.0.0.1";
    @Value("${server.port}")
    int port;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping(value = "/{short_url}", method = RequestMethod.GET)
    @Transactional
    public Object visit(@PathVariable String short_url) {
        TypedQuery<ShortUrlModel> query = entityManager.createQuery("Select s from ShortUrlModel s where s.shortUrl=:shortUrl", ShortUrlModel.class);
        query.setParameter("shortUrl", short_url);
        List<ShortUrlModel> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0) {
            return new ModelAndView("web/error.html");
        } else {
            ShortUrlModel shortUrlModel = resultList.get(0);
            shortUrlModel.setVisitCount(shortUrlModel.getVisitCount() + 1);
            shortUrlModel.setLatestVisitTime(new Date());
            entityManager.merge(shortUrlModel);
            return new RedirectView(shortUrlModel.getLongUrl());
        }
    }

    @RequestMapping(value = "/create.do", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public Map create(@RequestParam String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        url = url + "/" + UUID.randomUUID().toString();

        //检查是否存在长地址  这里使用MD5这样建立索引的时候会更快
        String md5 = DigestUtils.md5DigestAsHex(url.getBytes());
        TypedQuery<ShortUrlModel> query = entityManager.createQuery("Select s from ShortUrlModel s where s.longUrlMd5=:longUrlMd5", ShortUrlModel.class);
        query.setParameter("longUrlMd5", md5);
        List<ShortUrlModel> resultList = query.getResultList();
        if (resultList != null && resultList.size() > 0) {
            return ImmutableMap.of("short_url", getBaseUrl() + resultList.get(0).getShortUrl(), "long_url", url);
        }


        //检查短地址是不是存在
        String oneSite = getOneSite();
        TypedQuery<Long> queryShort = entityManager.createQuery("Select count(s) from ShortUrlModel s where s.shortUrl=:shortUrl", Long.class);
        queryShort.setParameter("shortUrl", oneSite);
        while (queryShort.getSingleResult() != 0) {
            oneSite = getOneSite();
            queryShort.setParameter("shortUrl", oneSite);
        }

        ShortUrlModel shortUrlModel = new ShortUrlModel();
        shortUrlModel.setShortUrl(oneSite);
        shortUrlModel.setLongUrl(url);
        shortUrlModel.setLongUrlMd5(md5);
        shortUrlModel.setCreateTime(new Date());
        entityManager.persist(shortUrlModel);

        return ImmutableMap.of("short_url", getBaseUrl() + oneSite, "long_url", url);
    }

    private String getOneSite() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = new Random().nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(index));
        }
        return sb.toString();
    }

    private String getBaseUrl() {
        return BASE_VISIT_URL + (port == 80 ? "" : ":" + port) + "/";
    }

}

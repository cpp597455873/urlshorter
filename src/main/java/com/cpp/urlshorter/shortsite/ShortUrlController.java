package com.cpp.urlshorter.shortsite;

import com.cpp.urlshorter.ApiResonse;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "shortUrl", description = "短连接")
public class ShortUrlController {

    private final String BASE_VISIT_URL = "127.0.0.1";

    @Value("${server.port}")
    int port;

    @PersistenceContext
    private EntityManager entityManager;

    @ApiOperation("访问短链接")

    @RequestMapping(value = "{short_url:[0-9a-zA-Z]{1,}}", method = RequestMethod.GET)
    @Transactional
    public Object visit(@PathVariable @ApiParam(value = "short_url", name = "短连接") String short_url) {
        TypedQuery<ShortUrlModel> query = entityManager.createQuery("Select s from ShortUrlModel s where s.shortUrlId=:shortUrlId", ShortUrlModel.class);
        query.setParameter("shortUrlId", ShortUrlHelper._62_to_10(short_url));
        List<ShortUrlModel> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0) {
            return new RedirectView("/static/error.html");

        } else {
            ShortUrlModel shortUrlModel = resultList.get(0);
            shortUrlModel.setVisitCount(shortUrlModel.getVisitCount() + 1);
            shortUrlModel.setLatestVisitTime(new Date());
            entityManager.merge(shortUrlModel);
            return new RedirectView(shortUrlModel.getLongUrl());
        }
    }

    @ApiOperation(value = "生成短连接接口", notes = "如果有就不重复创建")
    @RequestMapping(value = "/create.do", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public Map create(@RequestParam String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        //检查是否存在长地址  这里使用MD5这样建立索引的时候会更快
        String md5 = DigestUtils.md5DigestAsHex(url.getBytes());
        TypedQuery<ShortUrlModel> query = entityManager.createQuery("Select s from ShortUrlModel s where s.longUrlMd5=:longUrlMd5", ShortUrlModel.class);
        query.setParameter("longUrlMd5", md5);
        List<ShortUrlModel> resultList = query.getResultList();
        if (resultList != null && resultList.size() > 0) {
            return ImmutableMap.of("short_url", getBaseUrl() + ShortUrlHelper._10_to_62(resultList.get(0).getShortUrlId()), "long_url", url);
        }


        //检查短地址是不是存在
        ShortUrlModel shortUrlModel = new ShortUrlModel();
        shortUrlModel.setLongUrl(url);
        shortUrlModel.setLongUrlMd5(md5);
        shortUrlModel.setCreateTime(new Date());
        entityManager.persist(shortUrlModel);
        entityManager.flush();
        return ImmutableMap.of("short_url", getBaseUrl() + ShortUrlHelper._10_to_62(shortUrlModel.getShortUrlId()), "long_url", url);
    }


    //demo
    @ApiOperation(value = "生成短连接接口", notes = "如果有就不重复创建")
    @RequestMapping(value = "/create1.do", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ApiResonse<Integer> createObject(@RequestBody ShortUrlModel shortUrlModel) {
        return new ApiResonse<>(1);
    }


    private String getBaseUrl() {
        return BASE_VISIT_URL + (port == 80 ? "" : ":" + port) + "/";
    }

}

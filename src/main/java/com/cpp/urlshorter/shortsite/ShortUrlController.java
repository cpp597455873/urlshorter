package com.cpp.urlshorter.shortsite;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.*;

@RestController
public class ShortUrlController {
    private final String charSet = "12345mnABCD6wxyzEFGHIJKLMNOPjkl7980abcdefghiqrstuvopQRSTUVWXYZ";

    @Value("${server.port}")
    int port;

    private final String BASE_VISIT_URL = "127.0.0.1";

    @Resource
    private ShortUrlRepository shortUrlRepository;

    @RequestMapping(value = "/{short_url}", method = RequestMethod.GET)
    public Object visit(@PathVariable String short_url) {
        ShortUrlModel shortUrl = shortUrlRepository.findByShortUrl(short_url);
        if (shortUrl == null) {
            return new ModelAndView("web/error.html");
        } else {
            shortUrl.setVisitCount(shortUrl.getVisitCount() + 1);
            shortUrl.setLatestVisitTime(new Date());
            shortUrlRepository.save(shortUrl);
            return new RedirectView(shortUrl.getLongUrl());
        }
    }

    private String getBaseUrl() {
        return BASE_VISIT_URL + (port == 80 ? "" : ":" + port) + "/";
    }

    @RequestMapping(value = "/create.do", method = RequestMethod.GET)
    @ResponseBody
    public Map create(@RequestParam String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }


        //检查是否存在长地址  这里使用MD5这样建立索引的时候会更快
        String md5 = DigestUtils.md5DigestAsHex(url.getBytes());
        List<ShortUrlModel> longUrlList = shortUrlRepository.findByLongUrlMd5(md5);
        if (longUrlList != null && longUrlList.size() > 0) {
            return ImmutableMap.of("short_url", getBaseUrl() + longUrlList.get(0).getShortUrl(), "long_url", url);
        }


        //检查短地址是不是存在
        String oneSite = getOneSite();
        while (shortUrlRepository.findByShortUrl(oneSite) != null) {
            oneSite = getOneSite();
        }

        ShortUrlModel shortUrlModel = new ShortUrlModel();
        shortUrlModel.setShortUrl(oneSite);
        shortUrlModel.setLongUrl(url);
        shortUrlModel.setLongUrlMd5(md5);
        shortUrlModel.setCreateTime(new Date());
        shortUrlRepository.save(shortUrlModel);

        return ImmutableMap.of("short_url", getBaseUrl() + oneSite, "long_url", url);
    }

    private String getOneSite() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = new Random().nextInt(charSet.length());
            sb.append(charSet.charAt(index));
        }
        return sb.toString();
    }
}

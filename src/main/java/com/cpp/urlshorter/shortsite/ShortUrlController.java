package com.cpp.urlshorter.shortsite;

import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@RestController
public class ShortUrlController {

    private static final String BASE_VISIT_URL = "127.0.0.1:7070/";

    @Resource
    private ShortUrlRepository shortUrlRepository;

    @RequestMapping(value = "/{short_url}", method = RequestMethod.GET)
    public RedirectView visit(@PathVariable String short_url) {
        ShortUrlModel shortUrl = shortUrlRepository.findByShortUrl(short_url);
        if (shortUrl == null) {
            return new RedirectView("127.0.0.1:7070/error.html");
        } else {
            shortUrl.setVisitCount(shortUrl.getVisitCount()+1);
            shortUrl.setLatestVisitTime(new Date());
            shortUrlRepository.save(shortUrl);
            return new RedirectView(shortUrl.getLongUrl());
        }
    }

    @RequestMapping(value = "/create.do", method = RequestMethod.GET)
    @ResponseBody
    public Map create(@RequestParam String baseUrl) {
        if (!baseUrl.startsWith("http")) {
            baseUrl = "http://" + baseUrl;
        }
        String oneSite = ShortSite.getOneSite();
        ShortUrlModel shortUrlModel = new ShortUrlModel();
        shortUrlModel.setShortUrl(oneSite);
        shortUrlModel.setLongUrl(baseUrl);
        shortUrlRepository.save(shortUrlModel);

        return ImmutableMap.of("short_url", BASE_VISIT_URL + oneSite, "long_url", baseUrl);
    }
}

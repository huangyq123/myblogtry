package com.example.myblogtry.service;

import com.example.myblogtry.entity.BlogLink;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;

import java.util.List;
import java.util.Map;

public interface LinkService {

    PageResult findLinkList(PageQueryUtil pageQueryUtil);

    Boolean addLink(BlogLink blogLink);

    Map<Byte, List<BlogLink>> getLinksForLinkPage();
}

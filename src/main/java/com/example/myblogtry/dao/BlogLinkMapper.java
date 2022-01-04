package com.example.myblogtry.dao;

import com.example.myblogtry.entity.BlogLink;
import com.example.myblogtry.utils.PageQueryUtil;

import java.util.List;

public interface BlogLinkMapper {

    List<BlogLink> selectLinkLists(PageQueryUtil pageQueryUtil);

    int insertLink(BlogLink blogLink);

}

package com.example.myblogtry.service.Impel;

import com.example.myblogtry.dao.BlogLinkMapper;
import com.example.myblogtry.entity.BlogLink;
import com.example.myblogtry.service.LinkService;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {

    @Resource
    private BlogLinkMapper blogLinkMapper;

    @Override
    public PageResult findLinkList(PageQueryUtil pageQueryUtil) {
        List<BlogLink> blogLinks = blogLinkMapper.selectLinkLists(pageQueryUtil);
        int total = blogLinks==null?0:blogLinks.size();
        for(BlogLink bl:blogLinks){
            System.out.println(bl);
        }
        PageResult pageResult = new PageResult(blogLinks, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean addLink(BlogLink blogLink) {
        int length = blogLinkMapper.insertLink(blogLink);
//        System.out.println(length);
        if(length>0){
            return true;
        }
        return false;
    }

    @Override
    public Map<Byte, List<BlogLink>> getLinksForLinkPage() {
        //获取所有链接数据
        List<BlogLink> links = blogLinkMapper.selectLinkLists(null);
        if (!CollectionUtils.isEmpty(links)) {
            //根据type进行分组
            Map<Byte, List<BlogLink>> linksMap = links.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));
            return linksMap;
        }
        return null;
    }
}

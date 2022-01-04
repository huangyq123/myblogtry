package com.example.myblogtry.service.Impel;

import com.example.myblogtry.dao.BlogTagMapper;
import com.example.myblogtry.entity.BlogTag;
import com.example.myblogtry.entity.BlogTagCount;
import com.example.myblogtry.service.TagService;
import com.example.myblogtry.utils.PageQueryUtil;
import com.example.myblogtry.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private BlogTagMapper blogTagMapper;

    @Override
    public PageResult getTagPage(PageQueryUtil pageQueryUtil) {
        //项目实现——使用工具类，工具类就是把参数转换操作进行封装（由于多个模块都存在这个实现）
        List<BlogTag> tagListByUtil = blogTagMapper.findTagListByUtil(pageQueryUtil);
        PageResult pageResult = new PageResult(tagListByUtil,tagListByUtil.size(),pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public PageResult getTagPage(String page, String limit) {
        //自己实现——使用参数
        int newPage = Integer.parseInt(page);
        int newLimit = Integer.parseInt(limit);
        int start = (newPage-1)*newLimit;
        List<BlogTag> tagList = blogTagMapper.findTagList(start, newLimit);
//        for(BlogTag b:tagList){
//            System.out.println(b);
//        }
        PageResult pageResult = new PageResult(tagList,tagList.size(),newLimit,newPage);
        return pageResult;
    }

    @Override
    public boolean saveTag(String tagName) {
        BlogTag blogTag1 = blogTagMapper.selectByName(tagName);
        if(null==blogTag1) {
            BlogTag blogTag = new BlogTag();
            blogTag.setTagName(tagName);
            if(blogTagMapper.save(blogTag)>0){
                return true;
            }
        }
        return false;
    }

    @Override
//    @Transactional
    public boolean deleteTags(Integer[] ids) {
        int length = blogTagMapper.deleteTagsByPrimaryKeys(ids);
        if(length>0){
            return true;
        }
        return false;
    }

    @Override
    public List<BlogTagCount> getBlogTagCountForIndex() {
        List<BlogTagCount> tagCount = blogTagMapper.getTagCount();
        return tagCount;
    }
}

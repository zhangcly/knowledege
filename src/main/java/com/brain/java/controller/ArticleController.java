package com.brain.java.controller;

import com.brain.java.dao.Article;
import com.brain.java.dao.ArticleKNode;
import com.brain.java.dao.repository.ArticleKNodeRepository;
import com.brain.java.dao.repository.ArticleRepository;
import com.brain.java.dto.request.AddArticleRequest;
import com.brain.java.dto.request.QueryArticleRequest;
import com.brain.java.dto.request.RelateArticleRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: java
 * @description:
 * @author: lanj
 * @create: 2020-04-22 11:36
 */
@RestController
@CrossOrigin
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleKNodeRepository articleKNodeRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @PostMapping("add")
    public Object add(@RequestBody AddArticleRequest request) {

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setUrl(request.getUrl());
        Article save = articleRepository.save(article);
        return save;
    }

    @PostMapping("list")
    public Object list(@RequestBody QueryArticleRequest request) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(request.getContent()))
                .withPageable(PageRequest.of(0, 10))
                .build();
        List<Article> articles = elasticsearchOperations.queryForList(searchQuery, Article.class);
        return articles;
    }

    /**
     * 将已有的文章关联到节点上
     *
     * @param request
     * @return
     */
    @PostMapping("relate")
    public Object add(@RequestBody RelateArticleRequest request) {

        ArticleKNode articleKNode = new ArticleKNode();
        articleKNode.setArticleId(request.getArticleId());
        articleKNode.setNodeId(request.getNodeId());

        ArticleKNode save = articleKNodeRepository.save(articleKNode);
        return save;
    }
}
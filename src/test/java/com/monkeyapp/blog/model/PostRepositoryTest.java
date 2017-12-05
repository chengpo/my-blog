package com.monkeyapp.blog.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PostRepositoryTest {

    private PostRepository postRepository;

    @Before
    public void setUp() {
        List<String> fileNameList = Arrays.asList(
                "2017-0418-1053-tag2-post3.md",
                "2017-0418-1153-tag2-post2.md",
                "2018-0418-1205-tag1-post1.md"
        );

        postRepository = new PostRepository(fileNameList);
    }

    @Test
    public void testPostListOrderByCreatedTime() {
        assertEquals("wrong post list size", 3, postRepository.getPostEntities().count());
        List<Entity> entityList = postRepository.getPostEntities().collect(Collectors.toList());

        assertEquals("post 1 should be on the top of post list",
                     "Post1",
                     entityList.get(0).getTitle());

        assertEquals("post 2 should be in the middle of post list",
                "Post2",
                entityList.get(1).getTitle());

        assertEquals("post 3 should be on the end of post list",
                "Post3",
                entityList.get(2).getTitle());
    }

    @Test
    public void testFilterPostListByTag() {
        List<Entity> entityList;

        assertEquals("wrong post list size for tag1", 1, postRepository.getPostEntities("tag1").count());
        entityList = postRepository.getPostEntities("tag1").collect(Collectors.toList());

        assertEquals("post 1 should listed for tag1",
                     "Post1",
                      entityList.get(0).getTitle());

        assertEquals("wrong post list size for tag2", 2, postRepository.getPostEntities("tag2").count());
        entityList = postRepository.getPostEntities("tag2").collect(Collectors.toList());

        assertEquals("post 2 should the first of tag2 list",
                "Post2",
                entityList.get(0).getTitle());
        assertEquals("post 3 should the second of tag2 list",
                "Post3",
                entityList.get(1).getTitle());
    }
}

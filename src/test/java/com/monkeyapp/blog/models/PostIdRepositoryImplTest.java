package com.monkeyapp.blog.models;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
@Deprecated
public class PostIdRepositoryImplTest {

    private PostIdRepository postIdRepository;

    private Comparator<Paper.Id> byPriority = Comparator.comparingLong(Paper.Id::getPriority)
            .reversed();

    @Before
    public void setUp() {
        List<String> fileNameList = Arrays.asList(
                "2017-0418-1053-tag2-post3.md",
                "2017-0418-1153-tag2-post2.md",
                "2018-0418-1205-tag1-post1.md"
        );

        postIdRepository = new PostIdRepositoryImpl(fileNameList);
    }

    @Test
    public void testPostListOrderByCreatedTime() {
        assertEquals("wrong post list size", 3, postIdRepository.getAllPostIds().count());
        List<Paper.Id> idList = postIdRepository.getAllPostIds().sorted(byPriority).collect(Collectors.toList());

        assertEquals("post 1 should be on the top of post list",
                "Post1",
                idList.get(0).getTitle());

        assertEquals("post 2 should be in the middle of post list",
                "Post2",
                idList.get(1).getTitle());

        assertEquals("post 3 should be on the end of post list",
                "Post3",
                idList.get(2).getTitle());
    }

    @Test
    public void testFilterPostListByTag() {
        List<Paper.Id> idList;

        assertEquals("wrong post list size for tag1", 1, postIdRepository.getPostIdsByTag("tag1").count());
        idList = postIdRepository.getPostIdsByTag("tag1")
                .sorted(byPriority)
                .collect(Collectors.toList());

        assertEquals("post 1 should listed for tag1",
                     "Post1",
                      idList.get(0).getTitle());

        assertEquals("wrong post list size for tag2", 2, postIdRepository.getPostIdsByTag("tag2").count());
        idList = postIdRepository.getPostIdsByTag("tag2")
                .sorted(byPriority)
                .collect(Collectors.toList());

        assertEquals("post 2 should the first of tag2 list",
                "Post2",
                idList.get(0).getTitle());
        assertEquals("post 3 should the second of tag2 list",
                "Post3",
                idList.get(1).getTitle());
    }
}

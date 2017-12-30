/*
MIT License

Copyright (c) 2017 Po Cheng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

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

    private Comparator<PaperId> byPriority = Comparator.comparingLong(PaperId::getPriority)
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
        List<PaperId> idList = postIdRepository.getAllPostIds().sorted(byPriority).collect(Collectors.toList());

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
        List<PaperId> idList;

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

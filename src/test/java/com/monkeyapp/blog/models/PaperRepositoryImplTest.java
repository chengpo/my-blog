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

import com.monkeyapp.blog.adapters.PaperAdapter;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class PaperRepositoryImplTest {
    private PaperRepositoryImpl paperRepository;

    @Before
    public void setUp() {
        final List<String> fileNameList = Arrays.asList(
                "2017-0418-1053-tag2-post3.md",
                "2017-0418-1153-tag2-post2.md",
                "2018-0418-1205-tag1-post1.md"
        );

        final PaperAdapter paperAdapter = mock(PaperAdapter.class);
        doReturn(Optional.of(fileNameList)).when(paperAdapter).toPostFileNames(anyString());
        doAnswer(invocationOnMock -> {
            PaperId id = (PaperId) invocationOnMock.getArguments()[0];
            return Optional.of(new Paper(id, "mock partial post content of " + id));
        }).when(paperAdapter).toPartialPost(any(PaperId.class));

        doAnswer(invocationOnMock -> {
            PaperId id = (PaperId) invocationOnMock.getArguments()[0];
            return Optional.of(new Paper(id, "mock complete post content of " + id));
        }).when(paperAdapter).toCompletePost(any(PaperId.class));

        paperRepository = new PaperRepositoryImpl(paperAdapter);
    }

    @Test
    public void testPostListIsOrderedByCreatedTime() {
        final PaperChunk paperChunk = paperRepository.getPostsByTag("", 0, Integer.MAX_VALUE);
        final List<Paper> papers = paperChunk.getPapers();

        assertTrue(paperChunk.eof);
        assertEquals(0, paperChunk.offset);
        assertEquals(Integer.MAX_VALUE, paperChunk.capacity);

        final List<String> expectedTitles = Arrays.asList("Post1", "Post2", "Post3");
        final List<String> expectedTags = Arrays.asList("Tag1", "Tag2", "Tag2");
        final List<String> expectedCreationTimes = Arrays.asList("2018/04/18 12:05", "2017/04/18 11:53", "2017/04/18 10:53");
        final List<String> expectedUrls = Arrays.asList("2018/0418/post1", "2017/0418/post2", "2017/0418/post3");

        verifyPaperList(papers, PaperId::getTitle, expectedTitles);
        verifyPaperList(papers, PaperId::getTag, expectedTags);
        verifyPaperList(papers, PaperId::getCreationTime, expectedCreationTimes);
        verifyPaperList(papers, PaperId::getUrl, expectedUrls);

        final List<String> expectedPaperContents = new ArrayList<>();
        papers.forEach(paper -> expectedPaperContents.add("mock partial post content of " + paper.getId()));
        final List<String> actualPaperContents = papers.stream()
                                                       .map(Paper::getContent)
                                                       .collect(Collectors.toList());

        assertThat(actualPaperContents, CoreMatchers.is(expectedPaperContents));
    }

    @Test
    public void testPostListIsFilteredByTag() {
        final List<Paper> tag1Papers = paperRepository.getPostsByTag("tag1", 0, Integer.MAX_VALUE).papers;
        final List<String> expectedTag1Titles = Collections.singletonList("Post1");

        verifyPaperList(tag1Papers, PaperId::getTitle, expectedTag1Titles);

        final List<Paper> tag2Papers = paperRepository.getPostsByTag("tag2", 0, Integer.MAX_VALUE).papers;
        final List<String> expectedTag2Titles = Arrays.asList("Post2", "Post3");

        verifyPaperList(tag2Papers, PaperId::getTitle, expectedTag2Titles);
    }


    @Test
    public void testGetCompletePost() {
        final Optional<Paper> paper = paperRepository.getCompletePost("2017", "0418", "post2");
        assertTrue(paper.isPresent());
        assertEquals("mock complete post content of " + paper.get().getId(), paper.get().getContent());
    }

    @Test
    public void testGetTags() {
        final List<TagCounter> actualTagCounters = paperRepository.getPostTags();
        final List<TagCounter> expectedTagCounters = Arrays.asList(new TagCounter("Tag1", 1), new TagCounter("Tag2", 2));

        final List<String> actualTags = actualTagCounters.stream()
                                            .map(TagCounter::getTag)
                                            .collect(Collectors.toList());

        final List<String> expectedTags = expectedTagCounters.stream()
                .map(TagCounter::getTag)
                .collect(Collectors.toList());

        assertThat(actualTags, CoreMatchers.is(expectedTags));

        final List<Long> actualCounts = actualTagCounters.stream()
                                                .map(TagCounter::getCount)
                                                .collect(Collectors.toList());

        final List<Long> expectedCounts = expectedTagCounters.stream()
                .map(TagCounter::getCount)
                .collect(Collectors.toList());

        assertThat(actualCounts, CoreMatchers.is(expectedCounts));
    }

    @Test
    public void testGetFeed() {
        SyncFeed syncFeed = paperRepository.getPostFeed();
        assertEquals(3, syncFeed.getItems().size());
    }

    /**
     * Verify the given paper list meets expectation
     * @param papers - paper list for checking
     * @param transform - transform method for retrieving actual values
     * @param expectedValues - the expected values
     */
    private void verifyPaperList(List<Paper> papers,
                                 Function<PaperId, String> transform,
                                 List<String> expectedValues) {
       final List<String> actualValues = papers.stream()
                .map(Paper::getId)
                .map(transform)
                .collect(Collectors.toList());

        assertThat(actualValues, CoreMatchers.is(expectedValues));
    }
}

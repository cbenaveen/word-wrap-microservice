package com.naveen.microservice.wordwrap.service.persistent;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.naveen.microservice.wordwrap.model.CachedContent;
import com.naveen.microservice.wordwrap.model.Content;
import com.naveen.microservice.wordwrap.repository.CachedContentHazelcastContentRepository;
import com.naveen.microservice.wordwrap.service.PersistentWordWrapService;
import com.naveen.microservice.wordwrap.wrap.AbstractContentWrapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HazelcastCacheWordWrapServiceUnitTest {
    private static final int DEFAULT_MAX_LENGTH = 10;
    private static final int DEFAULT_PAGE_SIZE = 3;

    @Mock
    private ApplicationContext mockApplicationContext;
    @Mock
    private CachedContentHazelcastContentRepository contentRepository;
    @Mock
    private HazelcastInstance hazelcastInstance;
    @Mock
    private FlakeIdGenerator flakeIdGenerator;
    @Mock
    private AbstractContentWrapIterator mockAbstractContentWrapIterator;
    @Captor
    private ArgumentCaptor<String> beanNameCapture;
    @Captor
    private ArgumentCaptor<Object> varArgsCapture;

    private PersistentWordWrapService persistentWordWrapService;

    @BeforeEach
    public void setup() {
        persistentWordWrapService = new HazelcastCacheWordWrapService(mockApplicationContext, DEFAULT_MAX_LENGTH,
                DEFAULT_PAGE_SIZE, contentRepository, hazelcastInstance);
    }

    @Test
    @DisplayName("Test to verify the content is created in Hazelcast cluster or not")
    public void testContentCreationWithValidContent() {
        final String content = "A Content for test";
        final long id = 45434634564l;

        when(hazelcastInstance.getFlakeIdGenerator(anyString())).thenReturn(flakeIdGenerator);
        when(flakeIdGenerator.newId()).thenReturn(id);

        CachedContent cachedContent = persistentWordWrapService.create(content);

        verify(flakeIdGenerator, times(1)).newId();
        verify(hazelcastInstance, times(1)).getFlakeIdGenerator(anyString());
        verify(contentRepository, times(1)).save(any(CachedContent.class));
        assertEquals(id, cachedContent.getId());
        assertEquals(content, cachedContent.getContent().getContent());
    }

    @Test
    @DisplayName("Test to verify the content is not created in Hazelcast cluster when invalid content is passed")
    public void testContentCreationWithNullContent() {
        assertThrows(IllegalArgumentException.class, () -> persistentWordWrapService.create(null));
        verifyNoInteractions(flakeIdGenerator, hazelcastInstance, contentRepository);
    }

    @Test
    @DisplayName("Test to verify the content fetch call throws exception when content not found")
    public void testContentFetchByWhenInvalidIDPassed() {
        when(contentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> persistentWordWrapService.wrap(123l));

        verify(contentRepository, times(1)).findById(anyLong());
        verifyNoInteractions(flakeIdGenerator, hazelcastInstance);
    }

    @Test
    @DisplayName("Test to verify the wrap functionality with default page size")
    public void testContentWrapByPassingContentAloneWithDefaultPageAndMaxLength() {
        final String content = "A Content for test Content Wrap By Passing Content Alone unit test case";
        final long id = 9087866546745l;

        when(mockApplicationContext.getBean(anyString(), ArgumentMatchers.<Object>any()))
                .thenReturn(mockAbstractContentWrapIterator);
        when(mockAbstractContentWrapIterator.hasNext()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        when(mockAbstractContentWrapIterator.next()).thenReturn("A Content  ", "for test  ", "Content  ");

        when(hazelcastInstance.getFlakeIdGenerator(anyString())).thenReturn(flakeIdGenerator);
        when(flakeIdGenerator.newId()).thenReturn(id);

        //create the content
        CachedContent cachedContent = persistentWordWrapService.create(content);
        when(contentRepository.findById(anyLong())).thenReturn(Optional.of(cachedContent));

        Collection<String> collection = persistentWordWrapService.wrap(id);
        // since the default page size is 3, we should have only 3 entries in the collections
        assertEquals(DEFAULT_PAGE_SIZE, collection.size());
        assertEquals(Arrays.asList("A Content  ", "for test  ", "Content  "), collection);

        verify(mockApplicationContext).getBean(beanNameCapture.capture(), varArgsCapture.capture());
        assertEquals(content, ((Content) varArgsCapture.getAllValues().get(0)).getContent());
        assertEquals(DEFAULT_MAX_LENGTH, varArgsCapture.getAllValues().get(1));
        assertEquals(id, cachedContent.getId());
        assertEquals(content, cachedContent.getContent().getContent());
    }

    @Test
    @DisplayName("Test to verify the wrap functionality with default page size")
    public void testContentWrapByPassingContentAndCustomPageAndDefaultMaxLength() {
        final String content = "A Content for test Content Wrap By Passing Content Alone unit test case";
        final long id = 123987345123980l;
        final int CUSTOM_PAGE_SIZE = 20;

        when(mockApplicationContext.getBean(anyString(), ArgumentMatchers.<Object>any()))
                .thenReturn(mockAbstractContentWrapIterator);
        when(mockAbstractContentWrapIterator.hasNext()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        when(mockAbstractContentWrapIterator.next()).thenReturn(
                "A Content  ", "for test  ", "Content  ", "Wrap By  ",
                "Passing  ", "Content  ", "Alone  ", "unit test  ", "case ");

        when(hazelcastInstance.getFlakeIdGenerator(anyString())).thenReturn(flakeIdGenerator);
        when(flakeIdGenerator.newId()).thenReturn(id);

        //create the content
        CachedContent cachedContent = persistentWordWrapService.create(content);
        when(contentRepository.findById(anyLong())).thenReturn(Optional.of(cachedContent));

        Collection<String> collection = persistentWordWrapService.wrap(id, DEFAULT_MAX_LENGTH, CUSTOM_PAGE_SIZE);
        // since the default page size is 3, we should have only 3 entries in the collections
        assertEquals(9, collection.size());
        assertEquals(Arrays.asList("A Content  ", "for test  ", "Content  ", "Wrap By  ",
                "Passing  ", "Content  ", "Alone  ", "unit test  ", "case "), collection);

        verify(mockApplicationContext).getBean(beanNameCapture.capture(), varArgsCapture.capture());
        assertEquals(content, ((Content) varArgsCapture.getAllValues().get(0)).getContent());
        assertEquals(DEFAULT_MAX_LENGTH, varArgsCapture.getAllValues().get(1));
        assertEquals(id, cachedContent.getId());
        assertEquals(content, cachedContent.getContent().getContent());
    }
}

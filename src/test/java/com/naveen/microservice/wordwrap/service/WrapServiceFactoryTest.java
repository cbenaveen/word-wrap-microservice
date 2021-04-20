package com.naveen.microservice.wordwrap.service;

import com.naveen.microservice.wordwrap.wrap.WrapTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WrapServiceFactoryTest {

    @Autowired
    private ApplicationContext context;

    private WrapServiceFactory wrapServiceFactory;

    @BeforeAll
    public void setup() {
        wrapServiceFactory = new WrapServiceFactoryImpl(context);
    }

    @Test
    public void testGetOfInMemoryWrapServiceImpl() {
        assertThat(wrapServiceFactory.get(WrapTypes.INMEMORY), instanceOf(WordWrapServiceImpl.class));
    }

    @Test
    public void testGetOfPersistenceWrapServiceImpl() {
        assertThrows(UnsupportedOperationException.class, () -> {
            wrapServiceFactory.get(WrapTypes.PERSISTENCE);
        });
    }

    @Test
    public void testGetOfPaginationWrapServiceImpl() {
        assertThrows(UnsupportedOperationException.class, () -> {
            wrapServiceFactory.get(WrapTypes.PAGINATION);
        });
    }
}

package com.naveen.microservice.wordwrap.repository;

import com.naveen.microservice.wordwrap.wrap.model.CachedContent;
import org.springframework.data.hazelcast.repository.HazelcastRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CachedContentHazelcastContentRepository extends HazelcastRepository<CachedContent, Long> {
//    private static final String CACHED_CONTENT_MAP_NAME = "cached-contents";
//
//    private final HazelcastInstance hazelcastInstance;
//    private IMap<Long, CachedContent> cachedContentIMap;
//
//    @PostConstruct
//    private void initHazelcastCachedContentMap() {
//        cachedContentIMap = hazelcastInstance.getMap(CACHED_CONTENT_MAP_NAME);
//    }
//
//    @Override
//    public <S extends CachedContent> S save(S s) {
//        cachedContentIMap.putIfAbsent(s.getId(), s);
//        return s;
//    }
//
//    @Override
//    public <S extends CachedContent> Iterable<S> saveAll(Iterable<S> iterable) {
//        iterable.forEach(this::save);
//        return iterable;
//    }
//
//    @Override
//    public Optional<CachedContent> findById(Long aLong) {
//        if (existsById(aLong)) {
//            return Optional.of(cachedContentIMap.get(aLong));
//        }
//        return Optional.empty();
//    }
//
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return cachedContentIMap.containsKey(aLong);
//    }
//
//    @Override
//    public Iterable<CachedContent> findAll() {
//        return cachedContentIMap.values();
//    }
//
//    @Override
//    public Iterable<CachedContent> findAllById(Iterable<Long> iterable) {
//        return StreamSupport.stream(iterable.spliterator(), false)
//                .map(this::findById)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public long count() {
//        return cachedContentIMap.size();
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//        cachedContentIMap.remove(aLong);
//    }
//
//    @Override
//    public void delete(CachedContent content) {
//        cachedContentIMap.remove(content.getId());
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends CachedContent> iterable) {
//        iterable.forEach(this::delete);
//    }
//
//    @Override
//    public void deleteAll() {
//        cachedContentIMap.clear();
//    }
}

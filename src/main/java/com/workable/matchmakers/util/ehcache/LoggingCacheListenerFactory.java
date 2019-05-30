package com.workable.matchmakers.util.ehcache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Logs EhCache events.
 *
 */
public class LoggingCacheListenerFactory extends CacheEventListenerFactory {
	private static final Logger log = LoggerFactory.getLogger(LoggingCacheListenerFactory.class);

	@Override
	public CacheEventListener createCacheEventListener(Properties properties) {

		return new CacheEventListener() {

			@Override
			public Object clone() throws CloneNotSupportedException {
				return super.clone();
			}

			@Override
			public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
				if (log.isDebugEnabled()) {
					log.debug("L2 Cache REMOVE: cache=["+cache.getName()+"], key=["+element.getObjectKey()+"], value=["+element.getObjectValue()+"]");
				}
			}

			@Override
			public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
				if (log.isDebugEnabled()) {
					log.debug("L2 Cache PUT: cache=["+cache.getName()+"], key=["+element.getObjectKey()+"], value=["+element.getObjectValue()+"]");
				}
			}

			@Override
			public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
				if (log.isDebugEnabled()) {
					log.debug("L2 Cache UPDATE: cache=["+cache.getName()+"], key=["+element.getObjectKey()+"], value=["+element.getObjectValue()+"]");
				}
			}

			@Override
			public void notifyElementExpired(Ehcache cache, Element element) {
				if (log.isDebugEnabled()) {
					log.debug("L2 Cache EXPIRE: cache=["+cache.getName()+"], key=["+element.getObjectKey()+"], value=["+element.getObjectValue()+"]");
				}
			}

			@Override
			public void notifyElementEvicted(Ehcache cache, Element element) {
				if (log.isDebugEnabled()) {
					log.debug("L2 Cache EVICT: cache=["+cache.getName()+"], key=["+element.getObjectKey()+"], value=["+element.getObjectValue()+"]");
				}
			}

			@Override
			public void notifyRemoveAll(Ehcache cache) {
				if (log.isDebugEnabled()) {
					log.debug("L2 Cache REMOVE_ALL: cache=["+cache.getName()+"]");
				}
			}

			@Override
			public void dispose() {
			}
		};
	}
}

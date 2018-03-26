package pers.msidolphin.mblog.helper;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by msidolphin on 2018/1/1.
 */
@SuppressWarnings("ALL")
@ThreadSafe
public class Cache<K, V> {

	private ConcurrentHashMap<K, Future<V>> cache;

	private static final int INIT_CAPACITY = 16;

	@GuardedBy("lock")
	private Lock lock;

	public Cache() {
		cache = new ConcurrentHashMap<>(INIT_CAPACITY);
		lock = new ReentrantLock();
	}

	private Future<V> createFutureIfAbsent(final K key, final Callable<V> callable) {
		Future<V> future = cache.get(key);
		if(future == null) {
			final FutureTask<V> futureTask = new FutureTask<V>(callable);
			//如果存在key值相对应的value，那么返回该value，否则返回null
			future = cache.putIfAbsent(key, futureTask);
			lock.lock();
			try {
				if(future == null) {
					//如果返回null，表示map中不存在key-value映射 进行计算
					future = futureTask;
					futureTask.run(); //调用callable
				}
			}catch(Exception e) {

			}finally {
				lock.unlock();
			}
		}
		return future;
	}

	public Cache<K, V> set(final K key, final V value) {
		createFutureIfAbsent(key, ()->{
			return value;
		});
		return this;
	}

	public V get(final K key, final Callable<V> callable) {
		while(true) {
			try {
				final Future<V> future = createFutureIfAbsent(key, callable);
				if(future != null) {
					return future.get();
				}
			}catch (InterruptedException e) {
				cache.remove(key);
			}catch (ExecutionException e) {
				cache.remove(key);
			}finally {
			}
		}
	}

	public V get(final  K key) {
		try {
			Future<V> future = cache.get(key);
			if(future != null) {
				return future.get();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}

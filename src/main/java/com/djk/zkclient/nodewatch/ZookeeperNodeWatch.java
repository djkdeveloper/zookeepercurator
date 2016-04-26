package com.djk.zkclient.nodewatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;

import com.djk.zkclient.utils.ZookeeperFactory;

/**
 * zookeeper 节点观察者
 * 
 * @author dujinkai
 *
 */
public class ZookeeperNodeWatch {

	public static void main(String[] args) throws Exception {

		CuratorFramework client = ZookeeperFactory
				.createSimpleZkClient("127.0.0.1:2181");

		client.start();

		// 监听的节点发生变化 注意只有一层节点 比如说监听的是/djka 那么/djka/a 下面的子节点发生变化 则不会回调
		// CHILD_ADDED 表示监听的节点下面新增了子节点
		// CHILD_REMOVED 表示监听的节点下面删除了子节点
		// CHILD_UPDATED 表示监听的节点下面的子节点的数据发生了变化
		final PathChildrenCache childrenCache = new PathChildrenCache(client,
				"/djka", true);

		childrenCache.start(StartMode.BUILD_INITIAL_CACHE);

		childrenCache.getListenable().addListener(
				new PathChildrenCacheListener() {

					public void childEvent(CuratorFramework client,
							PathChildrenCacheEvent event) throws Exception {
						switch (event.getType()) {
						case CHILD_ADDED:
							System.out.println("CHILD_ADDED: "
									+ event.getData().getPath());
							break;
						case CHILD_REMOVED:
							System.out.println("CHILD_REMOVED: "
									+ event.getData().getPath());
							break;
						case CHILD_UPDATED:
							System.out.println("CHILD_UPDATED: "
									+ event.getData().getPath());
							break;
						default:
							break;
						}
					}
				});

		Thread.sleep(100000000);
		childrenCache.close();
		client.close();
	}
}

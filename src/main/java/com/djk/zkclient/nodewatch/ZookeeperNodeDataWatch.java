package com.djk.zkclient.nodewatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

import com.djk.zkclient.utils.ZookeeperFactory;

/**
 * zookeeper 的节点数据观察者
 * 
 * @author dujinkai
 *
 */
public class ZookeeperNodeDataWatch {

	public static void main(String[] args) throws Exception {

		CuratorFramework client = ZookeeperFactory
				.createSimpleZkClient("127.0.0.1:2181");

		client.start();

		// 观察节点的数据发生改变
		// 注意 只是监听了节点的数据发生变化 没有见到节点的子节点发生变化 所以一边回调的情况有2种
		// 1 :监听的节点刚创建的时候会回调
		// 2 :监听的节点数据发生变化的时候会回调
		final NodeCache nodeCache = new NodeCache(client, "/djka", false);

		nodeCache.start(true);

		nodeCache.getListenable().addListener(new NodeCacheListener() {

			public void nodeChanged() throws Exception {
				System.out.println(new String(nodeCache.getCurrentData()
						.getData()));
			}
		});

		Thread.sleep(200000);
		nodeCache.close();
		client.close();
	}
}

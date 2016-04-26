package com.djk.zkclient.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 
 * @author dujinkai
 *
 *         生产CuratorFramework 对象的类
 */
public final class ZookeeperFactory {

	/**
	 * 私有构造器防止初始化
	 */
	private ZookeeperFactory() {

	}

	/**
	 * 创建简易的zk实例
	 * 
	 * @param connectionString
	 *            链接信息 比如127.0.0.1:2181
	 * @return 返回CuratorFramework实例
	 */
	public static CuratorFramework createSimpleZkClient(String connectionString) {
		return CuratorFrameworkFactory.newClient(connectionString,
				new ExponentialBackoffRetry(1000, 3));
	}

	/**
	 * 创建自定义的zk实例
	 * 
	 * @param connectionString
	 *            链接信息 比如127.0.0.1:2181
	 * @param retryPolicy
	 *            重试策略
	 * @param connectionTimeoutMs
	 *            链接超时时间
	 * @param sessionTimeoutMs
	 *            session超时时间
	 * @return 返回CuratorFramework实例
	 */
	public static CuratorFramework createWithOptions(String connectionString,
			RetryPolicy retryPolicy, int connectionTimeoutMs,
			int sessionTimeoutMs) {

		return CuratorFrameworkFactory.builder()
				.connectString(connectionString).retryPolicy(retryPolicy)
				.connectionTimeoutMs(connectionTimeoutMs)
				.sessionTimeoutMs(sessionTimeoutMs).build();
	}

}

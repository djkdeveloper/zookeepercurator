package com.djk.zkclient.crud;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import com.djk.zkclient.utils.ZookeeperFactory;

/**
 * zookeeper 的增删改查的操作
 * 
 * @author dujinkai
 *
 */
public class ZookeeperCrud {

	public static void main(String[] args) throws Exception {

		// 获得zk客户端
		CuratorFramework client = ZookeeperFactory
				.createSimpleZkClient("127.0.0.1:2181");
		client.start();

		// 判断节点是否存在 不存在的话 则创建节点
		if (!isNodeExist(client, "/learn")) {
			create(client, "/learn", "".getBytes());
		}

		// 给节点设置值
		setData(client, "/djk", "djkddd".getBytes());

		// 删除节点
		deleteNode(client, "/learn");

		// 创建临时节点
		createEphemeral(client, "/temp", "test".getBytes());

		// 创建临时的有序节点
		System.out.println(createEphemeralSequential(client, "/djk/djk",
				"test".getBytes()));

		// 创建固定的有序节点
		System.out.println(createPresionetSequential(client, "/djk/djk",
				"test".getBytes()));

		// 活动节点下的数据
		System.out.println(getNodeDate(client, "/djk"));

		// 活动节点下的子节点
		System.out.println(getNodeChild(client, "/"));

		Thread.sleep(10000);

		client.close();
	}

	/**
	 * 创建节点 （固定节点）
	 * 
	 * @param client
	 *            zk客户端
	 * @param path
	 *            节点的路径
	 * @param payload
	 *            节点下的数据
	 * @throws Exception
	 *             抛出异常
	 */
	public static void create(CuratorFramework client, String path,
			byte[] payload) throws Exception {
		client.create().forPath(path, payload);
	}

	/**
	 * 判断节点是否存在
	 * 
	 * @param client
	 *            zk客户端
	 * @param path
	 *            节点路径
	 * @return
	 * @throws Exception
	 */
	public static boolean isNodeExist(CuratorFramework client, String path)
			throws Exception {
		return client.checkExists().forPath(path) == null ? false : true;
	}

	/**
	 * 给节点设置 值
	 * 
	 * @param client
	 *            zk客户端对象
	 * @param path
	 *            节点路径
	 * @param payload
	 *            节点数据
	 * @throws Exception
	 */
	public static void setData(CuratorFramework client, String path,
			byte[] payload) throws Exception {
		client.setData().forPath(path, payload);
	}

	/**
	 * 删除节点
	 * 
	 * @param client
	 *            zk客户端对象
	 * @param path
	 *            节点路径
	 */
	public static void deleteNode(CuratorFramework client, String path)
			throws Exception {
		client.delete().forPath(path);
	}

	/**
	 * 创建临时节点
	 * 
	 * @param client
	 *            zk客户端
	 * @param path
	 *            节点路径
	 * @param payload
	 *            节点数据
	 * @throws Exception
	 */
	public static void createEphemeral(CuratorFramework client, String path,
			byte[] payload) throws Exception {
		client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload);
	}

	/**
	 * 创建临时的有序节点
	 * 
	 * @param client
	 *            zk客户端
	 * @param path
	 *            节点路径
	 * @param payload
	 *            节点数据
	 * @return 返回临时节点的名称
	 * @throws Exception
	 */
	public static String createEphemeralSequential(CuratorFramework client,
			String path, byte[] payload) throws Exception {
		return client.create().withProtection()
				.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
				.forPath(path, payload);
	}

	/**
	 * 创建固定的有序节点
	 * 
	 * @param client
	 *            zk客户端
	 * @param path
	 *            节点路径
	 * @param payload
	 *            节点数据
	 * @return 返回创建的节点名称
	 * @throws Exception
	 */
	public static String createPresionetSequential(CuratorFramework client,
			String path, byte[] payload) throws Exception {
		return client.create().withProtection()
				.withMode(CreateMode.PERSISTENT_SEQUENTIAL)
				.forPath(path, payload);
	}

	/**
	 * 获得节点数据
	 * 
	 * @param client
	 *            客户端
	 * @param path
	 *            节点路径
	 * @return 返回节点下的数据
	 */
	public static String getNodeDate(CuratorFramework client, String path)
			throws Exception {
		return new String(client.getData().forPath(path));
	}

	/**
	 * 活动一个节点下面的子节点
	 * 
	 * @param client
	 *            zk客户端
	 * @param path
	 *            节点路径
	 * @return 返回节点下的子节点
	 * @throws Exception
	 */
	public static List<String> getNodeChild(CuratorFramework client, String path)
			throws Exception {
		return client.getChildren().forPath(path);
	}
}

package com.djk.zkclient.leader;

import com.djk.zkclient.utils.ZookeeperFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Test
 *
 * @author djk
 * @date 2016/5/11
 */
public class Test {
    public static void main(String[] args) {


        /**
         * 缓冲线程池
         */
        ExecutorService service = Executors.newCachedThreadPool();

        /**
         * 用多线程模拟多进程
         */
        for (int i = 0; i < 10; i++) {
            final int j = i;
            service.execute(() -> {
                CuratorFramework client = null;
                DjkLeaderSelector djkLeaderSelector = null;
                try {

                    client = ZookeeperFactory.createSimpleZkClient("112.124.63.64:2181");
                    djkLeaderSelector = new DjkLeaderSelector(client, "/leader", "Client #" + j);
                    client.start();
                    System.out.println("success....");
                    djkLeaderSelector.start();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            });
        }

    }
}

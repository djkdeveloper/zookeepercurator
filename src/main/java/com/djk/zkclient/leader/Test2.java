package com.djk.zkclient.leader;

import com.djk.zkclient.utils.ZookeeperFactory;
import org.apache.curator.framework.CuratorFramework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Test2
 *
 * @author djk
 * @date 2016/5/12
 */
public class Test2 {

    public static void main(String[] args) throws  Exception{
        /**
         * 缓冲线程池
         */
        ExecutorService service = Executors.newCachedThreadPool();

        List<DjkLeaderLatch> djkLeaderLatches = new ArrayList<DjkLeaderLatch>();


        for (int i = 0; i < 10; i++) {
            final int j = i;
            service.execute(() -> {
                CuratorFramework client = null;
                DjkLeaderLatch djkLeaderLatch = null;
                try {
                    client = ZookeeperFactory.createSimpleZkClient("112.124.63.64:2181");
                    djkLeaderLatch = new DjkLeaderLatch(client, "Client #" + j, "/leaderlatch");
                    client.start();
                    djkLeaderLatches.add(djkLeaderLatch);
                    System.out.println("success....");
                    djkLeaderLatch.start();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            });
        }


        Thread.sleep(10000);

        for (DjkLeaderLatch djkLeaderLatch : djkLeaderLatches)
        {
            if (djkLeaderLatch.isLeader())
            {
                djkLeaderLatch.close();
            }
        }
        Thread.sleep(1000000000);
    }

}

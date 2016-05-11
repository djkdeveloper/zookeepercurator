package com.djk.zkclient.lock;

import com.djk.zkclient.utils.ZookeeperFactory;
import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * LockTest
 *
 * @author djk
 * @date 2016/5/11
 */
public class LockTest {

    private static final String LOCK_PATH = "/lock";

    public static void main(String[] args) throws Exception {

        /**
         * 缓冲线程池
         */
        ExecutorService service = Executors.newCachedThreadPool();

        /**
         * 用多线程模拟多进程
         */
        for (int i = 0; i < 10; i++) {
            final int j = i;
            service.execute(() ->
            {
                try {

                    CuratorFramework client = ZookeeperFactory.createSimpleZkClient("112.124.63.64:2181");
                    client.start();
                    final DjkDistributedLock djkDistributedLock = DjkDistributedLock.build(System.out::println, client, LOCK_PATH, "Client:" + j, "djk");
                    djkDistributedLock.doWork(10, TimeUnit.SECONDS);
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }
}

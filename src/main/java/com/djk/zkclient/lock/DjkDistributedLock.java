package com.djk.zkclient.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * DjkDistributedLock
 * 分布式锁服务
 *
 * @author djk
 * @date 2016/5/11
 */
public final class DjkDistributedLock<T> {

    /**
     * 接口参数
     */
    private final T t;

    /**
     * 获得锁有 需要工作的接口
     */
    private final ResoucreInterface resoucreInterface;

    /**
     * 分布式锁
     */
    private final InterProcessMutex lock;

    /**
     * 客户端的名称 可以写机器中机器的ip
     */
    private final String clientName;


    private DjkDistributedLock(ResoucreInterface resoucreInterface, InterProcessMutex lock, String clientName, T t) {
        this.t = t;
        this.resoucreInterface = resoucreInterface;
        this.lock = lock;
        this.clientName = clientName;
    }

    /**
     * 开始工作
     *
     * @param time 获得锁的超时时间
     * @param unit 时间单位
     * @throws Exception
     */
    public void doWork(long time, TimeUnit unit) throws Exception {

        /**
         * 在规定的时间内没有获得锁 抛出异常
         */
        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }

        try {
            // 获得锁只有 开始工作
            System.out.println(clientName + " has the lock");
            resoucreInterface.doWork(t);
        } finally {

            System.out.println(clientName + " releasing the lock");
            lock.release(); // always release the lock in a finally block
        }

    }


    /**
     * 建造者 获得DjkDistributedLock对象
     *
     * @param resoucreInterface 工作接口
     * @param client            zk客户端
     * @param lockPath          锁的路径
     * @param clientName        客户端名称
     * @return 返回DjkDistributedLock对象
     */
    public static <T> DjkDistributedLock build(ResoucreInterface resoucreInterface, CuratorFramework client, String lockPath, String clientName, T t) {
        return new DjkDistributedLock(resoucreInterface, new InterProcessMutex(client, lockPath), clientName, t);
    }

}

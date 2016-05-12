package com.djk.zkclient.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;

import java.io.Closeable;
import java.io.IOException;

/**
 * DjkLeaderLatch
 * 领导选举 不轮询 只有当领导的客户端关闭后才会释放领导权限
 * @author djk
 * @date 2016/5/12
 */
public class DjkLeaderLatch implements Closeable {

    /**
     * 领导选举
     */
    private final LeaderLatch leaderLatch;

    public DjkLeaderLatch(CuratorFramework client, final String clientName, String path) {
        leaderLatch = new LeaderLatch(client, path);
        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                System.out.println("I am leader ...and name :" + clientName);
            }

            @Override
            public void notLeader() {
                System.out.println("I am not leader ...and name :" + clientName);
            }
        });
    }

    @Override
    public void close() throws IOException {
        leaderLatch.close();
    }

    /**
     * 开始选举
     * @throws Exception
     */
    public void start() throws Exception {
        leaderLatch.start();
    }

    /**
     * 是否是leader
     * @return
     */
    public boolean isLeader() {
        return leaderLatch.hasLeadership();
    }
}

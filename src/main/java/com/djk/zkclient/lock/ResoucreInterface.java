package com.djk.zkclient.lock;

/**
 * ResoucreInterface
 * 集群中  需要加锁的任务接口
 *
 * @author djk
 * @date 2016/5/11
 */
@FunctionalInterface
public interface ResoucreInterface<T> {

    /**
     * 获得锁之后 要做的工作
     *
     * @param t
     */
    void doWork(T t);
}

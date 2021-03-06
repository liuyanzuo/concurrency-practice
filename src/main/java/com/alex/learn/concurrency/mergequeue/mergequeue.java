/**
 * 在业务场景中，可能会需要对相同业务ID的操作进行合并操作，这里提供了一个简单的实现：
 * - 数据结构使用PriorityQueue
 * - 采用一个线程轮询PriorityQueue的堆顶（小顶堆），查看是否到了执行时间。PS：此思路与Timer当中一致
 */
# 字节TikTok生活服务 一面

1h，第二天感谢信

## 实习

具体问了一下业务，应该是觉得业务简单，没有细问

## 八股

### MySQL

- 隔离级别：四个
- 默认隔离界别是什么，如何实现：可重复度，MVCC(讲得比较具体，听到一半就叫停了)
- 三大日志：具体问了binlog的主从复制(不会)

### Redis

- 三个问题：雪崩、击穿、穿透 (过程把击穿和穿透讲混了，给了不好的印象)

- 内存淘汰策略：随机抽样、懒删除、LRU算法(我讲的比较简单给后面埋坑了)、LFU

### RPC

了解不深，感觉可以弄个轮子项目

## 实习或者项目经历困难的点

讲了设计模式的使用，不满意。

## 手撕

LRU算法，get与put操作都是 O1的时间复杂度（八股的时候说双向链表，但是get操作o1不行，最后时间到了才想到Map + Node双向链表，而且还忘了java有自己实现的类LinkedHashMap）

## 反问

- 业务：tiktok海外的生活服务项目
- 面试答得怎么样：代码能力差(手撕没撕出来)、项目简单(希望听到系统架构上自己的考量，比如时间优化，高可用等)、细节不够(击穿和穿透弄混了)

## 鼓励我的好话

- 知识面比较广(mysql和redis我答得比较深)，换句话说只会八股

## 反思

实习经历简单，可以往项目经历上靠，弄个轮子项目或者一些高可用高并发的项目，但是感觉只能支撑到一面，如果想要二面结果，自己得要有系统设计的思想


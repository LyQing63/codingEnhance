# 四周 Java 后端核心学习计划（80-20 法则）

## 第 1 周：Java 基础与数据结构

### 目标：夯实 Java 核心基础，掌握常见数据结构和基本算法。

#### 学习内容
1.	Java 核心语法
- 面向对象编程（OOP）：封装、继承、多态、抽象类、接口
- 常见关键字：static、final、synchronized、volatile
- 异常处理（try-catch-finally、throws、throw）
2.	Java 集合框架
- List（ArrayList vs. LinkedList）
- Set（HashSet vs. TreeSet）
- Map（HashMap vs. TreeMap）
- 手写 LRU 缓存（基于 LinkedHashMap）
3.	常见数据结构与算法
- 数组、链表、栈、队列、哈希表
- 排序算法（冒泡、选择、快速排序）
- 二叉树（遍历、增删查改）

#### 实战任务
- 实现一个 LRU 缓存（使用 LinkedHashMap & 手写实现）
- 手写常见排序算法（如快排、归并排序）

---

## 第 2 周：并发编程与 JVM

### 目标：掌握 Java 并发编程与 JVM 性能优化。

#### 学习内容
1.	Java 线程基础
- 线程的创建（Thread、Runnable、Callable）
- 线程池（ExecutorService、ForkJoinPool）
2.	并发编程
- synchronized、Lock、ReentrantLock
- 线程安全类（ConcurrentHashMap、CopyOnWriteArrayList）
- 线程通信（wait / notify / CountDownLatch）
- 无锁并发（CAS、AtomicInteger）
3.	JVM 调优
- JVM 内存模型（堆、栈、方法区）
- GC 原理（垃圾回收算法、GC 日志分析）
- 类加载机制（双亲委派模型）

#### 实战任务
- 手写线程安全的生产者-消费者模型
- 使用线程池并发处理任务
- 分析 GC 日志，优化 JVM 参数

---

## 第 3 周：Spring 生态与数据库优化

### 目标：掌握 Spring 生态（Spring Boot, Spring MVC）以及数据库优化。

#### 学习内容
1.	Spring Boot 核心
- @Component、@Service、@Controller、@Repository
- Spring AOP（切面编程）
- Spring Boot 配置管理（application.yml）
2.	数据库优化
- MySQL 索引优化（B+ 树、索引命中）
- 事务与隔离级别（ACID, MVCC）
- Redis（数据结构、缓存穿透/击穿/雪崩）
3.	Spring 事务与分布式
- Spring 事务管理（@Transactional）
- 分布式锁（基于 Redis）

#### 实战任务
- 使用 Spring Boot 构建 RESTful API
- 设计高效 SQL 查询，优化索引
- 实现基于 Redis 的分布式锁

---

## 第 4 周：分布式架构与高并发

### 目标：掌握微服务架构、消息队列、分布式事务。

#### 学习内容
1.	微服务架构
- Spring Cloud 组件（Eureka、Feign、Gateway）
- 分布式配置中心（Nacos）
2.	消息队列
- RabbitMQ / Kafka 基本概念
- 消息持久化、可靠性投递
- 延迟队列的实现
3.	高并发设计
- 限流与降级（令牌桶算法、滑动窗口）
- 分布式 ID 生成方案（雪花算法）

#### 实战任务
- 使用 Spring Cloud 构建微服务
- 实现 RabbitMQ 的延迟队列
- 构建一个高并发短链接服务

---

## 五个递增难度的 Java 后端项目

1. 任务管理系统（入门）

描述：实现一个简单的任务管理 API，支持任务的创建、删除、查询、更新。
强化概念：
- Spring Boot 基础
- RESTful API 设计
- MySQL CRUD 操作
- Lombok, Swagger 文档

---

2. 个人博客系统（初级）

描述：实现一个博客系统，包含用户注册、文章发布、评论、点赞等功能。
强化概念：
- 用户认证（JWT / OAuth2）
- MyBatis Plus 数据访问
- Redis 缓存优化
- Spring Security 权限控制

---

3. 分布式短链服务（中级）

描述：构建一个高并发的短链生成系统，支持自定义短链，统计访问量。
强化概念：
- Redis 高性能存储
- 雪花算法生成唯一 ID
- 短 URL 映射（Base62 编码）
- 限流与缓存策略

---

4. 秒杀系统（高级）

描述：构建一个高并发的电商秒杀系统，支持抢购、库存扣减、订单管理。
强化概念：
- 高并发限流（令牌桶/滑动窗口）
- MySQL 行锁优化
- Redis 分布式锁
- RabbitMQ 异步削峰

---

5. 自己实现一个 Redis（进阶）

描述：基于 Java 复现 Redis 核心功能，包括数据存储、LRU 缓存淘汰、持久化（RDB / AOF）。
强化概念：
- 手写 HashMap、跳表
- 实现 LRU/LFU 缓存淘汰
- 多线程与并发控制
- 持久化存储（AOF / RDB）

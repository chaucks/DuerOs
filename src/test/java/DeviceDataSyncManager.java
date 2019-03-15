//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayDeque;
//import java.util.Deque;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Future;
//
///**
// * 数据同步任务管理器
// * 单线程（单例对象）定时同步（业务数据）数据入库入缓存
// *
// * @author Chuck Lee
// */
//@Component
//@Scope("singleton")
//public class DeviceDataSyncManager implements Runnable {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * 任务队列
//     */
//    private final Deque<Runnable> taskDeque = new ArrayDeque<>(8);
//
//    /**
//     * 同步监视器&线程开关
//     */
//    private volatile Boolean monitorSwitch = true;
//
//    /**
//     * 线程空转最大空闲时间毫秒数
//     */
//    private final long maxIdleTimeThreshold = 1000L * 10L;
//
//    /**
//     * 线程自动唤醒等待时间毫秒数
//     */
//    private final long waitTimeOut = 1000L * 30L;
//
//    /**
//     * 线程睡眠时间毫秒数
//     */
//    private final long sleepTime = 1000L * 1;
//
//    /**
//     * 任务Task执行超时时间
//     */
//    private final long taskTimeOut = 1000L * 15L;
//
//    /**
//     * 线程池
//     */
//    @Autowired
//    private Executor processExecutor;
//
//    /**
//     * Current executing task
//     */
//    private volatile Runnable task;
//
//    /**
//     * Future 可用于判断线程是否执行成功
//     */
//    private Future future;
//
//    @Override
//    public void run() {
//        try {
//            /**
//             * 初始化任务执行时间
//             */
//            long taskExecuteTime = System.currentTimeMillis();
//            for (this.task = this.poll(); this.monitorSwitch; this.task = this.poll()) {
//                if (null != task) {
//                    taskExecuteTime = this.executeTaskAndGetTime(task);
//                    continue;
//                }
//                this.sleepWaitIfOutOfMaxIdleTime(taskExecuteTime);
//            }
//        } catch (InterruptedException e) {
//            logger.error("同步（设备使用时长Top10）线程执行失败：", e);
//            this.interrupt();
//        }
//    }
//
//    /**
//     * 执行任务并返回时间
//     *
//     * @param task task
//     * @return
//     */
//    private final long executeTaskAndGetTime(final Runnable task) {
//        final long t0 = System.currentTimeMillis();
//        final Future future = ((ThreadPoolTaskExecutor) processExecutor).submit(task);
//        long t1 = System.currentTimeMillis();
//        for (; !future.isDone(); t1 = System.currentTimeMillis()) {
//            if ((t1 - t0) > this.taskTimeOut) {
//                logger.info("Task:" + task.toString() + "execute more than " + this.taskTimeOut + "milliseconds");
////                future.cancel(false);
//                return t1;
//            }
//        }
//        return t1;
//    }
//
//    /**
//     * 线程先睡眠随后请求对象锁
//     * 如果线程空转超过最大空闲时间释放对象锁并进入等待状态
//     *
//     * @param taskExecuteTime taskExecuteTime
//     * @throws InterruptedException
//     */
//    private final void sleepWaitIfOutOfMaxIdleTime(final long taskExecuteTime) throws InterruptedException {
//        Thread.sleep(this.sleepTime);
//        this.waitIfOutOfMaxIdleTime(taskExecuteTime);
//    }
//
//    /**
//     * 如果线程空转超过最大空闲时间释放对象锁并进入等待状态
//     *
//     * @param taskExecuteTime taskExecuteTime
//     * @throws InterruptedException
//     */
//    private final void waitIfOutOfMaxIdleTime(final long taskExecuteTime) throws InterruptedException {
//        synchronized (this.monitorSwitch) {
//            final long idleTime = System.currentTimeMillis() - taskExecuteTime;
//            if (idleTime > this.maxIdleTimeThreshold) {
//                this.monitorSwitch.wait(waitTimeOut);
//            }
//        }
//    }
//
//    /**
//     * 从任务队列取出任务
//     *
//     * @return
//     */
//    public final Runnable poll() {
//        synchronized (this.monitorSwitch) {
//            return this.taskDeque.poll();
//        }
//    }
//
//    /**
//     * 往任务队列投放任务
//     *
//     * @param task task
//     * @return
//     */
//    public final boolean push(final Runnable task) {
//        synchronized (this.monitorSwitch) {
//            if (!taskEquals(task) && !this.taskDeque.contains(task)) {
//                this.taskDeque.push(task);
//                this.monitorSwitch.notify();
//                return true;
//            }
//            return false;
//        }
//    }
//
//    /**
//     * 启动线程
//     */
//    public final synchronized void startAsync() {
//        if (null == this.future) {
//            this.future = ((ThreadPoolTaskExecutor) processExecutor).submit(this);
//        }
//    }
//
//    /**
//     * 取消执行线程
//     *
//     * @param mayInterruptIfRunning mayInterruptIfRunning
//     */
//    public final void cancel(final boolean mayInterruptIfRunning) {
//        this.monitorSwitch = false;
//        this.future.cancel(mayInterruptIfRunning);
//    }
//
//    /**
//     * 中断当前线程
//     */
//    private final void interrupt() {
//        this.monitorSwitch = false;
//        Thread.currentThread().interrupted();
//    }
//
//    /**
//     * Task 与正在执行的task是否equals
//     *
//     * @param task task
//     * @return
//     */
//    private final boolean taskEquals(final Runnable task) {
//        return null != this.task && this.task.equals(task);
//    }
//
//}

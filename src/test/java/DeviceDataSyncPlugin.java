//import com.ztgm.iot.pojo.User;
//import com.ztgm.iot.service.DataChartService;
//import com.ztgm.iot.service.DeviceStatisticsService;
//import com.ztgm.iot.service.UserActionService;
//import com.ztgm.iot.util.DateUtil;
//import com.ztgm.iot.util.SpringUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.*;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.web.subject.support.WebDelegatingSubject;
//import org.springframework.context.ApplicationContext;
//
//import javax.servlet.ServletRequest;
//import java.text.ParseException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Properties;
//
///**
// * 增删改拦截器
// *
// * @author Chuck Lee
// */
//@Intercepts({
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
//})
//public class DeviceDataSyncPlugin implements Interceptor {
//
//    private volatile DeviceDataSyncManager deviceDataSyncManager;
//
//    public DeviceDataSyncPlugin() {
//        this.deviceDataSyncManager = null;
//        new Thread(() -> {
//            try {
//                final long sleep = 1000L;
//                for (; null == deviceDataSyncManager; Thread.sleep(sleep)) {
//                    final ApplicationContext applicationContext = SpringUtil.getApplicationContext();
//                    if (null != applicationContext) {
//                        this.deviceDataSyncManager = applicationContext.getBean(DeviceDataSyncManager.class);
//                    }
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        final Object result = invocation.proceed();
//        if (null != this.deviceDataSyncManager) {
//            final String sql = getSql(invocation);
//
//            if (!sql.contains("iot_devices_working_time")) {
//                boolean idslBool = sql.contains("iot_device_status_log");
//                boolean irBool = sql.contains("iot_region");
//                boolean irdBool = sql.contains("iot_region_device");
//                boolean idBool = sql.contains("iot_device");
//                boolean idpBool = sql.contains("iot_device_project");
//                boolean igBool = sql.contains("iot_gateway");
//                boolean ipBool = sql.contains("iot_project");
//
//                if (idslBool || irBool || irdBool || idBool || idpBool || igBool || ipBool) {
//                    deviceDataSyncManager.push(new DwtDataSyncTask());
//                }
//
//                if (idslBool || idBool || idpBool || igBool || irdBool || igBool || ipBool) {
//                    deviceDataSyncManager.push(new DtuDataSyncTask());
//                }
//
//                if (irdBool || irBool || idslBool || irBool || igBool || ipBool) {
//                    deviceDataSyncManager.push(new DfrDataSyncTask());
//                }
//
//                boolean iudlBool = sql.contains("iot_user_device_log");
//                boolean iurlBool = sql.contains("iot_user_region_log");
//                boolean iuslBool = sql.contains("iot_user_scene_log");
//                if (iudlBool || iurlBool || iuslBool) {
//                    String userId = getPrincipalUserId();
//                    deviceDataSyncManager.push(new UopDataSyncTask(userId));
//                }
//
//                boolean icBool = sql.contains("iot_combination");
//                if (iuslBool || icBool) {
//                    String userId = getPrincipalUserId();
//                    deviceDataSyncManager.push(new UstDataSyncTask(userId));
//                }
//
//            }
//
//        }
//        return result;
//    }
//
//    @Override
//    public Object plugin(Object o) {
//        return Plugin.wrap(o, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//
//    }
//
//    /**
//     * Get mybatis sql
//     *
//     * @param invocation invocation
//     * @return
//     */
//    private static final String getSql(Invocation invocation) {
//        final Object[] args = invocation.getArgs();
//        final MappedStatement ms = (MappedStatement) args[0];
//        final Object parameterObject = args[1];
//        final BoundSql boundSql = ms.getBoundSql(parameterObject);
//        return boundSql.getSql();
//    }
//
//    /**
//     * Get user id
//     *
//     * @return
//     */
//    private static final String getPrincipalUserId() {
//        try {
//            WebDelegatingSubject subject = (WebDelegatingSubject) SecurityUtils.getSubject();
//            ServletRequest request = subject.getServletRequest();
//            HashMap principal = (HashMap) request.getAttribute("principal");
//            User user = (User) principal.get("user");
//            return user.getId();
//        } catch (Exception e0) {
//            e0.printStackTrace();
//            try {
//                WebDelegatingSubject subject = (WebDelegatingSubject) SecurityUtils.getSubject();
//                HashMap hashMap = (HashMap) subject.getPrincipal();
//                return (String) hashMap.get("userId");
//            } catch (Exception e1) {
//                e1.printStackTrace();
//                return "";
//            }
//        }
//    }
//
//    /**
//     * 设备工作时长统计数据同步任务
//     *
//     * @author Chuck lee
//     */
//    private class DwtDataSyncTask implements Runnable {
//
//        private Date date;
//
//        public DwtDataSyncTask() {
//            try {
//                this.date = DateUtil.parse(new Date(), "yyyy/MM");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void run() {
//            try {
//                SpringUtil.getBean(DeviceStatisticsService.class).selectInsertAndCacheDeviceWorkingTimeLst(this.date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return obj instanceof DwtDataSyncTask && 0 == ((DwtDataSyncTask) obj).getDate().compareTo(this.date);
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + "设备工作时长数据同步任务";
//        }
//
//        public Date getDate() {
//            return date;
//        }
//    }
//
//    /**
//     * 常用设备top10数据同步任务
//     *
//     * @author Chuck Lee
//     */
//    private class DtuDataSyncTask implements Runnable {
//
//        private Date date;
//
//        public DtuDataSyncTask() {
//            try {
//                this.date = DateUtil.parse(new Date(), "yyyy/MM/dd");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void run() {
//            try {
//                DataChartService dataChartService = SpringUtil.getBean(DataChartService.class);
//                dataChartService.removeTopUseDevice(this.date);
//                dataChartService.selectTopUseDevice(this.date);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return obj instanceof DtuDataSyncTask && 0 == ((DtuDataSyncTask) obj).getDate().compareTo(this.date);
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + "常用设备top10数据同步任务";
//        }
//
//        public Date getDate() {
//            return date;
//        }
//    }
//
//    /**
//     * 区域设备使用次数统计数据同步任务
//     *
//     * @author Chuck lee
//     */
//    private class DfrDataSyncTask implements Runnable {
//
//        private String date;
//
//        public DfrDataSyncTask() {
//            this.date = DateUtil.format(new Date(), "yyyy/MM/dd");
//        }
//
//        @Override
//        public void run() {
//            try {
//                DataChartService dataChartService = SpringUtil.getBean(DataChartService.class);
//                dataChartService.removeFrequentUseRegion(this.date);
//                dataChartService.selectFrequentUseRegion(this.date);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return obj instanceof DfrDataSyncTask && ((DfrDataSyncTask) obj).getDate().equals(this.date);
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + "常用设备top10数据同步任务";
//        }
//
//        public String getDate() {
//            return date;
//        }
//    }
//
//    /**
//     * 用户使用习惯统计数据同步任务
//     *
//     * @author Chuck Lee
//     */
//    private class UopDataSyncTask implements Runnable {
//
//        private String userId;
//
//        private String date;
//
//        public UopDataSyncTask(String userId) {
//            this.userId = userId;
//            this.date = DateUtil.format(new Date(), "yyyy/MM/dd");
//        }
//
//        @Override
//        public void run() {
//            try {
//                if (StringUtils.isNotEmpty(this.userId)) {
//                    UserActionService userActionService = SpringUtil.getBean(UserActionService.class);
//                    userActionService.removeUserOperMode(this.userId, this.date);
//                    userActionService.selectUserOperMode(this.userId, this.date);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (obj instanceof UopDataSyncTask) {
//                final UopDataSyncTask item = ((UopDataSyncTask) obj);
//                return item.getUserId().equals(this.userId) && item.getDate().equals(this.date);
//            }
//            return false;
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + "用户使用习惯数据同步任务";
//        }
//
//        public String getUserId() {
//            return userId;
//        }
//
//        public String getDate() {
//            return date;
//        }
//    }
//
//    /**
//     * 用户常用场景Top10统计数据同步任务
//     *
//     * @author Chuck Lee
//     */
//    private class UstDataSyncTask implements Runnable {
//
//        private String userId;
//
//        private String date;
//
//        public UstDataSyncTask(String userId) {
//            this.userId = userId;
//            this.date = DateUtil.format(new Date(), "yyyy/MM/dd");
//        }
//
//        @Override
//        public void run() {
//            try {
//                if (StringUtils.isNotEmpty(this.userId)) {
//                    UserActionService userActionService = SpringUtil.getBean(UserActionService.class);
//                    userActionService.removeTopUserScene(this.userId, this.date);
//                    userActionService.selectTopUserScene(this.userId, this.date);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (obj instanceof UstDataSyncTask) {
//                final UstDataSyncTask item = ((UstDataSyncTask) obj);
//                return item.getUserId().equals(this.userId) && item.getDate().equals(this.date);
//            }
//            return false;
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + "用户使用习惯数据同步任务";
//        }
//
//        public String getUserId() {
//            return userId;
//        }
//
//        public String getDate() {
//            return date;
//        }
//    }
//
//
//}

package pers.msidolphin.mblog.helper;

public class AutoIdHelper {
    private SnowflakeIdWorker idWorker;
    private static volatile AutoIdHelper autoIdHelper;

    private AutoIdHelper() {
        this.idWorker = new SnowflakeIdWorker(0L, 0L);
    }

    public static long getId() {
        if (autoIdHelper == null) {
            synchronized (AutoIdHelper.class) {
                if (autoIdHelper == null) {
                    autoIdHelper = new AutoIdHelper();
                }
            }
        }
        return autoIdHelper.idWorker.nextId();
    }
}

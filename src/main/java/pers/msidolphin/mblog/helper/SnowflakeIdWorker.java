package pers.msidolphin.mblog.helper;

@SuppressWarnings("all")
public class SnowflakeIdWorker {
    private final long twepoch = 1420041600000L;

    // 机器标识位数
    private final long workerIdBits = 5L;

    //数据中心标识位数
    private final long datacenterIdBits = 5L;

    //机器ID最大值
    private final long maxWorkerId = 31L;

    //数据中心最大值 31
    private final long maxDatacenterId = 31L;

    //序列标识位数
    private final long sequenceBits = 12L;

    //机器ID偏左移位数
    private final long workerIdShift = sequenceBits;

    //数据中心ID左移位数
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    //时间戳标识左移位数
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    //序列的最大值 2^12 - 1 = 4095
    private final long sequenceMask = 4095L;

    private long workerId;
    private long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdWorker(long workerId, long datacenterId) {
        //工作机器id和数据中心id共10位，各占5位。所以两者id的最大值为2^5 - 1，即31
        if ((workerId > 31L) || (workerId < 0L)) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", new Object[]{Long.valueOf(31L)}));
        }
        if ((datacenterId > 31L) || (datacenterId < 0L)) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", new Object[]{Long.valueOf(31L)}));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        //生成时间戳
        long timestamp = timeGen(); // return System.currentTimeMillis();
        //判断当前时间戳是否小于上一次生成的时间戳
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", new Object[]{Long.valueOf(this.lastTimestamp - timestamp)}));
        }
        //判断当前时间戳是否等于上一次生成时间戳（同一时间内生成）
        if (this.lastTimestamp == timestamp) {
            //将当前序列+1 并取低12位作为新的序列值
            this.sequence = (this.sequence + 1L & 0xFFF);
            //判断序列是否溢出
            if (this.sequence == 0L) {
                /*
                    循环等待下一毫秒
                    long timestamp = timeGen();
                    while (timestamp <= lastTimestamp) {
                        timestamp = timeGen();
                    }
                    return timestamp
                 */
                timestamp = tilNextMillis(this.lastTimestamp);
            }
        } else {
            //两次ID生成时机不同，序列置为0
            this.sequence = 0L;
        }

        this.lastTimestamp = timestamp;

        //组合成一个64位的ID进行返回
        return timestamp - twepoch << timestampLeftShift |
                this.datacenterId << datacenterIdShift |
                this.workerId << workerIdShift |
                this.sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0L, 0L);
        for (int i = 0; i < 10; i++) {
            long id = idWorker.nextId();

            System.out.println(id);
        }
        System.out.println("--------");
        SnowflakeIdWorker idWorker1 = new SnowflakeIdWorker(1L, 2L);
        for (int i = 0; i < 10; i++) {
            long id = idWorker1.nextId();

            System.out.println(id);
        }
    }
}






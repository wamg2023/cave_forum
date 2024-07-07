package cn.fbi.helper;

import java.util.concurrent.atomic.AtomicInteger;

public class UniqueIdGenerator {
    private static final int START_SEQ = 10000000;//初始序列号
    private static final int MAX_SEQ = 99999999;//最大序列号
    private static final AtomicInteger sequence = new AtomicInteger(START_SEQ);//使用AtomicInteger来保证线程安全的递增

    public static int generateUniqueId(){
        int currentSeq = sequence.incrementAndGet();//递增获取当前序列号
        if(currentSeq > MAX_SEQ) {
            sequence.set(START_SEQ); // 如果超过最大序列号，重新从初始序列号开始
            currentSeq = sequence.get();
        }
        return currentSeq;
    }
}

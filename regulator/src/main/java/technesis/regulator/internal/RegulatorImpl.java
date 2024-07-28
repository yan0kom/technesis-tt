package technesis.regulator.internal;

import technesis.regulator.Regulator;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RegulatorImpl implements Regulator {
    private final List<Float> data;
    private final ReadWriteLock lock;
    private final Random random;

    public RegulatorImpl() {
        random = new Random();
        lock = new ReentrantReadWriteLock(true);
        data = new LinkedList<>();
        data.add(0.0f);
    }

    @Override
    public int adjustTemp(byte operation, float inData, List<Float> outData, int offsetOut) {
        if ((operation & Regulator.reservedBit) == 0) {
            return errReservedBitMissed;
        }

        if ((operation & Regulator.clearCmd) != 0) {
            clear();
        }

        if ((operation & Regulator.setCmd) != 0) {
            if (inData > 1000f) {
                return errTempTooHigh;
            }
            if (inData < -200f) {
                return errTempTooLow;
            }
            set(inData);
        }

        if ((operation & Regulator.getCmd) != 0) {
            int cnt = (operation & Regulator.getSizeMask) >> 1;
            outData.addAll(get(cnt, offsetOut));
        }

        return 0;
    }

    private void clear() {
        try {
            lock.writeLock().lock();
            data.clear();
            data.add(0.0f);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void set(float value) {
        try {
            lock.writeLock().lock();
            int cnt = 3 + random.nextInt( 6);
            float current = data.get(data.size() - 1);
            float step = (value - current) / (cnt + 1);
            for (int i = 0; i < cnt; ++i) {
                data.add(current + step);
                current += step;
            }
            data.add(value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private List<Float> get(int count, int offsetOut) {
        try {
            lock.readLock().lock();
            int fromIdx = data.size() - offsetOut - count;
            if (fromIdx < 0) {
                fromIdx = 0;
            }
            int toIdx = data.size() - offsetOut;
            if (toIdx < 0) {
                toIdx = data.size();
            }
            return data.subList(fromIdx, toIdx);
        } finally {
            lock.readLock().unlock();
        }
    }
}

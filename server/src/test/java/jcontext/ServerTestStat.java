package jcontext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerTestStat {
    private static final Logger log = LoggerFactory.getLogger(ServerTestStat.class);
    private static final TimeUnit MICROS = TimeUnit.MICROSECONDS;

    private final Map<Long, Long> tagToTime = Maps.newConcurrentMap();
    private final List<Long> times = Lists.newArrayList();
    private final TimeUnit timeUnit;

    public ServerTestStat(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void start(long tag) {
        tagToTime.put(tag, System.nanoTime());
    }

    public void end(long tag) {
        times.add(System.nanoTime() - tagToTime.remove(tag));
    }

    public void printStats() {
        times.sort(Long::compareTo);
        double[] values = times.stream().mapToDouble(Long::doubleValue).toArray();

        long avg = MICROS.convert(new Double(new Mean().evaluate(values)).longValue(), timeUnit);
        long min = MICROS.convert(times.get(0), timeUnit);
        long max = MICROS.convert(times.get(times.size() - 1), timeUnit);
        long stdDev = MICROS.convert(new Double(new StandardDeviation().evaluate(values)).longValue(), timeUnit);
        long percentile50 = MICROS.convert(times.get(times.size() / 2), timeUnit);
        long percentile95 = MICROS.convert(times.get(Math.min((times.size() * 99) / 100, times.size() - 1)), timeUnit);
        long count = times.size();

        log.info("avg={} min={} max={} std_dev={} 50th={} 95th={} count={} unit=μs",
                avg, min, max, stdDev, percentile50, percentile95, count);
    }

    public void reset() {
        tagToTime.clear();
        times.clear();
    }
}

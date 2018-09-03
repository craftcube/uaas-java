package com.cffex.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Ming on 2016/4/15.
 */

@Component
public class PerformanceMonitor {
    private static final ThreadLocal<StopWatch> WATCH = new ThreadLocal<StopWatch>() {
        @Override
        protected StopWatch initialValue() {
            return new Slf4JStopWatch(LoggerFactory.getLogger("instrument.org.perf4j.TimingLogger"));
        }
    };
    public static void start(String tag) {
        WATCH.get().start(tag);
    }
    public static void stop(String tag) {
        WATCH.get().stop(tag);
    }
}

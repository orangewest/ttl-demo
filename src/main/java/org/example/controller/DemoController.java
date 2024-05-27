package org.example.controller;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
public class DemoController {

    final ThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @GetMapping("/demo")
    public String demo() {
        String traceId = UUID.randomUUID().toString();
        log.info("traceID:{}",traceId);
        threadLocal.set(traceId);
        log.info("main thread,:{}",threadLocal.get());
        executorService.execute(TtlRunnable.get(()-> log.info("child thread 1:{}",threadLocal.get())));
        CompletableFuture.runAsync(TtlRunnable.get(()-> log.info("child thread 2:{}",threadLocal.get())))
                .thenRunAsync(TtlRunnable.get(()-> log.info("child thread 3:{}",threadLocal.get())));
        return "demo";
    }

}

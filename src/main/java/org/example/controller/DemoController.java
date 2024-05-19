package org.example.controller;

import com.alibaba.ttl.TransmittableThreadLocal;
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
        log.info("111111");
        String traceId = UUID.randomUUID().toString();
        log.info("traceID:{}",traceId);
        threadLocal.set(traceId);
        log.info("222222,:{}",threadLocal.get());
        executorService.execute(()->{
            log.info("3333:{}",threadLocal.get());
        });
        CompletableFuture.runAsync(()->{
            log.info("4444:{}",threadLocal.get());
        }).thenRunAsync(()->{
            log.info("5555:{}",threadLocal.get());
        });
        return "demo";
    }

}

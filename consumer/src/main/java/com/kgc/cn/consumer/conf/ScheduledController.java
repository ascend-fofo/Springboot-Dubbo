package com.kgc.cn.consumer.conf;

import com.kgc.cn.common.service.GoodsService;
import com.kgc.cn.common.service.OrderService;
import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Log4j
public class ScheduledController {
    @Reference
    private GoodsService goodsService;
    //@Scheduled(fixedRate = 1000)
    @Scheduled(cron = "1-3 * * * * ? ")
    public void testScheduled(long gId) {
        double count = goodsService.GoodsCount(gId);
        log.info("{当前库存总件数}"+count);
    }
}

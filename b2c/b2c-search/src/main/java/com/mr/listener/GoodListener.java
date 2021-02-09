package com.mr.listener;

import com.mr.common.constant.MessageConstant;
import com.mr.service.GoodService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodListener {
    @Autowired
    private GoodService goodService;
//监听给spu 的广播 当收到消息的时候跟新es库
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = MessageConstant.SPU_QUEUE_SEARCH_SAVE, durable = "true"),
        exchange = @Exchange(
                value = MessageConstant.ITEM_SPU_EXCHANGE,
                ignoreDeclarationExceptions = "true",
                type = ExchangeTypes.TOPIC
        ),
        key = {MessageConstant.SPU_ROUTINGKEY_SAVE}))
public void save(String spuId){
    System.out.println("接收到spu新增消息 id是 "+spuId);
    goodService.saveGood(Long.valueOf(spuId));
}
//下架
//修改
}


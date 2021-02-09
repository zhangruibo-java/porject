package com.mr.common.constant;

/**
 * 消息队列的名称常量
 */
public class MessageConstant {
    //spu的交换机和routingkey
    public static final String ITEM_SPU_EXCHANGE ="item_spu_exchange";
    public static final String SPU_ROUTINGKEY_SAVE ="spu.save";
    public static final String SPU_ROUTINGKEY_UPDATE ="spu.update";
    public static final String SPU_ROUTINGKEY_DELETE ="spu.delete";

    //es 队列名称
    public static final String SPU_QUEUE_SEARCH_SAVE="spu_queue_search_save";
    public static final String SPU_QUEUE_SEARCH_UPDATE="spu_queue_search_update";
    public static final String SPU_QUEUE_SEARCH_DELETE="spu_queue_search_delete";


}

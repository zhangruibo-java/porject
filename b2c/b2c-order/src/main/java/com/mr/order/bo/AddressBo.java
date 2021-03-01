package com.mr.order.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

//地址对象，由于没有做地址服务，所以写死了两条地址
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressBo {
    private Long id; //主键id
    private String name;//收货人名称
    private String phone;//电话号码
    private String state;//省份
    private String city;//城市
    private String district;//区
    private String address;//街道详细
    private String zipCode;//邮编
    private Boolean isDefault;//是否默认
    private Long userId;//用户id
    //初始化数据，正常应从数据库加载，
    public  static Map<Long, AddressBo> addreddMap;
    static {
        AddressBo add1=new AddressBo(1l,"李雷雷","18888888888","河南省","郑州市","xx区","xx街道1号院","300000",true,7l);
        AddressBo add2=new AddressBo(2l,"韩梅梅","17777777777","北京市","北京市","昌平区","xx街道5号院","100000",false,7l);
        addreddMap=new HashMap<>();
        addreddMap.put(add1.getId(),add1);
        addreddMap.put(add2.getId(),add2);
    }

}

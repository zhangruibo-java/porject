package com.mr.order.cleint;

import com.mr.order.bo.AddressBo;


public class AddressClient  {
    /**
     * 根据地址id查询地址详细
     * @param id
     * @return
     */
    public static AddressBo getAddressByID(Long id){
        return AddressBo.addreddMap.get(id);
    };
}

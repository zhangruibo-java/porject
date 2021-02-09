package com.mr.service;

import com.mr.mapper.UserMapper;
import com.mr.pojo.User;
import com.mr.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Boolean validNameAndPhone(String data, Integer type) {
//如果 type是1 验证用户名 如果是2 验证手机号
        User user = new User();
        if(type==1){
            user.setUsername(data);
        }else if(type==2){
            user.setPhone(data);
        }
        //返回为0 证明数据库不存在记录 可以使用
        //返回为1 证明数据库存在记录 不可以使用
        return userMapper.selectCount(user)==0;
    }

    /**
     * 注册
     * @param user
     * @return
     */
    public boolean register(User user) {
        //设置注册时间
        user.setCreated(new Date());
        //设置uuid为盐
        user.setSalt(Md5Utils.generateSalt());
        //将原始密码进行加密，配合盐再次加密，防止破解
        user.setPassword(Md5Utils.md5Hex(user.getPassword(),user.getSalt()));
       //如果返回结果为1 证明成功 0则失败
        return  userMapper.insert(user)==1;
    }

    /**
     * 根据账号密码查询
     * @param username
     * @param password
     * @return
     */
    public User query(String username, String password) {

        User user = new User();
        user.setUsername(username);
        //根据用户名称查询
        User srcUser= userMapper.selectOne(user);
        if (srcUser==null){//判断数据库是否由此用户
            return null;
        }else if(!srcUser.getPassword().equals(Md5Utils.md5Hex(password,srcUser.getSalt()))){
        //用户存在 判断密码 密码加密和加盐之后进行比较 密码错误
            return null;
        }
        srcUser.setPassword(null);
        srcUser.setSalt(null);
        return srcUser;
    }
}

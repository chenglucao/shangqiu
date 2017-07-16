package com.shangqiu.persistence.mapper;

import java.util.Map;

/**
 * 微信用户
 *
 * Created by caochenglu
 */
public interface UserMapper {

    public Map queryUserByOpenId(Map map);

}

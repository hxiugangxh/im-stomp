package com.us.example.dao.impl;

import com.us.example.bean.ImUser;
import com.us.example.bean.OnlineInfoBean;
import com.us.example.dao.ImDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Repository("imDao")
public class ImDaoImpl implements ImDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(this.jdbcTemplate);
    }

    @Override
    public String getNick(String userName) {
        String querySQL = "select nick from cm_im_user where user_name = :userName";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userName", userName);

        log.info("getNick:\n{}\n参数: {}", querySQL, paramMap);
        List<Map<String, Object>> list = this.getNamedParameterJdbcTemplate().queryForList(querySQL, paramMap);

        return (list.size() > 0) ? MapUtils.getString(list.get(0), "nick", "") : "";
    }

    @Override
    public OnlineInfoBean listOnlineUser(List<String> onlineUserList) {
        String querySQL = "select * from cm_im_user";

        log.info("listOnlineUser: {}", querySQL);
        List<ImUser> list = this.getNamedParameterJdbcTemplate().query(querySQL, new
                BeanPropertyRowMapper(ImUser.class));

        AtomicReference<Integer> userCount = new AtomicReference<>(0);
        List<ImUser> imUserList = list.stream().map(imUser -> {
            if (onlineUserList.contains(imUser.getUserName())) {
                imUser.setOnline("1");
                userCount.getAndSet(userCount.get() + 1);
            }
            return imUser;
        }).collect(Collectors.toList());

        OnlineInfoBean onlineInfoBean = new OnlineInfoBean();
        onlineInfoBean.setImUserList(imUserList);
        onlineInfoBean.setUserCount(userCount.get());

        return onlineInfoBean;
    }
}

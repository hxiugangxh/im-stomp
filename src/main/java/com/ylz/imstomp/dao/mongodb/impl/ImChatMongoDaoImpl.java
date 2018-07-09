package com.ylz.imstomp.dao.mongodb.impl;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.ImUser;
import com.ylz.imstomp.dao.mongodb.ImChatLogMongoJpa;
import com.ylz.imstomp.dao.mongodb.ImChatMongoDao;
import com.ylz.imstomp.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Update.update;

@Slf4j
@Repository("imChatMongoDao")
public class ImChatMongoDaoImpl implements ImChatMongoDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ImChatLogMongoJpa imChatLogMongoJpa;

    @Override
    public Page<ChatMessage> listChatMessage(Integer type, String fromUserName, String toUserName, Integer pn,
                                Integer pageSize) {

        Sort sort = new Sort(DESC, "sendTime");
        Pageable pageable = new PageRequest(pn, pageSize, sort);

        if (type == 1) {
            return new PageImpl(imChatLogMongoJpa.findChatMessagesByType(type, pageable).getContent());
        }

        Query query = new Query(
                new Criteria().orOperator(
                        Criteria.where("fromUserName").is(fromUserName).and("toUserName").is(toUserName),
                        Criteria.where("fromUserName").is(toUserName).and("toUserName").is(fromUserName)
                ))
                .with(sort)
                .with(pageable);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", type);
        paramMap.put("fromUserName", fromUserName);
        paramMap.put("toUserName", toUserName);
        paramMap.put("pn", pn);
        paramMap.put("pageSize", pageSize);

        log.info("listChatMessage--从mongodb获取聊天记录:\n{},\n参数: {}", JSONUtil.format(query.toString()), paramMap);

        List<ChatMessage> chatMessages = mongoTemplate.find(query, ChatMessage.class);

        return new PageImpl(chatMessages);
    }

    @Override
    public List<ChatMessage> listNoReadChatMessage() {

        TypedAggregation<ChatMessage> aggregation = newAggregation(
                ChatMessage.class,

                match(new Criteria().andOperator(
                        Criteria.where("toUserName").exists(true),
                        Criteria.where("readFlag").is(0)
                )),
                group("fromUserName", "toUserName")
                        .first("fromUserName").as("fromUserName")
                        .first("toUserName").as("toUserName")
                        .count().as("noReadCount")
        );

        log.info("listNoReadChatMessage--从mongdb汇聚出未读数据:\n{}", JSONUtil.format(aggregation.toString()));

        List<ChatMessage> chatMessageList = mongoTemplate.aggregate(aggregation, ChatMessage.class).getMappedResults();

        return chatMessageList;
    }

    @Override
    public void readChatMessage(String fromUserName, String toUserName) {
        Query query = new Query(
                new Criteria().andOperator(
                        Criteria.where("fromUserName").is(toUserName),
                        Criteria.where("toUserName").is(fromUserName)
                )
        );

        log.info("readChatMessage: {}", query.toString());
        mongoTemplate.updateMulti(query, update("readFlag", 1), ChatMessage.class);
    }

    @Override
    public ImUser getImUserCount(String fromUserName, String toUserName) {
        TypedAggregation<ChatMessage> aggregation = newAggregation(
                ChatMessage.class,

                match(new Criteria().andOperator(
                        Criteria.where("fromUserName").is(fromUserName),
                        Criteria.where("toUserName").is(toUserName),
                        Criteria.where("readFlag").is(0)
                )),
                group("fromUserName")
                        .first("fromUserName").as("fromUserName")
                        .count().as("noReadCount")
        );

        log.info("getImUserCount--获取对象的未读信息:\n{}: ", JSONUtil.format(aggregation.toString()));

        List<ChatMessage> chatMessageList = mongoTemplate.aggregate(aggregation, ChatMessage.class).getMappedResults();

        ImUser imUser = new ImUser();
        if (CollectionUtils.isNotEmpty(chatMessageList)) {
            imUser.setUserName(chatMessageList.get(0).getFromUserName());
            imUser.setNoReadCount(chatMessageList.get(0).getNoReadCount());
        }

        return imUser;
    }
}

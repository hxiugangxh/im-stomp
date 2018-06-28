package com.ylz.imstomp.dao.mongodb.impl;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.dao.mongodb.ImChatLogMongoJpa;
import com.ylz.imstomp.dao.mongodb.ImChatMongoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@Slf4j
@Repository("imChatMongoDao")
public class ImChatMongoDaoImpl implements ImChatMongoDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ImChatLogMongoJpa imChatLogMongoJpa;

    @Override
    public List<ChatMessage> listChatMessage(Integer type, String fromUserName, String toUserName, Integer pn,
                                             Integer pageSize) {


        Sort sort = new Sort(DESC, "sendTime");
        Pageable pageable = new PageRequest(pn, pageSize, sort);

        if (type == 1) {
            return imChatLogMongoJpa.findChatMessagesByType(type, pageable).getContent();
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
        log.info("listChatMessage--从mongodb获取聊天记录:\n{},\n参数: {}", query.toString(), paramMap);

        return mongoTemplate.find(query, ChatMessage.class);
    }

    @Override
    public List<ChatMessage> listNoReadChatMessage() {

        TypedAggregation<ChatMessage> aggregation = newAggregation(
                ChatMessage.class,

                match( new Criteria().andOperator(
                        Criteria.where("toUserName").exists(true),
                        Criteria.where("readFlag").is(0)
                )),
                group("toUserName")
                        .first("toUserName").as("toUserName")
                        .count().as("noReadCount")
        );

        log.info("listNoReadChatMessage--从mongdb汇聚出未读数据: " + aggregation.toString());

        List<ChatMessage> chatMessageList = mongoTemplate.aggregate(aggregation, ChatMessage.class).getMappedResults();

        System.out.println(chatMessageList);

        return chatMessageList;
    }
}
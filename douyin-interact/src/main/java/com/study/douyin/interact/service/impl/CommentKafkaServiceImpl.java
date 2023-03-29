package com.study.douyin.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.interact.dao.CommentDao;
import com.study.douyin.interact.entity.CommentEntity;
import com.study.douyin.interact.service.CommentKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/29/16:51
 * @Description:
 */
@Service
public class CommentKafkaServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentKafkaService {

    @KafkaListener(topics = "commentQueue")
    public void commentQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
            CommentEntity commentEntity = JSON.parseObject((String) kafkaMessage, CommentEntity.class);
            this.save(commentEntity);
        }
    }

    @KafkaListener(topics = "removeCommentQueue")
    public void removeCommentQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
            String comment_id = JSON.parseObject((String) kafkaMessage, String.class);
            this.removeById(comment_id);
        }
    }
}

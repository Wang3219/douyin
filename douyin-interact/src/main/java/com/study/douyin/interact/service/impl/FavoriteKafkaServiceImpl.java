package com.study.douyin.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.interact.dao.FavoriteDao;
import com.study.douyin.interact.entity.CommentEntity;
import com.study.douyin.interact.entity.FavoriteEntity;
import com.study.douyin.interact.service.FavoriteKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/29/17:28
 * @Description:
 */
public class FavoriteKafkaServiceImpl extends ServiceImpl<FavoriteDao, FavoriteEntity> implements FavoriteKafkaService {

    @KafkaListener(topics = "favoriteQueue")
    public void favoriteQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
            FavoriteEntity favoriteEntity = JSON.parseObject((String) kafkaMessage, FavoriteEntity.class);
            baseMapper.insert(favoriteEntity);
        }
    }

    @KafkaListener(topics = "removeFavoriteQueue")
    public void removeFavoriteQueueListener(ConsumerRecord<?, ?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            //开始处理消息
            String message = JSON.parseObject((String) kafkaMessage, String.class);
            String[] split = message.split(":");
            Long videoId=Long.parseLong(split[0]);
            Long userId=Long.parseLong(split[1]);
            baseMapper.delete(new QueryWrapper<FavoriteEntity>().eq("user_id", userId).eq("video_id", videoId));
        }
    }
}

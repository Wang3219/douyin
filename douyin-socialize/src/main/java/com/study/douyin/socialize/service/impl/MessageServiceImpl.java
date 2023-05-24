package com.study.douyin.socialize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.socialize.dao.MessageDao;
import com.study.douyin.socialize.entity.MessageEntity;
import com.study.douyin.socialize.service.MessageService;
import com.study.douyin.socialize.util.ThreadPool;
import com.study.douyin.socialize.vo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 注入当前service，避免本类调用使用springCache的方法不通过AOP
    @Autowired
    private MessageService messageService;

    @Caching(evict = {
            @CacheEvict(value = "message", key = "#fromUserId"),
            @CacheEvict(value = "message", key = "#toUserId")
    })
    @Override
    public boolean action(long fromUserId, long toUserId, int actionType, String content) {
        // 当前token对应的user不存在
        if (fromUserId == -1)
            return false;
        if (actionType == 1) {
            // 发送消息
            // 创建要存储的数据对象并填充数据
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setFromUserId(fromUserId);
            messageEntity.setToUserId(toUserId);
            messageEntity.setMessageTime(new Date());
            messageEntity.setContent(content);

            int count = baseMapper.insert(messageEntity);

            // 修改redis当前用户最后访问时间
            Date currentTime = new Date();
            stringRedisTemplate.opsForValue().set("visitTime:"+fromUserId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));

            return count == 1;
        }
        // actionType的值不合法
        return false;
    }

    @Override
    public Message[] getMessageList(long fromUserId, long toUserId) throws Exception {
        // 通过当前service调用使用springCache的方法，避免不通过AOP导致注释不生效
        List<MessageEntity> messageEntities = messageService.getMessageFromRedis(fromUserId, toUserId);

        // 对比访问时间，避免返回重复数据
        Date currentTime = new Date();
        String s = stringRedisTemplate.opsForValue().get("visitTime:"+fromUserId);
        stringRedisTemplate.opsForValue().set("visitTime:"+fromUserId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime));

        if (s != null) {
            Date lastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);

            // 两次访问不超过3秒认为是同一个会话框
            if (currentTime.getTime() - lastTime.getTime() < 3000) {
                messageEntities = messageEntities.stream().filter(messageEntity -> {
                    lastTime.setTime(lastTime.getTime()+1000);
                    return messageEntity.getMessageTime().after(lastTime);
                }).collect(Collectors.toList());
            }
        }

        int size = messageEntities.size();
        Message[] messageList = new Message[size];

        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int finalI = i;
            MessageEntity messageEntity = messageEntities.get(finalI);
            // 放入线程池中运行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Message message = new Message();
                message.setId(messageEntity.getId());
                message.setFromUserid(messageEntity.getFromUserId());
                message.setToUserid(messageEntity.getToUserId());
                message.setCreateTime(messageEntity.getMessageTime().getTime());
                message.setContent(messageEntity.getContent());
                messageList[finalI] = message;
            }, ThreadPool.executor);
            futureList.add(future);
        }

        // 阻塞主线程等待，避免主线程提前返回结果，导致数据错误
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        return messageList;
    }

    @Cacheable(value = "message", key = "#fromUserId", sync = true)
    public List<MessageEntity> getMessageFromRedis(long fromUserId, long toUserId) {
        List<MessageEntity> messageEntities = baseMapper.selectList(new QueryWrapper<MessageEntity>()
                .or(i -> i.eq("from_user_id", fromUserId).eq("to_user_id", toUserId))
                .or(i -> i.eq("from_user_id", toUserId).eq("to_user_id", fromUserId)));
        return messageEntities;
    }
}

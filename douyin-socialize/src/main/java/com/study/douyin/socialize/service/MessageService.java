package com.study.douyin.socialize.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.socialize.entity.MessageEntity;
import com.study.douyin.socialize.vo.Message;

import java.text.ParseException;
import java.util.List;

public interface MessageService extends IService<MessageEntity> {
    boolean action(long fromUserId, long toUserId, int actionType, String content);

    Message[] getMessageList(long fromUserId, long toUserId) throws ParseException;
    List<MessageEntity> getMessageFromRedis(long fromUserId, long toUserId);
}

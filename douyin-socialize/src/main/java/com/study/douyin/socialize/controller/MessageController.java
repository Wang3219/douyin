package com.study.douyin.socialize.controller;

import com.study.douyin.common.utils.JwtUtils;
import com.study.douyin.socialize.feign.BasicFeignService;
import com.study.douyin.socialize.service.MessageService;
import com.study.douyin.socialize.vo.Message;
import com.study.douyin.socialize.vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private BasicFeignService basicFeignService;

    @Autowired
    private MessageService messageService;


    @PostMapping("/action")
    public MessageVo action(
            @RequestParam("token") String token,
            @RequestParam("to_user_id") long toUserId,
            @RequestParam("action_type") int actionType,
            @RequestParam("content") String content) {
        if (!JwtUtils.verifyTokenOfUser(token))
            return MessageVo.fail();
        long fromUserId = JwtUtils.getUserId(token);
        boolean flag = messageService.action(fromUserId, toUserId, actionType, content);
        if (flag)
            return MessageVo.success();
        return MessageVo.fail();
    }

    @GetMapping("/chat")
    public MessageVo chat(
            @RequestParam("token") String token,
            @RequestParam("to_user_id") long toUserId) throws ParseException {
        if (!JwtUtils.verifyTokenOfUser(token))
            return MessageVo.fail();
        long fromUserId = JwtUtils.getUserId(token);
        Message[] messageList = new Message[0];
        try {
            messageList = messageService.getMessageList(fromUserId, toUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return MessageVo.fail();
        }
        MessageVo success = MessageVo.success();
        success.setMessageList(messageList);
        return success;
    }

}

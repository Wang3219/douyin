package com.study.douyin.interact.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/29/16:03
 * @Description:
 */
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic commentQueue(){
        return new NewTopic("commentQueue",1, (short) 1);
    }

    @Bean
    public NewTopic removeCommentQueue(){
        return new NewTopic("removeCommentQueue",1, (short) 1);
    }

    @Bean
    public NewTopic favoriteQueue(){
        return new NewTopic("favoriteQueue",1, (short) 1);
    }

    @Bean
    public NewTopic removeFavoriteQueue(){
        return new NewTopic("removeFavoriteQueue",1, (short) 1);
    }

}

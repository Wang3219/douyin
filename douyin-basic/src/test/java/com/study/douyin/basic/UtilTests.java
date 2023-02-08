package com.study.douyin.basic;

import com.study.douyin.basic.util.ModelUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/02/08/15:51
 * @Description:
 */
public class UtilTests {
    public static void main(String[] args) {
        String path = ModelUtil.getPath();
        System.out.println(path);
    }
}

package com.study.douyin.basic.util;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/02/08/15:50
 * @Description:
 */
public class ModelUtil {
    /**
     * 模板文件夹  model (src/main/resources  下)
     * @author hypnos
     */
    final static String MODEL_URL = "model";
    public static String getPath() {
        // 返回项目根路径（编译之后的根路径）
        if(System.getProperties().getProperty("os.name").contains("Windows")) {
            // windows
            return ModelUtil.class.getResource("/").getPath().substring(1).concat(MODEL_URL).concat("/");
        }
        // linux
        return ModelUtil.class.getResource("/").getPath().concat(MODEL_URL).concat("/");
    }
}

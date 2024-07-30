package com.bless.paysystemmanager.bootstrap;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.bless.paysystemmanager.config.SystemYmlConfig;
import com.bless.paysystemservice.impl.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.util.Date;

/**
 * @Author bless
 * @Version 1.0
 * @Description 项目初始化操作
 * 比如初始化配置文件， 读取基础数据， 资源初始化等。 避免在Main函数中写业务代码。
 *  CommandLineRunner  / ApplicationRunner都可以达到要求， 只是调用参数有所不同。
 * @Date 2024-07-20 17:30
 */

@Component
public class initRunner implements CommandLineRunner {
    @Autowired
    private SystemYmlConfig systemYmlConfig;

    @Override
    public void run(String... args) throws Exception {
        //配置是否使用缓存模式
        SysConfigService.IS_USE_CACHE = systemYmlConfig.getCacheConfig();

        //初始化处理fastjson格式
        //当使用fastjson序列化包含'Date'类型的对象时，‘Date’类型的数据将按照'DatePattern.NORM_DATETIME_PATTERN'指定的格式进行转换
        SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();//获取一个全局的序列化配置实例
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer(DatePattern.NORM_DATETIME_PATTERN));//自定义日期格式序列化

        //解决json 序列化时候的  $ref：问题
        /**
         * 般情况下，json序列化时，对象之间存在循环引用，默认的序列化器会使用‘$ref'来表示这些循环引用，防止出现无线递归
         * 以下这样书写后，序列化结果将直接序列化对象属性，而不是‘$ref'
         */
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();

    }
}

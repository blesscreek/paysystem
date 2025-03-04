package com.bless.paysystemmanager.bootstrap;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Arrays;

@SpringBootApplication
//todo 任务调度注解
@EnableSwagger2WebMvc
@MapperScan("com.bless.paysystemservice.mapper")
@ComponentScan(basePackages = "com.bless.*") //由于MainApplication没有在项目根目录， 需要配置basePackages属性使得成功扫描所有Spring组件；
@Configuration
public class PaysystemManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaysystemManagerApplication.class, args);
    }
    /** fastJson 配置信息 **/
    @Bean
    public HttpMessageConverters fastJsonConfig() {
        //新建fast-json转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        //开启fastjson安全模式 防止反序列化过程中潜在的安全风险，反序列化时限制某些类的加载，防止反序列攻击
        ParserConfig.getGlobalInstance().setSafeMode(true);

        //fastjson配置信息 使得转换器在处理日期类型数据时使用指定的日期格式。
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        converter.setFastJsonConfig(config);

        //设置相应的Content-Type 它指定了转换器可以处理 application/json 和 application/json;charset=UTF-8 两种类型的内容。
        converter.setSupportedMediaTypes(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8}));
        return new HttpMessageConverters(converter);
    }
    /** Mybatis plus 分页插件 **/
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        return paginationInterceptor;
    }
    /**
     * 功能描述:  API访问地址： http://localhost:9217/doc.html
     *
     * @Return: springfox.documentation.spring.web.plugins.Docket
     * @Author: terrfly
     * @Date: 2023/6/13 15:04
     */
    @Bean(value = "knife4jDockerBean")
    public Docket knife4jDockerBean() {
        return new Docket(DocumentationType.SWAGGER_2)  //指定使用Swagger2规范
                .apiInfo(new ApiInfoBuilder().version("1.0").build()) //描述字段支持Markdown语法
                .groupName("运营平台") //分组名称
                .select() // 配置： 如何扫描
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) // 只扫描： ApiOperation 注解文档。 也支持配置包名、 路径等扫描模式。
                .build();
    }
}

package com.sadalsuud.push.starter.config;

import io.swagger.annotations.ApiModel;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Description swagger 配置类
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 12/12/2023
 * @Package com.sadalsuud.push.starter.config
 */
@Component
@EnableOpenApi
@ApiModel
public class SwaggerConfiguration {
    /**
     * 对C端用户的接口文档
     * <p>
     * 地址：http://localhost:8080/swagger-ui/index.html
     *
     * @return
     */
    @Bean
    public Docket webApiDoc() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("用户端接口文档")
                .pathMapping("/")
                //定义是否开启Swagger，false是关闭，可以通过变量去控制，线上关闭
                .enable(true)
                //配置文档的元信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sadalsuud.push.adapter.web"))
                //正则匹配请求路径，并分配到当前项目组
                //.paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DoPush平台")
                .description("消息推送接口接口文档")
                .contact(new Contact("sadalsuud", "https://github.com/nemuigaki", "85842843@qq.com"))
                .version("v1.0")
                .build();
    }

}

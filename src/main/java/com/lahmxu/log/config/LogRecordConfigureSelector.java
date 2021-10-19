package com.lahmxu.log.config;

import com.lahmxu.log.config.LogRecordProxyAutoConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 返回需要导入到 spring 容器中的类
 */
public class LogRecordConfigureSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // eg. return new String[]{User.class.getName(),Role.class.getName()};
        return new String[]{LogRecordProxyAutoConfiguration.class.getName()};
    }
}

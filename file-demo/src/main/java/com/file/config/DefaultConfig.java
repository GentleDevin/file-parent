package com.file.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: 数据源配置文件
 * @Description: 多数据参考此模板修改
 * @Author: Devin
 * @CreateDate: 2021/01/08 10:28:55
 **/
@Configuration
@EnableTransactionManagement //开启事务支持
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryDefault", transactionManagerRef = "transactionManagerDefault",
        basePackages = {"com.file.dao"}) //设置Repository所在位置
public class DefaultConfig {

    @Autowired
    @Qualifier("defaultDataSource")
    private DataSource defaultDataSource;

    @Primary
    @Bean(name = "entityManagerDefault")
    public EntityManager entityManagerDefault(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryDefault(builder).getObject().createEntityManager();
    }

    /**
     * @param builder
     * @return
     * @Title: 配置数据源
     * @Description: 获取EntityManagerFactory
     */
    @Bean(name = "entityManagerFactoryDefault")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryDefault(EntityManagerFactoryBuilder builder) {

        return builder
                // 设置数据源
                .dataSource(defaultDataSource)
                // 设置实体类所在位置.扫描所有带有 @Entity 注解的类
                .packages("com.file.entity")
                // Spring会将EntityManagerFactory注入到Repository之中.有了
                // EntityManagerFactory之后,
                // Repository就能用它来创建 EntityManager 了,然后 EntityManager
                // 就可以针对数据库执行操作
                .persistenceUnit("primaryPersistenceUnit") //使用持久化单元配置
                .properties(jpaProperties()).build();
    }


    /**
     * @Title: 实体类与数据库表字段映射
     * @Description: 属性与字段名遵循驼峰命名法
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     **/

    protected Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        return props;
    }


    /**
     * @param builder
     * @return
     * @Title: 配置事务管理器
     */
    @Bean(name = "transactionManagerDefault")
    @Primary
    PlatformTransactionManager transactionManagerDefault(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryDefault(builder).getObject());
    }
}



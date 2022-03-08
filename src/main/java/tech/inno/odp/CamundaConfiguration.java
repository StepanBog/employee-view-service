package tech.inno.odp;

//import org.camunda.bpm.engine.impl.history.HistoryLevel;
//import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

/**
 * @author VKozlov
 */
@Configuration
public class CamundaConfiguration {

//    @Bean
//    public SpringProcessEngineConfiguration processEngineConfiguration(@Autowired DataSourceTransactionManager dataSourceTransactionManager,
//                                                                       @Autowired DataSource dataSource) {
//        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
////        processEngineConfiguration.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_NONE);
//        processEngineConfiguration.setDatabaseSchemaUpdate("true");
//        processEngineConfiguration.setTransactionManager(dataSourceTransactionManager);
//        processEngineConfiguration.setProcessEngineName("my-engine");
//        processEngineConfiguration.setDataSource(dataSource);
//        return processEngineConfiguration;
//    }
}

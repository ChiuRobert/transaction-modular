package transactioncompare.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"transactioncompare.web", "transactioncompare.core"})
public class TransactionComparator {
	
	public static void main(String[] args) {
		SpringApplication.run(TransactionComparator.class, args);
	}
	
//	@Bean
//	CommandLineRunner init(StorageService storageService) {
//		return (args) -> {
//			storageService.deleteAll();
//			storageService.init();
//		};
//	}
}
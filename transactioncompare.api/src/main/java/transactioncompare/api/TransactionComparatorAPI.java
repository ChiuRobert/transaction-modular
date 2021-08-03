package transactioncompare.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"transactioncompare.api", "transactioncompare.core"})
public class TransactionComparatorAPI {
	
	public static void main(String[] args) {
		SpringApplication.run(TransactionComparatorAPI.class, args);
	}
}
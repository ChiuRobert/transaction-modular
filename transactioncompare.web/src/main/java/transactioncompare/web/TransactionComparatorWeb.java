package transactioncompare.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"transactioncompare.web", "transactioncompare.core"})
public class TransactionComparatorWeb {
	
	public static void main(String[] args) {
		SpringApplication.run(TransactionComparatorWeb.class, args);
	}
}
package transactioncompare.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import transactioncompare.api.controller.TransactionCompareAPIController;

@SpringBootApplication
@ComponentScan(basePackages = {"transactioncompare.web", "transactioncompare.core"})
@Import({TransactionCompareAPIController.class})
public class TransactionComparatorWeb {
	
	public static void main(String[] args) {
		SpringApplication.run(TransactionComparatorWeb.class, args);
	}
}
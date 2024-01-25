package com.enviro.assessment.grad001.favourokwara.investment;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.ProductType;
import com.enviro.assessment.grad001.favourokwara.investment.repository.InvestorRepository;
import com.enviro.assessment.grad001.favourokwara.investment.repository.ProductRepository;

@SpringBootApplication
public class InvestmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestmentApplication.class, args);
	}

	// Uncomment bean to populate database (for testing purposes)
	// @Bean CommandLineRunner commandLineRunner(
	// 	InvestorRepository investorRepository,
	// 	ProductRepository productRepository
	// ) {
	// 	return args -> {
	// 		Investor investor1 = new Investor("James", "Bond", "jamesbond007@email.com", "+44 12 345 6789", LocalDate.of(1968, 3, 2));
	// 		Investor investor2 = new Investor("Bilbo", "Baggins", "bilbobaggins@email.com", "+44 98 765 4321", LocalDate.of(1956, 3, 2));

	// 		investorRepository.saveAll(List.of(investor1, investor2));

	// 		Product product1 = new Product("James' Savings", ProductType.SAVINGS, 20000.0);
	// 		product1.setInvestor(investor1);

	// 		Product product2 = new Product("James' RETIREMENT", ProductType.RETIREMENT, 23500.0);
	// 		product2.setInvestor(investor1);

	// 		Product product3 = new Product("Bilbo' Savings", ProductType.SAVINGS, 2200.0);
	// 		product3.setInvestor(investor2);

	// 		Product product4 = new Product("Bilbo' RETIREMENT", ProductType.RETIREMENT, 500.0);
	// 		product4.setInvestor(investor2);

	// 		productRepository.saveAll(List.of(product1, product2, product3, product4));
	// 	};
	// }
}

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

// 	@Bean
// 	CommandLineRunner commandLineRunner(
// 		InvestorRepository investorRepository,
// 		ProductRepository productRepository
// 	) {
// 		return args -> {
// 			Investor investor = new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17));
// 			investorRepository.save(investor);

// 			Product product = new Product("James' Retirement Fund", ProductType.RETIREMENT, 30000.0);
// 			product.setInvestor(investor);

// 			Product product1 = new Product("James' Savings", ProductType.SAVINGS, 4000.0);
// 			product1.setInvestor(investor);

// 			productRepository.saveAll(List.of(product, product1));
// 		};
// 	}
}

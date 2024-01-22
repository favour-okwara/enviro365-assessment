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

	@Bean
	CommandLineRunner commandLineRunner(
		InvestorRepository investorRepository,
		ProductRepository productRepository
	) {
		return args -> {
			Investor investor = new Investor();
			investor.setFirstName("James");
			investor.setLastName("Bond");
			investor.setEmail("jamesBond007@mi6.com");
			investor.setPhoneNumber("+43 44 343 3434");
			investor.setDateOfBirth(LocalDate.of(1983, 1, 23));
			
			investorRepository.save(investor);

			Product product = new Product();
			product.setName("james bond 401K");
			product.setProductType(ProductType.RETIREMENT);
			product.setBalance(700000.0);
			product.setInvestor(investor);

			Product product1 = new Product();
			product1.setName("bond savings");
			product1.setProductType(ProductType.SAVINGS);
			product1.setBalance(50000.0);
			product1.setInvestor(investor);

			productRepository.saveAll(List.of(product, product1));
		};
	}
}

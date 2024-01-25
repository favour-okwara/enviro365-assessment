package com.enviro.assessment.grad001.favourokwara.investment.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.enviro.assessment.grad001.favourokwara.investment.dto.WithdrawalNoticeDTO;
import com.enviro.assessment.grad001.favourokwara.investment.model.BankingDetails;
import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.ProductType;
import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;
import com.enviro.assessment.grad001.favourokwara.investment.service.CSVGeneratorUtil;
import com.enviro.assessment.grad001.favourokwara.investment.service.InvestorService;
import com.enviro.assessment.grad001.favourokwara.investment.service.ProductService;
import com.enviro.assessment.grad001.favourokwara.investment.service.WithdrawalNoticeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InvestorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith( MockitoExtension.class )
public class InvestorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestorService investorService;

    @MockBean
    private ProductService productService;

    @MockBean
    WithdrawalNoticeService withdrawalNoticeService;

    @MockBean
    private CSVGeneratorUtil csvGeneratorUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void InvestorController_GetInvestor_ReturnsInvestor() throws Exception {
        Long investorId = 1L;
        Investor expectedInvestor = new Investor( "James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of( 1960, 10, 17 ) );
        expectedInvestor.setId( investorId );

        when( investorService.getInvestorById( investorId ) ).thenReturn( expectedInvestor );


        mockMvc.perform(get("/api/investors/{id}", investorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedInvestor.getId().intValue()))
                .andExpect(jsonPath("$.firstName").value(expectedInvestor.getFirstName()));

        verify(investorService, times(1)).getInvestorById(investorId);
    }

    @Test
    public void InvestorController_GetInvestorProducts_ReturnsListOfProducts() throws Exception {
        // Arrange
        Long investorId = 1L;
        List<Product> expectedProducts = List.of(
                new Product("Product 1", ProductType.RETIREMENT, 1000.0),
                new Product("Product 2", ProductType.SAVINGS, 2000.0)
        );
        when(productService.getInvestorProducts(investorId)).thenReturn(expectedProducts);

        // Act & Assert
        mockMvc.perform(get("/api/investors/{id}/products", investorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(expectedProducts.size()))
                .andExpect(jsonPath("$[0].name").value(expectedProducts.get(0).getName()))
                .andExpect(jsonPath("$[0].productType").value(expectedProducts.get(0).getProductType().toString()))
                .andExpect(jsonPath("$[0].balance").value(expectedProducts.get(0).getBalance()))
                .andExpect(jsonPath("$[1].name").value(expectedProducts.get(1).getName()))
                .andExpect(jsonPath("$[1].productType").value(expectedProducts.get(1).getProductType().toString()))
                .andExpect(jsonPath("$[1].balance").value(expectedProducts.get(1).getBalance()));

        // Verify
        verify(productService, times(1)).getInvestorProducts(investorId);
    }


    @Test
    public void InvestorController_CreateWithdrawalNotice_ReturnsCreatedStatus() throws Exception {
        // Arrange
        Long investorId = 1L;
        Long productId = 1L;
        Long noticeId = 1L;
        WithdrawalNoticeDTO withdrawalNoticeDTO = new WithdrawalNoticeDTO(300.0, "FNB", "12345678910", LocalDate.of( 2024, 10, 24 ));
        WithdrawalNotice expectedWithdrawalNotice = withdrawalNoticeDTO.toWithdrawalNotice();
        expectedWithdrawalNotice.setId( noticeId );

        when(withdrawalNoticeService.createWithdrawalNotice(investorId, productId, withdrawalNoticeDTO))
                .thenReturn(expectedWithdrawalNotice);

        // Act & Assert
        mockMvc.perform(post("/api/investors/{investorId}/products/{productId}/withdraw", investorId, productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawalNoticeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedWithdrawalNotice.getId().intValue()))
                .andExpect(jsonPath("$.amount").value(expectedWithdrawalNotice.getAmount()));

        // Verify
        verify(withdrawalNoticeService, times(1)).createWithdrawalNotice(investorId, productId, withdrawalNoticeDTO);
    }

    @Test
    public void InvestorController_GetProductWithdrawalNotices_ReturnsCsvFile() throws Exception {
        // Arrange
        Long investorId = 1L;
        Long productId = 1L;
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();

        WithdrawalNotice notice1 = new WithdrawalNotice(200.0, start, new BankingDetails("FNB", "12345678910"));
        notice1.setId( 1L );

        WithdrawalNotice notice2 = new WithdrawalNotice(800.0, start, new BankingDetails("FNB", "48531234512"));
        notice1.setId( 2L );

        List<WithdrawalNotice> withdrawalNotices = List.of(notice1, notice2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyyMMdd" );

        String fileName = "withdrawal-"
                + (start.equals( end ) ? formatter.format( start )  :
                   String.format( "%s-%s", formatter.format( start ), formatter.format( end ) ))
                + ".csv";

        when(withdrawalNoticeService.getProductWithdrawalNotices(investorId, productId, start, end))
                .thenReturn(withdrawalNotices);

        when(csvGeneratorUtil.generateCSVFromWithdrawalNotices(Mockito.anyList()))
                .thenReturn("");

        mockMvc.perform(get("/api/investors/{investorId}/products/{productId}/statements", investorId, productId)
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"" + fileName + "\""))
                .andExpect(content().bytes(csvGeneratorUtil.generateCSVFromWithdrawalNotices( withdrawalNotices ).getBytes( StandardCharsets.UTF_8)));

        // Verify
        verify(withdrawalNoticeService, times(1)).getProductWithdrawalNotices(investorId, productId, start, end);
    }
}

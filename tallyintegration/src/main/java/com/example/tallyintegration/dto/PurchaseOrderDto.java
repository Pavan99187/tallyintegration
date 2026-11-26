
package com.example.tallyintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PurchaseOrderDto(

        @JsonProperty("unique_id")
        Long uniqueId,

        @JsonProperty("erp_name")
        String erpName,

        @JsonProperty("name")
        String name,

        @JsonProperty("company")
        String company,

        @JsonProperty("party")
        String party,

        @JsonProperty("party_unique_id")
        Long partyUniqueId,

        @JsonProperty("party_name")
        String supplierName,

        @JsonProperty("transaction_date")
        LocalDate transactionDate,

        @JsonProperty("required_by")
        LocalDate requiredBy,

        @JsonProperty("items")
        List<Item> items

) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Item(

            @JsonProperty("item_code")
            String itemCode,

            @JsonProperty("item_name")
            String itemName,

            @JsonProperty("uom")
            String uom,

            @JsonProperty("qty")
            BigDecimal qty,

            @JsonProperty("stock_qty")
            BigDecimal stockQty,

            @JsonProperty("stock_uom")
            String stockUom,

            @JsonProperty("conversion_factor")
            BigDecimal conversionFactor,

            @JsonProperty("rate")
            BigDecimal rate,

            @JsonProperty("amount")
            BigDecimal amount,

            @JsonProperty("warehouse")
            String warehouse,

            @JsonProperty("expense_account")
            String expenseAccount,

            @JsonProperty("cost_center")
            String costCenter

    ) {
    }
}

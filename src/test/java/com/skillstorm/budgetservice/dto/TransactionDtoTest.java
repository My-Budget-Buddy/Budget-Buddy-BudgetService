package com.skillstorm.budgetservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

public class TransactionDtoTest {
    @Test
    public void testBean() {
        BeanVerifier.verifyBean(TransactionDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setTransactionId(1);
        transactionDTO1.setUserId(1);
        transactionDTO1.setAccountId(1);
        transactionDTO1.setVendorName("name");
        transactionDTO1.setAmount(0);
        transactionDTO1.setCategory("category");
        transactionDTO1.setDescription("description");
        transactionDTO1.setDate(null);

        TransactionDTO transactionDTO2 = new TransactionDTO();
        transactionDTO2.setTransactionId(1);
        transactionDTO2.setUserId(1);
        transactionDTO2.setAccountId(1);
        transactionDTO2.setVendorName("name");
        transactionDTO2.setAmount(0);
        transactionDTO2.setCategory("category");
        transactionDTO2.setDescription("description");
        transactionDTO2.setDate(null);

        assertEquals(transactionDTO1, transactionDTO2);
        assertEquals(transactionDTO1.hashCode(), transactionDTO2.hashCode());
    }

    @Test
    public void testNotEquals() {
        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setTransactionId(1);
        transactionDTO1.setUserId(1);
        transactionDTO1.setAccountId(1);
        transactionDTO1.setVendorName("name");
        transactionDTO1.setAmount(0);
        transactionDTO1.setCategory("category");
        transactionDTO1.setDescription("description");
        transactionDTO1.setDate(null);

        TransactionDTO transactionDTO2 = new TransactionDTO();
        transactionDTO2.setTransactionId(2);
        transactionDTO2.setUserId(2);
        transactionDTO2.setAccountId(2);
        transactionDTO2.setVendorName("name");
        transactionDTO2.setAmount(0);
        transactionDTO2.setCategory("category");
        transactionDTO2.setDescription("description");
        transactionDTO2.setDate(null);

        assertNotEquals(transactionDTO1, transactionDTO2);
        assertNotEquals(transactionDTO1.hashCode(), transactionDTO2.hashCode());
    }
}

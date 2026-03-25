package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import io.github.vantiv.sdk.generate.*;

import org.hamcrest.MatcherAssert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class TestQueryTransaction {

    private static CnpOnline cnp;
    private static Properties p;
    private static CnpOnline cnpOnlineMock;
    QueryTransaction queryTransaction;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cnp = new CnpOnline();
        p = Mockito.mock(Properties.class);
        cnpOnlineMock = Mockito.mock(CnpOnline.class);
    }

    @Test
    public void simpleQueryTransaction() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse) response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        //assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found", queryTransactionResponse.getMessage());
        assertEquals(1, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }

    @Test
    public void simpleQueryTransaction_showStatusOnly() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");
        queryTransaction.setShowStatusOnly(YesNoType.Y);

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse) response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found", queryTransactionResponse.getMessage());
        assertEquals(1, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }

    @Test
    public void simpleQueryTransaction_multipleResponses() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId2");
        queryTransaction.setOrigActionType(ActionTypeEnum.D);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse) response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals("Original transaction found", queryTransactionResponse.getMessage());
        assertEquals(2, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }

    @Test
    public void simpleQueryTransaction_transactionNotFound() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId0");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        queryTransaction.setReportGroup("default");

        TransactionTypeWithReportGroup response = cnp.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse) response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("151", queryTransactionResponse.getResponse());
        assertEquals("Original transaction not found", queryTransactionResponse.getMessage());
        assertNull(queryTransactionResponse.getResultsMax10());
        assertEquals("sandbox", queryTransactionResponse.getLocation());
    }

    @Test
    public void simpleQueryTransaction_transactionNotFoundWrongActionType() throws Exception {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("orgId5");
        queryTransaction.setOrigActionType(ActionTypeEnum.R);
        queryTransaction.setReportGroup("default");
    }
}
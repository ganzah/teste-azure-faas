package br.unb.ppgi.tsc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpStatus;


public class FunctionTest {
    @Test
    public void testHttpTriggerJava() throws Exception {
        // Setup
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<RequisicaoDeAlinhamento>> req = mock(HttpRequestMessage.class);

        var alignmentRequest = new RequisicaoDeAlinhamento("ACGT", "GCTA", -1, 1, -1);
        when(req.getBody()).thenReturn(Optional.of(alignmentRequest));

        when(req.createResponseBuilder(any(HttpStatus.class))).thenAnswer(invocation -> {
            var status = (HttpStatus) invocation.getArgument(0);
            return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
        });

        final ExecutionContext context = mock(ExecutionContext.class);
        when(context.getLogger()).thenReturn(Logger.getGlobal());

        // Invoke
        var ret = new Function().runScore(req, context);

        // Verify
        assertEquals(ret.getStatus(), HttpStatus.OK);
    }
}

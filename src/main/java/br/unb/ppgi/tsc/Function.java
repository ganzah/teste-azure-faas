package br.unb.ppgi.tsc;

import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import br.unb.ppgi.tsc.hirshcberg.Alinhador;
import br.unb.ppgi.tsc.hirshcberg.Alinhamento;
import br.unb.ppgi.tsc.hirshcberg.Parametros;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    @FunctionName("HirschbergAlignment")
    public HttpResponseMessage runAlignment(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<RequisicaoDeAlinhamento>> request,
            final ExecutionContext context) {
        return executar(request, false);
    }

    @FunctionName("HirschbergScore")
    public HttpResponseMessage runScore(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<RequisicaoDeAlinhamento>> request,
            final ExecutionContext context) {
        return executar(request, true);
    }

    private HttpResponseMessage executar(HttpRequestMessage<Optional<RequisicaoDeAlinhamento>> request, boolean apenasScore) {
        return request.getBody()
            .map(requisicao -> executar(requisicao, apenasScore))
            .map(alinhamento -> montarResposta(alinhamento, request))
            .orElse(request.createResponseBuilder(HttpStatus.BAD_REQUEST).build());
    }

    private HttpResponseMessage montarResposta(Alinhamento alinhamento, HttpRequestMessage<?> request) {
        return request.createResponseBuilder(HttpStatus.OK)
            .body(alinhamento)
            .build();
    }

    private Alinhamento executar(RequisicaoDeAlinhamento requisicaoDeAlinhamento, boolean apenasScore) {
        var parametros = parametros(requisicaoDeAlinhamento, apenasScore);
        var alinhador = new Alinhador(parametros);
        return alinhador.alinhar(requisicaoDeAlinhamento.getSeq1(), requisicaoDeAlinhamento.getSeq2());
    }

    private Parametros parametros(RequisicaoDeAlinhamento requisicaoDeAlinhamento, boolean apenasScore) {
        return new Parametros(
                requisicaoDeAlinhamento.getGap(),
                requisicaoDeAlinhamento.getMatch(),
                requisicaoDeAlinhamento.getMismatch(),
                apenasScore);
    }
}

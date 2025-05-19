/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes.geradorCodigo;

import com.super_bits.modulosSB.SBCore.UtilGeral.MapaAcoesSistema;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringsCammelCase;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ItfAcaoController;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ItfAcaoControllerEntidade;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ItfAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ItfAcaoSelecionarAcao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.permissoes.ItfAcaoFormulario;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.permissoes.ItfAcaoFormularioEntidade;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.permissoes.ItfAcaoGerenciarEntidade;
import org.jboss.forge.roaster.model.source.JavaClassSource;

/**
 *
 * @author SalvioF
 */
public class GeradorGetAcaoDaGestao extends GeradorClasseEscopoApp {

    private final ItfAcaoGerenciarEntidade pAcao;
    private boolean codigoGerado = false;

    public GeradorGetAcaoDaGestao(ItfAcaoGerenciarEntidade pAcao) {
        super("org.coletivoJava.superBitsFW.webPaginas.config", getNomeClasseGetAcoesGestao(pAcao));
        this.pAcao = pAcao;
        gerarCodigo();

    }

    private static String getNomeClasseGetAcoesGestao(ItfAcaoGerenciarEntidade pAcao) {

        return "Acoes"
                + UtilSBCoreStringsCammelCase.getCamelByTextoPrimeiraLetraMaiusculaSemCaracterEspecial(pAcao.getModulo().getEnumVinculado().toString())
                + "_"
                + UtilSBCoreStringsCammelCase.getCamelByTextoPrimeiraLetraMaiusculaSemCaracterEspecial(pAcao.getEnumAcaoDoSistema().toString());
    }

    public static void adicionarAcao(JavaClassSource estruturaClasse, ItfAcaoDoSistema pAcao) {
        String nomePropriedade = UtilSBCoreStringsCammelCase.getCamelByTextoPrimeiraLetraMaiusculaSemCaracterEspecial(pAcao.getEnumAcaoDoSistema().toString());

        String nomeMetodo = "get" + nomePropriedade;

        Class tipoRetorno = ItfAcaoDoSistema.class;
        switch (pAcao.getTipoAcaoSistema()) {
            case ACAO_DO_SISTEMA:
                tipoRetorno = ItfAcaoDoSistema.class;

                break;
            case ACAO_ENTIDADE_FORMULARIO:
                tipoRetorno = ItfAcaoFormularioEntidade.class;
                break;
            case ACAO_FORMULARIO:
                tipoRetorno = ItfAcaoFormulario.class;
                break;
            case ACAO_ENTIDADE_FORMULARIO_MODAL:
                tipoRetorno = ItfAcaoFormulario.class;
                break;
            case ACAO_ENTIDADE_GERENCIAR:
                tipoRetorno = ItfAcaoFormulario.class;
                break;
            case ACAO_ENTIDADE_CONTROLLER:
                tipoRetorno = ItfAcaoControllerEntidade.class;
                break;
            case ACAO_CONTROLLER:
                tipoRetorno = ItfAcaoController.class;
                break;
            case ACAO_SELECAO_DE_ACAO:
                tipoRetorno = ItfAcaoSelecionarAcao.class;
                break;
            default:
                throw new AssertionError(pAcao.getTipoAcaoSistema().name());

        }

        //+ "" + pAcao.getNomeUnico() + " }";
        estruturaClasse.addMethod()
                .setPublic()
                .setName(nomeMetodo)
                .setReturnType(tipoRetorno)
                .setBody(" return  (" + tipoRetorno.getSimpleName() + ") MapaAcoesSistema.getAcaoDoSistemaByNomeUnico(\"" + pAcao.getNomeUnico() + "\");");

    }

    public final JavaClassSource gerarCodigo() {
        if (codigoGerado) {
            return getCodigoJava();
        }

        System.out.println("AcoesVinculadas=" + pAcao.getAcoesVinculadas());

        getCodigoJava().addImport(MapaAcoesSistema.class);
        adicionarAcao(getCodigoJava(), pAcao);

        for (ItfAcaoDoSistema acao : pAcao.getAcoesVinculadas()) {
            adicionarAcao(getCodigoJava(), acao);
        }

        return getCodigoJava();
    }

}

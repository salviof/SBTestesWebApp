/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes.geradorCodigo;

import com.super_bits.modulosSB.SBCore.UtilGeral.MapaAcoesSistema;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCStringsCammelCase;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ComoAcaoController;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ComoAcaoControllerEntidade;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ComoAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ComoAcaoSelecionarAcao;
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
                + UtilCRCStringsCammelCase.getCamelByTextoPrimeiraLetraMaiusculaSemCaracterEspecial(pAcao.getModulo().getEnumVinculado().toString())
                + "_"
                + UtilCRCStringsCammelCase.getCamelByTextoPrimeiraLetraMaiusculaSemCaracterEspecial(pAcao.getEnumAcaoDoSistema().toString());
    }

    public static void adicionarAcao(JavaClassSource estruturaClasse, ComoAcaoDoSistema pAcao) {
        String nomePropriedade = UtilCRCStringsCammelCase.getCamelByTextoPrimeiraLetraMaiusculaSemCaracterEspecial(pAcao.getEnumAcaoDoSistema().toString());

        String nomeMetodo = "get" + nomePropriedade;

        Class tipoRetorno = ComoAcaoDoSistema.class;
        switch (pAcao.getTipoAcaoSistema()) {
            case ACAO_DO_SISTEMA:
                tipoRetorno = ComoAcaoDoSistema.class;

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
                tipoRetorno = ComoAcaoControllerEntidade.class;
                break;
            case ACAO_CONTROLLER:
                tipoRetorno = ComoAcaoController.class;
                break;
            case ACAO_SELECAO_DE_ACAO:
                tipoRetorno = ComoAcaoSelecionarAcao.class;
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

        for (ComoAcaoDoSistema acao : pAcao.getAcoesVinculadas()) {
            adicionarAcao(getCodigoJava(), acao);
        }

        return getCodigoJava();
    }

}

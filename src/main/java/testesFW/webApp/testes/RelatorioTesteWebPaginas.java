/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.MapaAcoesSistema;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCReflexaoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfModuloAcaoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ComoAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.permissoes.ItfAcaoGerenciarEntidade;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.FabMensagens;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.FabTipoAgenteDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.InfoErroSBCoreFW;

import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabricaAcoes;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.tratamentoErros.ItfInfoErroSB;

import java.util.ArrayList;
import java.util.List;
import org.coletivojava.fw.api.objetoNativo.mensagem.Mensagem;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import testesFW.RelatorioTesteAbstrato;

/**
 *
 * @author desenvolvedor
 */
public abstract class RelatorioTesteWebPaginas extends RelatorioTesteAbstrato {

    @Override
    public List<ItfInfoErroSB> executarTestesBanco() {
        List<ItfInfoErroSB> erros = new ArrayList<>();
        List<Class> clasesesObjetos = new ArrayList<>();
        MapaObjetosProjetoAtual.getListaTodosEstruturaObjeto().forEach(es -> clasesesObjetos.add(MapaObjetosProjetoAtual.getClasseDoObjetoByNome(es.getNomeEntidade())));
        for (Class entidade : clasesesObjetos) {
            try {
                System.out.println("entidade::::" + entidade.getSimpleName());
                UtilCRCReflexaoObjeto.getNomeDoObjetoPorAnotacaoInfoClasse(entidade);
            } catch (Throwable t) {
                InfoErroSBCoreFW errro = new InfoErroSBCoreFW();
                errro.configurar(new Mensagem(FabTipoAgenteDoSistema.DESENVOLVEDOR, FabMensagens.ERRO, t.getMessage()), FabErro.ARQUIVAR_LOG, t);
                erros.add(errro);
            }
        }
        return erros;
    }

    @Override
    public List<ItfInfoErroSB> executarTestesBancoAcoes() {
        List<ItfInfoErroSB> erros = new ArrayList<>();
        erros.addAll(executarTestesAcoes());
        erros.addAll(executarTestesBanco());
        return erros;
    }

    public List<ItfInfoErroSB> gerarMangedBeansPgs(ComoFabricaAcoes pAcao) {
        //    ComoAcaoDoSistema acao = UtilSBDevelGeradorCodigoWeb.gerarCodigoGetAcoesGestao(pAcao);
        //AcaoGestaoEntidade acaogestao =
        throw new UnsupportedOperationException("Não foi implementado");
    }

    public List<ItfInfoErroSB> gerarMangedBeansPgs() {

        throw new UnsupportedOperationException("Não foi implementado");

    }

    public List<ItfInfoErroSB> gerarMangedBeansAcessoAcoes() {

        List<ItfInfoErroSB> erros = new ArrayList<>();

        for (ComoAcaoDoSistema acao : MapaAcoesSistema.getListaTodasAcoes()) {

            try {
                if (acao.isUmaAcaoGestaoDominio()) {

                    UtilSBDevelGeradorCodigoWeb.gerarGetAppScopeAcoesDeGestaoDoProjeto(acao.getComoGestaoEntidade());

                }
            } catch (Throwable t) {
                InfoErroSBCoreFW errro = new InfoErroSBCoreFW();
                errro.configurar(new Mensagem(FabTipoAgenteDoSistema.DESENVOLVEDOR, FabMensagens.ERRO, t.getMessage()), FabErro.ARQUIVAR_LOG, t);
                erros.add(errro);
            }

        }
        return erros;

    }

    @Override
    public List<ItfInfoErroSB> executarTestesAcoes() {
        List<ItfInfoErroSB> erros = new ArrayList<>();

        for (ComoAcaoDoSistema acao : MapaAcoesSistema.getListaTodasAcoes()) {

            try {
                UtilTestePagina.testaconfigIcone(acao.getEnumAcaoDoSistema());
                if (acao.isUmaAcaoFormulario()) {

                    UtilTestePagina.testaAcaoFormulario(acao.getComoFormulario());
                }
            } catch (Throwable t) {
                InfoErroSBCoreFW errro = new InfoErroSBCoreFW();
                errro.configurar(new Mensagem(FabTipoAgenteDoSistema.DESENVOLVEDOR, FabMensagens.ERRO, t.getMessage()), FabErro.ARQUIVAR_LOG, t);
                erros.add(errro);
            }

        }
        return erros;

    }

    public void criarLayoutsDeObjetos() {
        UtilSBDevelGeradorCodigoWeb.gerarGetAppScopeContainersDeObjetos();
    }

    public void criarMapaDeAcoesEscopoAplicacao() {
        for (ItfModuloAcaoSistema modulo : MapaAcoesSistema.getModulos()) {
            for (ItfAcaoGerenciarEntidade gestao : MapaAcoesSistema.getAcoesGestaoByModulo(modulo)) {
                if (SBCore.getNomeProjeto().equals("webApp")) {
                    UtilSBDevelGeradorCodigoWeb.gerarGetAppScopeAcoesDeGestaoDoProjeto(gestao);
                }
            }
        }
    }

}

/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.permissoes.ItfAcaoGerenciarEntidade;
import com.super_bits.modulosSB.SBCore.modulos.ManipulaArquivo.UtilCRCArquivoTexto;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.icones.ComoFabricaIcone;
import com.super_bits.modulosSB.SBCore.modulos.view.componenteObjeto.ContainersVisualizacaoDoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.view.fabricasCompVisual.ComoFabTipoComponenteVisual;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import testesFW.webApp.testes.geradorCodigo.GeradorGetAcaoDaGestao;
import testesFW.webApp.testes.geradorCodigo.GeradorGetComponentesVisuais;
import testesFW.webApp.testes.geradorCodigo.GeradorGetIcones;
import testesFW.webApp.testes.geradorCodigo.GeradorGetVisualizacaoContainer;

/**
 *
 * @author desenvolvedor
 */
public class UtilSBDevelGeradorCodigoWeb {

    public static void gerarGetAppScopeAcoesDeGestaoDoProjeto(ItfAcaoGerenciarEntidade pAcao) {
        try {
            GeradorGetAcaoDaGestao geradorGetAcaoAppScopo = new GeradorGetAcaoDaGestao(pAcao);
            UtilCRCArquivoTexto.escreverEmArquivoSubstituindoArqAnterior(geradorGetAcaoAppScopo.getCaminhoLocalSalvarCodigo(), geradorGetAcaoAppScopo.getCodigoJava().toString());
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro gerando Bean escopo de aplicação para ação de gestão:" + pAcao, t);
        }
    }

    public static void criarClassesDeComponentes(Class<? extends ComoFabTipoComponenteVisual> pFabrica, boolean componenteNativo) {
        GeradorGetComponentesVisuais geradorComp = new GeradorGetComponentesVisuais(pFabrica, componenteNativo);
        geradorComp.gerarCodigo();
        String conteudoClasse = geradorComp.getCodigoJava().toString();
        String arquivo = geradorComp.getCaminhoLocalSalvarCodigo();
        UtilCRCArquivoTexto.escreverEmArquivoSubstituindoArqAnterior(arquivo, conteudoClasse);

    }

    public static void criarClassesDeIcones(Class<? extends ComoFabricaIcone> pFabrica, boolean componenteNativo) {

        GeradorGetIcones geradorGetIcones = new GeradorGetIcones(pFabrica, componenteNativo);
        geradorGetIcones.gerarCodigo();
        UtilCRCArquivoTexto.escreverEmArquivoSubstituindoArqAnterior(geradorGetIcones.getCaminhoLocalSalvarCodigo(), geradorGetIcones.getCodigoJava().toString());

    }

    public static void gerarGetAppScopeContainersDeObjetos() {

        for (EstruturaDeEntidade objEst : MapaObjetosProjetoAtual.getListaTodosEstruturaObjeto()) {
            Class classe = MapaObjetosProjetoAtual.getClasseDoObjetoByNome(objEst.getNomeEntidade());
            if (new ContainersVisualizacaoDoObjeto(classe).isPossuiVisualizacoes()) {
                GeradorGetVisualizacaoContainer geradorContainers = new GeradorGetVisualizacaoContainer(classe);
                UtilCRCArquivoTexto.escreverEmArquivoSubstituindoArqAnterior(geradorContainers.getCaminhoLocalSalvarCodigo(), geradorContainers.getCodigoJava().toString());
            }
        }

    }

}

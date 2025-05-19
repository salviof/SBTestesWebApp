/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes.geradorCodigo;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringValidador;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringsMaiuculoMinusculo;
import com.super_bits.modulosSB.SBCore.modulos.view.componenteObjeto.ContainerVisualizacaoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.view.componenteObjeto.ContainersVisualizacaoDoObjeto;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

/**
 *
 * @author SalvioF
 */
public class GeradorGetVisualizacaoContainer extends GeradorClasseEscopoApp {

    private final Class classeReferencia;
    private boolean gerouCodigo = false;
    private final ContainersVisualizacaoDoObjeto containersVisuais;

    public GeradorGetVisualizacaoContainer(Class pClasse) {
        super("com.super_bits.proj." + SBCore.getGrupoProjeto() + ".webApp.constantes", "Container" + pClasse.getSimpleName());
        classeReferencia = pClasse;
        containersVisuais = new ContainersVisualizacaoDoObjeto(pClasse);
        if (!containersVisuais.isPossuiVisualizacoes()) {
            throw new UnsupportedOperationException();
        } else {
            gerarCodigo();
        }

    }

    @Override
    public final JavaClassSource gerarCodigo() {
        if (gerouCodigo) {
            return getCodigoJava();
        }
        for (String visualizacao : containersVisuais.getListaVisualizacoesPossiveis()) {
            containersVisuais.gerarCaminhoRelativoMasProximoAColuna(visualizacao, 0, gerouCodigo);
            getCodigoJava().addImport(classeReferencia);
            MethodSource<JavaClassSource> constructor = getCodigoJava().addMethod();
            getCodigoJava().addProperty(ContainersVisualizacaoDoObjeto.class, "containersVisuais")
                    .setMutable(false);
            constructor.setBody("this.containersVisuais=new ContainersVisualizacaoDoObjeto(" + classeReferencia.getSimpleName() + ");");
            constructor.setConstructor(true);

            for (ContainerVisualizacaoObjeto containerVisual : containersVisuais.getContainerPorTipoVisualizacao(visualizacao).values()) {

                getCodigoJava().addMethod()
                        .setPublic()
                        .setName("getTipoVisao" + UtilSBCoreStringsMaiuculoMinusculo.getPrimeiraLetraMaiusculo(visualizacao) + containerVisual.getColunas() + "ColDesk")
                        .setReturnType(ContainerVisualizacaoObjeto.class)
                        .setBody(" return  containersVisuais.getContainerAdequado(\"" + visualizacao + "\"," + containerVisual.getColunas() + ",false);");
                if (containerVisual.isTemVersaoMobile()) {
                    getCodigoJava().addMethod()
                            .setPublic()
                            .setName("getTipoVisao" + UtilSBCoreStringsMaiuculoMinusculo.getPrimeiraLetraMaiusculo(visualizacao) + containerVisual.getColunas() + "ColMobile")
                            .setReturnType(String.class)
                            .setBody(" return  containersVisuais.getContainerAdequado(\"" + visualizacao + "\"," + containerVisual.getColunas() + ",true);");
                }

            }

        }

        gerouCodigo = true;
        return getCodigoJava();
    }

}

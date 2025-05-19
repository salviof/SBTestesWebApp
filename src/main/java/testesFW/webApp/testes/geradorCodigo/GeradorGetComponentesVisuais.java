/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes.geradorCodigo;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringFiltros;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringsMaiuculoMinusculo;

import com.super_bits.modulosSB.SBCore.modulos.view.fabricasCompVisual.FabFamiliaCompVisual;
import com.super_bits.modulosSB.SBCore.modulos.view.fabricasCompVisual.ItfComponenteVisualSB;

import com.super_bits.modulosSB.SBCore.modulos.view.fabricasCompVisual.ItfFabTipoComponenteVisual;

import org.coletivojava.fw.api.objetoNativo.view.componente.FamiliaComponente;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 *
 * @author SalvioF
 */
public class GeradorGetComponentesVisuais extends GeradorClasseEscopoApp {

    private final boolean componenteNativo;
    private final Class<? extends ItfFabTipoComponenteVisual> fabricaFamiliaComponente;
    private boolean foiGerado = false;

    public GeradorGetComponentesVisuais(Class<? extends ItfFabTipoComponenteVisual> pFabrica, boolean pComponenteNativo) {
        super("org.coletivoJava.superBitsFW.webPaginas.constantes.componentes", pFabrica.getSimpleName().replace("Fab", "CompSB"), null);
        componenteNativo = pComponenteNativo;
        fabricaFamiliaComponente = pFabrica;
    }

    private void adicionaComponente(ItfFabTipoComponenteVisual pcomponente, JavaClassSource pEstruturaClasse) {

        pEstruturaClasse.addProperty(ItfComponenteVisualSB.class,
                UtilSBCoreStringsMaiuculoMinusculo.getPrimeiraLetraMinuscula(UtilSBCoreStringFiltros.gerarUrlAmigavel(pcomponente.getRegistro().getNomeComponente())))
                .setMutable(false);

    }

    private void adicionaFamiliaComponente(FabFamiliaCompVisual pFamilia, JavaClassSource pEstruturaClasse) {

        PropertySource prop = pEstruturaClasse.addProperty(FamiliaComponente.class, "familia").setMutable(false);

    }

    @Override
    public JavaClassSource gerarCodigo() {
        if (!componenteNativo) {
            throw new UnsupportedOperationException("Metodo criar componente específico não foi criado");
        }
        if (foiGerado) {
            return getCodigoJava();
        }

        getCodigoJava().addImport(fabricaFamiliaComponente);
        getCodigoJava().addImport(SBCore.class);
        getCodigoJava().addImport(FabErro.class);

        String corpoCosntructor = "";
        FabFamiliaCompVisual familia = fabricaFamiliaComponente.getEnumConstants()[0].getFamilia();

        corpoCosntructor += "this.familia = " + fabricaFamiliaComponente.getTypeName() + ".class.getEnumConstants()[0].getFamilia().getRegistro(); ";
        for (ItfFabTipoComponenteVisual pcomp : fabricaFamiliaComponente.getEnumConstants()) {
            String nomeVariavel = UtilSBCoreStringsMaiuculoMinusculo.getPrimeiraLetraMinuscula(UtilSBCoreStringFiltros.gerarUrlAmigavel(pcomp.getRegistro().getNomeComponente()));
            corpoCosntructor += "this." + nomeVariavel + " = " + fabricaFamiliaComponente.getSimpleName() + "." + pcomp.toString() + ".getRegistro(); ";
        }

        MethodSource<JavaClassSource> metodoConstructor = getCodigoJava().addMethod();
        System.out.println("Constructor:" + corpoCosntructor);
        metodoConstructor.setBody(corpoCosntructor);
        metodoConstructor.setConstructor(true);

        MethodSource metodoGetPadrao = getCodigoJava().addMethod();
        metodoGetPadrao.setName("getComponentePadrao");
        metodoGetPadrao.setReturnType(ItfComponenteVisualSB.class);
        metodoGetPadrao.setPublic();
        metodoGetPadrao.addParameter(ItfComponenteVisualSB.class, "pComponente");
        metodoGetPadrao.setBody(" "
                + ""
                + "  try {"
                + "            if (pComponente == null) {"
                + "                return familia.getFabrica().getComponentePadrao();"
                + "            } else {"
                + "                if (!pComponente.getFamilia().equals(familia.getFabrica())) { "
                + "                    throw new UnsupportedOperationException(\"O layout enviado não pertence a família de componentes compatíveis,"
                + "\" + pComponente.getNomeComponente() + \"é da família: \" + pComponente.getFamilia().getNomeFAmilia() + "
                + "\"incompativel com \" + familia.getNome());"
                + "                }"
                + "                return pComponente;"
                + "            }"
                + "  } catch (Throwable t) {\n"
                + "            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, \"Erro obtendo componente padrão para \" + familia.getNome(), t);\n"
                + "            return null;\n"
                + "        }"
                + ""
                + "");

        adicionaFamiliaComponente(familia, getCodigoJava());
        for (ItfFabTipoComponenteVisual pcomp : fabricaFamiliaComponente.getEnumConstants()) {
            adicionaComponente(pcomp, getCodigoJava());
        }
        foiGerado = true;
        return getCodigoJava();

    }

}

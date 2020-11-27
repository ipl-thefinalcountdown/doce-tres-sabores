package io.github.ipl.tfc.docetressabores.ejbs;

import io.github.ipl.tfc.docetressabores.entities.Client;
import io.github.ipl.tfc.docetressabores.entities.Family;
import io.github.ipl.tfc.docetressabores.entities.structures.*;
import io.github.ipl.tfc.docetressabores.entities.Material;
import io.github.ipl.tfc.docetressabores.entities.MaterialType;
import io.github.ipl.tfc.docetressabores.entities.Product;
import io.github.ipl.tfc.docetressabores.entities.Project;
import io.github.ipl.tfc.docetressabores.entities.Variant;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

@Singleton(name = "ConfigEJB")
@Startup
public class ConfigBean {

	@EJB private ProductBean productBean;
	@EJB private VariantBean variantBean;
	@EJB private SimulationBean simulationBean;
	@EJB private ClientBean clientBean;
	@EJB private ProjectBean projectBean;
	@EJB private StructureBean structureBean;
	@EJB private LightSteelStructureBean lightSteelStructureBean;
	@EJB private SlabStructureBean slabStructureBean;
	@EJB private MaterialBean materialBean;
	@EJB private FamilyBean familyBean;

	@PostConstruct
	public void populateBD(){
		System.out.println("####### Creating materials...");
		materialBean.create(MaterialType.LIGHT_STEEL);
		materialBean.create(MaterialType.PROFILED_SHEETING);
		materialBean.create(MaterialType.SLAB);
		materialBean.create(MaterialType.SANDWICH_PANEL);

		System.out.println("####### Creating families...");
		Family familyOmega = familyBean.create(MaterialType.LIGHT_STEEL, "omega");
		Family familyC = familyBean.create(MaterialType.LIGHT_STEEL, "c");
		Family familyZ = familyBean.create(MaterialType.LIGHT_STEEL, "z");

		System.out.println("####### Creating products...");
		Product product1 = productBean.create(familyC.getId(), "Section C 220 BF");
		Product product2 = productBean.create(familyZ.getId(), "Section Z 220 BF");

		System.out.println("####### Creating variants...");
		//PODE LER-SE OS VALORES DOS PRODUTOS/VARIANTES DE EXCELS OU CSVs (ver excels fornecidos)
		//Exemplo básico de adição de variantes "à mão"
		Variant variant1 = variantBean.create(product1.getId(), "C 120/50/21 x 1.5", 13846, 13846, 375, 220000);
		Variant variant2 = variantBean.create(product1.getId(), "C 120/60/13 x 2.0", 18738, 18738, 500, 220000);

		System.out.println("####### Creating clients...");
		Client client1 = clientBean.create("Foo", "999999999", "foo@foo.foo", "fooland, 1234 foohouse");

		System.out.println("####### Creating projects...");
		Project project1 = projectBean.create("fooProject", client1.getId());
		Project project2 = projectBean.create("foobarProject", client1.getId());

		System.out.println("####### Creating structures...");
		LightSteelStructure structure1 = lightSteelStructureBean.create(6, 3, 1, Arrays.asList(variant1.getId()));
		LightSteelStructure structure2 = lightSteelStructureBean.create(15, 3, 1, Arrays.asList(variant1.getId(), variant2.getId()));
		lightSteelStructureBean.create(15, 3, 1, Arrays.asList(variant1.getId(), variant2.getId()));
		slabStructureBean.create(15, 19, 2, new ArrayList<>());
		structureBean.create(MaterialType.PROFILED_SHEETING, 154, 78, new ArrayList<>());
		structureBean.create(MaterialType.SANDWICH_PANEL, 74, 10, new ArrayList<>());

		System.out.println("####### Adding structures to projects...");
		project1.addStructure(structureBean.findStructure(structure1.getId()));
		project2.addStructure(structureBean.findStructure(structure1.getId()));
		project2.addStructure(structureBean.findStructure(structure2.getId()));

		//PODE LER-SE OS VALORES mcr_p E mcr_n A PARTIR DE UM EXCEL OU CSV (ver excels fornecidos para os produtos Perfil C e Z, que têm os valores mcr)
		//Exemplo básico de adição de valores mcr "à mão"
		variant1.addMcr_p(3.0,243.2123113);
		variant1.addMcr_p(4.0,145.238784);
		variant1.addMcr_p(5.0,99.15039028);
		variant1.addMcr_p(6.0,73.71351699);
		variant1.addMcr_p(7.0,58.07716688);
		variant1.addMcr_p(8.0,47.68885195);
		variant1.addMcr_p(9.0,40.37070843);
		variant1.addMcr_p(10.0,34.9747033);
		variant1.addMcr_p(11.0,30.84866055);
		variant1.addMcr_p(12.0,27.59984422);

		//Válido para variantes simétricas, em que os mcr_p são iguais aos mcr_n
		variant1.setMcr_n((LinkedHashMap<Double, Double>) variant1.getMcr_p().clone());

		variant2.addMcr_p(3.0,393.1408237);
		variant2.addMcr_p(4.0,241.9157907);
		variant2.addMcr_p(5.0,169.7815504);
		variant2.addMcr_p(6.0,129.3561949);
		variant2.addMcr_p(7.0,104.0782202);
		variant2.addMcr_p(8.0,86.9803928);
		variant2.addMcr_p(9.0,74.71876195);
		variant2.addMcr_p(10.0,65.52224563);
		variant2.addMcr_p(11.0,58.37786338);
		variant2.addMcr_p(12.0,52.65428332);

		//Válido para variantes de geometria simétrica, em que os mcr_p são iguais aos mcr_n
		variant2.setMcr_n((LinkedHashMap<Double, Double>) variant2.getMcr_p().clone());


		System.out.println("####### FINISHED!!");

		//EXEMPLO DA SIMULAÇÃO PARA DUAS VARIANTES DO PERFIL C, E PARA UMA ESTRUTURA DE 3 vãos (nb) de 3m cada (LVao) E SOBRECARGA 500000 (q)
		if(simulationBean.simulateVariant(3, 3.0, 500000, variant1)){
			System.out.println(variant1.getName() + " pode ser usada.");
		}else{
			System.out.println(variant1.getName() + " não pode ser usada.");
		}

		if(simulationBean.simulateVariant(3, 3.0, 500000, variant2)){
			System.out.println("A variante " + variant2.getName() + " pode ser usada.");
		}else{
			System.out.println("A variante " + variant2.getName() + " não pode ser usada.");
		}
	}
}

package eu.disi.unitn.swip.start;

import eu.disi.unitn.swip.algorithms.NAryDependency;
import eu.disi.unitn.swip.algorithms.UnaryDependency;
import eu.disi.unitn.swip.util.Utility;

import java.util.Map;

public class DiscoverOrderDependency {

	public void execute(){
		UnaryDependency.execute();
        Map<String, String> detectedOrderDependencies = UnaryDependency.getUnaryDependecies();
		Map<Integer, String> ListOfcandidateAttributeList = UnaryDependency.getCandidateAttributeList();
		int n = 2;
		while (!ListOfcandidateAttributeList.isEmpty()){
			NAryDependency.execute(n, detectedOrderDependencies, ListOfcandidateAttributeList);
            detectedOrderDependencies.putAll(NAryDependency.getNaryDependencies());
			ListOfcandidateAttributeList = NAryDependency.getCandidateAttributeList();
			Utility.write(ListOfcandidateAttributeList.toString(), "./data/attributeList.txt", true);
			n++;
		}
        System.out.println("[Info]: Detected order dependencies");
		for (String key: detectedOrderDependencies.keySet()){
			System.out.println("[Info]: " + detectedOrderDependencies.get(key));
		}
	}
}

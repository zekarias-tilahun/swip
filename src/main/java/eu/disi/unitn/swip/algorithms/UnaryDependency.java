package eu.disi.unitn.swip.algorithms;

import eu.disi.unitn.swip.model.Echocardiogram;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnaryDependency {
	private static Map<String, String> unaryDependecies = null;
	private static Map<Integer, String> candidateAttributeList = null;

	public static void execute() {
		System.out.println("[Info]: Detecting order dependencies at level 1: ...");
		unaryDependecies = new HashMap<>();
		candidateAttributeList = new HashMap<>();
		Object rhsPrevValue = null;
		Object rhsCurrentValue = null;
		boolean exitWithDependency = true;
		Map<String, List<Echocardiogram>> tuplesOrderedByFieldsMap = new HashMap<>();
		Collection<String> attributes = Echocardiogram.columnIndexToNameMap.values();
		for (String attribute : attributes) {
			List<Echocardiogram> list = Echocardiogram.orderBy(attribute);
			tuplesOrderedByFieldsMap.put(attribute, list);
		}

		for (String attribute : attributes) {
			for (String attribute1 : attributes) {
				exitWithDependency = true;
				if (!attribute.equals(attribute1)) {
					List<Echocardiogram> list = tuplesOrderedByFieldsMap.get(attribute);
					for (Echocardiogram e : list) {
						rhsCurrentValue = Echocardiogram.getDataFromFields(e, attribute1);

						if (rhsPrevValue == null) {
							rhsPrevValue = rhsCurrentValue;
							continue;
						}
						if(Echocardiogram.compareTuples(rhsCurrentValue.toString(), rhsPrevValue.toString()) < 0){
							exitWithDependency = false;
							break;
						}
//						if (rhsCurrentValue.toString().compareTo(rhsPrevValue.toString()) < 0) {
//							exitWithDependency = false;
//							break;
//						}
						rhsPrevValue = rhsCurrentValue;
					}
					if (exitWithDependency) {
						String attributeListKey = attribute + attribute1;
						String attributeListValue = attribute + "-->" + attribute1;
						unaryDependecies.put(attributeListKey, attributeListValue);
                        /**
                         * Making sure that attribute lists that are not minimal are not kept in the candidate attribute
                         * list for the next level
                         */
                        candidateAttributeList.remove((attribute + "cand" + attribute1).hashCode());
                        candidateAttributeList.remove((attribute1 + "cand" + attribute).hashCode());
					} else {
						// The list contains all the possible candidates for the
						// next level
                        if (unaryDependecies.get(attribute1+attribute)==null && unaryDependecies.get(attribute+attribute1)==null) {
                            //System.out.println(attribute+attribute1);
                            int candidatesKey = (attribute + "cand" + attribute1).hashCode();
                            candidateAttributeList.put(candidatesKey, attribute + attribute1);
                        }
					}
					rhsPrevValue = null;
				}
			}
		}
	}

	public static Map<String, String> getUnaryDependecies() {
		return unaryDependecies;
	}

	public static Map<Integer, String> getCandidateAttributeList() {
		return candidateAttributeList;
	}
}

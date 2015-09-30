package eu.disi.unitn.swip.algorithms;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.result_receiver.OrderDependencyResultReceiver;
import eu.disi.unitn.swip.start.DiscoverOrderDependency;

import java.util.ArrayList;

/**
 * Created by zack on 08/09/15.
 */
public class SwipOrderConfigurator extends DiscoverOrderDependency implements OrderDependencyAlgorithm, RelationalInputParameterAlgorithm {

    public void setResultReceiver(OrderDependencyResultReceiver resultReceiver) {

    }

    public void setRelationalInputConfigurationValue(String identifier, RelationalInputGenerator... values) throws AlgorithmConfigurationException {

    }

    public ArrayList<ConfigurationRequirement> getConfigurationRequirements() {
        return null;
    }

    @Override
    public void execute() {
        super.execute();
    }
}

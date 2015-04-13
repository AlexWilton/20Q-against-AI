package ajw28.cs3105.p02.part2;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.ElmanPattern;

/**
 * Self Programming Neural Network.
 */
public class SelfProgrammingNeuralNet {

    BasicNetwork net;
    public SelfProgrammingNeuralNet() {
        /*Setup an Elman type neural network*/
        ElmanPattern elmanPattern = new ElmanPattern();
        elmanPattern.setActivationFunction(new ActivationSigmoid());
        elmanPattern.setInputNeurons(1);
        elmanPattern.addHiddenLayer(6);
        elmanPattern.setOutputNeurons(1);
        net = (BasicNetwork) elmanPattern.generate();
    }
}

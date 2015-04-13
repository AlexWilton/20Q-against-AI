package ajw28.cs3105.p02.part2;

import ajw28.cs3105.p02.part2.fsm.FSM;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.ElmanPattern;

/**
 * Self Programming Neural Network.
 */
public class SelfProgrammingNeuralNet {

    BasicNetwork net;
    FSM fsm;

    public SelfProgrammingNeuralNet(FSM fsm) {
        this.fsm = fsm;
        construct();
    }

    /**
     * Construct a neural network for the given Finite State Machine
     */
    private void construct(){
        /*Setup an Elman type neural network*/
        ElmanPattern elmanPattern = new ElmanPattern();
        elmanPattern.setActivationFunction(new ActivationSigmoid());
        elmanPattern.setInputNeurons(1);
        elmanPattern.addHiddenLayer(6);
        elmanPattern.setOutputNeurons(1);
        net = (BasicNetwork) elmanPattern.generate();
    }
}

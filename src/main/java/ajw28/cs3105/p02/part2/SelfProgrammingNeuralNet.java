package ajw28.cs3105.p02.part2;

import ajw28.cs3105.p02.part2.fsm.FSM;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
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


    /**
     * Train Neural Network using ideal set of question answers for each concept.
     */
    public void train() {
//        double[][] netInputs = calculateIdealInputs();
//        double[][] netOuts = new double[concepts.size()][numberOfOutputUnits];
//        for(int conceptNum=0; conceptNum < concepts.size(); conceptNum++){
//            netOuts[conceptNum] = concepts.get(conceptNum).getIdEncodedInBinary(numberOfOutputUnits);
//        }
//
//        MLDataSet trainingSet = new BasicMLDataSet(netInputs, netOuts);
//
//        // train the neural network
//        while(true){
//            Backpropagation trainer = new Backpropagation(net, trainingSet, 0.3, 0.8);
//
//            int numOfEpochsToTrainFor = 10000 + 100 * concepts.size() * concepts.size() * numberOfHiddenUnits * numberOfHiddenUnits;
//            trainer.iteration(numOfEpochsToTrainFor);
//            if(trainer.getError() > MAX_ERROR){
//                //add hidden unit and reconstructNet and trainer
//                System.out.println("Trained for " + numOfEpochsToTrainFor + " epochs. Maximum Error (" + MAX_ERROR + ") not reached. " +
//                        "Increasing number of hidden units from " + numberOfHiddenUnits + " to " + (numberOfHiddenUnits +1) + " and will try again");
//                numberOfHiddenUnits++;
//                constructNet();
//            }else{
//                trainer.finishTraining();
//                break;
//            }
//        }
//
//        System.out.println("Neural Network successfully trained!\n");

    }
}

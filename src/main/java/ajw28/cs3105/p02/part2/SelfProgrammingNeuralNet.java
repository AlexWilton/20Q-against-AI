package ajw28.cs3105.p02.part2;

import ajw28.cs3105.p02.part2.EulerOnTour.Digraph;
import ajw28.cs3105.p02.part2.EulerOnTour.DirectedEulerianCycle;
import ajw28.cs3105.p02.part2.EulerOnTour.StdOut;
import ajw28.cs3105.p02.part2.fsm.*;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.CalculateScore;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.Greedy;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.ml.train.strategy.StopTrainingStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.pattern.ElmanPattern;
import org.encog.util.arrayutil.Array;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Self Programming Neural Network.
 * A neural network which learns to behave in the same way as a given Finite State Machine (FSM)
 */
public class SelfProgrammingNeuralNet {

    /**
     * Number of input units
     */
    private int numberOfInputUnits;
    /**
     * Number of Hidden users
     */
    private int numberOfHiddenUnits;

    /**
     * Number of Output Units
     */
    private int numberOfOutputUnits;

    /**
     * Elman Neural Network
     */
    private BasicNetwork net;

    /**
     * Finite State Machine
     */
    private FSM fsm;

    /**
     * Construct Self Programming
     * @param fsm
     */
    public SelfProgrammingNeuralNet(FSM fsm) {
        this.fsm = fsm;
        numberOfInputUnits = (int) Math.ceil(Math.log(fsm.getInputSymbols().size()) / Math.log(2));
        numberOfHiddenUnits = 10;
        numberOfOutputUnits = (int) Math.ceil(Math.log(fsm.getOutputSymbols().size()) / Math.log(2));
        construct();
    }

    /**
     * Construct a neural network for the given Finite State Machine
     */
    private void construct(){
        /*Setup an Elman type neural network*/
        ElmanPattern elmanPattern = new ElmanPattern();
        elmanPattern.setActivationFunction(new ActivationSigmoid());
        elmanPattern.setInputNeurons(numberOfInputUnits);
        elmanPattern.addHiddenLayer(numberOfHiddenUnits);
        elmanPattern.setOutputNeurons(numberOfOutputUnits);
        net = (BasicNetwork) elmanPattern.generate();
    }


    /**
     * Train Neural Network using FSM's set of transitions
     */
    public void train() {
        MLDataSet trainingSet = generateTrainingData();
        CalculateScore score = new TrainingSetScore(trainingSet);
        final MLTrain trainAlt = new NeuralSimulatedAnnealing( net, score, 10, 2, 100);

        final MLTrain trainMain = new Backpropagation(net, trainingSet,0.000001, 0.0);

        final StopTrainingStrategy stop = new StopTrainingStrategy();
        trainMain.addStrategy(new Greedy());
        trainMain.addStrategy(new HybridStrategy(trainAlt));
        trainMain.addStrategy(stop);

        int epoch = 0;
        System.out.println("Training Start... (please me patience)");
        while (!stop.shouldStop()) {
            trainMain.iteration();

            epoch++;
        }
        System.out.println("Neural Net trained for " + epoch + " and with a final error of " + trainMain.getError());

    }

    /**
     * Generate Training Data for training neural network to behave like the FSM
     * @return Training Data
     */
    private MLDataSet generateTrainingData(){
        ArrayList<Transition> transitions = fsm.getTransitions();
        ArrayList<State> states = fsm.getStates();

        //Create a directed graph from transition table
        Digraph digraph = new Digraph(states.size());
        for(Transition t : transitions){
            digraph.addEdge(t.getOrigin().getId(), t.getDestination().getId());
        }

        //Generate sequence of transitions which includes every transition
        DirectedEulerianCycle eulerianCycle = new DirectedEulerianCycle(digraph);
        ArrayList<State> stateSeqFromEulerianCycle = new ArrayList<>();
        for (int stateId : eulerianCycle.cycle()) { //convert list of state ids to list of states
            for(State s : states){
                if(s.getId() == stateId){
                    stateSeqFromEulerianCycle.add(s); break;
                }
            }
        }

        ArrayList<Transition> path = new ArrayList<>();
        boolean[] transitionVisited = new boolean[transitions.size()];
        for(int currentStateSeqIndex=0; currentStateSeqIndex < stateSeqFromEulerianCycle.size() - 1; currentStateSeqIndex++){
            State currentState = stateSeqFromEulerianCycle.get(currentStateSeqIndex);
            HashSet<Transition> transitionsFromCurrentState = new HashSet<>();
            for(Transition t : transitions) if(t.getOrigin() == currentState) transitionsFromCurrentState.add(t);
            for(Transition possibleNextTransition : transitionsFromCurrentState){
                if(possibleNextTransition.getDestination() == stateSeqFromEulerianCycle.get(currentStateSeqIndex + 1) &&
                        !transitionVisited[possibleNextTransition.getId()]){
                    path.add(possibleNextTransition);
                    transitionVisited[possibleNextTransition.getId()] = true;
                    break;
                }
            }

        }

        double[][] inputTrainingData = new double[path.size()][];
        double[][] outputTrainingData = new double[path.size()][];
        for(int transitionIndex = 0; transitionIndex < path.size(); transitionIndex++){
            Transition currentTransition = path.get(transitionIndex);
            inputTrainingData[transitionIndex] = currentTransition.getInputSymbol().getIdEncodedInBinary(numberOfInputUnits);
            outputTrainingData[transitionIndex] = currentTransition.getOutputSymbol().getIdEncodedInBinary(numberOfOutputUnits);
        }

        return new BasicMLDataSet(inputTrainingData, outputTrainingData);
    }


    /**
     * Put an input into the neural network and determine the output
     * @param inputSymbol Input Symbol
     * @return Output Symbol
     */
    public OutputSymbol interactWithMachine(InputSymbol inputSymbol) {
        MLData netOutput = net.compute(new BasicMLData(inputSymbol.getIdEncodedInBinary(fsm.getInputSymbols().size())));
        int outputSymbolId = OutputSymbol.convertOutputToID(netOutput);
        OutputSymbol output = fsm.getOutputSymbols().get(outputSymbolId);
        if(output.getId() != outputSymbolId) System.out.println("Error!!!! Output Ids don't match");
        return output;
    }

    /**
     * List of Possible Input Symbols which the system recognises
     * @return List of Input Symbols
     */
    public ArrayList<InputSymbol> getPossibleInputs(){
        return fsm.getInputSymbols();
    }
}

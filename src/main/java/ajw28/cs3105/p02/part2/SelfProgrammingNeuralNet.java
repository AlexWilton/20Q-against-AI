package ajw28.cs3105.p02.part2;

import ajw28.cs3105.p02.part2.EulerOnTour.Digraph;
import ajw28.cs3105.p02.part2.EulerOnTour.DirectedEulerianCycle;
import ajw28.cs3105.p02.part2.EulerOnTour.StdOut;
import ajw28.cs3105.p02.part2.fsm.FSM;
import ajw28.cs3105.p02.part2.fsm.State;
import ajw28.cs3105.p02.part2.fsm.Transition;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.CalculateScore;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
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
     * Elman Neural Network
     */
    BasicNetwork net;

    /**
     * Finite State Machine
     */
    FSM fsm;

    /**
     * Construct Self Programming
     * @param fsm
     */
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
     * Train Neural Network using FSM's set of transitions
     */
    public void train() {
        MLDataSet trainingSet = generateTrainingData(1000);
        CalculateScore score = new TrainingSetScore(trainingSet);
        final MLTrain trainAlt = new NeuralSimulatedAnnealing( net, score, 10, 2, 100);

        final MLTrain trainMain = new Backpropagation(net, trainingSet,0.000001, 0.0);

        final StopTrainingStrategy stop = new StopTrainingStrategy();
        trainMain.addStrategy(new Greedy());
        trainMain.addStrategy(new HybridStrategy(trainAlt));
        trainMain.addStrategy(stop);

        int epoch = 0;
        while (!stop.shouldStop()) {
            trainMain.iteration();
            System.out.println("Training Elman net. Epoch #" + epoch
                    + " Error:" + trainMain.getError());
            epoch++;
        }

    }

    private MLDataSet generateTrainingData(int length){
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

        LinkedList<Transition> path = new LinkedList<>();
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


        return null;//todo
    }


}

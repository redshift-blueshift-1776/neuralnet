package neuralnet2;

//the idea here is to make this file agnostic about the specifics of the application
//thus, there's no reference to minesweeping here, outside of potentially the width/height parameters
public class Params { 
 //general parameters
 public static final int WIN_WIDTH = Wrapper.FRAMESIZE;   //width of world map
 public static final int WIN_HEIGHT = Wrapper.FRAMESIZE;   //height of world map
 public static final int FPS = 60;        //frames per second for drawing...not used

 //for the neural network
 public static final int INPUTS = 2;     //number of inputs
 public static final int HIDDEN = 1;     //number of hidden layers
 public static final int NEURONS_PER_HIDDEN = 2;  //number of neurons in each hidden layer
 public static final int OUTPUTS = 2;    //number of outputs
 public static final double BIAS = -1;    //the threshold (bias) value
 public static final double ACT_RESPONSE = 1;  //adjusts the sigmoid function
 
 //for the genetic algorithm 
 public static final double CROSSOVER_RATE = 0.3; //0.3, the chance of crossover happening
 public static final double MUTATION_RATE = 0.1;  //0.1, the chance of a particular value in a genome changing
 public static final double MAX_PERTURBATION = 0.3; //maximum magnitude of the delta from mutation
 public static final int NUM_ELITE = 4;    //4, how many of the top performers advance to the next generation
 public static final int NUM_COPIES_ELITE = 1;  //1, and how many copies of those performers we'll use

}

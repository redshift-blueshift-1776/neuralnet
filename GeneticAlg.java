package neuralnet2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.*;

//note that this is generic (nothing directly related to minesweeping)
public class GeneticAlg {
  
  class Genome implements Comparable<Genome> {   //a simple class to handle a single genome
    private ArrayList<Double> weights;     //since no other class needs to know how Genome is implemented
    private double fitness;        //it is a subclass of the genetic algorithm class
    
    public Genome() {
      weights = new ArrayList<Double>();    //here the 'chromosomes' for a genetic alg influenced by a neural net are the weights of the neuron's inputs
      fitness = 0;         //fitness increases as the genome becomes more fit
    }
    
    public Genome(ArrayList<Double> w, double f) {
      weights = new ArrayList<Double>();
      for (Double d : w) {
        weights.add(d);
      }
      fitness = f;
    }
    
    public Genome clone() {        //mmm, cloning genomes
      return new Genome(weights, fitness);
    }
    
    public ArrayList<Double> getWeights() { return weights; }
    public double getFitness() { return fitness; }
    public void setFitness(double f) { fitness = f; }
    public void setWeights(ArrayList<Double> w)
    {
      weights = w;
    }
    
    @Override
    public int compareTo(Genome o) {     //the comparable interface needs a definition of compareTo
      if (this.fitness > o.getFitness()) {   //the interface is being used so that the genomes can be sorted by fitness
        return 1;
      } else if (this.fitness < o.getFitness()) {
        return -1;
      }
      return 0;
    }
  }
  
  private ArrayList<Genome> pop;  //the genomes (weights for neural nets) who are the members of the genetic algorithm's gene pool     
  private int popSize;    //the pools' size
  private int chromosomeLength;  //the length of the weights list
  private double totalFitness;  //the summation of all the genomes' fitnesses
  private double bestFitness;   //the best fitness of all the genomes, then the average, then the worst
  private double avgFitness;   //could be used for plotting fitnesses
  private double worstFitness;
  private int fittestGenome;   //the index of the most fit genome in the population
  private int genCount;    //what generation the pool has made it to
  private double mutationRate;  //how often mutation (for each entry in a weight list) and crossover occurs
  private double crossoverRate;
  private ArrayList<Double> child1;
  private ArrayList<Double> child2;
  
  public GeneticAlg(int populationSize, double mutRate, double crossRate, int numWeights) {
    popSize = populationSize;
    mutationRate = mutRate;
    crossoverRate = crossRate;
    chromosomeLength = numWeights;
    totalFitness = 0;
    genCount = 0;
    fittestGenome = 0;
    bestFitness = 0;
    worstFitness = 99999999;
    avgFitness = 0;
    //initialize population with randomly generated weights
    pop = new ArrayList<Genome>();
    Random rnd = new Random();
    for (int i = 0; i < popSize; i++) {
      pop.add(new Genome());
      if (Wrapper.thing == 2)
      {
        ArrayList<Double> givenWeights2 = new ArrayList<Double>();
        givenWeights2 = Wrapper.givenWeights;
        //System.out.println(givenWeights2);
        for (int j = 0; j < chromosomeLength; j++)
        {
          pop.get(i).weights.add(givenWeights2.get(j));
        }
      }
      else
      {
        for (int j = 0; j < chromosomeLength; j++) {
          pop.get(i).weights.add(rnd.nextDouble()*2 - 1);
        }
      }
      
    }
  }
  
  @SuppressWarnings("unchecked")
  public void crossover(ArrayList<Double> parent1, ArrayList<Double> parent2) {
    //implement crossover, similar to the previous project
    
    //your code goes here
    child1 = new ArrayList<Double>();
    child2 = new ArrayList<Double>();
    int q = 0;
    while (q < parent1.size())
    {
      //child1.add((parent1.get(q) + parent2.get(q)) / 2);
      //child2.add((parent1.get(q) + parent2.get(q)) / 2);
      if (Math.random() > 0.5)
      {
        child1.add(parent1.get(q));
        child2.add(parent2.get(q));
      }
      else
      {
        child2.add(parent1.get(q));
        child1.add(parent2.get(q));
      }
      q++;
    }
    
  }
  
  public void mutate(ArrayList<Double> chromo) {
    //mutate each weight dependent upon the mutation rate
    //the weights are bounded by the maximum allowed perturbation
    
    //your code goes here
    //int q = (int) Math.floor(Math.random() * (chromo.size() - 1));
    int q = 0;
    while (q < chromo.size())
    {
      Double k = chromo.get(Math.max(q, q));
//Double k = chromo.get(Math.max(0, q));
      double randb = Math.random();
      if (randb < Params.MUTATION_RATE)
      {
        k += (Math.random() - 0.5) * Params.MAX_PERTURBATION * 2;
        chromo.set(q, k);
      }
      q++;
    }
  }
  
  //you're welcome to implement other parent selection methods
  public Genome getChromoByRoulette() {  //random parent selection using a roulette approach
    Random rnd = new Random();
    double stop = rnd.nextDouble() * totalFitness; //pick a random fitness value at which to stop
    double fitnessSoFar = 0;
    Genome result = new Genome();
    int i = 0;
    while (i < popSize && fitnessSoFar <= stop) {
      fitnessSoFar += pop.get(i).fitness;
      if (fitnessSoFar >= stop) {
        result = pop.get(i).clone();
      }
      i++;
    }
    return result;
  }
  
  @SuppressWarnings("unchecked")
  public ArrayList<Genome> epoch(ArrayList<Genome> oldpop) {//get the new generation from the old generation
    pop = (ArrayList<Genome>) oldpop.clone();   //the previous population is the current population
    reset();           //reinitialize fitness stats
    Collections.sort((List<Genome>) pop);    //sort them by fitness
    //if (Wrapper.UPDATEMETHOD != 1)
    {
      calculateBestWorstAvgTot();       //calculate the fitness stats
    }
    ArrayList<Genome> newPop = new ArrayList<Genome>();
    if (Params.NUM_COPIES_ELITE * Params.NUM_ELITE % 2 == 0) {   //take the top NUM_ELITE performers and add them to the new population
      grabNBest(Params.NUM_ELITE, Params.NUM_COPIES_ELITE, newPop);
    }
    while (newPop.size() < popSize) {     //fill the rest of the new population by children from parents using the classic genetic algorithm
      //your 9-ish lines of code goes here
      int q = (int) Math.floor(Math.random() * (popSize - 1));
      //newPop.add(pop.get(Math.max(0, popSize - 1 - q)));
      Genome a = getChromoByRoulette();
      Genome b = getChromoByRoulette();
      child1 = a.getWeights();
      //Genome a = pop.get(Math.max(0, q));
      q = (int) Math.floor(Math.random() * (popSize - 1));
      //Genome b = pop.get(Math.max(0, q));
      double randa = Math.random();
      if (randa < Params.CROSSOVER_RATE)
      {
        crossover(a.getWeights(), b.getWeights());
      }
      mutate(a.getWeights());
      /*
       if (randa > Params.CROSSOVER_RATE && randb > Params.MUTATION_RATE)
       {
       a = new Genome();
       Random rnd = new Random();
       for (int j = 0; j < chromosomeLength; j++)
       {
       a.weights.add(rnd.nextDouble()*2 - 1);
       }
       }
       */
      a = new Genome(child1, 0);
      newPop.add(a);
      
    }
    pop = (ArrayList<Genome>) newPop.clone();
    //System.out.println("sdfsdfd");
    return pop;           //this probably could have been written better, why return a class variable?
  }
  
  public void grabNBest(int nBest, int numCopies, ArrayList<Genome> popList) { //hopefully the population is sorted correctly...
    while (nBest-->0) {
      for (int i = 0; i < numCopies; i++) {
        popList.add(pop.get(popSize - 1 - nBest));
      }
    }
  }
  
  public void calculateBestWorstAvgTot() { //fairly self-explanatory, try commenting it
    //popSize = ControllerStars.SWEEPERS;
    totalFitness = 0;
    double highestSoFar = 0;
    double lowestSoFar = 99999999;
    for (int i = 0; i < popSize; i++) {
      if (pop.get(i).fitness > highestSoFar) {
        highestSoFar = pop.get(i).fitness;
        fittestGenome = i;
        bestFitness = highestSoFar;
      }
      if (pop.get(i).fitness < lowestSoFar) {
        lowestSoFar = pop.get(i).fitness;
        worstFitness = lowestSoFar;
      }
//    System.out.println(pop.get(i).fitness);
      totalFitness += pop.get(i).fitness;
      //System.out.print(pop.get(i).fitness + " ");
    }
    avgFitness = totalFitness / Math.max(popSize, 1);
  }
  
  public void reset() {  //reset fitness stats
    totalFitness = 0;
    bestFitness = 0;
    worstFitness = 99999999;
    avgFitness = 0;
  }
  
  //self-explanatory
  public ArrayList<Genome> getChromosomes() { return pop; }
  public double avgFitness() { return totalFitness / popSize; }
  public double bestFitness() { return bestFitness; }
  
}

package neuralnet2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import neuralnet2.GeneticAlg.Genome;

//specific to the stars scenario, hence the Stars at the end of the class name
//this runs the simulation
@SuppressWarnings("serial")
public class ControllerStars extends JPanel implements ActionListener {
  
  private Timer timer;     //timer runs the simulation
  private int ticks;      //tick counter for a run of a generation's agents
  private int numMines;     //the number of mines to use in the simulator
  public static int generations;    //the counter for which generation the sim's on
  private int numAgents;     //how many agents
  private int numWeights;     //how many weights an agent has
  private ArrayList<Double> avgFitness;  //useful if you were plotting the progression of fitness
  private ArrayList<Double> bestFitness;
  private GeneticAlg GA;     //the genetic algorithm that manages the genome weights
  private ArrayList<Genome> pop;   //the weights of the neural nets for each of the agents
  private ArrayList<AgentStars> agents;  //the agents themselves (the sweepers)
  private ArrayList<Point2D> mines;  //the mines
  private BufferedImage pic;    //the image in which things are drawn
  private JLabel picLabel;    //the label that holds the image
  private JLabel dataLabel;    //the label that holds the fitness information
  
  //these are specific to the mine sweeping scenario
  //for the controller to run the whole simulation
  public static final int MINES = 60; //60
  public static final int SWEEPERS = 20;//20; //30. 5 then 10 then 60 then 30 then 20.
  //public static final int TICKS = 1600;  
  public static final int TICKS = 2; //how long agents have a chance to gain fitness
  public static final double MINE_SIZE = 4;  //4
  
  //for the mine sweepers
  //public static final double MAX_TURN_RATE = 0.2;
  public static final double MAX_TURN_RATE = 0.2;//how quickly they may turn
  public static final double MAX_SPEED = 2;   //2, how fast they can go
  //public static final double MAX_SPEED = 0.001;
  public static final int SCALE = 15;     //15, the size of the sweepers
  
  public ControllerStars(int xDim, int yDim) {
    setBackground(Color.LIGHT_GRAY);
    //addMouseListener(new MAdapter());
    //addMouseMotionListener(new MAdapter());
    setFocusable(true);
    setDoubleBuffered(true);
    //create the things to display, then add them
    pic = new BufferedImage(xDim, yDim, BufferedImage.TYPE_INT_RGB);
    picLabel = new JLabel(new ImageIcon(pic));
    dataLabel = new JLabel("Info");
    dataLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    add(picLabel);
    add(dataLabel);
    //initialize all of the variables!
    numMines = MINES;
    numAgents = SWEEPERS;
    ticks = 0;
    generations = 0;
    avgFitness = new ArrayList<Double>();
    bestFitness = new ArrayList<Double>();
    
    //make up agents
    agents = new ArrayList<AgentStars>(numAgents);
    for (int i = 0; i < numAgents; i++) {
      agents.add(new AgentStars());
    }
    numWeights = agents.get(0).getNumberOfWeights();
    
    //give agent neural nets their weights
    GA = new GeneticAlg(numAgents, Params.MUTATION_RATE, Params.CROSSOVER_RATE, numWeights);
    pop = GA.getChromosomes();
    if (Wrapper.thing == 2)
    {
      for (int i = 0; i < numAgents; i++) {
        agents.get(i).putWeights(Wrapper.givenWeights);
        pop.get(i);
      }
    }
    else
    {
      for (int i = 0; i < numAgents; i++) {
        agents.get(i).putWeights(pop.get(i).getWeights());
      }
    }
    /*
     //set up the mines
     mines = new ArrayList<Point2D>(numMines);
     
     Random rnd = new Random();
     for (int i = 0; i < numMines; i++) {
     if (Wrapper.MINESTUFF == 3)
     {
     mines.add(new Point2D.Double(xDim / 2, rnd.nextDouble() * yDim)); //3
     }
     else
     {
     mines.add(new Point2D.Double(rnd.nextDouble() * xDim, rnd.nextDouble() * yDim)); //1
     }
     }
     */
    //start it up!
    timer = new Timer(1, this);
    timer.start();
  }
  
  public void drawThings(Graphics2D g) {
    //cover everything with a blank screen
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, pic.getWidth(), pic.getHeight());
    //draw agents
    for (AgentStars a : agents) {
      a.draw(g);
    }
    //draw mines
    /*
     g.setColor(Color.RED);
     for (Point2D m : mines) {
     g.fillRect((int)(m.getX()-MINE_SIZE/2), (int)(m.getY()-MINE_SIZE/2), (int)MINE_SIZE, (int)MINE_SIZE);
     }
     */
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    //drawThings(pic.getGraphics());
  }
  
  
  public void updateAgents() {
    //ticks = (ticks + 1); //count ticks to set the length of the generation run
    //if (ticks < TICKS) {  //do another tick toward finishing a generation
    Random rnd = new Random();
    //update each agent by calling their update function and checking to see if they got a mine
    
    for (int i = 0; i < numAgents; i++)
    {
      boolean thing = agents.get(i).update(mines);
      if (!thing) {
        System.out.println("Error: Wrong amount of neural net inputs.");
        break;
      }
      //if ((Wrapper.UPDATEMETHOD == 1))
      {
        GA.calculateBestWorstAvgTot();
      }
      //keep track of that agent's fitness in the GA as well as the NN
      pop.get(i).setFitness(0);
      pop.get(i).setFitness(agents.get(i).getFitness());
      pop.get(i).setFitness(agents.get(i).getFitness());
      pop.get(i).setFitness(agents.get(i).getFitness());
      pop.get(i).setFitness(agents.get(i).getFitness());
    }
    //}
    //else { //a generation has completed, run the genetic algorithm and update the agents
    double avgFitness = 0.0;
    
    double bestFitness = GA.bestFitness();
    generations++;
    //dataLabel.setText("Previous generation " + (generations - 1) + ":  Avg. fitness of " + Math.round(100 * GA.avgFitness()) / 100.0 + ".  Best fitness of " + GA.bestFitness() + ".");
    if (generations <= Wrapper.MAXGEN + 1)
    {
      if ((GA.avgFitness() == Double.NaN))
      {
        System.out.println("average fitness has errored");
        avgFitness = 450.0;
        generations = 23423;
      }
      else
      {
        avgFitness = GA.avgFitness();
      }      
//System.out.println("Previous generation " + (generations - 1) + ":  Avg. fitness of " + Math.round(GA.avgFitness()) + ".  Best fitness of " + Math.round(GA.bestFitness()) + ".");
      //System.out.println("" + (generations - 1) + " " + GA.avgFitness() /*Math.round(GA.avgFitness())*/ + " " + GA.bestFitness() /*Math.round(GA.bestFitness())*/ + "");
      System.out.println("" + (generations - 1) + " " + avgFitness /*Math.round(GA.avgFitness())*/ + " " + GA.bestFitness() /*Math.round(GA.bestFitness())*/ + "");
    }
    ticks = 0;
    if ((Wrapper.UPDATEMETHOD == 0))
    {
      pop = GA.epoch(pop); //the big genetic algorithm process line
      for (int i = 0; i < numAgents; i++) { //give the agents all the new weights information
        agents.get(i).putWeights(pop.get(i).getWeights());
        //agents.get(i).reset();
      }
    }
    if ((Wrapper.UPDATEMETHOD == 2))
    {
      for (int i = 0; i < numAgents; i++) { //give the agents all the new weights information
        pop.get(i).setWeights(agents.get(i).getWeights());
      }
      if (generations % 5 == 0)
      {
        pop = GA.epoch(pop); //the big genetic algorithm process line
      }
      for (int i = 0; i < numAgents; i++) { //give the agents all the new weights information
        //pop.get(i).setWeights(agents.get(i).getWeights());
        agents.get(i).putWeights(pop.get(i).getWeights());
        pop.get(i).setWeights(agents.get(i).getWeights());
        //agents.get(i).reset();
      }
    }
    else
    {
      
      for (int i = 0; i < numAgents; i++) { //give the agents all the new weights information
        pop.get(i).setWeights(agents.get(i).getWeights());
        agents.get(i).putWeights(pop.get(i).getWeights());
        //agents.get(i).reset();
      }
      
    }
    
    int i = 0;
    while (i < pop.size())
    {
      int q = 0;
      while (q < pop.get(i).getWeights().size())
      {
        if (agents.get(i).getWeights().get(q) != pop.get(i).getWeights().get(q))
        {
          System.out.println("Weights do not match");
        }
        q++;
      }
      i++;
    }
    
  }
  
  //@Override
  public void actionPerformed(ActionEvent e) {
    
    
    
    updateAgents();
    
    if (generations == Wrapper.MAXGEN + 1)
    {
      
      for (int j = 0; j < pop.get(0).getWeights().size(); j++)
      {
        for (int i = 0; i < numAgents; i++)
        { //output all of the weight informations,
          System.out.print("" + pop.get(i).getWeights().get(j) + ", ");
        }
        System.out.println();
      }
      GA.calculateBestWorstAvgTot();
      for (int i = 0; i < numAgents; i++)
      { //output all of the fitness informations,
        System.out.print("" + pop.get(i).getFitness() + ", ");
      }
    }
    //drawThings((Graphics2D) pic.getGraphics());
    //repaint();
  }
  
}
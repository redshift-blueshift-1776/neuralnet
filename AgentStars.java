package neuralnet2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;
import java.io.*;

//you'd write a different agent class for something other than this particular approach to minesweeping (or something other than minesweeping)
public class AgentStars {
  private NeuralNetwork brain; //each agent has a brain (neural net)
  private Point2D position;  //where the agent is on the map
  private Point2D facing;   //which way they're facing (used as inputs) as an (x, y) pair
  private double rotation;  //the angle from which facing is calculated
  private double rotation2; //temporary storage
  private double speed;   //the speed of the agent
  private double lTrack, rTrack;  //the influence rating toward turning left and turning right, used as outputs
  private double fitness;   //how well the agent is doing, quantified (for the genetic algorithm)
  private double scale;    //the size of the agent
  private int closestMine;  //the index in the mines list of the mine closest to the agent (used to determine inputs for the neural net)
  
  public AgentStars() { //initialization
    Random rnd = new Random();
    brain = new NeuralNetwork(Params.INPUTS, Params.OUTPUTS, Params.HIDDEN, Params.NEURONS_PER_HIDDEN);
    rotation = rnd.nextDouble() * Math.PI * 2;
    lTrack = 0.16;
    rTrack = 0.16;
    fitness = 0;
    scale = ControllerMS.SCALE;
    closestMine = 0;
    //position = new Point2D.Double(rnd.nextDouble() * Params.WIN_WIDTH, rnd.nextDouble() * Params.WIN_HEIGHT);
    facing = new Point2D.Double(-Math.sin(rotation), Math.cos(rotation));
    Scanner sc = new Scanner(System.in);
    ArrayList<Integer> answers = new ArrayList<Integer>();
  }
  
  /*
   ArrayList<String> numberToConstellation = new ArrayList<String>();
   
   //get it to put the constellations into an ArrayList
   
   ArrayList<Integer> answers = new ArrayList<Integer>(); //store the answers as Integers
   
   double ra = 0;
   double dec = 0;
   
   //get it to read from a file
   
   int case = 0;
   while (case < answers.size())
   {
   ra = sc.next();
   dec = next();
   inputs.add(ra);
   inputs.add(dec);
   
   answer = next();
   
   ArrayList<Double> output = brain.Update(inputs);
   if (output.size() < Params.OUTPUTS) {
   System.out.println("Incorrect number of outputs.");
   return false; //something went really wrong if this happens
   }
   
   ArrayList<Double> results = new ArrayList<Double>(); //get the results
   int q = 0;
   while (q < Params.OUTPUTS)
   {
   results.add = output.get(q);
   q++;
   }
   
   
   ArrayList<Double> rightAnswer = new ArrayList<Double>();
   q = 0;
   while (q < Params.OUTPUTS)
   {
   if (numberToConstellation.indexOf(answers.get(case)) == q)
   {
   rightAnswer.add(1);
   }
   else
   {
   rightAnswer.add(0);
   }
   q++;
   }
   
   q = 0;
   while (q < Params.OUTPUTS)
   {
   fitness -= (rightAnswer.get(q) - results.get(q) * (rightAnswer.get(q) - results.get(q);
   q++;
   }
   
   }
   */
  
  public boolean update(ArrayList<Point2D> mines) {  //updates all the parameters of the sweeper, sounds fairly important
    //Scanner sc = new Scanner(System.in);
    
    //get it to put the constellations into an ArrayList
    
    //ArrayList<Integer> answers = new ArrayList<Integer>(); //store the answers as Integers
    
    int answer = 0;
    
    ArrayList<Double> inputs = new ArrayList<Double>();
    
    double ra = 0;
    double dec = 0;
    
    fitness = 2 * Wrapper.answers.size();
    //fitness = 63 * 300;
    
    //get it to read from a file
    int cAse = 0;
    while (cAse < Wrapper.answers.size())
      //while (Wrapper.sc.hasNext())
    {
      //fitness = 0;
      inputs = new ArrayList<Double>();
      //System.out.println("Line");
      if(((Wrapper.UPDATEMETHOD == 1) || (Wrapper.UPDATEMETHOD == 2)))// && ControllerStars.generations % 2 == 0)
      {
        //ra = 0.0;
      }
      else
      {
        ra = Wrapper.rightAscensions.get(cAse);
      }
      //System.out.println("Declination");
      dec = Wrapper.declinations.get(cAse);
      inputs.add(ra);
      //inputs.add(dec);
      inputs.add(dec / 90.0);
      
      /*
       for (int t = 0; t < 61; t++)
       {
       inputs.add(0.0);
       }
       */
      
//System.out.println("Answer");
      String answerString = Wrapper.answers.get(cAse);
      answer = Wrapper.numberToConstellation.indexOf(answerString);
      
      ArrayList<Double> output = brain.Update(inputs);
      if (output.size() < Params.OUTPUTS) {
        System.out.println("Incorrect number of outputs.");
        return false; //something went really wrong if this happens
      }
      
      /*
       double sum = 0;
       int q = 0;
       while (q < Params.OUTPUTS)
       {
       sum += output.get(q);
       q++;
       }
       q = 0;
       while (q < Params.OUTPUTS)
       {
       output.set(q, output.get(q) / sum);
       q++;
       }
       */
      
      ArrayList<Double> results = new ArrayList<Double>(); //get the results
      int q = 0;
      while (q < Params.OUTPUTS)
      {
        //System.out.println(output.get(q));
        results.add(output.get(q));
        q++;
      }
      
      //System.out.println(ControllerStars.generations + " " + results);      
      
      ArrayList<Double> rightAnswer = new ArrayList<Double>();
      q = 0;
      while (q < Params.OUTPUTS)
      {
        //if (Wrapper.numberToConstellation.indexOf(answers.get(cAse)) == q)
        if (q == answer)
          //if (1 == q)
        {
          rightAnswer.add(1.0);
        }
        else
        {
          rightAnswer.add(0.0);
        }
        q++;
      }
      
      
      q = 0;
      while (q < Params.OUTPUTS)
      {
        fitness -= (rightAnswer.get(q) - results.get(q)) * (rightAnswer.get(q) - results.get(q));
        //System.out.println(fitness);
        q++;
      }
      
      double storeThis = fitness;
      
      //fitness = Math.max(fitness, 0);
      
      //position.setLocation(ra, dec);
      
      //make the fitness here?
      if(((Wrapper.UPDATEMETHOD == 1) || (Wrapper.UPDATEMETHOD == 2)))// && ControllerStars.generations % 2 == 0)
      {
        
        if (Wrapper.TRYTHIS == 1)
        {
          boolean actuallyUpdate = true;
          ArrayList<Double> delta = new ArrayList<Double>();
          for(int j = 0; j < brain.layers.get(brain.layers.size()-1).neurons.size(); j++) //each node j in the output layer
          {
            delta.add((rightAnswer.get(j) - output.get(j)) * sigmoid2(output.get(j)));//somefunction(output.get(q)));
            //delta.add((rightAnswer.get(j) - output.get(j)) * sigmoid2(output.get(j)));//somefunction(output.get(q)));
          }
          
          ArrayList<ArrayList<Double>> deltaI = new ArrayList<ArrayList<Double>>();
          
          for (int i = 0; i < brain.layers.size() + 6; i++)
          {
            deltaI.add(new ArrayList<Double>());
            for (int j = 0; j < brain.layers.size() + 6; j++)
            {
              deltaI.get(i).add(0.0);
            }
          }
          deltaI.set(brain.layers.size() - 1, delta);
          for (int l = brain.layers.size() - 2; l >= 0; l--)
          {
            
            for(int i = 0; i < brain.layers.get(l).neurons.size(); i++) //each node i in layer l do, nodes in the previous layer
            {
              
              double temp = 0.0;
              for(int j = 0; j < brain.layers.get(l + 1).neurons.size(); j++) //nodes in the current layer
                //for(int j = 0; j < brain.layers.get(l).neurons.get(i).weights.size(); j++)
              {
                
//temp += sigmoid2(output.get(j)) * (brain.layers.get(l).neurons.get(i).weights.get(j) * delta.get(j));
                if ((brain.layers.get(l).neurons.get(i).getBrightness() == Double.NaN) || (brain.layers.get(l).neurons.get(i).weights.get(j) == Double.NaN) || (deltaI.get(l).get(j) == Double.NaN))
                {
                  actuallyUpdate = false;
                }
                temp += (brain.layers.get(l).neurons.get(i).weights.get(j) * deltaI.get(l+1).get(j)/* brain.layers.get(l + 1).neurons.get(i).get, delta.get(j)*/);
              }
              if ((temp != temp) || (temp == Double.POSITIVE_INFINITY)|| (temp == Double.NEGATIVE_INFINITY) || (temp == Double.NaN))
              {
                
                //System.out.println(brain.layers.get(l).neurons.get(i).getBrightness() + "sdiof");
                brain.layers.get(l).neurons.get(i).setBrightness(1.0);
                for(int j = 0; j < brain.layers.get(l + 1).neurons.size(); j++)
                {
                  deltaI.get(l+1).set(j, 1.0);
                  brain.layers.get(l).neurons.get(i).weights.set(j, 1.0);
                }
                
                temp = 1.0;
                deltaI.get(l).set(i, temp);
                
                actuallyUpdate = false;
              }
              else
              {
                deltaI.get(l).set(i, temp);
              }
            }
          }
          //System.out.println(brain.layers.size());
          for (int l = 0; l < brain.layers.size()-2; l++)
          {
            for (int i = 0; i < brain.layers.get(l+1).neurons.size(); i++)
            {
              deltaI.get(l).set(i, sigmoid2(brain.layers.get(l+1).neurons.get(i).getBrightness()) * deltaI.get(l).get(i));
              //System.out.println("1234987123904817sdiojfhsdakfhasdkljfhkjalsdhfl");
            }
          }
          if (actuallyUpdate)
          {
            for (int l = 0; l <= brain.layers.size() - 1; l++)
            {
              for(int i = 0; i < brain.layers.get(l).neurons.size(); i++) //each weight wi,j in network
              {
                for(int j = 0; j < brain.layers.get(l).neurons.get(i).weights.size(); j++)
                {
//System.out.println(i+ " " + j + " " + l + " " + brain.layers.get(l).neurons.get(i).weights.size() + " " + deltaI.get(l).size());
                  double sdfsaf = (brain.layers.get(l).neurons.get(i).weights.get(j) + 1 * brain.layers.get(l).neurons.get(i).getBrightness() * deltaI.get(l).get(j)) /*/10000*/;//?
                  //double sdfsaf = brain.layers.get(l).neurons.get(i).weights.get(j) + ((600.0 - fitness) / 600.0) * brain.layers.get(l).neurons.get(i).getBrightness() * deltaI.get(l).get(j);//?
                  if ((sdfsaf != sdfsaf) || (sdfsaf == Double.NaN))
                  {
                    System.out.println(deltaI.get(l));
                    System.out.println(sdfsaf + " " + i + " " + j + " " + l + " " + brain.layers.get(l).neurons.get(i).weights.get(j) + " " + brain.layers.get(l).neurons.get(i).getBrightness() + " " + deltaI.get(l).get(i) +" fail");
                    sdfsaf = 1.0;
                  }
                  else
                  {
//                System.out.println(sdfsaf + " " + i + " " + j + " " + l  + "pass");
                    //System.out.println(10000 * brain.layers.get(l).neurons.get(i).getBrightness() * deltaI.get(l).get(i));
                    //brain.layers.get(l).neurons.get(i).weights.set(j, Math.max(Math.min((sdfsaf + brain.layers.get(l).neurons.get(i).weights.get(j)), 10000), -10000.0));
                    brain.layers.get(l).neurons.get(i).weights.set(j, sdfsaf/* + brain.layers.get(l).neurons.get(i).weights.get(j)*/);
                    //brain.layers.get(l).neurons.get(i).weights.set(j, sdfsaf + (Math.random() - 0.5) / 100);//+ 1 * results.get(j) * deltaI.get(i).get(j)); //α * ai * Δ[j];
                  }
                }
              }
            }
          }
        }
        else if (Wrapper.TRYTHIS == 2)
        {
          boolean actuallyUpdate = true;
          ArrayList<Double> delta = new ArrayList<Double>();
          for(int j = 0; j < brain.layers.get(brain.layers.size()-1).neurons.size(); j++) //each node j in the output layer
          {
            delta.add((rightAnswer.get(j) - output.get(j)) * sigmoid2(output.get(j)) / 300);//somefunction(output.get(q)));
            //delta.add((rightAnswer.get(j) - output.get(j)) * sigmoid2(output.get(j)));//somefunction(output.get(q)));
          }
          
          ArrayList<ArrayList<Double>> deltaI = new ArrayList<ArrayList<Double>>();
          
          for (int i = 0; i < brain.layers.size() + 6; i++)
          {
            deltaI.add(new ArrayList<Double>());
            for (int j = 0; j < brain.layers.size() + 6; j++)
            {
              deltaI.get(i).add(0.0);
            }
          }
          deltaI.set(brain.layers.size() - 1, delta);
          for (int l = brain.layers.size() - 3; l >= 0; l--)
          {
            
            for(int i = 0; i < brain.layers.get(l+1).neurons.size(); i++) //each node i in layer l do, nodes in the previous layer
            {
              
              double temp = 0.0;
              for(int j = 0; j < brain.layers.get(l + 2).neurons.size(); j++) //nodes in the current layer
                //for(int j = 0; j < brain.layers.get(l).neurons.get(i).weights.size(); j++)
              {
                
//temp += sigmoid2(output.get(j)) * (brain.layers.get(l).neurons.get(i).weights.get(j) * delta.get(j));
                if ((brain.layers.get(l+1).neurons.get(i).getBrightness() == Double.NaN) || (brain.layers.get(l+1).neurons.get(i).weights.get(j) == Double.NaN) || (deltaI.get(l+1).get(j) == Double.NaN))
                {
                  actuallyUpdate = false;
                }
                temp += (brain.layers.get(l).neurons.get(i).weights.get(j) * deltaI.get(l+1).get(j)/* brain.layers.get(l + 1).neurons.get(i).get, delta.get(j)*/);
              }
              if ((temp != temp) || (temp == Double.POSITIVE_INFINITY)|| (temp == Double.NEGATIVE_INFINITY) || (temp == Double.NaN))
              {
                
                //System.out.println(brain.layers.get(l).neurons.get(i).getBrightness() + "sdiof");
                brain.layers.get(l).neurons.get(i).setBrightness(1.0);
                for(int j = 0; j < brain.layers.get(l + 1).neurons.size(); j++)
                {
                  deltaI.get(l+1).set(j, 1.0);
                  brain.layers.get(l).neurons.get(i).weights.set(j, 1.0);
                }
                
                temp = 1.0;
                deltaI.get(l).set(i, temp);
                
                actuallyUpdate = false;
              }
              else
              {
                deltaI.get(l).set(i, temp);
              }
            }
          }
          //System.out.println(brain.layers.size());
          for (int l = 0; l < brain.layers.size()-3; l++)
          {
            for (int i = 0; i < brain.layers.get(l+1).neurons.size(); i++)
            {
              deltaI.get(l).set(i, sigmoid2(brain.layers.get(l+1).neurons.get(i).getBrightness()) * deltaI.get(l).get(i));
              //System.out.println("1234987123904817sdiojfhsdakfhasdkljfhkjalsdhfl");
            }
          }
          if (actuallyUpdate)
          {
            for (int l = 0; l <= brain.layers.size() - 1; l++)
            {
              for(int i = 0; i < brain.layers.get(l).neurons.size(); i++) //each weight wi,j in network
              {
                for(int j = 0; j < brain.layers.get(l).neurons.get(i).weights.size(); j++)
                {
//System.out.println(i+ " " + j + " " + l + " " + brain.layers.get(l).neurons.get(i).weights.size() + " " + deltaI.get(l).size());
                  double sdfsaf = (brain.layers.get(l).neurons.get(i).weights.get(j) + 1 * brain.layers.get(l).neurons.get(i).getBrightness() * deltaI.get(l).get(j)) /10000;//?
                  //double sdfsaf = brain.layers.get(l).neurons.get(i).weights.get(j) + ((600.0 - fitness) / 600.0) * brain.layers.get(l).neurons.get(i).getBrightness() * deltaI.get(l).get(j);//?
                  if ((sdfsaf != sdfsaf) || (sdfsaf == Double.NaN))
                  {
                    System.out.println(deltaI.get(l));
                    System.out.println(sdfsaf + " " + i + " " + j + " " + l + " " + brain.layers.get(l).neurons.get(i).weights.get(j) + " " + brain.layers.get(l).neurons.get(i).getBrightness() + " " + deltaI.get(l).get(i) +" fail");
                    sdfsaf = 1.0;
                  }
                  else
                  {
//                System.out.println(sdfsaf + " " + i + " " + j + " " + l  + "pass");
                    //System.out.println(10000 * brain.layers.get(l).neurons.get(i).getBrightness() * deltaI.get(l).get(i));
                    //brain.layers.get(l).neurons.get(i).weights.set(j, Math.max(Math.min((sdfsaf + brain.layers.get(l).neurons.get(i).weights.get(j)), 10000), -10000.0));
                    brain.layers.get(l).neurons.get(i).weights.set(j, sdfsaf + brain.layers.get(l).neurons.get(i).weights.get(j));
                    //brain.layers.get(l).neurons.get(i).weights.set(j, sdfsaf + (Math.random() - 0.5) / 100);//+ 1 * results.get(j) * deltaI.get(i).get(j)); //α * ai * Δ[j];
                  }
                }
              }
            }
          }
        }
        else
        {
          boolean actuallyUpdate = true;
          ArrayList<Double> delta = new ArrayList<Double>();
          for(int j = 0; j < brain.layers.get(brain.layers.size()-1).neurons.size(); j++) //each node j in the output layer
          {
            delta.add((rightAnswer.get(j) - output.get(j)) * sigmoid2(output.get(j)));//somefunction(output.get(q)));
          }
          
          ArrayList<ArrayList<Double>> deltaI = new ArrayList<ArrayList<Double>>();
          
          for (int i = 0; i < brain.layers.size() + 1; i++)
          {
            deltaI.add(new ArrayList<Double>());
            for (int j = 0; j < brain.layers.size() + 1; j++)
            {
              deltaI.get(i).add(0.0);
            }
          }
          deltaI.set(brain.layers.size() - 1, delta);
          
          for (int l = brain.layers.size() - 2; l >= 0; l--)
          {
            
            for(int i = 0; i < brain.layers.get(l).neurons.size(); i++) //each node i in layer l do, nodes in the previous layer
            {
              
              double temp = 0.0;
              for(int j = 0; j < brain.layers.get(l + 1).neurons.size(); j++) //nodes in the current layer
                //for(int j = 0; j < brain.layers.get(l).neurons.get(i).weights.size(); j++)
              {
                //System.out.println(delta.size() + " " + j + " " + brain.layers.get(l).neurons.get(i).weights.size());
                //temp += sigmoid2(output.get(j)) * (brain.layers.get(l).neurons.get(i).weights.get(j) * delta.get(j));
                if ((brain.layers.get(l).neurons.get(i).getBrightness() == Double.NaN) || (brain.layers.get(l).neurons.get(i).weights.get(j) == Double.NaN) || (deltaI.get(l+1).get(j) == Double.NaN))
                {
                  actuallyUpdate = false;
                }
                temp += sigmoid2(brain.layers.get(l).neurons.get(i).getBrightness()) * (brain.layers.get(l).neurons.get(i).weights.get(j) * deltaI.get(l+1).get(j)/* brain.layers.get(l + 1).neurons.get(i).get, delta.get(j)*/);
              }
              if ((temp != temp) || (temp == Double.POSITIVE_INFINITY)|| (temp == Double.NEGATIVE_INFINITY) || (temp == Double.NaN))
              {
                
                //System.out.println(brain.layers.get(l).neurons.get(i).getBrightness() + "sdiof");
                brain.layers.get(l).neurons.get(i).setBrightness(1.0);
                for(int j = 0; j < brain.layers.get(l + 1).neurons.size(); j++)
                {
                  deltaI.get(l+1).set(j, 1.0);
                  brain.layers.get(l).neurons.get(i).weights.set(j, 1.0);
                }
                deltaI.get(l).set(i, 1.0);
                temp = 1.0;
                
                actuallyUpdate = false;
              }
              else
              {
                deltaI.get(l).set(i, temp);
              }
            }
          }
          if (actuallyUpdate)
          {
            for (int l = 0; l <= brain.layers.size() - 1; l++)
            {
              for(int i = 0; i < brain.layers.get(l).neurons.size(); i++) //each weight wi,j in network
              {
                for(int j = 0; j < brain.layers.get(l).neurons.get(i).weights.size(); j++)
                {
//System.out.println(i+ " " + j + " " + l + " " + brain.layers.get(l).neurons.get(i).weights.size() + " " + deltaI.get(l).size());
                  double sdfsaf = brain.layers.get(l).neurons.get(i).weights.get(j) + 1 * brain.layers.get(l).neurons.get(i).getBrightness() * deltaI.get(l).get(j);//?
                  if ((sdfsaf != sdfsaf) || (sdfsaf == Double.NaN))
                  {
                    System.out.println(deltaI.get(l));
                    System.out.println(sdfsaf + " " + i + " " + j + " " + l + " " + brain.layers.get(l).neurons.get(i).weights.get(j) + " " + brain.layers.get(l).neurons.get(i).getBrightness() + " " + deltaI.get(l).get(i) +" fail");
                  }
                  else
                  {
//                System.out.println(sdfsaf + " " + i + " " + j + " " + l  + "pass");
                    //System.out.println(10000 * brain.layers.get(l).neurons.get(i).getBrightness() * deltaI.get(l).get(i));
                    brain.layers.get(l).neurons.get(i).weights.set(j, sdfsaf /*+ (Math.random() - 0.5) / 100*/);//+ 1 * results.get(j) * deltaI.get(i).get(j)); //α * ai * Δ[j];
                  }
                }
              }
            }
          }
        }
        fitness = storeThis;
      }
      fitness = storeThis;
      
      cAse++;
    }
    //System.out.println(fitness);
    fitness = findFitness();
    return true;
  }
  /*
   public Point2D getClosestMine(ArrayList<Point2D> mines) { //finds the mine closest to the sweeper
   double closestSoFar = 99999999;
   Point2D closestObject = new Point2D.Double(0,0);
   double length;
   for (int i = 0; i < mines.size(); i++) {
   length = Point2D.distanceSq(mines.get(i).getX(), mines.get(i).getY(), position.getX(), position.getY());
   if (length < closestSoFar) {
   closestSoFar = length;
   //closestObject = new Point2D.Double(mines.get(i).getX(), mines.get(i).getY());
   closestObject = new Point2D.Double(position.getX() - mines.get(i).getX(), position.getY() - mines.get(i).getY());
   closestMine = i;
   }
   }
   return closestObject;
   }
   
   public int checkForMine(ArrayList<Point2D> mines, double size) { //has the sweeper actually swept up the closest mine to it this tick?
   if (Point2D.distance(position.getX(), position.getY(), mines.get(closestMine).getX(), mines.get(closestMine).getY()) < (size + scale / 2)) {
   //incrementFitness();
   return closestMine;
   }
   return -1;
   }
   */
  public void reset() { //reinitialize this sweeper's position/direction values
    Random rnd = new Random();
    rotation = rnd.nextDouble() * Math.PI * 2;
    position = new Point2D.Double(rnd.nextDouble() * Params.WIN_WIDTH, rnd.nextDouble() * Params.WIN_HEIGHT);
    facing = new Point2D.Double(-Math.sin(rotation), Math.cos(rotation));
    //fitness = 0;
  }
  
  public void draw(Graphics2D g) { //draw the sweeper in its correct place
    AffineTransform at = g.getTransform(); //affine transforms are a neat application of matrix algebra
    g.rotate(rotation, position.getX(), position.getY()); //they allow you to rotate a g.draw kind of function's output
    //draw the sweeper using a fancy color scheme
    g.setColor(new Color(255, 255, 0));
    g.drawRect((int)(position.getX() - scale / 2), (int)(position.getY()-scale / 2), (int)scale,  (int)scale);
    g.setColor(new Color(0, Math.min(255, 15+(int)fitness*12), Math.min(255, 15+(int)fitness*12)));
    g.fillRect((int)(position.getX() - scale / 2)+1, (int)(position.getY()-scale / 2)+1, (int)scale-2,  (int)scale-2);
    
    
    //draw the direction it's facing 
    g.setTransform(at); //set the transform back to the normal transform
    g.setColor(new Color(255, 0, 255));
    //g.drawLine((int)(position.getX()), (int)(position.getY()), (int)(position.getX() - scale / 2 + facing.getX()*scale), (int)(position.getY() - scale / 2 + facing.getY()*scale));
    g.drawLine((int)(position.getX()), (int)(position.getY()), (int)(position.getX() + facing.getX()*scale), (int)(position.getY() + facing.getY()*scale));
    
    //draw its fitness
    g.setColor(new Color(0, 255, 255));
    g.drawString("" + fitness, (int)position.getX() - (int)(scale / 2), (int)position.getY() +2*(int)scale);
    
    //you're welcome to alter the drawing, I just wanted something simple and quasi-functional
  }
  
  public void backPropogate()
  {
    int cAse = 0;
    String answerString = Wrapper.answers.get(cAse);
    int answer = Wrapper.numberToConstellation.indexOf(answerString);
    ArrayList<Double> rightAnswer = new ArrayList<Double>();
    int q = 0;
    while (q < Params.OUTPUTS)
    {
      //if (Wrapper.numberToConstellation.indexOf(answers.get(cAse)) == q)
      if (q == answer)
        //if (1 == q)
      {
        rightAnswer.add(1.0);
      }
      else
      {
        rightAnswer.add(0.0);
      }
      q++;
    }
    
    
  }
  
  /*
   public double sigmoid2(double activation) { //the sigmoid function returns a value between 0 and 1, <0.5 for negative inputs, >0.5 for positive inputs
   return Math.max((Math.exp(activation) / ((1.0 + Math.exp(activation)) * (1.0 + Math.exp(activation)))), 0.0000000000000001);
   }
   */
  public double sigmoid(double activation, double response) { //the sigmoid function returns a value between 0 and 1, <0.5 for negative inputs, >0.5 for positive inputs
    return 1.0 / (1.0 + Math.exp(-activation / response));
  }
  
// public double sigmoid2(double activation) { //the sigmoid function returns a value between 0 and 1, <0.5 for negative inputs, >0.5 for positive inputs
// return Math.exp(activation) / ((1.0 + Math.exp(activation)) * (1.0 + Math.exp(activation)));
// }
  public double sigmoid2(double activation) { //the sigmoid function returns a value between 0 and 1, <0.5 for negative inputs, >0.5 for positive inputs
    return sigmoid(activation, Params.ACT_RESPONSE) * (1 - sigmoid(activation, Params.ACT_RESPONSE));
  }
  
//simple functions 
  public Point2D getPos() { return position; }
//public void incrementFitness() { fitness++; } //this may need to get more elaborate pending what you would want sweepers to learn...
  public double getFitness() { return fitness; }
  public void putWeights(ArrayList<Double> w) { brain.replaceWeights(w); }
  public int getNumberOfWeights() { return brain.getNumberOfWeights(); }
  public ArrayList<Double> getWeights() { return brain.getWeights(); }
  
  public double findFitness()
  {
    fitness = 2 * Wrapper.answers.size();
    int q = 0;
    NeuralNetwork brain = new NeuralNetwork(Params.INPUTS, Params.OUTPUTS, Params.HIDDEN, Params.NEURONS_PER_HIDDEN);
    q = 0;
    while (q < 300)
    {
      
        double ra = Wrapper.rightAscensions.get(q);
      
      //System.out.println("Declination");
      double dec = Wrapper.declinations.get(q);
      ArrayList<Double> inputs = new ArrayList<Double>();
      //System.out.println("Right Ascension");
      inputs.add(ra);
      //System.out.println("Declination");
      inputs.add(dec);
      ArrayList<Double> output = brain.Update(inputs);
      if (output.size() < Params.OUTPUTS) {
        System.out.println("Incorrect number of outputs."); //something went really wrong if this happens
      }
      int answer = 0;
      int k = 0;
      
      ArrayList<Double> results = new ArrayList<Double>(); //get the results
      k = 0;
      while (k < Params.OUTPUTS)
      {
        results.add(output.get(k));
        k++;
      }
      String answerString = Wrapper.answers.get(q);
      answer = Wrapper.numberToConstellation.indexOf(answerString);
      
      ArrayList<Double> rightAnswer = new ArrayList<Double>();
      k = 0;
      while (k < Params.OUTPUTS)
      {
        if (k == answer)
          //if (1 == q)
        {
          rightAnswer.add(1.0);
        }
        else
        {
          rightAnswer.add(0.0);
        }
        k++;
      }
      
      k = 0;
      while (k < Params.OUTPUTS)
      {
        fitness -= (rightAnswer.get(k) - results.get(k)) * (rightAnswer.get(k) - results.get(k));
        //System.out.println(fitness);
        k++;
      }
      q++;
    }
    return fitness;
  }
}

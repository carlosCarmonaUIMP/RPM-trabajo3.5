///////////////////////////////////////////////////////////////////////////////
///            Steady State Genetic Algorithm v1.0                          ///
///                by Enrique Alba, July 2000                               ///
///                                                                         ///
///   Executable: set parameters, problem, and execution details here       ///
///////////////////////////////////////////////////////////////////////////////

package ga.ssGA;

public class Exe
{
  public static void main(String args[]) throws Exception
  {
     
    // PARAMETERS PPEAKS 
    int    gn         = 128;                           // Gene number
    int    gl         = 1;                            // Gene length
    int    popsize    = 100;                          // Population size
    double pc         = 0.5;                          // Crossover probability
    double pm  = 0.05; // Mutation probability
    double tf         = (double)300500 ;              // Target fitness being sought
    long   MAX_ISTEPS = 50000;

/*
    // PARAMETERS ONEMAX
    int    gn         = 512;                          // Gene number
    int    gl         = 1;                            // Gene length
    int    popsize    = 500;                          // Population size
    double pc         = 0.9;                          // Crossover probability
    double pm  = 1.0/(double)((double)gn*(double)gl); // Mutation probability
    double tf         = (double)gn*gl ;           // Target fitness being sought
    long   MAX_ISTEPS = 50000;
*/
    ga.Problem   problem;                             // The problem being solved

    problem = new ga.ProblemPPeaks(); 
    //problem = new ga.ProblemOneMax();
    
    problem.set_geneN(gn);
    problem.set_geneL(gl);
    problem.set_target_fitness(tf);



    Algorithm gen_alg;          // The ssGA being used
    gen_alg = new Algorithm(problem, popsize, gn, gl, pc, pm);


    for (int step=0; step<MAX_ISTEPS; step++)
    {  
      gen_alg.go_one_step();
      System.out.print(step); System.out.print("  ");
      System.out.print(gen_alg.get_bestf()); System.out.print("  ");
      System.out.print(gen_alg.get_worstf()); System.out.print("  ");
      System.out.print(gen_alg.get_avgf()); System.out.print("\n");

      if(     (problem.tf_known())                    &&
    		  (gen_alg.get_solution()).get_fitness()>=problem.get_target_fitness()
      )
      { System.out.print("Solution Found! After ");
        System.out.print(problem.get_fitness_counter());
        System.out.println(" evaluations");
        break;
      }

    }

    // Print the solution
    for(int i=0;i<gn*gl;i++)
      System.out.print( (gen_alg.get_solution()).get_allele(i) ); System.out.println();
    System.out.println((gen_alg.get_solution()).get_fitness());
  }

}
// END OF CLASS: Exe

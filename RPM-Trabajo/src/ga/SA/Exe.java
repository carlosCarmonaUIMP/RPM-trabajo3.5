///////////////////////////////////////////////////////////////////////////////
///            Simulated Annealing Algorithm 2020                           ///
///                by Carlos Carmona, August 2020                           ///
///                                                                         ///
///   Executable: set parameters, problem, and execution details here       ///
///////////////////////////////////////////////////////////////////////////////

package ga.SA;

public class Exe {
	public static void main(String args[]) throws Exception {
		ga.Problem problem; // The problem being solved

		// PARAMETERS PPEAKS
		int gn = 128; // Gene number
		int gl = 1; // Gene length
		double init_temp = 10000; // Initial temperature (T0)
		int accept_func = 2; // Acceptance function type (1=Exponential;
								// 2=Logistic)
		int cooling_scheme = 4; // Cooling scheme type (1=Exponential; 2=Linear;
								// 3=Boltzmann/Logistic; 4=Cauchy)
		double tf = (double) 300500; // Target fitness being sought
		long MAX_ISTEPS = 50000;

/*
		// PARAMETERS ONEMAX
		int gn = 512; // Gene number
		int gl = 1; // Gene length
		double init_temp = -1; // Initial temperature (T0)
		int accept_func = 1; // Acceptance function type (1=Exponential;
								// 2=Logistic)
		int cooling_scheme = 1; // Cooling scheme type (1=Exponential; 2=Linear;
								// 3=Boltzmann/Logistic; 4=Cauchy)
		double tf = (double) gn * gl; // Target fitness being sought
		long MAX_ISTEPS = 50000;
*/
		problem = new ga.ProblemPPeaks();
		//problem = new ga.ProblemOneMax();
		
		problem.set_geneN(gn);
		problem.set_geneL(gl);
		problem.set_target_fitness(tf);

		Algorithm sim_anneal_alg; // The ssSA being used
		sim_anneal_alg = new Algorithm(problem, gn, gl, init_temp, accept_func,
				cooling_scheme);

		for (int step = 1; step <= MAX_ISTEPS; step++) {
			sim_anneal_alg.go_one_step();
			System.out.print(step);
			System.out.print("  ");
			System.out.print(sim_anneal_alg.get_current_solution()
					.get_fitness());
			System.out.print("  ");
			System.out.println(sim_anneal_alg.get_bestf());

			if ((problem.tf_known())
					&& (sim_anneal_alg.get_bestf()) >= problem
							.get_target_fitness()) {
				System.out.print("Solution Found! After ");
				System.out.print(problem.get_fitness_counter());
				System.out.println(" evaluations");
				break;
			}

		}

		// Print algorithm's parameters
		sim_anneal_alg.print_parameters();

		// Print the solution
		for (int i = 0; i < gn * gl; i++)
			System.out
					.print((sim_anneal_alg.get_best_solution()).get_allele(i));
		System.out.println();
		System.out.println((sim_anneal_alg.get_best_solution()).get_fitness());
	}

}
// END OF CLASS: Exe

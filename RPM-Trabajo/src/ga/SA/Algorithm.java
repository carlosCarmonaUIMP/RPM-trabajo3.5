///////////////////////////////////////////////////////////////////////////////
///                 Simulated Annealing Algorithm                           ///
///                by Carlos Carmona, August 2020                           ///
///                                                                         ///
///////////////////////////////////////////////////////////////////////////////

package ga.SA;

import java.util.Random;
import java.lang.Math;

public class Algorithm {
	private int chrom_length; // Alleles per chromosome
	private int gene_number; // Number of genes in every chromosome
	private int gene_length; // Number of bits per gene
	private double init_temp; // Initial temperature
	private double curr_temp; // Current temperature
	private int accept_func; // Acceptance function (1=Exponential; 2=Logistic)
	private int cooling_scheme; // Cooling scheme (1=Exponential; 2=Linear;
								// 3=Boltzmann/Logistic; 4=Cauchy)
	private ga.Problem problem; // The problem being solved
	private static Random r; // Source for random values in this class
	private ga.Individual best_sol; // Best solution found
	private int best_sol_it; // Iteration at best_sol was found
	private ga.Individual curr_sol; // Current solution in search process
	private int curr_it; // Current algorithm iteration (at first 0. Needed for
							// cooling schemes)

	// CONSTRUCTOR
	public Algorithm(ga.Problem p, int gn, int gl, double init_temp,
			int accept_func, int cooling_scheme) throws Exception {
		gene_number = gn;
		gene_length = gl;
		chrom_length = gene_number * gene_length;

		this.problem = p;
		this.r = new Random();

		if (init_temp < 0) {
			this.init_temp = initialize_temp(30);
		} else {
			this.init_temp = init_temp;
		}
		curr_temp = this.init_temp;
		this.accept_func = accept_func;
		this.cooling_scheme = cooling_scheme;

		curr_sol = new ga.Individual(chrom_length);
		best_sol = new ga.Individual(chrom_length);
		best_sol_it = 0;
		curr_it = 0;

		curr_sol.set_fitness(problem.evaluateStep(curr_sol));
		best_sol.assign(curr_sol);
	}

	// AUTOMATIC TEMPERATURE INITIALIZATION
	private double initialize_temp(int sample_size) throws Exception {
		double meanSumDiffs = 0.0;

		for (int i = 0; i < sample_size; i++) {
			ga.Individual init_ind = new ga.Individual(chrom_length);
			problem.evaluateStep(init_ind);

			ga.Individual neighbor = generate_neighbor(init_ind);
			problem.evaluateStep(neighbor);

			meanSumDiffs += Math.abs((init_ind.get_fitness() - neighbor
					.get_fitness()));
		}

		meanSumDiffs /= sample_size;
		return (-meanSumDiffs / Math.log(0.9));
	}

	// GENERATE AND EVALUATE RANDOM NEIGHBOR
	private ga.Individual generate_neighbor(ga.Individual ind) throws Exception {
		ga.Individual neighbor = new ga.Individual(ind.get_length());
		neighbor.assign(ind);
		int i = r.nextInt(ind.get_length());
		if (neighbor.get_allele(i) == 1)
			neighbor.set_allele(i, (byte) 0);
		else
			neighbor.set_allele(i, (byte) 1);

		problem.evaluateStep(neighbor);

		return neighbor;
	}

	public void go_one_step() throws Exception {
		curr_it++;
		// 1. Generate neighbor solution
		ga.Individual candidate = generate_neighbor(curr_sol);

		// 2. Move to the new solution if better or accepted
		// deltaE = s0 - s'
		double deltaE = curr_sol.get_fitness() - candidate.get_fitness();
		if (deltaE < 0.0)  // If candidate improves current solution
			curr_sol.assign(candidate);
		else {
			switch (accept_func) {
			case 1:
				if (accept_exponential(deltaE))
					curr_sol.assign(candidate);
				break;

			case 2:
				if (accept_logistic(deltaE))
					curr_sol.assign(candidate);
				break;
			}
		}

		if (curr_sol.get_fitness() > best_sol.get_fitness()) {
			best_sol.assign(curr_sol);
			best_sol_it = curr_it;
		}

		// 3. Cool temperature
		switch (cooling_scheme) {
		case 1:
			cool_exponential(0.99);
			break;

		case 2:
			cool_linear(0.9);
			break;

		case 3:
			cool_Boltzmann();
			break;

		case 4:
			cool_Cauchy();
			break;
		}
	}

	private boolean accept_exponential(double deltaE) throws Exception {
		return r.nextDouble() <= Math.exp(-deltaE / curr_temp);
	}

	private boolean accept_logistic(double deltaE) throws Exception {
		double k = 1.0;
		return r.nextDouble() <= (1 - (1 / (1 + Math.exp(-deltaE
				/ (k * curr_temp)))));
	}

	private void cool_exponential(double alpha) throws Exception {
		curr_temp = alpha * curr_temp;
	}

	private void cool_linear(double eta) throws Exception {
		curr_temp = init_temp - eta * curr_it;
	}

	private void cool_Boltzmann() throws Exception {
		curr_temp = init_temp / (1 + Math.log(1 + curr_it));
	}

	private void cool_Cauchy() throws Exception {
		curr_temp = init_temp / (1 + curr_it);
	}

	public void print_parameters() throws Exception {
		System.out.println("Initial temperature: " + init_temp);
		switch (accept_func) {
		case 1:
			System.out.println("Acceptance function: " + "Exponential");
			break;
		case 2:
			System.out.println("Acceptance function: " + "Logistic");
			break;
		}
		switch (cooling_scheme) {
		case 1:
			System.out.println("Cooling scheme: " + "Exponential");
			break;
		case 2:
			System.out.println("Cooling scheme: " + "Linear");
			break;
		case 3:
			System.out.println("Cooling scheme: " + "Boltzmann/Logistic");
			break;
		case 4:
			System.out.println("Cooling scheme: " + "Cauchy");
			break;
		}
		System.out.println("--------------------------------");
	}

	public ga.Individual get_current_solution() throws Exception {
		return curr_sol;
	}

	public double get_bestf() throws Exception {
		return best_sol.get_fitness();
	}

	public ga.Individual get_best_solution() throws Exception {
		return best_sol; // The best solution found is returned
	}

	public int get_best_solution_it() throws Exception {
		return best_sol_it;
	}

	public double get_current_temp() throws Exception {
		return curr_temp;
	}

}
// END OF CLASS: Algorithm


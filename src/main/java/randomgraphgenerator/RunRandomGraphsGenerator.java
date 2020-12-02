package randomgraphgenerator;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomStringUtils;


public class RunRandomGraphsGenerator {

	static public String getRandomAlphaNumericString() {
		String randomString = RandomStringUtils.random(10, true, true);
		return randomString;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting....");
		
		
		int num_datasets = 50;
		//double prob = 0.1000;
		int nodes = 500; //initialize starting number of nodes
		//int max_nodes = 10000;
		//int increments = 500;
		

		AtomicInteger countdown = new AtomicInteger(num_datasets);

		System.out.println("Starting (" + num_datasets + " datasets with " + nodes
				+ " nodes)");
		System.out.print("Remaining: ");
		//nodes = max_nodes;
		Random random = new Random();


		for(int i=0; i<num_datasets; i++) {

			GenerateRG rg = new GenerateRG(random.nextDouble(), nodes, getRandomAlphaNumericString(), i+1, GraphDataType.NETWORK);
			rg.setCountDown(countdown);
			rg.startThread(); 
			//threads.submit

		}

		System.out.println("....Done");

	}

}

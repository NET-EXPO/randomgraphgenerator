package randomgraphgenerator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

public class GenerateRG implements Runnable{

	private Thread generator;
	private int thread_id = -1;

	private GraphDataType graph_type;

	NumberFormat formatter = new DecimalFormat("#0.0000000");

	private double p = 0.00000000;
	private int num_nodes = 0; //default

	private Multimap<Integer, Integer> graph = null; // for simple graph

	private Map<Integer, Integer> y_data = null;

	//private Map<Map<Integer, Integer>, Integer> y_mode_data = null;
	AtomicInteger countdown;

	private StringBuilder y_mode_data;

	private String outputFileName = "";

	public GenerateRG(double probability_link, int nodes, String output_file, int thread_number, GraphDataType data_type) {
		this.thread_id = thread_number;
		this.p = probability_link;
		this.num_nodes = nodes;

		outputFileName = nodes + "/" + output_file;
		this.graph_type = data_type;
	}

	public GenerateRG(double probability_link, int nodes, String output_file, GraphDataType data_type) {
		// TODO Auto-generated constructor stub
		this.p = probability_link;
		this.num_nodes = nodes;

		outputFileName = nodes + "/" + output_file;

		
	}
	
	/*
	public GenerateRG(int max_tie, int nodes, String output_file, GraphDataType data_type) {
		
		this.num_nodes = nodes;
		outputFileName = nodes + "/" + output_file;
		
	}*/
	
	public void setCountDown(AtomicInteger value) {
		this.countdown = value;
	}

	private void init() throws IOException {

		graph = ArrayListMultimap.create();

		//init writing

		File file = new File(outputFileName +".csv");

		try {
			Files.createParentDirs(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CharSink chs = Files.asCharSink(
				file, Charsets.UTF_8, FileWriteMode.APPEND);

		chs.write("source,target\n");

		//populate

		for(int i=0; i< num_nodes; i++) {
			for(int j=0; j < num_nodes; j++) {

				if(i != j) {

					//generate random number
					//double rp = this.getRandomProbability();

					double rp = Math.random();
					System.out.print("numbers are " + rp + " and " + p);
					if(rp <= p) {
						//graph.put(i, j);
						chs.write(i +"," +j +"\n");
						System.out.println(" added");
					}
					else {
						System.out.println();
					}


				}

			}

		}


	}

	private int assignMode() {
		int mode_value = 0;

		double m1 = this.getRandomProbability();
		double m2 = this.getRandomProbability();

		if(getRandomNumber(0,100)<50) {

			if(m1>=m2) {
				//1
				mode_value = 1;
			}
			else {
				//2
				mode_value = 2;
			}

		}
		else {

			if(m1<m2) {
				//1
				mode_value = 1;
			}
			else {
				//2
				mode_value = 2;
			}

		}


		return mode_value;
	}

	public void createYModeData() throws IOException 
	{

		File yfile = new File(outputFileName +"_y_mode.csv");
		Files.createParentDirs(yfile);

		CharSink chs = Files.asCharSink(
				yfile, Charsets.UTF_8, FileWriteMode.APPEND);

		chs.write("id,y,mode\n");
		

		for(int i=0; i< num_nodes; i++) {
			//assign the y value
			double r1 = this.getRandomProbability();
			double r2 = this.getRandomProbability();

			if(getRandomNumber(0,100)<50) {

				if(r1>=r2) {

					chs.write(i +",1," + this.assignMode()+"\n");

				}
				else {

					chs.write(i +",0," +this.assignMode()+"\n");
				}

			}
			else {

				if(r1<r2) {
					chs.write(i +",1," + this.assignMode()+"\n");
				}
				else {
					chs.write(i +",0," +this.assignMode()+"\n");
				}

			}


		}
	}

	public void createYData() throws IOException
	{
		//System.out.println("stoppe here");
		//System.exit(0);

		File yfile = new File(outputFileName +"_y.csv");

		Files.createParentDirs(yfile);

		CharSink chs = Files.asCharSink(
				yfile, Charsets.UTF_8, FileWriteMode.APPEND);

		chs.write("id,y\n");


		for(int i=0; i< num_nodes; i++) 
		{
			double r1 = this.getRandomProbability();
			double r2 = this.getRandomProbability();

			if(getRandomNumber(0,100)<50) {

				if(r1>=r2) {
					//y_data.put(i, 1);
					chs.write(i +",1\n");
				}
				else {
					//y_data.put(i, 0);
					chs.write(i +",0\n");
				}

			}
			else {

				if(r1<r2) {
					//y_data.put(i, 1);
					chs.write(i +",1\n");
				}
				else {
					chs.write(i +",0\n");
					//y_data.put(i, 0);
				}

			}


		}



	}

	private double getRandomProbability() {

		double randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
		return randomNum/100;
	}

	private int getRandomNumber(int min, int max) {
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

		return randomNum;
	}

	static public String getRandomAlphaNumericString() {
		String randomString = RandomStringUtils.random(10, true, true);
		return randomString;
	}



	

	public void run() {
		// TODO Auto-generated method stub
		//
		//System.out.println("Generator " + thread_id + "\t: started");
		startThread();
		
		try {
			//System.out.println("Generator " + thread_id + "\t: creating network data");
			init();
			//System.out.println("Generator " + thread_id + "\t: creating attributes");
			if(this.graph_type == GraphDataType.NETWORK) {
				createYData();
			}
			else if(this.graph_type == GraphDataType.AFFILIATION) {
				createYModeData();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.print(countdown.decrementAndGet() + " ");
	}
	
	public void setJoin() {
		try {
			generator.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startThread() {
		//GenerateRG rg = new GenerateRG(prob, nodes, getRandomAlphaNumericString());

		if(generator == null) {
			generator = new Thread(this, Integer.toString(thread_id));
			generator.start(); 
		}



	}

}



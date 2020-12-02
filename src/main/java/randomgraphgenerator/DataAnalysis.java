package randomgraphgenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;

import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;

public class DataAnalysis {
	
	
	private String folderPath = null;
	public DataAnalysis(String folderPath) {
		
		
		this.folderPath = folderPath;
		
		System.out.println("\n\n*****************************");
		System.out.println(this.folderPath);
		System.out.println("*****************************\n\n");
		
	}
	
	public void extractYAttributeCount() {
		long total_count = 0;
		FilenameFilter filterByAnyYFile = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				
				if(name.contains(".DS_Store")) {
					return false;
				}
				
				if(name.endsWith("_y.csv")) {
					return true;
				}
				else if (name.endsWith("_y_mode.csv")) {
					return true;
				}
				else {
					return false;
				}
				
			}
		};
		
		File targetDirectory = new File(this.folderPath);
		File[] yFiles = targetDirectory.listFiles(filterByAnyYFile);
		
		for(File yFile : yFiles) {
			try {
				Table table = Table.read().csv(yFile.getAbsolutePath());
				total_count = total_count + table.where(table.numberColumn("y").isEqualTo(1)).rowCount();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("\nY SUMMARY DATA");
		System.out.println("--------------");
		System.out.println("Y attribute counts: " + total_count);
		//System.out.println("Total files: " + yFiles.length);
		System.out.println("Average y attributes: " + total_count/yFiles.length);
		
	}
	
	public void extractTieCounts() {
		long total_count = 0;
		
		FilenameFilter filterByNetFile = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				
				if(name.contains(".DS_Store")) {
					return false;
				}
				
				if(!name.endsWith("_y.csv") && !name.endsWith("_y_mode.csv")) {
					return true;
				}
				else {
					return false;
				}
				
			}
		};
		
		File targetDirectory = new File(this.folderPath);
		File[] netFiles = targetDirectory.listFiles(filterByNetFile);
		for(File netFile : netFiles) {
			//System.out.println(netFile.getAbsolutePath());
			
			try {
				//get tie count from the file
				total_count = total_count + Table.read().csv(netFile.getAbsolutePath()).rowCount();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("\nTIE SUMMARY DATA");
		System.out.println("----------------");
		System.out.println("Total Count: " + total_count);
		System.out.println("Average Tie Count: " + total_count/netFiles.length);
		
	}

	public void extractModeData() {
		long first_mode_total_count = 0;
		long second_mode_total_count = 0;
		FilenameFilter filterByAnyYFile = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				
				if(name.contains(".DS_Store")) {
					return false;
				}
				
				if(name.endsWith("_y.csv")) {
					return true;
				}
				else if (name.endsWith("_y_mode.csv")) {
					return true;
				}
				else {
					return false;
				}
				
			}
		};
		
		File targetDirectory = new File(this.folderPath);
		File[] yFiles = targetDirectory.listFiles(filterByAnyYFile);
		
		
		for(File yFile : yFiles) {
			try {
				Table table = Table.read().csv(yFile.getAbsolutePath());
				first_mode_total_count = first_mode_total_count + table.where(table.numberColumn("mode").isEqualTo(1)).rowCount();
				second_mode_total_count = second_mode_total_count + table.where(table.numberColumn("mode").isEqualTo(2)).rowCount();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		System.out.println("\n\nMODE SUMMARY DATA");
		System.out.println("-------------------");
		System.out.println("first mode totals: " + first_mode_total_count);
		System.out.println("first mode average: " + first_mode_total_count/yFiles.length);
		System.out.println("second mode totals: " + second_mode_total_count);
		System.out.println("second mode average: " + second_mode_total_count/yFiles.length);
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String path_to_folder = "/Users/mac/Desktop/10000";
		/*
		System.setOut(new PrintStream(new FileOutputStream("data_analysis_output_"+
		//"9500" +
				path_to_folder.substring(path_to_folder.lastIndexOf('/')+1) +
				".txt"), true));
		/*  Execution */
		DataAnalysis da = new DataAnalysis(path_to_folder);
		da.extractTieCounts();
		da.extractYAttributeCount();
		da.extractModeData();
		
	}

}

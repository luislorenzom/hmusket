package es.udc.gac.hmusket;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import es.udc.gac.hadoop.sequence.parser.mapreduce.FastAInputFormat;
import es.udc.gac.hadoop.sequence.parser.mapreduce.FastQInputFormat;

public class HMusket {
	
	public static String fileIn;
	public static String fileOut;
	public static String fileType;

	public static void main(String[] args) throws Exception {
		
	    // Instances a configuration
		Configuration conf = new Configuration();

		// Parses arguments. Establishes some global variables
		// and creates the string arguments to send it to musket
		String arguments = CLIParser.parse(args);
		conf.set("arguments", arguments);
		conf.set("localSequenceDataset", fileIn+"_local");
		
		// Creates a job
		Job job = Job.getInstance(conf, "HMusket");
		job.setJarByClass(HMusket.class);
		
		
		// Sets input and output path
		FileInputFormat.addInputPath(job, new Path(fileIn));
		FileOutputFormat.setOutputPath(job, new Path(fileOut+"_hadoop"));
		
		// Establishes what kind of input format is required
		if (fileType.equalsIgnoreCase("a")) {
			job.setInputFormatClass(FastAInputFormat.class);
		} else if (fileType.equalsIgnoreCase("q")) {
			job.setInputFormatClass(FastQInputFormat.class);
		}
		
		// Establishes key, value and format class
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// Set mapper and reducers
		job.setMapperClass(HMusketMapper.class);
		job.setNumReduceTasks(0);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}

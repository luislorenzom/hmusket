package es.udc.gac.hmusket;

import java.util.ArrayList;
import java.util.List;

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
import es.udc.gac.hadoop.sequence.parser.mapreduce.PairedEndSequenceInputFormat;
import es.udc.gac.hadoop.sequence.parser.mapreduce.SingleEndSequenceInputFormat;

public class HMusket {
    
    public static final String applicationName = "HMusket";
	public static List<String> fileIn = new ArrayList<>();
	public static String fileOut;
	public static String fileType;
	public static String localCopyPath;
	public static Boolean pairEnd = Boolean.FALSE;
	private static Class<? extends SingleEndSequenceInputFormat> inputFormatClass;

	public static void main(String[] args) throws Exception {

		// Instances a configuration
		Configuration conf = new Configuration();

		// Parses arguments. Establishes some global variables
		// and creates the string arguments to send it to musket
		String arguments = CLIParser.parse(args);
		conf.set("arguments", arguments);
		conf.set("localSequenceDataset", HMusket.localCopyPath);

		if (pairEnd) {
			// Adds the second input file in the job configuration
			conf.set("localSequenceDataset_right", fileIn.get(1) + "_local");
		}

		// Creates a job
		Job job = Job.getInstance(conf, HMusket.applicationName);
		job.setJarByClass(HMusket.class);

		// Sets output path
		FileOutputFormat.setOutputPath(job, new Path(fileOut + "_hadoop"));

		// Establishes what kind of input format is required
		if (fileType.equalsIgnoreCase("a")) {
			inputFormatClass = FastAInputFormat.class;
		} else if (fileType.equalsIgnoreCase("q")) {
			inputFormatClass = FastQInputFormat.class;
		}

		// Sets input format class
		if (pairEnd) {
			// Sets input format class for paired end dataset
			job.setInputFormatClass(PairedEndSequenceInputFormat.class);

			// Sets the input path and also the input format class
			PairedEndSequenceInputFormat.setLeftInputPath(job, new Path(fileIn.get(0)), inputFormatClass);
			PairedEndSequenceInputFormat.setRightInputPath(job, new Path(fileIn.get(1)), inputFormatClass);

			// Sets Mapper
			job.setMapperClass(HMusketPairEndMapper.class);
		} else {
			// Sets input format class for single end dataset
			job.setInputFormatClass(inputFormatClass);

			// Sets input path
			FileInputFormat.addInputPath(job, new Path(fileIn.get(0)));

			// Sets Mapper
			job.setMapperClass(HMusketSingleEndMapper.class);
		}

		// Sets Reducers
		job.setNumReduceTasks(0);

		// Establishes job's key, value and format class
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}

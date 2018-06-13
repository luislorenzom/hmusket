package es.udc.gac.hmusket;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HMusketPairEndMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	private PrintWriter leftWriter;
	private PrintWriter rightWriter;

	@Override
	protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {

		super.setup(context);
		this.leftWriter = new PrintWriter(context.getConfiguration().get("localSequenceDataset_left"), "UTF-8");
		this.rightWriter = new PrintWriter(context.getConfiguration().get("localSequenceDataset_right"), "UTF-8");
	}

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {

		// Converts and saves the edge between both DataSets
		Integer k = Integer.parseInt(key.toString());

		// Gets the different DataSets
		byte[] valueAsStringArray = value.copyBytes();
		byte[] leftValue = Arrays.copyOfRange(valueAsStringArray, 0, k);
		byte[] rightValue = Arrays.copyOfRange(valueAsStringArray, k, valueAsStringArray.length);

		// Prints both DataSets
		this.leftWriter.println(new String(leftValue));
		this.rightWriter.println(new String(rightValue));
	}

	@Override
	protected void cleanup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {

		super.cleanup(context);

		// Closes both writers
		this.leftWriter.close();
		this.rightWriter.close();

		// Retrieve the arguments from the configuration
		String arguments = context.getConfiguration().get("arguments");

		// Change the argument placeholder by a mix of TimeStamp and task
		// attempt id
		String timestamp = String.valueOf((System.currentTimeMillis()));
		String taskAttempId = String.valueOf(context.getTaskAttemptID().getTaskID().getId());

		String uniqueId = timestamp + "-" + taskAttempId;

		arguments = arguments.replace("${placeholder}", uniqueId);

		// Native call to musket
		new MusketCaller().callMusket(arguments);

		// Delete both files
		new File(context.getConfiguration().get("localSequenceDataset_left"), "UTF-8").delete();
		new File(context.getConfiguration().get("localSequenceDataset_right"), "UTF-8").delete();

		// Upload the musket output to HDFS
		FileSystem hdfs = FileSystem.get(context.getConfiguration());

		Path src0 = new Path(context.getConfiguration().get("fileOut").replace("${placeholder}", uniqueId) + ".0");
		Path src1 = new Path(context.getConfiguration().get("fileOut").replace("${placeholder}", uniqueId) + ".1");

		Path dst0 = new Path(context.getConfiguration().get("folderOut") + "HMusket-output-"
				+ String.valueOf((System.currentTimeMillis())) + ".0");
		Path dst1 = new Path(context.getConfiguration().get("folderOut") + "HMusket-output-"
				+ String.valueOf((System.currentTimeMillis())) + ".1");

		hdfs.create(dst0);
		hdfs.create(dst1);

		hdfs.copyFromLocalFile(src0, dst0);
		hdfs.copyFromLocalFile(src1, dst1);
	}
}

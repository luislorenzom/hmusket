package es.udc.gac.hmusket;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

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

		// Converts and saves the edge between both datasets
		Integer k = Integer.parseInt(key.toString());

		// Gets the different datasets
		byte[] valueAsStringArray = value.copyBytes();
		byte[] leftValue = Arrays.copyOfRange(valueAsStringArray, 0, k);
		byte[] rightValue = Arrays.copyOfRange(valueAsStringArray, k, valueAsStringArray.length);

		// Prints both datasets
		this.leftWriter.println(leftValue);
		this.rightWriter.println(rightValue);
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

		// Native call to musket
		new MusketCaller().callMusket(arguments);

		// Delete both files
		new File(context.getConfiguration().get("localSequenceDataset_left")).delete();
		new File(context.getConfiguration().get("localSequenceDataset_right")).delete();
	}
}

package es.udc.gac.hmusket;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HMusketSingleEndMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	private PrintWriter writer;

	@Override
	protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {

		super.setup(context);
		this.writer = new PrintWriter(context.getConfiguration().get("localSequenceDataset"), "UTF-8");
	}

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String line = value.toString();
		this.writer.print(line);
	}

	@Override
	protected void cleanup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {

		super.cleanup(context);

		this.writer.close();

		// Retrieve the arguments from the configuration
		String arguments = context.getConfiguration().get("arguments");

		// Native call to musket
		new MusketCaller().callMusket(arguments);
		
		// Delete file
		new File(context.getConfiguration().get("localSequenceDataset"), "UTF-8").delete();
		
		// Upload the musket ouput to hdfs
		FileSystem hdfs = FileSystem.get(context.getConfiguration());
		
		Path src = new Path(context.getConfiguration().get("fileOut"));
		Path dst = new Path(context.getConfiguration().get("folderOut") + "HMusket-output-" + String.valueOf((System.currentTimeMillis())));
		
		hdfs.create(dst);
		
		hdfs.copyFromLocalFile(src, dst);
	}
}

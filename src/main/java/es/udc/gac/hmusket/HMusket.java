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
import es.udc.gac.hmusket.exception.FileInputTypeNotFoundException;

public class HMusket {

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.err.printf("Usage: [generic options] <input> <output> <file input type>\n");
		}

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "HMusket");
		job.setJarByClass(HMusket.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		if (args[2].equalsIgnoreCase("a")) {
			job.setInputFormatClass(FastAInputFormat.class);
		} else if (args[2].equalsIgnoreCase("q")) {
			job.setInputFormatClass(FastQInputFormat.class);
		} else {
			throw new FileInputTypeNotFoundException(args[2] + ": type not found");
		}

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setMapperClass(HMusketMapper.class);
		job.setNumReduceTasks(0);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}

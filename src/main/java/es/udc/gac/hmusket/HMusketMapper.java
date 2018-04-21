package es.udc.gac.hmusket;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HMusketMapper extends
        Mapper<LongWritable, Text, Text, IntWritable> {

    private final String SAVE_FILE_PATH = "/Volumes/Datos/luis/Desktop/prueba.txt";
    private PrintWriter writer;

    @Override
    protected void setup(
            Mapper<LongWritable, Text, Text, IntWritable>.Context context)
            throws IOException, InterruptedException {

        super.setup(context);
        this.writer = new PrintWriter(this.SAVE_FILE_PATH, "UTF-8");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        this.writer.print(line);
    }

    @Override
    protected void cleanup(
            Mapper<LongWritable, Text, Text, IntWritable>.Context context)
            throws IOException, InterruptedException {

        super.cleanup(context);
        
        // Retrieve the arguments from the configuration
        String arguments = context.getConfiguration().get("arguments");
        
        // Native call to musket
        new MusketCaller().callMusket(arguments);
        
        this.writer.close();
    }
}

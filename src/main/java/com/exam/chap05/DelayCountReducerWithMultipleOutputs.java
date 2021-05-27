package com.exam.chap05;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class DelayCountReducerWithMultipleOutputs 
		extends Reducer<Text, IntWritable, Text, IntWritable>{
	
	private MultipleOutputs<Text, IntWritable> mos;
	private Text outputKey = new Text();
	private IntWritable result = new IntWritable();
	
	@Override
	protected void setup(Reducer<Text, IntWritable, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		mos = new MultipleOutputs<Text, IntWritable>(context);
	}
	
	public void reduce( Text key, Iterable<IntWritable> values, Context context ) 
			throws IOException, InterruptedException {
		
		String[] colums = key.toString().split(",");
		outputKey.set( colums[1] + "," + colums[2] );
		
		if( colums[0].equals("D") ) {
			
			int sum = 0;
			for( IntWritable value : values ) sum += value.get();
			result.set(sum);
			
			mos.write( "departure", outputKey, result );
			
		} else {
			
			int sum = 0;
			for( IntWritable value : values ) sum += value.get();
			result.set(sum);
			
			mos.write( "arrival", outputKey, result );
			
		}
		
	}
	
	
	@Override
	protected void cleanup(Reducer<Text, IntWritable, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
			mos.close();
	}
	
	
}

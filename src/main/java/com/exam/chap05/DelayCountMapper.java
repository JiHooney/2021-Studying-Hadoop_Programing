package com.exam.chap05;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DelayCountMapper 
		extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	//작업구분
	private String workType;
	
	//맵출력값
	private final static IntWritable outputValue = new IntWritable(1);
	//맵출력키
	private Text outputKey = new Text();
	
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		workType = context.getConfiguration().get("workType");
	}
	
	public void map( LongWritable key, Text value, Context context ) 
			throws IOException, InterruptedException {
		
		AirlinePerformanceParser parser = new AirlinePerformanceParser(value);
		
		//출발 지연 데이터 출력
		if( workType.equals( "departure" ) ) {
			
			if( parser.getDepartureDelayTime() > 0 ) {
				
				outputKey.set( parser.getYear() + "," + parser.getMonth() );
				context.write(outputKey, outputValue);
			}
			
		} else if( workType.equals( "arrival" ) ) {
			
			if( parser.getArriveDelayTime() > 0 ) {
				
				outputKey.set( parser.getYear() + "," + parser.getMonth() );
				context.write(outputKey, outputValue);
			}
			
		}
	}
	
	
	
}

package com.exam.chap05;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DelayCountMapperWithMultipleOutputs 
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
		if( parser.isDepartureDelayAvailable() ) {
			
			if( parser.getDepartureDelayTime() > 0 ) {
				
				outputKey.set( "D," + parser.getYear() + "," + parser.getMonth() );
				context.write(outputKey, outputValue);
				
			} else if( parser.getDepartureDelayTime() == 0 ) {
				
				context.getCounter(DelayCounter.scheduled_departure).increment(1);
				
			} else if( parser.getDepartureDelayTime() < 0 ) {
				
				context.getCounter(DelayCounter.early_departure).increment(1);
				
			} else {
				
				context.getCounter(DelayCounter.not_availavle_departure).increment(1);
			}
			
		}
		
		
		if( parser.isArriveDelayAvailable() ) {
			
			if( parser.getArriveDelayTime() > 0 ) {
				
				outputKey.set( "A," + parser.getYear() + "," + parser.getMonth() );
				context.write(outputKey, outputValue);
			}	
			
		} else if( parser.getArriveDelayTime() == 0 ) {
			
			context.getCounter(DelayCounter.scheduled_arrival).increment(1);
			
		} else if( parser.getArriveDelayTime() < 0 ) {
			
			context.getCounter(DelayCounter.early_arrival).increment(1);		
			
		} else {
			
			context.getCounter(DelayCounter.not_available_arrival).increment(1);
		}
			
	}
}

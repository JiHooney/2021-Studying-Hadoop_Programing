package com.exam.chap05;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ArrivalDelayCountMapper	
	extends Mapper<LongWritable, Text,	Text, IntWritable> {
	
	//맵 출력값
	private final static IntWritable outputValue = new IntWritable(1);
	
	//맵 출력키
	private Text outputKey = new Text();
	
	public void map( LongWritable key, Text value, Context context ) 
			throws IOException, InterruptedException {
		
		AirlinePerformanceParser parser = new AirlinePerformanceParser(value);
		
		outputKey.set( parser.getYear() + "-" + parser.getMonth() + "," );
		
		if( parser.getArriveDelayTime() > 0 ) {
			//출력 데이터 생성
			context.write( outputKey, outputValue );
		}
	}
	
	
}

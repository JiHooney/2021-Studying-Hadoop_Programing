package com.exam.chap05;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class ArrivalDelayCount {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
		//입출력 데이터 경로 확인
		if( args.length != 2 ) {
			System.err.println("Usage: ArrivalDelayCount <input> <output>");
			System.exit(2);
		}
		
		//잡 이름 설정
		Job job = new Job( conf, "ArrivalDelayCount" );
		
		//입출력 데이터 경로 설정
		FileInputFormat.addInputPath( job, new Path(args[0]) );
		FileOutputFormat.setOutputPath( job,  new Path(args[1]) );
		
		//잡 클래스 설정
		job.setJarByClass( ArrivalDelayCount.class );
		//매퍼 클래스 설정
		job.setMapperClass( ArrivalDelayCountMapper.class );
		//리듀서 클래스 설정
		job.setReducerClass( DelayCountReducer2.class );
		
		//입출력 데이터 포맷 설정
		job.setInputFormatClass( TextInputFormat.class );
		job.setOutputFormatClass( TextOutputFormat.class );
		
		//출력키값 데이터 유형 설정
		job.setOutputKeyClass( Text.class );
		job.setOutputValueClass( IntWritable.class );
		
		job.waitForCompletion( true );
		
		
		
		
		

	}

}

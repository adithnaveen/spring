package com.comviva.springbatch;

import org.springframework.batch.item.ItemProcessor;

/*
 * 
 * ItemProcessor is Optional, and called after item read but before item write. It gives us the opportunity to perform a business logic on each item.In our case, for example, we will filter out all the items whose percentage is less than 75. So final result will only have records with percentage >= 75.
 */
public class ExamResultItemProcessor implements ItemProcessor<ExamResult, ExamResult> {

	@Override
	public ExamResult process(ExamResult result) throws Exception {
//		System.out.println("Processing result :" + result);
//
//		if (result.getPercentage() < 75) {
//			return null;
//		}

		return result;
	}

}

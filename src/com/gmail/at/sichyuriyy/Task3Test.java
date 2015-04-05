package com.gmail.at.sichyuriyy;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class Task3Test {
	
	int n;
	int countOfThreads;
	BigInteger answer;
	
	public Task3Test(int n, int countOfThreads, BigInteger answer) {
		this.n = n;
		this.countOfThreads = countOfThreads;
		this.answer = answer;
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		Object [][]data = {
				{1, 1, new BigInteger("4")},
				{2, 1, new BigInteger("6")},
				{3, 3, new BigInteger("22")},
				{4, 2, new BigInteger("30")}
		};
		return Arrays.asList(data);
	}

	@Test
	public void test() {
		SumByMe sum1 = new SumByMe();
		SumByExecutor sum2 = new SumByExecutor();
		sum1.countSum(n, countOfThreads);
		sum2.countSum(n, countOfThreads);
		assertEquals(sum1.getSum().toString(), answer.toString());
		assertEquals(sum2.getSum().toString(), answer.toString());
		
	}

}

package com.gmail.at.sichyuriyy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Task3v1 {
	public static void main(String[] args) throws IOException {
		int n;
		int countOfThreads;
		int mode;
		Scanner inputStream;
		PrintWriter outputStream;
		String inputFile = null;
		String outputFile = null;
		Scanner in = new Scanner(System.in);
		if (args.length == 0) {
			System.out
					.println("choose mode\n1)Inputs from console\n2)Inputs from file");
			mode = in.nextInt();
		} else if (args.length == 1) {
			mode = Integer.parseInt(args[0]);
		} else if (args.length == 2) {
			mode = Integer.parseInt(args[0]);
			inputFile = args[1];
		} else {
			mode = Integer.parseInt(args[0]);
			inputFile = args[1];
			outputFile = args[2];
		}

		if (mode == 1) {

			System.out.println("Enter input parametr n  and count of threads");
			n = in.nextInt();
			countOfThreads = in.nextInt();

		} else {

			if (inputFile == null) {
				System.out.println("Enter path to the input file");
				inputFile = in.next();
			}
			inputStream = new Scanner(new BufferedReader(new FileReader(inputFile)));
			n =inputStream.nextInt();
			countOfThreads = inputStream.nextInt();
			inputStream.close();
			
		}
		if (outputFile == null) {
			System.out.println("Enter path to the output file");
			outputFile = in.next();
		}
		outputStream = new PrintWriter(new FileWriter(outputFile));
		
		SumByMe sum1 = new SumByMe();
		sum1.countSum(n, countOfThreads);
		SumByExecutor sum2 = new SumByExecutor();
		sum2.countSum(n, countOfThreads);
		outputStream.println("Without using executor: " + sum1.getSum());
		outputStream.println("Executor :" + sum2.getSum());
		outputStream.close();
		in.close();
		
		
	}

}

class SumByExecutor {
	BigInteger sum = new BigInteger("0");

	Vector<Future<BigInteger>> elementOfSum = new Vector<Future<BigInteger>>();

	void countSum(int n, int countThreads) {
		ExecutorService exec = Executors.newFixedThreadPool(countThreads);
		int from = n + 1;
		int to = 0;

		for (int i = 0; i < countThreads - 1; i++) {
			to = from - 1;
			from = to - n / countThreads + 1;
			elementOfSum.add(exec.submit(new countElementOfSum(from, to)));
		}
		to = from - 1;
		from = 1;
		elementOfSum.add(exec.submit(new countElementOfSum(from, to)));
		exec.shutdown();
		try {
			exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		for (Future<BigInteger> fi : elementOfSum) {
			try {
				sum = sum.add(fi.get());
			} catch (ExecutionException e) {
				System.out.println(e);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}

	public BigInteger getSum() {
		return sum;
	}
}

class SumByMe {
	BigInteger sum = new BigInteger("0");
	Vector<Future<BigInteger>> elementOfSum = new Vector<Future<BigInteger>>();

	void countSum(int n, int countThreads) {
		taskElementOfSum[] pool = new taskElementOfSum[countThreads];
		int from = n + 1;
		int to = 0;
		int i;

		for (i = 0; i < countThreads - 1; i++) {
			to = from - 1;
			from = to - n / countThreads + 1;
			pool[i] = new taskElementOfSum(from, to);
		}
		to = from - 1;
		from = 1;
		pool[i] = new taskElementOfSum(from, to);
		for(i=0; i<pool.length; i++) pool[i].start();
		for(i=0; i<pool.length; i++) 
			try {
				pool[i].join();
			}catch(InterruptedException e) {
				System.out.println(e);
			}
		for(i=0; i < pool.length; i++)
			sum = sum.add(pool[i].partOfSum);
	}
	public BigInteger getSum(){
		return sum;
	}

}

class taskElementOfSum extends Thread {
	public BigInteger partOfSum = new BigInteger("0");
	int from;
	int to;

	public taskElementOfSum(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public void run() {
		int step;

		BigInteger elOfSum = new BigInteger("0");
		for (int i = from; i <= to; i++) {
			BigInteger stepin = new BigInteger("2");
			step = i - (i % 2 == 0 ? 1 : -1);
			stepin = stepin.pow(step);
			elOfSum = elOfSum.add(stepin);

		}

		partOfSum = elOfSum;
	}
}

class countElementOfSum implements Callable<BigInteger> {
	int from;
	int to;

	public countElementOfSum(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public BigInteger call() {
		int step;

		BigInteger elOfSum = new BigInteger("0");
		for (int i = from; i <= to; i++) {
			BigInteger stepin = new BigInteger("2");
			step = i - (i % 2 == 0 ? 1 : -1);
			stepin = stepin.pow(step);

			elOfSum = elOfSum.add(stepin);

		}

		return elOfSum;

	}
}
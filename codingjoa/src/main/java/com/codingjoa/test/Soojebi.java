package com.codingjoa.test;

import java.util.ArrayList;
import java.util.List;

class Parent {
	int x = 100;
	
	Parent() {
        this(500);
        System.out.println("Parent() 끝");
    }
	
	Parent(int x) {
		System.out.println("Parent(int x) 시작");
        this.x = x;
        System.out.println("Parent(int x) 끝");
	}
	
	int getX() {
		return this.x;
	}
}

class Child extends Parent {
	int x = 4000;
	
	Child() {
		this(5000);
		System.out.println("Child() 끝");
	}
	
	Child(int x) {
		System.out.println("Child(int x) 시작");
        this.x = x;
        System.out.println("Child(int x) 끝");
	}
	
	
}

public class Soojebi {
	public static void main(String[] args) {
		Child child = new Child();
		System.out.println(child.getX());
		
		List<String> a = new ArrayList<>();
		a.get(0);
	}
	
}

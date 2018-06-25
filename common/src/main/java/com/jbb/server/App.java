package com.jbb.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.jbb.server.common.util.StringUtil;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
    	
    	
    	try {
			File file = new File("/Users/VincentTang/ws/jbbmgthome/text.txt");
			file.setReadable(true, false);
			
			FileOutputStream fop = new FileOutputStream(file);
			fop.write(123);
			fop.flush();
			fop.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
//        byte[] x1 = null ;
//        new String(x1);

        // String str = "012|011";
        // String[] x = str.split("\\|");
        // System.out.println(Util.printListWithDelimiter(x, ",", 20));
        //
        // for (int i = 0; i < 10; i++) {
        // int index = (int)(Math.random() * 5);
        // System.out.println(index);
        // }
//        for (int i = 0; i < 100; i++) {
//            Set<Integer> setA = new HashSet<Integer>();
//
//            Set<Integer> setD = new HashSet<Integer>();
//
//            setA.add(1);
//            setA.add(2);
//            setA.add(3);
//            setA.add(4);
//            setA.add(5);
//            setA.add(6);
//            setA.add(7);
//            setA.add(8);
//            setA.add(9);
//
//            Set<Integer> setG1 = new HashSet<Integer>();
//            setG1.add(1);
//            setG1.add(2);
//            setG1.add(3);
//
//            Set<Integer> setG2 = new HashSet<Integer>();
//            setG2.add(1);
//            setG2.add(3);
//            setG2.add(5);
//
//            Set<Integer> setG3 = new HashSet<Integer>();
//            setG3.add(2);
//            setG3.add(5);
//            setG3.add(8);
//
//            Set<Integer> setG4 = new HashSet<Integer>();
//            setG4.add(2);
//            setG4.add(7);
//            setG4.add(9);
//
//            Map<String, Set<Integer>> map = new HashMap<String, Set<Integer>>();
//
//            map.put(StringUtil.randomAlphaNum(4), setG1);
//            map.put(StringUtil.randomAlphaNum(4), setG2);
//            map.put(StringUtil.randomAlphaNum(4), setG3);
//            map.put(StringUtil.randomAlphaNum(4), setG4);
//
//            map.forEach((key, setC) -> {
//                // 差值
//                Set<Integer> setE = new HashSet<Integer>();
//                setE.addAll(setC);
//                setE.removeAll(setD);
//                // System.out.println("==========setE=======");
//                // pringSet(setE);
//
//                if(setE.size() == 0){
//                   return;
//                }   
//                
//                // 先把一个用户U
//                Integer u = getRandomElement(setE);
//                // System.out.println("=========U="+u+"=======");
//                // setA = setA-setE + U
//                setA.removeAll(setE);
//                setA.add(u);
//                // System.out.println("==========setA=======");
//                // pringSet(setA);
//
//                // setD = setD+setE-U
//                setD.addAll(setE);
//                setD.remove(u);
//                // System.out.println("==========setD=======");
//                // pringSet(setD);
//            });
//            System.out.println("========SET RESULT=========");
//            pringSet(setA);
//
//        }
    }

    private static <E> void pringSet(Set<E> set) {
        System.out.println("=================");
        for (E e : set) {
            System.out.print(e + " ");
        }
        System.out.println();
    }

    /**
     * 从set中随机取得一个元素
     * 
     * @param set
     * @return
     */
    public static <E> E getRandomElement(Set<E> set) {
        int index = (int)(Math.random() * set.size());
//        System.out.println("index:"+index + ", size =" + set.size());
        int i = 0;
        for (E e : set) {
            if (i == index) {
                return e;
            }
            i++;
        }
        return null;
    }

}

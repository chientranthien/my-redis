package com.chientt.test;

import com.chientt.myredis.Dict;

public class Test {
    public static void main(String[] args) {
Test test = new Test();
        System.out.println(test.solution(2,3, new int[]{0,0,1,1,2}));
    }
    public String solution(int U, int L, int[] C) {
        // write your code in Java SE 8
        if(C.length == 0)
            return "IMPOSSIBLE";
        String rs =helper(U,L,C,"","",0);
        return rs != null? rs : "IMPOSSIBLE";
    }

    private String helper(int U, int L, int[]C, String prefix1, String prefix2, int index) {
        if(index >= C.length)
            return prefix1 + "," + prefix2;

        if(U + L < C[index])
            return null;

        if(C[index] == 2) {
            String rs = helper(U - 1, L - 1, C, prefix1 + "1",  prefix2 + "1", index + 1);
            if(rs != null)
                return rs;

        } else if (C[index] ==1 ) {
            if(U > 0){
                String rs = helper(U - 1, L , C, prefix1 + "1",  prefix2 + "0", index + 1);
                if(rs != null)
                    return rs;
            }
            if(L > 0) {
                String rs = helper(U , L - 1, C, prefix1 + "0",  prefix2 + "1", index + 1);
                if(rs != null)
                    return rs;


            }
        } else {
            String rs = helper(U, L, C, prefix1 + "0",  prefix2 + "0", index + 1);
            if(rs != null)
                return rs;

        }
        return null;
    }

}

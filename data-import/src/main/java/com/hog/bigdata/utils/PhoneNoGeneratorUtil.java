package com.hog.bigdata.utils;

import java.util.Random;

public final class PhoneNoGeneratorUtil {
    private static String firstNum = "1";
    private static String[] secondNumArray = {"3", "4", "5", "7", "8","9"};

    /**
     * 调用一次生成一个手机号，手机号后9位数字通过循环生成
     */
    public static String getPhoneNo() {

        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        String secondNum = secondNumArray[random.nextInt(secondNumArray.length)];
        sb.append(firstNum);
        sb.append(secondNum);
        for (int i = 0; i < 9; i++) {
            Integer thirdNum = random.nextInt(10);
            sb.append(thirdNum.toString());
        }
        return sb.toString();
    }

    /**
     * 当需要生成较大量的手机号时调用该方法
     * 后9位数字随机生成，长度不足时则补0
     */
    public static String getMultiPhoneNo() {

        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        String secondNum = secondNumArray[random.nextInt(secondNumArray.length)];
        sb.append(firstNum);
        sb.append(secondNum);
        Integer thirdNum = 1 + random.nextInt(999999999);
        if (thirdNum.toString().length() <= 9) {
            sb.append(thirdNum);
            for (int i = 1; i <= 9 - thirdNum.toString().length(); i++) {
                sb.append(0);
            }
        } else {
            sb.append(thirdNum.toString());
        }
        return sb.toString();

    }

    public static void main(String[] args) {
        System.out.println(getPhoneNo());
        System.out.println(getMultiPhoneNo());
    }

}

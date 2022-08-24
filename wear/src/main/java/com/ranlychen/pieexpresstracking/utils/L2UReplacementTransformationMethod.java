package com.ranlychen.pieexpresstracking.utils;


import android.text.method.ReplacementTransformationMethod;

/**
 * @description Lower case to upper case
 * @author chencanyi
 * @time 2022/8/24 13:33
 */
public class L2UReplacementTransformationMethod extends ReplacementTransformationMethod {
    @Override
    protected char[] getOriginal() {
        char[] originalCharArr = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
        return originalCharArr;
    }

    @Override
    protected char[] getReplacement() {
        char[] replacementCharArr = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
        return replacementCharArr;
    }
}

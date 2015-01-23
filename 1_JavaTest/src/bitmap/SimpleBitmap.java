/*
 * Copyright (c) 2012, Incito Corporation, All Rights Reserved
 */
package bitmap;

/**
 * @description 
 * @author   tianran
 * @createDate Jan 22, 2015
 * @version  1.0
 */
public class SimpleBitmap {
    private final int[] bitmap;
    private final int size;
    /**
     * Build bitmap by size
     */
    public SimpleBitmap(final int size) {
        this.size = size;
        int sLen = ((size%32) == 0) ? size/32 : size/32 + 1;
        this.bitmap = new int[sLen];
    }
    /**
     * get index of number
     * @description 
     * @author   tianran
     * @createDate Jan 22, 2015
     * @param number
     * @return
     */
    private static int _Index(final int number ){
        return number / 32;
    }
    /**
     * get position of number
     * @description 
     * @author   tianran
     * @createDate Jan 22, 2015
     * @param number
     * @return
     */
    private static int _Position(final int number){
        return number % 32;
    }

    private void adjustBitMap(final int index, final int position) {
        int bit = bitmap[index] | (1 << position);
        bitmap[index] = bit;
    }
}

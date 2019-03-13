package com.gemfire.functions;

public class MultArgs extends BaseArgs {
    private int i1;
    private int i2;

    public MultArgs(){

    }
    public MultArgs(int i1, int i2) {

        this.i1 = i1;
        this.i2 = i2;
    }

    public int getI1() {
        return i1;
    }

    public int getI2() {
        return i2;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }

    public void setI2(int i2) {
        this.i2 = i2;
    }
}

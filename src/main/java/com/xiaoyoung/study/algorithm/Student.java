package com.xiaoyoung.study.algorithm;

public class Student {

    private Integer sum;
    private Integer subject;
    private Integer math;

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public Integer getMath() {
        return math;
    }

    public void setMath(Integer math) {
        this.math = math;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sum=" + sum +
                ", subject=" + subject +
                ", math=" + math +
                '}';
    }
}

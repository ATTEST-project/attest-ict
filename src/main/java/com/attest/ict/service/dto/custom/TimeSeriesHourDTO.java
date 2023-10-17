package com.attest.ict.service.dto.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class TimeSeriesHourDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonProperty("Year")
    private Integer year;

    @JsonProperty("Day")
    private String day;

    private String t0;
    private String t1;
    private String t2;

    private String t3;
    private String t4;
    private String t5;
    private String t6;
    private String t7;
    private String t8;
    private String t9;
    private String t10;
    private String t11;
    private String t12;
    private String t13;
    private String t14;
    private String t15;
    private String t16;
    private String t17;
    private String t18;

    private String t19;
    private String t20;
    private String t21;

    private String t22;

    private String t23;

    // T1..T24 instant values could be = 'N/A' or 'value %'
    public TimeSeriesHourDTO(
        Integer year,
        String day,
        String t0,
        String t1,
        String t2,
        String t3,
        String t4,
        String t5,
        String t6,
        String t7,
        String t8,
        String t9,
        String t10,
        String t11,
        String t12,
        String t13,
        String t14,
        String t15,
        String t16,
        String t17,
        String t18,
        String t19,
        String t20,
        String t21,
        String t22,
        String t23
    ) {
        this.year = year;
        this.day = day;
        this.t0 = t0;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
        this.t7 = t7;
        this.t8 = t8;
        this.t9 = t9;
        this.t10 = t10;
        this.t11 = t11;
        this.t12 = t12;
        this.t13 = t13;
        this.t14 = t14;
        this.t15 = t15;
        this.t16 = t16;
        this.t17 = t17;
        this.t18 = t18;
        this.t19 = t19;
        this.t20 = t20;
        this.t21 = t21;
        this.t22 = t22;
        this.t23 = t23;
    }

    public TimeSeriesHourDTO(
        String t0,
        String t1,
        String t2,
        String t3,
        String t4,
        String t5,
        String t6,
        String t7,
        String t8,
        String t9,
        String t10,
        String t11,
        String t12,
        String t13,
        String t14,
        String t15,
        String t16,
        String t17,
        String t18,
        String t19,
        String t20,
        String t21,
        String t22,
        String t23
    ) {
        this.t0 = t0;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
        this.t7 = t7;
        this.t8 = t8;
        this.t9 = t9;
        this.t10 = t10;
        this.t11 = t11;
        this.t12 = t12;
        this.t13 = t13;
        this.t14 = t14;
        this.t15 = t15;
        this.t16 = t16;
        this.t17 = t17;
        this.t18 = t18;
        this.t19 = t19;
        this.t20 = t20;
        this.t21 = t21;
        this.t22 = t22;
        this.t23 = t23;
    }

    public String getT0() {
        return t0;
    }

    public void setT0(String t0) {
        this.t0 = t0;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getT4() {
        return t4;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }

    public String getT5() {
        return t5;
    }

    public void setT5(String t5) {
        this.t5 = t5;
    }

    public String getT6() {
        return t6;
    }

    public void setT6(String t6) {
        this.t6 = t6;
    }

    public String getT7() {
        return t7;
    }

    public void setT7(String t7) {
        this.t7 = t7;
    }

    public String getT8() {
        return t8;
    }

    public void setT8(String t8) {
        this.t8 = t8;
    }

    public String getT9() {
        return t9;
    }

    public void setT9(String t9) {
        this.t9 = t9;
    }

    public String getT10() {
        return t10;
    }

    public void setT10(String t10) {
        this.t10 = t10;
    }

    public String getT11() {
        return t11;
    }

    public void setT11(String t11) {
        this.t11 = t11;
    }

    public String getT12() {
        return t12;
    }

    public void setT12(String t12) {
        this.t12 = t12;
    }

    public String getT13() {
        return t13;
    }

    public void setT13(String t13) {
        this.t13 = t13;
    }

    public String getT14() {
        return t14;
    }

    public void setT14(String t14) {
        this.t14 = t14;
    }

    public String getT15() {
        return t15;
    }

    public void setT15(String t15) {
        this.t15 = t15;
    }

    public String getT16() {
        return t16;
    }

    public void setT16(String t16) {
        this.t16 = t16;
    }

    public String getT17() {
        return t17;
    }

    public void setT17(String t17) {
        this.t17 = t17;
    }

    public String getT18() {
        return t18;
    }

    public void setT18(String t18) {
        this.t18 = t18;
    }

    public String getT19() {
        return t19;
    }

    public void setT19(String t19) {
        this.t19 = t19;
    }

    public String getT20() {
        return t20;
    }

    public void setT20(String t20) {
        this.t20 = t20;
    }

    public String getT21() {
        return t21;
    }

    public void setT21(String t21) {
        this.t21 = t21;
    }

    public String getT22() {
        return t22;
    }

    public void setT22(String t22) {
        this.t22 = t22;
    }

    public String getT23() {
        return t23;
    }

    public void setT23(String t23) {
        this.t23 = t23;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

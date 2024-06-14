package cn.xymind.healthdetection.synthesis.androidsdksamples;

import java.io.Serializable;

public class MeasureResult implements Serializable {
    private static final long serialVersionUID = 4446368434718866609L;

    private String measurementId;
    private float hR_BPM;
    private float hrv;
    private int isAf;
    private double spo2h;
    private float bP_SYSTOLIC;
    private float bP_DIASTOLIC;
    private int age;
    private float Bmi;
    private float bP_HEART_ATTACK;
    private float bP_STROKE;
    private float bP_CVD;
    private float bP_PP;
    private float bP_TAU;
    private double healthScore;
    private String afMsg;
    private String spo2hMsg;
    private String ageBmiMsg;
    private String riskMsg;
    private double Msi;

    @Override
    public String toString() {
        return "MeasureResult{" +
                "measurementId='" + measurementId + '\'' +
                ", hR_BPM=" + hR_BPM +
                ", hrv=" + hrv +
                ", isAf=" + isAf +
                ", spo2h=" + spo2h +
                ", bP_SYSTOLIC=" + bP_SYSTOLIC +
                ", bP_DIASTOLIC=" + bP_DIASTOLIC +
                ", age=" + age +
                ", bP_HEART_ATTACK=" + bP_HEART_ATTACK +
                ", bP_STROKE=" + bP_STROKE +
                ", bP_CVD=" + bP_CVD +
                ", bP_PP=" + bP_PP +
                ", bP_TAU=" + bP_TAU +
                ", Bmi=" + Bmi +
                '}';
    }

    public double getMsi() {
        return Msi;
    }

    public void setMsi(double msi) {
        Msi = msi;
    }

    public double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAfMsg() {
        return afMsg;
    }

    public void setAfMsg(String afMsg) {
        this.afMsg = afMsg;
    }

    public String getSpo2hMsg() {
        return spo2hMsg;
    }

    public void setSpo2hMsg(String spo2hMsg) {
        this.spo2hMsg = spo2hMsg;
    }

    public String getAgeBmiMsg() {
        return ageBmiMsg;
    }

    public void setAgeBmiMsg(String ageBmiMsg) {
        this.ageBmiMsg = ageBmiMsg;
    }

    public String getRiskMsg() {
        return riskMsg;
    }

    public void setRiskMsg(String riskMsg) {
        this.riskMsg = riskMsg;
    }

    public float getBmi() {
        return Bmi;
    }

    public void setBmi(float bmi) {
        Bmi = bmi;
    }

    public String getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(String measurementId) {
        this.measurementId = measurementId;
    }

    public float gethR_BPM() {
        return hR_BPM;
    }

    public void sethR_BPM(float hR_BPM) {
        this.hR_BPM = hR_BPM;
    }

    public float getHrv() {
        return hrv;
    }

    public void setHrv(float hrv) {
        this.hrv = hrv;
    }

    public int getIsAf() {
        return isAf;
    }

    public void setIsAf(int isAf) {
        this.isAf = isAf;
    }

    public double getSpo2h() {
        return spo2h;
    }

    public void setSpo2h(double spo2h) {
        this.spo2h = spo2h;
    }

    public float getbP_SYSTOLIC() {
        return bP_SYSTOLIC;
    }

    public void setbP_SYSTOLIC(float bP_SYSTOLIC) {
        this.bP_SYSTOLIC = bP_SYSTOLIC;
    }

    public float getbP_DIASTOLIC() {
        return bP_DIASTOLIC;
    }

    public void setbP_DIASTOLIC(float bP_DIASTOLIC) {
        this.bP_DIASTOLIC = bP_DIASTOLIC;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getbP_HEART_ATTACK() {
        return bP_HEART_ATTACK;
    }

    public void setbP_HEART_ATTACK(float bP_HEART_ATTACK) {
        this.bP_HEART_ATTACK = bP_HEART_ATTACK;
    }

    public float getbP_STROKE() {
        return bP_STROKE;
    }

    public void setbP_STROKE(float bP_STROKE) {
        this.bP_STROKE = bP_STROKE;
    }

    public float getbP_CVD() {
        return bP_CVD;
    }

    public void setbP_CVD(float bP_CVD) {
        this.bP_CVD = bP_CVD;
    }

    public float getbP_PP() {
        return bP_PP;
    }

    public void setbP_PP(float bP_PP) {
        this.bP_PP = bP_PP;
    }

    public float getbP_TAU() {
        return bP_TAU;
    }

    public void setbP_TAU(float bP_TAU) {
        this.bP_TAU = bP_TAU;
    }
}

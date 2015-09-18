package com.fourlines.model;

import java.io.Serializable;

public class DoctorItem implements Serializable {
    private String doctorName;
    private int doctorAvt;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getDoctorAvt() {
        return doctorAvt;
    }

    public void setDoctorAvt(int doctorAvt) {
        this.doctorAvt = doctorAvt;
    }

    public DoctorItem(String doctorName, int doctorAvt) {
        this.doctorName = doctorName;
        this.doctorAvt = doctorAvt;
    }
}

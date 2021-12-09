package com.example.demo.entity;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

public class MachineInfo {

    public MachineInfo() {
        System.out.println("构造MachineInfo<<<<<<");
    }

    private int  id;
    private String dep_name;
    private String m_num;
    private String date_of_repair;
    private String breakDown;
    private String reason;
    private String solution;
    private String m_desp;
    private String remark;

    @Override
    public String toString() {
        return "MachineInfo{" +
                "id=" + id +
                ", dep_name='" + dep_name + '\'' +
                ", m_num='" + m_num + '\'' +
                ", date_of_repair=" + date_of_repair +
                ", breakDown='" + breakDown + '\'' +
                ", reason='" + reason + '\'' +
                ", solution='" + solution + '\'' +
                ", m_desp='" + m_desp + '\'' +
                ", remark='" + remark + '\'' +
                ", name='" + name + '\'' +
                ", order_date='" + order_date + '\'' +
                ", end_date=" + end_date +
                '}';
    }

    public String[] toArrayForString(int size){

        String[] arrStr=new String[size];
        arrStr[--size]= String.valueOf(this.getId());
        arrStr[--size]= this.getRemark();

        arrStr[--size]= this.getEnd_date()==""?null:this.getEnd_date();

        arrStr[--size] = this.getOrder_date()==""?null: this.getOrder_date();

        arrStr[--size]= this.getName();
        arrStr[--size]= this.getM_desp();
        arrStr[--size]= this.getSolution();
        arrStr[--size]=this.getReason();
        arrStr[--size]= this.getBreakDown();
        arrStr[--size]= this.getDate_of_repair()==""?null: this.getDate_of_repair();
        arrStr[--size]= this.getM_num();
        arrStr[--size]=this.getDep_name();
        return arrStr;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String name;
    private String order_date;
    private String end_date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getM_num() {
        return m_num;
    }

    public void setM_num(String m_num) {
        this.m_num = m_num;
    }

    public String getDate_of_repair() {
        return date_of_repair;
    }

    public void setDate_of_repair(String date_of_repair) {
        this.date_of_repair = date_of_repair;
    }

    public String getBreakDown() {
        return breakDown;
    }

    public void setBreakDown(String breakDown) {
        this.breakDown = breakDown;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getM_desp() {
        return m_desp;
    }

    public void setM_desp(String m_desp) {
        this.m_desp = m_desp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}

package com.example.demo.dao;

import com.example.demo.entity.MachineInfo;

import java.util.List;

public interface IMachineDAO {

    public MachineInfo findInfoById(int id);


    public List<List<MachineInfo>> finaAll();

    public List<MachineInfo> finaAll1();

}

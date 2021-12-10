package com.example.demo.dao;

import com.example.demo.dbUtil.DBUtil;
import com.example.demo.entity.MachineInfo;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MachineImpl extends DBUtil implements IMachineDAO {

    public void insertToGetKey( MachineInfo info){

        String sql="insert into m_info(dep_name,m_num, date_of_repair,breakDown,reason,solution,m_desp,name,order_date,end_date,remark,r_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        KeyHolder kh=new GeneratedKeyHolder();
        this.getJdbcTemplate().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String[] array=info.toArrayForString(12);
                PreparedStatement preparedStatement=con.prepareStatement(sql,array);
                for(int i=1;i<=12;i++) {
                    preparedStatement.setString(i, array[i-1]);
                }
                return   preparedStatement;
            }
        },kh);
        System.out.println(kh.getKey());
    }


    public int insert(MachineInfo info){//dep_name,m_num, date_of_repair,breakDown,reason,solution,m_desp,name,order_date,end_date,remark, ?,?,?,?,?,?,?,?,?,?,?,
        String sql="insert into m_info(dep_name,m_num, date_of_repair,breakDown,reason,solution,m_desp,name,order_date,end_date,remark,r_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";

//        /**
//         * 有异常 Caused by: java.sql.SQLException: Parameter index out of range (0 < 1 ).下标为1开始
//         */
//        return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement preparedStatement) throws SQLException {
//                String[] array=info.toArrayForString(info,12);
//                for(int i=0;i<12;i++) {
//                    preparedStatement.setString(i, array[i]);
//                }
//            }
//        });
        String[] array=info.toArrayForString(12);
        return this.getJdbcTemplate().update(sql,array);


    }

    @Override
    public MachineInfo findInfoById(int id) {
            String sql="SELECT *FROM M_INFO WHERE id=?";
            RowMapper<MachineInfo> rowMapper=new RowMapper<MachineInfo>() {
                @Override
                public MachineInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                    System.out.println("findById:"+id+"=====================================================");
                    MachineInfo info = setMachineInfo(resultSet);

                    return info;
                }
            };

        return this.getJdbcTemplate().queryForObject(sql,rowMapper,id);
    }

    public  MachineInfo findByName(int id){

        return null;
    }


    @Override
    public List<List<MachineInfo>> finaAll() {
        return this.getJdbcTemplate().query("select * from m_info", new RowMapper<List<MachineInfo>>() {
            @Override
            public List<MachineInfo> mapRow(ResultSet resultSet, int i) throws SQLException {
                List<MachineInfo> lists = new ArrayList<>();
                do {
                    MachineInfo info = setMachineInfo(resultSet);
                    lists.add(info);
                } while (resultSet.next());
                return lists;
            }

        });
    }


    @Override
    public List<MachineInfo> finaAll1() {
        return this.getJdbcTemplate().query("select * from m_info", new RowMapper<MachineInfo>() {
            @Override
            public MachineInfo mapRow(ResultSet resultSet, int i) throws SQLException {


                MachineInfo info = setMachineInfo(resultSet);

                return info;
            }

        });
    }



    private MachineInfo setMachineInfo(ResultSet resultSet) throws SQLException {
        System.out.println("setFindById:"+resultSet+"=====================================================");
        MachineInfo info=new MachineInfo();
        info.setId(resultSet.getInt("r_id"));
        info.setName(resultSet.getString("name"));
        info.setM_num(resultSet.getString("m_num"));
        info.setBreakDown(resultSet.getString("breakDown"));
        info.setReason(resultSet.getString("reason"));
        info.setSolution(resultSet.getString("solution"));
        info.setM_desp(resultSet.getString("m_desp"));
        info.setDep_name(resultSet.getString("dep_name"));
        info.setDate_of_repair(resultSet.getString("date_of_repair"));
        info.setOrder_date(resultSet.getString("order_date"));
        info.setEnd_date(resultSet.getString("end_date"));
        info.setRemark(resultSet.getString("remark"));
        return info;
    }
}

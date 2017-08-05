package com.mktj.cn.web.service;

import com.opencsv.CSVReader;

import java.io.IOException;

/**
 * @author zhanwang
 * @create 2017-07-27 13:39
 **/
public interface CsvProcess {

    void process(String Key, CSVReader csvReader) throws IOException;

}

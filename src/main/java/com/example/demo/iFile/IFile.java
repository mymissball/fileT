package com.example.demo.iFile;

import java.io.File;

public interface IFile {
    public void saveFile(File file ,String path);
    public void  readFile(File file);
    public void traver(File file);
}

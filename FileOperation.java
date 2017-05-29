/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenerateValueObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zengmh
 */
public class FileOperation
{
    private String currentpath;
    private String generatepath;
    private BufferedWriter bufferedwriter;
    private BufferedReader bufferedReader;
    private void setCurrentPath()
    {
        currentpath=Class.class.getClass().getResource("/").getPath();
    }
    private void setGeneratePath()
    {
        generatepath=currentpath.replaceFirst("build/classes/","")+"src/"+"ValueObject/";
    }
    public String getGeneratePath()
    {
        if(null==generatepath||"".equalsIgnoreCase(generatepath))
        {
            setGeneratePath();
        }
        return generatepath;
    }
    public String getCurrentPath()
    {
        if(null==currentpath||"".equalsIgnoreCase(currentpath))
        {
            setCurrentPath();
            currentpath=currentpath.substring(1);
        }
        return currentpath;
    }
    public void BufferedWriter(String path)
    {
        File file=new File(path);
        if(!file.exists())
        {
            try
            {
                File f_d=file.getParentFile();
                if(!f_d.exists())
                {
                    f_d.mkdirs();
                }
                file.createNewFile();
            } catch (IOException ex)
            {
                Logger.getLogger(FileOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            bufferedwriter=new BufferedWriter(new FileWriter(file));
        } catch (IOException ex)
        {
            Logger.getLogger(FileOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(null==bufferedwriter)
        {
            System.out.println("bufferedwriter is "+bufferedwriter);
        }
    }
    public BufferedWriter getBufferedwriter()
    {
        return bufferedwriter;
    }
    public BufferedReader getBufferedReader()
    {
        return bufferedReader;
    }
}

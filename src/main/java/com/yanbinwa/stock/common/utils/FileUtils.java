package com.yanbinwa.stock.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.emotibot.middleware.utils.StringUtils;

public class FileUtils
{
    /**
     * 如果文件路径不存在，自动创建路径，文件是否存在
     * 
     * @param filePath
     * @param context
     * @return
     */
    public static boolean writeFile(String filePath, String context)
    {
        if (StringUtils.isEmpty(context))
        {
            return false;
        }
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try
        {
            File file = new File(filePath);            
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(context);
            pw.flush();
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (pw != null) 
            {
                pw.close();
            }
            if (fos != null) 
            {
                try
                {
                    fos.close();
                } 
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static String readFile(String filePath)
    {
        if (StringUtils.isEmpty(filePath))
        {
            return null;
        }
        FileInputStream fi = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try
        {
            fi = new FileInputStream(filePath);
            isr = new InputStreamReader(fi);
            br = new BufferedReader(isr);
            String output = "";
            String line = null;
            while((line = br.readLine()) != null)
            {
                output += line;
            }
            return output;
        } 
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        } 
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
                if (br != null)
                {
                    br.close();
                }
                if (isr != null)
                {
                    isr.close();
                }
                if (fi != null)
                {
                    fi.close();
                }
            }
            catch(Exception e)
            {
                //Do not need
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenerateValueObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zengmh
 */
public class GenerateValueObject
{
    public static String TypeConvert(String type)
    {
        String str=type.replaceAll("\\(.*\\)", "");
        System.out.println(str);
        switch(str.toLowerCase())
        {
            case "varchar":;
            case "char":;
            case "text":;
            case "enum":return "String";
            case "tinyint":;
            case "int":return "int";
            case "float":;
            case"double":return "double";
            case"bit":return "boolean";
            default:return null;
        }
    }
    public static String toUpperCase4Index(String string)
    {
        char[] methodName = string.toCharArray();
        methodName[0] = toUpperCase(methodName[0]);
        return String.valueOf(methodName);
    }
    /**
     * 字符转成大写
     *
     * @param chars
     * @return
     */
    public static char toUpperCase(char chars)
    {
        if (97 <= chars && chars <= 122)
        {
            chars ^= 32;
        }
        return chars;
    }
    private final DBConnect db;
    private final FileOperation fileope;

    private List<String> table_list;
    private List<String> table_type_list;
    private List<String> column_list;
    private List<String> comment_list;
    private List<String> column_type_list;
    private List<String> is_nullable_list;

    private BufferedWriter bw;
    private String path;
    private String databasename="diseaseknowledgedatabase";

    public GenerateValueObject()
    {
        db = new DBConnect();
        fileope = new FileOperation();
    }

    public void getListValues(String databasename)
    {
        db.setDatabasename(databasename);
        db.getDataBaseInfo();
        table_list = db.getTable_list();
        table_type_list = db.getTable_type_list();
        column_list = db.getColumn_list();
        column_type_list = db.getColumn_type_list();
        comment_list = db.getComment_list();
        is_nullable_list = db.getTable_type_list();
    }

    public void getListValues()
    {
        getListValues("diseaseknowledgedatabase");
    }

    public void setPath(String package_, String classname)
    {
        fileope.getCurrentPath();
        path = fileope.getGeneratePath() + package_ + "/" + classname + ".java";
    }

    public void setBW(String path)
    {
        fileope.BufferedWriter(path);
        bw = fileope.getBufferedwriter();
    }

    public void WriteVariable(BufferedWriter bw, String name, String type) throws IOException
    {
        String str = "\tprivate " + type + " " + name + ";";
        bw.write(str);
        bw.newLine();
    }

    public void WriteGetter(BufferedWriter bw, String name, String type) throws IOException
    {
        String str = "\tpublic " + type + " " + "get" + toUpperCase4Index(name) + "()";
        bw.write(str);
        bw.newLine();
        bw.write("\t{");
        bw.newLine();
        str = "\t\treturn this." + name + ";";
        bw.write(str);
        bw.newLine();
        bw.write("\t}");
        bw.newLine();
    }

    public void WriteSetter(BufferedWriter bw, String name, String type) throws IOException
    {
        String str = "\tpublic " + "void" + " " + "set" + toUpperCase4Index(name) + "(" + type + " " + name + ")";
        bw.write(str);
        bw.newLine();
        bw.write("\t{");
        bw.newLine();
        str = "\t\tthis." + name + "=" + name + ";";
        bw.write(str);
        bw.newLine();
        bw.write("\t}");
        bw.newLine();
    }

    public void WriteToFile()
    {
        int index=0;
        for (String tablename : table_list)
        {
            int j=index;
            setPath(databasename, tablename);
            setBW(path);
            try
            {
                bw.write("package "+"ValueObject."+databasename+";");
                bw.newLine();
                bw.write("class " + tablename);
                bw.newLine();
                bw.write("{");
                bw.newLine();
                for(int i=j;i<column_list.size();i++)
                {
                    if("#".equals(column_list.get(i)))
                    {
                        index=i+1;
                        break;
                    }
                    WriteVariable(bw,column_list.get(i),TypeConvert(column_type_list.get(i)));
                }
                for(int i=j;i<column_list.size();i++)
                {
                    if("#".equals(column_list.get(i)))
                    {
                        index=i+1;
                        break;
                    }
                    WriteGetter(bw,column_list.get(i),TypeConvert(column_type_list.get(i)));
                    WriteSetter(bw,column_list.get(i),TypeConvert(column_type_list.get(i)));
                }
                bw.write("}");
                bw.flush();
                bw.close();
            } catch (IOException ex)
            {
                Logger.getLogger(GenerateValueObject.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    public static void main(String [] args)
    {
       GenerateValueObject g= new GenerateValueObject();
       g.getListValues("diseaseknowledgedatabase");
       g.WriteToFile();
    }
}

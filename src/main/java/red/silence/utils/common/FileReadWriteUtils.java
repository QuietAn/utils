package red.silence.utils.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * @ClassName FileReadWriteUtils
 * @author quiet
 * @date 2017年9月15日
 */
public class FileReadWriteUtils{
    
    private File file;
    
    private FileInputStream is;
    
    private FileOutputStream os;
    
    private BufferedInputStream bis;
    
    private BufferedOutputStream bos;
    
    private BufferedReader br;
    
    private BufferedWriter bw;
    
    private FileWriter fw;
    
    private FileReader fr;
    
    
    public FileReadWriteUtils(File file) {
        this.file = file;
    }
    
    public FileReadWriteUtils(String fileName) {
        this(new File(fileName));
    }
    
    public FileInputStream getInputStream() throws FileNotFoundException {
        if(null == is) {
            is = new FileInputStream(file);
        }
        return is;
    }

    public FileOutputStream getOutputStream() throws FileNotFoundException {
        if(null == os) {
            os = new FileOutputStream(file);
        }
        return os;
    }

    private void closeInputStream() throws IOException {
        if(null != is) {
            is.close();
        }
    }

    private void closeOutputStream() throws IOException {
        if(null != os) {
            os.close();
        }
    }
    
    private void closeBufferedInputStream() throws IOException {
        if(null != bis) {
            bis.close();
        }
    }
    
    private void closeBufferedOutStream() throws IOException {
        if(null != bos) {
            bos.close();
        }
    }
    
    private void closeBufferWrite() throws IOException {
        if(null != bw) {
            bw.close();
        }
    }
    
    private void closeBufferRead() throws IOException {
        if(null != br) {
            br.close();
        }
    }
    
    private void closeFileWrite() throws IOException {
        if(null != fw) {
            fw.close();
        }
    }
    
    private void closeFileRead() throws IOException {
        if(null != fr) {
            fr.close();
        }
    }
    
    public void close() throws IOException {
        closeBufferRead();
        closeBufferWrite();
        closeFileRead();
        closeFileWrite();
        closeBufferedInputStream();
        closeBufferedOutStream();
        closeInputStream();
        closeOutputStream();
        
        
    }

    public void setFile(File file) {
       closeOldStream();
       this.file = file; 
    }

    public void setFile(String fileName) {
        setFile(new File(fileName));
    }

    public BufferedInputStream getBufferedInputStream() throws FileNotFoundException {
        if(null == bis) {
           bis = new BufferedInputStream(this.getInputStream());
        }
        return bis;
    }

    public BufferedOutputStream getBufferedOutputStream() throws FileNotFoundException {
        if(null == bos) {
            bos = new BufferedOutputStream(this.getOutputStream());
        }
        return bos;
    }
    
    public FileReader getFileRead() throws FileNotFoundException {
        if(null == fr) {
            fr = new FileReader(file);
        }
        
        return fr;
    }
    
    public FileWriter getFileWriter() throws IOException {
        if(null == fw) {
            fw = new FileWriter(file);
        }
        
        return fw;
    }
    
    public BufferedWriter getBufferWriter() throws IOException {
        if(null == bw) {
            bw = new BufferedWriter(this.getFileWriter());
        }
        return bw;
    }
    public BufferedWriter getBufferWriter(String charset) throws FileNotFoundException {
        if(null == bw) {
            bw = new BufferedWriter(new OutputStreamWriter(this.getOutputStream(), Charset.forName(charset)));
        }
        
        return bw;
    }
    
    public BufferedReader getBufferedReader() throws FileNotFoundException {
        if(null == br) {
            br = new BufferedReader(this.getFileRead());
        }
        
        return br;
    }
    
    public BufferedReader getBufferedReader(String charset) throws FileNotFoundException {
        if(null == br) {
            br = new BufferedReader(new InputStreamReader(this.getInputStream(), Charset.forName(charset)));
        }
        
        return br;
    }
    
    public void createFile() throws IOException {
        if(!this.file.exists()) {
        	file.createNewFile();
        }
    }
    
    public void destroy() {
        closeOldStream();
    }
    
    private void closeOldStream() {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

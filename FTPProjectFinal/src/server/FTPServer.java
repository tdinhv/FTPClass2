package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import lib.FileDetails;
import lib.LibraryConnectDb;

public class FTPServer extends Thread
{
	private LibraryConnectDb connectDb;
	private java.sql.Connection conn;
	private Statement st;
	private PreparedStatement pst;
	private ResultSet rs;
	Socket ClientSoc;
    DataInputStream din;
    DataOutputStream dout;  
    private boolean logined;
    private  byte data[];
    public FTPServer(Socket soc)
    {
        try
        {
            ClientSoc=soc;                        
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            System.out.println("FTP Client Connected ...");
            connectDb = new LibraryConnectDb();
            start();
            
        }
        catch(Exception ex)
        {
        }        
    }
    void SendFile() throws Exception
    {        
        String filename=din.readUTF();
        File f=new File(filename);
        if(!f.exists())
        {
            dout.writeUTF("File Not Found");
            return;
        }
        else
        {
            dout.writeUTF("READY");
            FileInputStream fin=new FileInputStream(f);
            int ch;
            do
            {
                ch=fin.read();
                dout.writeUTF(String.valueOf(ch));
            }
            while(ch!=-1);    
            fin.close();    
            dout.writeUTF("File Receive Successfully");                            
        }
    }
    
    void ReceiveFile() throws Exception
    {
        String filename=din.readUTF();
        if(filename.compareTo("File not found")==0)
        {
            return;
        }
        File f=new File(filename);
        String option;
        
        if(f.exists())
        {
            dout.writeUTF("File Already Exists");
            option=din.readUTF();
        }
        else
        {
            dout.writeUTF("SendFile");
            option="Y";
        }
            
            if(option.compareTo("Y")==0)
            {
                FileOutputStream fout=new FileOutputStream(f);
                int ch;
                String temp;
                do
                {
                    temp=din.readUTF();
                    ch=Integer.parseInt(temp);
                    if(ch!=-1)
                    {
                        fout.write(ch);                    
                    }
                }while(ch!=-1);
                fout.close();
                dout.writeUTF("File Send Successfully");
            }
            else
            {
                return;
            }
            
    }

    void BrowseDir() throws Exception
    {
        String filename=din.readUTF();
        if(filename.equalsIgnoreCase("back")){
        	 dout.writeUTF("Initial Point");
        	 return;
        }
        File folder = new File(filename);
        ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> directories = new ArrayList<String>();
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        files.add(listOfFiles[i].getName());
	      } else if (listOfFiles[i].isDirectory()) {
	    	  directories.add(listOfFiles[i].getName());
	      }
	    }
	    
	    String allmsg="Directories:\n";
	    for(int i=0;i<directories.size();i++){
	    	allmsg=allmsg+""+(i+1)+":"+directories.get(i)+"\n";
	    }
	    allmsg=allmsg+"\n\nFiles\n";
	    for(int i=0;i<files.size();i++){
	    	allmsg= allmsg+""+(i+1)+":"+files.get(i)+"\n";
	    }
	    dout.writeUTF(allmsg);
	    return;
    }

    void verify(){
    	while(true){
    		try {
				String usr = din.readUTF();
				String pass =din.readUTF();
				File fl = new File("D:\\HOC TAP\\DoAnLtm\\ftpassignment-20180411T001356Z-001\\ftpassignment\\users.txt");
				FileInputStream fi= new FileInputStream(fl);
				DataInputStream di=new DataInputStream(fi);
				BufferedReader reader =  new BufferedReader(new InputStreamReader(fi));
				String str=null;
				boolean dec =false;
				str=reader.readLine();
				while(true){
					if(str==null) break;
					String pas = reader.readLine();
					if(str.equals(usr) && pas.equals(pass)){
						dec=true;
						break;
					}
					str=reader.readLine();
				}
				if(dec){
					dout.writeUTF("login successful");
					break;
				}
				else{
					dout.writeUTF("login failed");
				}
				System.out.println("reached");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    }
    public void run()
    {
    	String sReq="";
    	try {
    		sReq = din.readUTF();
			System.out.println(sReq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("1"+logined);
    	String[] words =sReq.split("\\&");
    	String loginedCmd= words[0];
    	String resquestCmd = words[1];
    	String fileResquest = words[2];
    	
    	if(!loginedCmd.equals("logined")){
			try {
				logined=checkLogin(sReq);
				System.out.println("Vao");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else{
    		switch (resquestCmd) {
			case "download":
				downloadFile(fileResquest);
				break;
			case "upload":
				String pathDirSave = words[3];
				uploadFile(fileResquest,pathDirSave);
				break;

			default:
				break;
			}
    			
    	}
    	System.out.println("2"+logined);
    	/*verify();
    	
    	while(true)
        {
            try
            {
            String Command=din.readUTF();
            if(Command.compareTo("RECEIVE")==0)
            {
                SendFile();
                continue;
            }
            else if(Command.compareTo("SEND")==0)
            {             
                ReceiveFile();
                continue;
            }
            else if(Command.compareTo("LIST")==0)
            {           
                BrowseDir();
                continue;
            }
            else if(Command.compareTo("DISCONNECT")==0)
            {
                System.exit(1);
            }
            }
            catch(Exception ex)
            {
            }
        }*/
    }
	private void downloadFile(String fileResquest) {
		// TODO Auto-generated method stub
		FileDetails details;
	    
	    try {
	        File file = new File(fileResquest);

	        // Getting file name and size
	        if (file.length() > Integer.MAX_VALUE) {
	            System.out.println("File size exceeds 2 GB");
	        } else {         
	            data = new byte[2048]; // Here you can increase the size also which will send it faster
	            details = new FileDetails();
	            details.setDetails(file.getName(), file.length());

	            // Sending file details to the client
	            System.out.println("Sending file details...");
	            ObjectOutputStream sendDetails = new ObjectOutputStream(ClientSoc.getOutputStream());
	            sendDetails.writeObject(details);
	            sendDetails.flush();
	            // Sending File Data 
	            System.out.println("Sending file data...");
	            FileInputStream fileStream = new FileInputStream(file);
	            BufferedInputStream fileBuffer = new BufferedInputStream(fileStream);
	            OutputStream out = ClientSoc.getOutputStream();
	            int count;
	            while ((count = fileBuffer.read(data)) > 0) {
	                System.out.println("Data Sent : " + count);
	                out.write(data, 0, count);
	                out.flush();
	            }
	            out.close();
	            fileBuffer.close();
	            fileStream.close();
	        }
	    } catch (Exception e) {
	        System.out.println("Error : " + e.toString());
	    }
		
	}
	private boolean checkLogin(String sReq) throws SQLException {
		// TODO Auto-generated method stub
		String[] words =sReq.split("\\&");
		String username = words[1];
		String password = words[2];
		System.out.println(username+" va "+password);
		boolean check = false;
		String sql ="SELECT * FROM users WHERE username = '"+username+"' AND password = '"+password+"'";
		conn = connectDb.getConnectMySql();
		System.out.println(sql);
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			if(rs.next()){
				dout.writeUTF("login_succeed");
				check = true;
			}else {
				dout.writeUTF("login_fail");
				check = false;
			}
			dout.flush();
		} catch (SQLException exception) {
			exception.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				st.close();
				conn.close();
			}catch(SQLException exception2){
				exception2.printStackTrace();
			}
		}
		return check;
	}
	public void uploadFile(String fileResquest, String pathDirSave){
		
		try {
			String pathSelected = pathDirSave;
			//dout.writeUTF("logined&download&"+pathSelected);
			System.out.println("path save "+pathDirSave);
			//dout.flush();

	        System.out.println("Getting details from Server...");
	        ObjectInputStream getDetails = new ObjectInputStream(ClientSoc.getInputStream());
	        FileDetails details = (FileDetails) getDetails.readObject();
	        System.out.println("Now receiving file...");
	        // Storing file name and sizes

	        String fileName = details.getName();
	        System.out.println("File Name : " + fileName);
	        data = new byte[2048]; // Here you can increase the size also which will receive it faster
	        FileOutputStream fileOut = new FileOutputStream(pathSelected+"\\" + fileName);
	        InputStream fileIn = ClientSoc.getInputStream();
	        BufferedOutputStream fileBuffer = new BufferedOutputStream(fileOut);
	        int count;
	        int sum = 0;
	        int i=0;
	        while ((count = fileIn.read(data)) > 0) {
	            sum += count;
	            fileBuffer.write(data, 0, count);
	            System.out.println("Data received : " + sum);
	            fileBuffer.flush();
	            System.out.println(i++);
	        }
	        System.out.println("File Received...");
	        fileBuffer.close();
	        fileIn.close();
	        getDetails.close();
	        ClientSoc.close();
	    } catch (Exception e) {
	        System.out.println("Error : " + e.toString());
	    }
		
	}
}
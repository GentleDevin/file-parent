package com.file.commons.utils.io;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * 文件操作的基本工具类，具体看源码，包含了文件、文件夹的 解压、压缩、复制、移动、读取目录、文件修改、文件读写等各种操作的常用方法
 * */
public class FileUtil extends org.apache.commons.io.FileUtils {

	/**
	 *  解压文件的基本方法
	 *  @param zipFilename 压缩文件的路径及名称
	 *  @param destDir 解压的目标路径
	 *  @return void
	 * */
	public static void unzipFile(String zipFilename, String destDir) throws IOException {
		unzipFile(new File(zipFilename),new File(destDir));
	}

	/**
	 *  解压文件的基本方法,
	 *  @param zipFile 压缩文件
	 *  @param destDir 解压的目标路径
	 *  @return void
	 * */
	public static void unzipFile(File zipFile, String destDir) throws IOException {
		unzipFile(zipFile,new File(destDir));
	}
	/**
	 *  解压文件的基本方法,
	 *  @param zipFile 压缩文件
	 *  @param destDir 解压的目标路径
	 *  @return void
	 * */
	public static void unzipFile(File zipFile, File destDir) throws IOException {
		ZipFile zip = new ZipFile(zipFile);
		Enumeration en = zip.entries();
		int bufSize = 8196;

		while (en.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) en.nextElement();
			File file = (destDir != null) ? new File(destDir, entry.getName()) : new File(entry.getName());
			if (entry.isDirectory()) {
				if (!file.mkdirs()) {
					if (file.isDirectory() == false) {
						throw new IOException("Error creating directory: " + file);
					}
				}
			} else {
				File parent = file.getParentFile();
				if (parent != null && !parent.exists()) {
					if (!parent.mkdirs()) {
						if (file.isDirectory() == false) {
							throw new IOException("Error creating directory: " + parent);
						}
					}
				}

				InputStream in = zip.getInputStream(entry);
				try {
					OutputStream out = new BufferedOutputStream(new FileOutputStream(file), bufSize);
					try {
						copyPipe(in, out, bufSize);
					} finally {
						out.close();
					}
				} finally {
					in.close();
				}
			}
		}
		zip.close();		
	}
	
	/**
	 * 获取文件大小
	 * @param fileName 文件名称
	 * @return long 文件大小
	 * */
	public static long getFileSize(String fileName) {
		return new File(fileName).length();
	}

	/**
	 * 获取文件大小
	 * @param file
	 * @return long 文件大小
	 * */
	public static long getFileSize(File file) {
		return file.length();
	}

	/**
	 * 建立目录，文件夹方法
	 * @param dirs 文件夹
	 * */
	public static boolean mkdirs(String dirs) {
		return new File(dirs).mkdirs();
	}

	/**
	 * 建立目录，文件夹方法
	 * @param dirs 文件夹
	 * */
	public static boolean mkdirs(File dirs) {
		return dirs.mkdirs();
	}

	
	public static boolean mkdir(String dir) {
		return new File(dir).mkdir();
	}

	public static boolean mkdir(File dir) {
		return dir.mkdir();
	}

	/**
	 * 判断两个文件的是否为同一个文件
	 * */
	public static boolean equals(String file1, String file2) {
		return equals(new File(file1), new File(file2));
	}

	/**
	 * 判断两个文件的是否相同
	 * */
	public static boolean equals(File file1, File file2) {
		try {
			file1 = file1.getCanonicalFile();
			file2 = file2.getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return file1.equals(file2);
	}

	
	/**
	 * 从某个文件夹下得到所有的文件
	 * @param filepath 文件夹名称
	 * @return List<File> 
	 * */
	public static List getFileList(String filepath){
		List fileList=new ArrayList();
		File d= new File(filepath);   
         if(d.exists())   
         {   
                 File list[]=d.listFiles();   
                 for(int i = 0; i<list.length;i++)   
                 {   
                         if (list[i].isFile())   
                         {   
                        	 fileList.add(list[i]);   
                         }
                 }       
         }
		
		return fileList;
	}

	// file copy vairants

	public static int FILE_BUFFER_SIZE = 32 * 1024;

	/**
	 * 文件的copy 操作
	 * 
	 * */
	public static boolean copy(String fileIn, String fileOut) {
		return copy(new File(fileIn), new File(fileOut), FILE_BUFFER_SIZE, true);
	}

	/**
	 * 文件的安全copy
	 * */
	public static boolean copySafe(String fileIn, String fileOut) {
		return copy(new File(fileIn), new File(fileOut), FILE_BUFFER_SIZE, false);
	}

	public static boolean copy(String fileIn, String fileOut, int bufsize) {
		return copy(new File(fileIn), new File(fileOut), bufsize, true);
	}

	public static boolean copySafe(String fileIn, String fileOut, int bufsize) {
		return copy(new File(fileIn), new File(fileOut), bufsize, false);
	}

	public static boolean copy(File fileIn, File fileOut) {
		return copy(fileIn, fileOut, FILE_BUFFER_SIZE, true);
	}

	public static boolean copySafe(File fileIn, File fileOut) {
		return copy(fileIn, fileOut, FILE_BUFFER_SIZE, false);
	}

	public static boolean copy(File fileIn, File fileOut, int bufsize) {
		return copy(fileIn, fileOut, bufsize, true);
	}

	public static boolean copySafe(File fileIn, File fileOut, int bufsize) {
		return copy(fileIn, fileOut, bufsize, false);
	}

	public static boolean copy(String fileIn, String fileOut, int bufsize, boolean overwrite) {
		return copy(new File(fileIn), new File(fileOut), bufsize, overwrite);
	}

	public static boolean copy(File fileIn, File fileOut, int bufsize, boolean overwrite)  {
		// check if source exists
		if (fileIn.exists() == false) {
			return false;
		}

		// check if source is a file
		if (fileIn.isFile() == false) {
			return false;
		}

		// if destination is folder, make it to be a file.
		if (fileOut.isDirectory() == true) {
			fileOut = new File(fileOut.getPath() + File.separator + fileIn.getName());
		}

		if (overwrite == false) {
			if (fileOut.exists() == true) {
				return false;
			}
		} else {
			if (fileOut.exists()) {		// if overwriting, check if destination is the same file as source
				try {
					if (fileIn.getCanonicalFile().equals(fileOut.getCanonicalFile()) == true) {
						return true;
					}
				} catch (IOException ioex) {
					return false;
				}
			}
		}
		return copyFile(fileIn, fileOut, bufsize);
	}

	public static boolean copyFile(String fileIn, String fileOut) {
		return copyFile(new File(fileIn), new File(fileOut), FILE_BUFFER_SIZE);
	}

//	public static boolean copyFile(File fileIn, File fileOut) {
//		return copyFile(fileIn, fileOut, FILE_BUFFER_SIZE);
//	}
    
	public static boolean copyFile(String fileIn, String fileOut, int bufsize) {
		return copyFile(new File(fileIn), new File(fileOut), bufsize);
	}

	public static boolean copyFile(File fileIn, File fileOut, int bufsize) {
		FileInputStream in = null;
		FileOutputStream out = null;
		boolean result = false;
		try {
			in = new FileInputStream(fileIn);
			out = new FileOutputStream(fileOut);
			copyPipe(in, out, bufsize);
			result = true;
		} catch(IOException ioex) {
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch(IOException ioex) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch(IOException ioex) {
				}
			}
		}
		return result;
	}

	/**
	 * copy IO 管道，将一个输出作为另外一个的输入
	 * */
	public static void copyPipe(InputStream in, OutputStream out, int bufSizeHint) throws IOException {
		int read = -1;
		byte[] buf = new byte[bufSizeHint];
		while ((read = in.read(buf, 0, bufSizeHint)) >= 0) {
			out.write(buf, 0, read);
		}
		out.flush();
	}
	
	
	// file move variants
	/**
	 * 文件的移动操作
	 * */
	public static boolean move(String fileNameIn, String fileNameOut) {
		return move(new File(fileNameIn), new File(fileNameOut), true);
	}
	public static boolean moveSafe(String fileNameIn, String fileNameOut) {
		return move(new File(fileNameIn), new File(fileNameOut), false);
	}

	public static boolean move(File fileIn, File fileOut) {
		return move(fileIn, fileOut, true);
	}
	
	public static boolean moveSafe(File fileIn, File fileOut) {
		return move(fileIn, fileOut, false);
	}

	public static boolean move(String fileNameIn, String fileNameOut, boolean overwrite) {
		return move(new File(fileNameIn), new File(fileNameOut), overwrite);
	}

	public static boolean move(File fileIn, File fileOut, boolean overwrite) {
		// check if source exists
		if (fileIn.exists() == false) {
			return false;
		}

		// check if source is a file
		if (fileIn.isFile() == false) {
			return false;
		}

		// if destination is folder, make it to be a file.
		if (fileOut.isDirectory() == true) {
			fileOut = new File(fileOut.getPath() + File.separator + fileIn.getName());
		}

		if (overwrite == false) {
			if (fileOut.exists() == true) {
				return false;
			}
		} else {
			if (fileOut.exists()) {			// if overwriting, check if destination is the same file as source
				try {
					if (fileIn.getCanonicalFile().equals(fileOut.getCanonicalFile()) == true) {
						return true;
					} else {
						fileOut.delete();	// delete destination
					}
				} catch (IOException ioex) {
					return false;
				}
			}
		}

		return fileIn.renameTo(fileOut);
	}

	public static boolean moveFile(String src, String dest) {
		return new File(src).renameTo(new File(dest));
	}

	/*public static boolean moveFile(File src, File dest) {
		return src.renameTo(dest);
	}*/

	
	// move/copy directory

	/**
	 * 文件夹的移动操作
	 * */
	public static boolean moveDir(String fileIn, String fileOut) {
		return moveDir(new File(fileIn), new File(fileOut));
	}

	public static boolean moveDir(File fileIn, File fileOut) {
		// check if source exists
		if (fileIn.exists() == false) {
			return false;
		}

		// check if source is a directory
		if (fileIn.isDirectory() == false) {
			return false;
		}

		// check if destination exists
		if (fileOut.exists() == true) {
			try {
				if (fileIn.getCanonicalFile().equals(fileOut.getCanonicalFile()) == true) {
					return true;
				} else {
					return false;
				}
			} catch (IOException ioex) {
				return false;
			}
		}

		return fileIn.renameTo(fileOut);
	}

	/**
	 * 文件夹的移动操作
	 * **/
	public static boolean copyDir(String srcDir, String dstDir) throws Exception {
		return copyDir(new File(srcDir), new File(dstDir));
	}

	public static boolean copyDir(File srcDir, File dstDir) throws Exception {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}
			String[] files = srcDir.list();
			for (int i = 0; i < files.length; i++) {
				if (copyDir(new File(srcDir, files[i]), new File(dstDir, files[i])) == false) {
					return false;
				}
			}
			return true;
		}
		copyFile(srcDir, dstDir);
		return true;
	}


	// ---------------------------------------------------------------- delete file/dir

	/**
	 * 文件的删除操作
	 * */
	public static boolean delete(String fileName) {
		return delete(new File(fileName));
	}

	public static boolean delete(File fileIn) {
		return fileIn.delete();
	}
	
	/**
	 * 
	 * @Title:多个文件删除
	 * @Description: 指定时间删除
	 * @author: wqq
	 * @return
	 * @Date time: 2018年12月22日 下午5:19:41
	 */
	public static boolean deleteFileArr(File[] fileArr,Date date) {
		try {
			if (fileArr != null && fileArr.length > 0) {
				for (File file : fileArr) {
					if (new Date(file.lastModified()).before(date)) {
						return file.delete();
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("删除文件报错");
		}
		
		return false;
	}
	

	/**
	 * 文件夹的删除操作
	 * */
	public static boolean deleteDir(String pathName) {
		return deleteDir(new File(pathName));
	}

	public static boolean deleteDir(File path) {
		if (path.isDirectory()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (deleteDir(files[i]) == false) {
					return false;
				}
			}
		}
		return path.delete();
	}


	// ---------------------------------------------------------------- string utilities

	public static int STRING_BUFFER_SIZE = 32 * 1024;

	public static String readString(String fileName) throws IOException {
		return readString(new File(fileName), STRING_BUFFER_SIZE);
	}

	public static String readString(String fileName, int bufferSize) throws IOException {
		return readString(new File(fileName), bufferSize);
	}

	public static String readString(File file) throws IOException {
		return readString(file, STRING_BUFFER_SIZE);
	}

	public static String readString(File file, int bufferSize) throws IOException {
		long fileLen = file.length();
		if (fileLen <= 0L) {
			if (file.exists() == true) {
				return "";						// empty file
			}
			return null;						// all other file len problems
		}
		if (fileLen > Integer.MAX_VALUE) {		// max String size
			throw new IOException("File too big for loading into a String!");
		}

		FileReader fr = null;
		BufferedReader brin = null;
		char[] buf = null;
		try {
			fr = new FileReader(file);
			brin = new BufferedReader(fr, bufferSize);
			int length = (int) fileLen;
			buf = new char[length];
			brin.read(buf, 0, length);
		} finally {
			if (brin != null) {
				brin.close();
				fr = null;
			}
			if (fr != null) {
				fr.close();
			}
		}
		return new String(buf);
	}

	public static void writeString(String fileName, String s) throws IOException {
		writeString(new File(fileName), s, STRING_BUFFER_SIZE);
	}

	public static void writeString(String fileName, String s, int bufferSize) throws IOException {
		writeString(new File(fileName), s, bufferSize);
	}

	public static void writeString(File file, String s) throws IOException {
		writeString(file, s, STRING_BUFFER_SIZE);
	}

	public static void writeString(File file, String s, int bufferSize) throws IOException {
		FileWriter fw  = null;
		BufferedWriter out = null;
		if (s == null) {
			return;
		}
		try {
			fw = new FileWriter(file);
			out = new BufferedWriter(fw, bufferSize);
			out.write(s);
		} finally {
			if (out != null) {
				out.close();
				fw = null;
			}
			if (fw != null) {
				fw.close();
			}
		}
	}

	//  unicode string utilities

	public static String readString(String fileName, String encoding) throws IOException {
		return readString(new File(fileName), STRING_BUFFER_SIZE, encoding);
	}

	public static String readString(String fileName, int bufferSize, String encoding) throws IOException {
		return readString(new File(fileName), bufferSize, encoding);
	}

	public static String readString(File file, String encoding) throws IOException {
		return readString(file, STRING_BUFFER_SIZE, encoding);
	}

	public static String readString(File file, int bufferSize, String encoding) throws IOException {
		long fileLen = file.length();
		if (fileLen <= 0L) {
			if (file.exists() == true) {
				return "";						// empty file
			}
			return null;						// all other file len problems
		}
		if (fileLen > Integer.MAX_VALUE) {		// max String size
			throw new IOException("File too big for loading into a String!");
		}

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader brin = null;
		
		int length = (int) fileLen;
		char[] buf = null;
		int realSize = 0;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, encoding);
			brin = new BufferedReader(isr, bufferSize);
			buf = new char[length];						// this is the weakest point, since real file size is not determined
			int c;										// anyhow, this is the fastest way doing this
			while ((c = brin.read()) != -1) {
				buf[realSize] = (char) c;
				realSize++;
			}
		} finally {
			if (brin != null) {
				brin.close();
				isr = null;
				fis = null;
			}
			if (isr != null) {
				isr.close();
				fis = null;
			}
			if (fis != null) {
				fis.close();
			}
		}
		return new String(buf, 0, realSize);
	}

	public static void writeString(String fileName, String s, String encoding) throws IOException {
		writeString(new File(fileName), s, STRING_BUFFER_SIZE, encoding);
	}

	public static void writeString(String fileName, String s, int bufferSize, String encoding) throws IOException {
		writeString(new File(fileName), s, bufferSize, encoding);
	}

	public static void writeString(File file, String s, String encoding) throws IOException {
		writeString(file, s, STRING_BUFFER_SIZE, encoding);
	}

	public static void writeString(File file, String s, int bufferSize, String encoding) throws IOException {
		if (s == null) {
			return;
		}
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter out = null;
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, encoding);
			out = new BufferedWriter(osw, bufferSize);
			out.write(s);
		} finally {
			if (out != null) {
				out.close();
				osw = null;
				fos = null;
			}
			if (osw != null) {
				osw.close();
				fos = null;
			}
			if (fos != null) {
				fos.close();
			}
		}
	}


	//  object serialization

	public static int OBJECT_BUFFER_SIZE = 32 * 1024;

	public static void writeObject(String f, Object o) throws IOException {
		writeObject(f, o, OBJECT_BUFFER_SIZE);
	}

	public static void writeObject(String f, Object o, int bufferSize) throws IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(f);
			bos = new BufferedOutputStream(fos, bufferSize);
			oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
		} finally {
			if (oos != null) {
				oos.close();
				bos = null;
				fos = null;
			}
			if (bos != null) {
				bos.close();
				fos = null;
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static Object readObject(String f) throws IOException, ClassNotFoundException, FileNotFoundException {
		return readObject(f, OBJECT_BUFFER_SIZE);
	}

	public static Object readObject(String f, int bufferSize) throws IOException, ClassNotFoundException, FileNotFoundException {
		Object result = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis, bufferSize);
			ois = new ObjectInputStream(bis);
			result = ois.readObject();
		} finally {
			if (ois != null) {
				ois.close();
				bis = null;
				fis = null;
			}
			if (bis != null) {
				bis.close();
				fis = null;
			}
			if (fis != null) {
				fis.close();
			}
		}
		return result;
	}


	// byte array
	public static final byte[] readBytes(String s) throws IOException {
		return readBytes(new File(s));
	}

	public static final byte[] readBytes(File file) throws IOException {
		FileInputStream fileinputstream = new FileInputStream(file);
		long l = file.length();
		if (l > Integer.MAX_VALUE) {
			throw new IOException("File too big for loading into a byte array!");
		}
		byte byteArray[] = new byte[(int)l];
		int i = 0;
		for (int j = 0; (i < byteArray.length) && (j = fileinputstream.read(byteArray, i, byteArray.length - i)) >= 0; i += j);
		if (i < byteArray.length) {
			throw new IOException("Could not completely read the file " + file.getName());
		}
		fileinputstream.close();
		return byteArray;
	}

	public static void writeBytes(String filename, byte[] source) throws IOException {
		if (source == null) {
			return;
		}
		writeBytes(new File(filename), source, 0, source.length);
	}

	public static void writeBytes(File file, byte[] source) throws IOException {
		if (source == null) {
			return;
		}
		writeBytes(file, source, 0, source.length);
	}

	
	public static void writeBytes(String filename, byte[] source, int offset, int len) throws IOException {
		writeBytes(new File(filename), source, offset, len);
	}

	public static void writeBytes(File file, byte[] source, int offset, int len) throws IOException 
	{
		if (len < 0) {
			throw new IOException("File size is negative!");
		}
		if (offset + len > source.length) {
			len = source.length - offset;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(source, offset, len);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		return;
	}
	
}
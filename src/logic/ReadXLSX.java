package logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReadXLSX {
	
	public int[][] Read(String filepath) throws IOException {
		int[][] res = new int[48][48];
		FileInputStream fis = new FileInputStream(new File(filepath));
		XSSFWorkbook workbook = new XSSFWorkbook (fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> ite = sheet.rowIterator();
		int rowNr = -1;
		int colNr = -1;
		while(ite.hasNext() && rowNr < 48){
			Row row = ite.next();
			Iterator<Cell> cite = row.cellIterator();
			while(cite.hasNext() && colNr < 48){
				Cell c = cite.next();
				if (!(rowNr == -1 || colNr == -1)) {
					res[rowNr][colNr] = (int) Double.parseDouble(c.toString());
				}				
				colNr++;
			}
			colNr = -1;
			rowNr++;
		}
		fis.close();
		workbook.close();
		
		return res;
	}
	
	public static void main(String[] args) {
		ReadXLSX reader = new ReadXLSX();
		try {
			int[][] costs = reader.Read("C:\\Users\\Are\\workspace\\Cost.xlsx");
			System.out.println(Arrays.deepToString(costs));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
} 
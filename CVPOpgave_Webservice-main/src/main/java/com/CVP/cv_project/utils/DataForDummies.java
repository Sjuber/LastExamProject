package com.CVP.cv_project.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataForDummies {

    static List<String> firstNames = new ArrayList<>();

    public static List<String> getFirstNames() throws IOException {

        /*More info at https://www.javatpoint.com/how-to-read-excel-file-in-java*/

        //obtaining input bytes from a file
        FileInputStream fileWithFirstNames = new FileInputStream(
                new File(".\\src\\main\\java\\com\\CVP\\cv_project\\utils\\AlleGodkendteFornavneDec2022.xlsx")); //file with names downloaded from but is edited: https://familieretshuset.dk/navne/navne/godkendte-fornavne
        //creating workbook instance that refers to .xls file
        XSSFWorkbook wb=new XSSFWorkbook(fileWithFirstNames);
        //creating a Sheet object to retrieve the object
        XSSFSheet sheet=wb.getSheetAt(0);
        //evaluating cell type
        FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();

        for(Row row: sheet)     //iteration over row using for each loop
        {
            for(Cell cell: row)    //iteration over cell using for each loop
            {
                switch(formulaEvaluator.evaluateInCell(cell).getCellType())
                {
                    case Cell.CELL_TYPE_NUMERIC:   //field that represents numeric cell type
        //getting the value of the cell as a number
                        break;
                    case Cell.CELL_TYPE_STRING:    //field that represents string cell type
        //getting the value of the cell as a string
                        firstNames.add(cell.getStringCellValue());
                        break;
                }
            }
        }
        return firstNames;
    }
}

package org.microapp.Diary.report;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

public class Reporter {

	public static void main(String[] args) {
		programWay();
	}
	
	private static void xmlWay() {
		//connection
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/microapp_Diary?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true",
					"root", "r00t");
		} catch(SQLException e) {
			e.printStackTrace();
			return;
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		
		String sourceFileName = "src/main/resources/activityReport";
		
		try {
			System.out.print("Compiling report design... ");
			JasperCompileManager.compileReportToFile(sourceFileName+".xml");
			System.out.println("Done.");
			
			System.out.print("Filling the data in... ");
			JasperFillManager.fillReportToFile(sourceFileName+".jasper", new HashMap<String, Object>(), connection);
			System.out.println("Filled.");
			
			System.out.print("Printing to pdf... ");
			JasperExportManager.exportReportToPdfFile(sourceFileName+".jrprint");
			System.out.print("Done.");
			
		} catch(JRException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private static void programWay() {
		//connection
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/microapp_Diary?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true",
					"root", "r00t");
		} catch(SQLException e) {
			e.printStackTrace();
			return;
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		//style 
		StyleBuilders stl = DynamicReports.stl;
		StyleBuilder boldStyle = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle)
				.setBorder(stl.pen1Point())
				.setBackgroundColor(Color.LIGHT_GRAY);
		
		//new report
		System.out.print("Creating report... ");
		JasperReportBuilder report = DynamicReports.report();
		report
		.columns(Columns.column("Activity Id","id",DataTypes.longType()),
				Columns.column("Name","name",DataTypes.stringType()),
				Columns.column("Activity type","activityType",DataTypes.stringType()),
				Columns.column("Value","value",DataTypes.doubleType()),
				Columns.column("Daily record Id","dailyRecordId",DataTypes.longType()))
				.title(
						Components.text("Activity report")
						.setStyle(boldCenteredStyle))
						.pageFooter(Components.pageXofY().setStyle(boldCenteredStyle))
						.setColumnTitleStyle(columnTitleStyle)
						.highlightDetailEvenRows()
						.setDataSource("SELECT id,name,activityType,value,dailyRecordId FROM activity", connection);
		System.out.println("created.");
		
		
		System.out.print("Exporting to pdf... ");
		//show and export
		try {
			//export to pdf
			report.toPdf(new FileOutputStream("../data/report.pdf"));
		} catch(DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("done.");
	}

}

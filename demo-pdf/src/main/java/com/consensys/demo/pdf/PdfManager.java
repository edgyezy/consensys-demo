package com.consensys.demo.pdf;

import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

@Service
@ComponentScan("com.consensys.demo")
@PropertySource("classpath:/common.properties")
public class PdfManager {
	private static Logger log = LoggerFactory.getLogger(PdfManager.class);

	@Value("files.imageLocation")
	private String imageLocation;

	@Value("files.pdfLocation")
	private String pdfLocation;

	@Value("files.pdfMaster")
	private String pdfMaster;

	public void createDocumentPdf(Image img, String imgLocation) throws IOException, DocumentException {
		Document document = new Document(img);
		PdfWriter.getInstance(document, new FileOutputStream(imgLocation));
		document.close();
	}

	public void appendPdfToMaster(String newPdfLocation) throws IOException, DocumentException {
		Document document = new Document();

		PdfCopy copy = new PdfCopy(document, new FileOutputStream(this.pdfMaster));

		document.open();
		PdfReader reader = new PdfReader(newPdfLocation);
		copy.addDocument(reader);
		copy.freeReader(reader);
		reader.close();

		document.close();

	}
}
